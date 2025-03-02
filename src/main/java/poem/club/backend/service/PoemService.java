package poem.club.backend.service;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import poem.club.backend.dto.NewPoemDto;
import poem.club.backend.dto.PoemDto;
import poem.club.backend.entity.Category;
import poem.club.backend.entity.Language;
import poem.club.backend.entity.Poem;
import poem.club.backend.entity.Poet;
import poem.club.backend.repository.CategoryRepository;
import poem.club.backend.repository.LanguageRepository;
import poem.club.backend.repository.PoemRepository;
import poem.club.backend.repository.PoetRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PoemService {

    @Autowired
    private PoemRepository poemRepository;

    @Autowired
    private PoetRepository poetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${huggingface.api.key}")
    private String huggingfaceApiKey;

    public PoemDto toDto(Poem poem) {
        return new PoemDto(
                poem.getId(),
                poem.getTitle(),
                poem.getContent(),
                poem.getDateCreated(),
                poem.getNumberOfLikes(),
                poem.getCategory() != null ? poem.getCategory().getName() : null,
                poem.getLanguage() != null ? poem.getLanguage().getName() : null,
                poem.getAuthor() != null ? poem.getAuthor().getUsername() : null
        );
    }

    public List<PoemDto> toDtoList(List<Poem> poems) {
        return poems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<PoemDto>> getPoems() {

        List<Poem> poems = poemRepository.findAll();
        return new ResponseEntity<>(toDtoList(poems), HttpStatus.OK);
    }

    public ResponseEntity<List<PoemDto>> getFanFavouritePoems() {

        List<Poem> topPoems = poemRepository.findTop20ByOrderByNumberOfLikesDesc();
        return new ResponseEntity<>(toDtoList(topPoems), HttpStatus.OK);
    }

    public ResponseEntity<List<PoemDto>> getLatestPoems() {

        List<Poem> latestPoems = poemRepository.findTop20ByOrderByDateCreatedDesc();
        return new ResponseEntity<>(toDtoList(latestPoems), HttpStatus.OK);
    }

    public ResponseEntity<List<PoemDto>> getUserLikedPoems(String email) {

        Optional<Poet> poetOptional = poetRepository.findByEmail(email);
        if (poetOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Poet poet = poetOptional.get();
        List<Poem> likedPoems = poet.getLikes();

        return new ResponseEntity<>(toDtoList(likedPoems), HttpStatus.OK);
    }

    public ResponseEntity<List<PoemDto>> getUserPoems(String email) {

        Optional<Poet> poetOptional = poetRepository.findByEmail(email);
        if (poetOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Poet poet = poetOptional.get();
        List<Poem> writtenPoems = poet.getPoems();

        return new ResponseEntity<>(toDtoList(writtenPoems), HttpStatus.OK);
    }

    public ResponseEntity<PoemDto> addNewPoem(String email, NewPoemDto newPoemDto) {
        Poem newPoem = new Poem();
        newPoem.setTitle(newPoemDto.getTitle());
        newPoem.setContent(newPoemDto.getContent());
        newPoem.setDateCreated(new Date());
        newPoem.setNumberOfLikes(0);
        newPoem.setCategory(null);
        newPoem.setLanguage(null);

        Optional<Poet> poetOptional = poetRepository.findByEmail(email);
        if (poetOptional.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        Poet poet = poetOptional.get();
        newPoem.setAuthor(poet);

        Poem savedPoem = poemRepository.save(newPoem);

        try {
            String scriptPath = new ClassPathResource("scripts/classify_poems.py").getFile().getAbsolutePath();

            String[] command = {
                    "python",
                    scriptPath,
                    savedPoem.getTitle(),
                    savedPoem.getContent()
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Pass secrets as environment variables
            processBuilder.environment().put("DB_URL", dbUrl);
            processBuilder.environment().put("HUGGINGFACE_API_KEY", huggingfaceApiKey);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();

            if (output != null && output.contains("|")) {
                String[] results = output.split("\\|");
                String category = results[0].trim();
                String language = results[1].trim();

                Optional<Category> categoryOptional = categoryRepository.findByName(category);
                categoryOptional.ifPresent(savedPoem::setCategory);

                Optional<Language> languageOptional = languageRepository.findByName(language);
                languageOptional.ifPresent(savedPoem::setLanguage);

                poemRepository.save(savedPoem);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Error in Python script execution.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(toDto(savedPoem), HttpStatus.CREATED);
    }

    public ResponseEntity<Boolean> likeOrUnlike(String email, PoemDto poemDto) {

        Optional<Poet> poetOptional = poetRepository.findByEmail(email);
        if (poetOptional.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);  }
        Poet poet = poetOptional.get();

        Optional<Poem> poemOptional = poemRepository.findById(poemDto.getId());
        if (poemOptional.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        Poem poem = poemOptional.get();

        if (poet.getLikes().contains(poem)) {
            poet.getLikes().remove(poem);
            poem.setNumberOfLikes(poem.getNumberOfLikes() - 1);
        } else {
            poet.getLikes().add(poem);
            poem.setNumberOfLikes(poem.getNumberOfLikes() + 1);
        }

        poetRepository.save(poet);
        poemRepository.save(poem);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
