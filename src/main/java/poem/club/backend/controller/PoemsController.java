package poem.club.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class PoemsController {


    @GetMapping("/movies")
    public List<String> getMovies(@AuthenticationPrincipal OAuth2User user) {
        return Arrays.asList("Movie 1", "Movie 2", "Movie 3", "Movie 4");
    }

    @GetMapping("/poems")
    public List<String> getPoems(@AuthenticationPrincipal OAuth2User user) {
        return Arrays.asList("Poem 1", "Poem 2", "Poem 3", "Poem 4");
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(@AuthenticationPrincipal OAuth2User user) {
        return Map.of(
                "name", user.getAttribute("name"),
                "email", user.getAttribute("email")
        );
    }
}
