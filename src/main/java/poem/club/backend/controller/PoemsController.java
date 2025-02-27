package poem.club.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poem.club.backend.entity.Poem;
import poem.club.backend.service.PoemService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/poems")
public class PoemsController {

    @Autowired
    private PoemService poemService;

    @GetMapping("/movies")
    public List<String> getMovies(@AuthenticationPrincipal OAuth2User user) {
        return Arrays.asList("Movie 1", "Movie 2", "Movie 3", "Movie 4");
    }

    @GetMapping("/poems")
    public List<Poem> getAllPoems() {
        return poemService.getAllPoems();
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(@AuthenticationPrincipal OAuth2User user) {
        return Map.of(
                "name", user.getAttribute("name"),
                "email", user.getAttribute("email")
        );
    }
}
