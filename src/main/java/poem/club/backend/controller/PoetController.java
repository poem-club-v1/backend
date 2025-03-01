package poem.club.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import poem.club.backend.dto.PoetDto;
import poem.club.backend.service.PoetService;

@RestController
@RequestMapping("/api/poets")
public class PoetController {

    @Autowired
    private PoetService poetService;

    @GetMapping("/me")
    public ResponseEntity<PoetDto> getOrRegisterPoet(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) { return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); }

        return poetService.getOrRegisterPoet(user);
    }

    @PostMapping("/username")
    public ResponseEntity<PoetDto> changeUsername(@AuthenticationPrincipal OAuth2User user, @RequestBody String username) {
        if (user == null) { return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); }

        String email = user.getAttribute("email");
        return poetService.changeUsername(email, username);
    }
}
