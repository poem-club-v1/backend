package poem.club.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import poem.club.backend.dto.NewPoemDto;
import poem.club.backend.dto.PoemDto;
import poem.club.backend.entity.Poem;
import poem.club.backend.service.PoemService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/poems")
public class PoemController {

    @Autowired
    private PoemService poemService;

    @GetMapping
    public ResponseEntity<List<PoemDto>> getPoems() {
        return poemService.getPoems();
    }

    @GetMapping("/fanFavourite")
    public ResponseEntity<List<PoemDto>> getFanFavouritePoems() {
        return poemService.getFanFavouritePoems();
    }

    @GetMapping("/latest")
    public ResponseEntity<List<PoemDto>> getLatestPoems() {
        return poemService.getLatestPoems();
    }

    @GetMapping("/myLikes")
    public ResponseEntity<List<PoemDto>> getUserLikedPoems(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) { return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); }

        String email = user.getAttribute("email");
        return poemService.getUserLikedPoems(email);
    }

    @GetMapping("/myPoems")
    public ResponseEntity<List<PoemDto>> getUserPoems(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) { return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); }

        String email = user.getAttribute("email");
        return poemService.getUserPoems(email);
    }

    @PostMapping("/addNewPoem")
    public ResponseEntity<PoemDto> addNewPoem(
            @RequestBody NewPoemDto newPoemDto,
            @AuthenticationPrincipal OAuth2User user) {

//        if (user == null) { return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); }
//
//        String email = user.getAttribute("email");
//        return poemService.addNewPoem(email, newPoemDto);
        return poemService.addNewPoem("dojemiljak@gmail.com", newPoemDto);
    }

    @PostMapping("/like")
    public ResponseEntity<Boolean> likeOrUnlikePoem(
            @AuthenticationPrincipal OAuth2User user,
            @RequestBody PoemDto poemDto) {
        if (user == null) { return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); }

        String email = user.getAttribute("email");
        return poemService.likeOrUnlike(email, poemDto);
    }
}
