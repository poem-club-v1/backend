package poem.club.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import poem.club.backend.dto.PoemDto;
import poem.club.backend.dto.PoetDto;
import poem.club.backend.entity.Poem;
import poem.club.backend.entity.Poet;
import poem.club.backend.repository.PoetRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PoetService {

    @Autowired
    private PoetRepository poetRepository;

    private static final List<String> POETIC_STYLES = List.of(
            "Sonnet", "Ode", "Verse", "Haiku", "Limerick", "Rhyme",
            "Lyric", "Stanza", "Epic", "Ballad", "Metaphor", "Prose");

    private static final List<String> POETIC_ROLES = List.of(
            "Dreamer", "Wanderer", "Whisperer", "Sculptor", "Weaver", "Seeker",
            "Teller", "Voyager", "Writer", "Storyteller", "Composer", "Bard", "Singer");

    private static final Random RANDOM = new Random();

    public PoetDto toDto(Poet poet) {
        return new PoetDto(
                poet.getId(),
                poet.getName(),
                poet.getEmail(),
                poet.getUsername()
        );
    }

    public List<PoetDto> toDtoList(List<Poet> poets) {
        return poets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private String generateUniquePoeticName() {
        String name;
        do {
            String poeticStyle = POETIC_STYLES.get(RANDOM.nextInt(POETIC_STYLES.size()));
            String poeticRole = POETIC_ROLES.get(RANDOM.nextInt(POETIC_ROLES.size()));
            name = poeticStyle + poeticRole;
        } while (poetRepository.existsByUsername(name));

        return name;
    }

    public ResponseEntity<PoetDto> getOrRegisterPoet(OAuth2User user) {
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");

        Optional<Poet> existingPoet = poetRepository.findByEmail(email);
        if (existingPoet.isPresent()) {
            return new ResponseEntity<>(toDto(existingPoet.get()), HttpStatus.OK);
        }

        String poeticUsername = generateUniquePoeticName();

        Poet newPoet = new Poet();
        newPoet.setName(name);
        newPoet.setEmail(email);
        newPoet.setUsername(poeticUsername);

        Poet savedPoet = poetRepository.save(newPoet);
        return new ResponseEntity<>(toDto(savedPoet), HttpStatus.CREATED);
    }

    public ResponseEntity<PoetDto> changeUsername(String email, String username) {

        if (username == null || username.length() < 3 || !username.matches("^[a-zA-Z]+$")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (poetRepository.existsByUsername(username)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Optional<Poet> poetOptional = poetRepository.findByEmail(email);
        if (poetOptional.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

        Poet poet = poetOptional.get();
        poet.setUsername(username);
        Poet updatedPoet = poetRepository.save(poet);

        return new ResponseEntity<>(toDto(updatedPoet), HttpStatus.OK);
    }
}
