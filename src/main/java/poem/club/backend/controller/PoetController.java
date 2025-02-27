package poem.club.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poem.club.backend.service.PoetService;

@RestController
@RequestMapping("/api/poets")
public class PoetController {

    @Autowired
    private PoetService poetService;
}
