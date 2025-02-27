package poem.club.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poem.club.backend.entity.Poem;
import poem.club.backend.repository.PoemRepository;

import java.util.List;

@Service
public class PoemService {

    @Autowired
    private PoemRepository poemRepository;

    public List<Poem> getAllPoems() {
        return poemRepository.findAll();
    }
}
