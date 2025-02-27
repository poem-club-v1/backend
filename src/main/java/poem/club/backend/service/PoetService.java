package poem.club.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poem.club.backend.repository.PoetRepository;

@Service
public class PoetService {

    @Autowired
    private PoetRepository poetRepository;
}
