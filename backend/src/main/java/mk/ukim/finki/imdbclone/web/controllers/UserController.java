package mk.ukim.finki.imdbclone.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.imdbclone.model.dto.*;
import mk.ukim.finki.imdbclone.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.imdbclone.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.imdbclone.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.imdbclone.service.application.RecommendationApplicationService;
import mk.ukim.finki.imdbclone.service.application.UserApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "Endpoints for user authentication and registration")
public class UserController {
    private final UserApplicationService userApplicationService;
    private final RecommendationApplicationService recommendationApplicationService;

    public UserController(UserApplicationService userApplicationService,  RecommendationApplicationService recommendationApplicationService) {
        this.userApplicationService = userApplicationService;
        this.recommendationApplicationService = recommendationApplicationService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @PostMapping("/register")
    public ResponseEntity<DisplayUserDto> register(@RequestBody CreateUserDto createUserDto) {
        try {
            return userApplicationService.register(createUserDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "User login", description = "Authenticates a user and starts a session")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserDto loginUserDto) {
        try {
            return userApplicationService.login(loginUserDto)
                    .map(ResponseEntity::ok)
                    .orElseThrow(InvalidUserCredentialsException::new);
        } catch (InvalidUserCredentialsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "User logout", description = "Ends the user's session")
    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @Operation(summary = "Add media to watchlist")
    @PostMapping("/{username}/watchlist/{mediaId}")
    public ResponseEntity<DisplayUserDto> addMediaToWatchlist(
            @PathVariable String username,
            @PathVariable Long mediaId) {

        return userApplicationService.addMediaToWatchlist(username, mediaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remove media from watchlist")
    @DeleteMapping("/{username}/watchlist/{mediaId}")
    public ResponseEntity<DisplayUserDto> removeMediaFromWatchlist(
            @PathVariable String username,
            @PathVariable Long mediaId) {

        return userApplicationService.removeMediaFromWatchlist(username, mediaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{username}/watchlist")
    public ResponseEntity<List<DisplayCardMediaDto>> getWatchlist(@PathVariable String username) {
        return ResponseEntity.ok(userApplicationService.getWatchlist(username));
    }

    @Operation(summary = "Check if media is in watchlist")
    @GetMapping("/{username}/watchlist/{mediaId}")
    public ResponseEntity<Boolean> isMediaInWatchlist(
            @PathVariable String username,
            @PathVariable Long mediaId) {

        return ResponseEntity.ok(userApplicationService.isMediaInWatchlist(username, mediaId));
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<List<DisplayCardMediaDto>> getRecommendations(@PathVariable Long userId) {
        return ResponseEntity.ok(recommendationApplicationService.getRecommendationsForUser(userId));
    }
}