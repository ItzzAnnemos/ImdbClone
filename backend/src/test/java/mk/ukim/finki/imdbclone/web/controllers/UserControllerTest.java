package mk.ukim.finki.imdbclone.web.controllers;

import mk.ukim.finki.imdbclone.model.dto.CreateUserDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayUserDto;
import mk.ukim.finki.imdbclone.model.dto.LoginResponseDto;
import mk.ukim.finki.imdbclone.model.dto.LoginUserDto;
import mk.ukim.finki.imdbclone.model.dto.ChangePasswordDto;
import mk.ukim.finki.imdbclone.service.application.RecommendationApplicationService;
import mk.ukim.finki.imdbclone.service.application.UserApplicationService;
import mk.ukim.finki.imdbclone.web.helpers.ControllerAuthorizationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    private final FakeUserApplicationService userApplicationService = new FakeUserApplicationService();
    private final FakeRecommendationApplicationService recommendationApplicationService =
            new FakeRecommendationApplicationService();
    private final ControllerAuthorizationHelper authorizationHelper =
            new ControllerAuthorizationHelper(userApplicationService);
    private final UserController userController = new UserController(
            userApplicationService,
            recommendationApplicationService,
            authorizationHelper
    );

    @Test
    void rejectsWatchlistMutationForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);

        var response = userController.addMediaToWatchlist("bob", 10L, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(userApplicationService.addWatchlistCalled).isFalse();
    }

    @Test
    void rejectsRecommendationsForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);
        userApplicationService.currentUser = new DisplayUserDto(1L, "alice", "Alice", "User");

        var response = userController.getRecommendations(2L, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(recommendationApplicationService.called).isFalse();
    }

    @Test
    void rejectsPasswordChangeForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);
        userApplicationService.currentUser = new DisplayUserDto(1L, "alice", "Alice", "User");

        var response = userController.changePassword(
                2L,
                new ChangePasswordDto("oldPass", "newPass", "newPass"),
                authentication
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(userApplicationService.changePasswordCalled).isFalse();
    }

    private static class FakeUserApplicationService implements UserApplicationService {
        private DisplayUserDto currentUser;
        private boolean addWatchlistCalled;
        private boolean changePasswordCalled;

        @Override
        public Optional<DisplayUserDto> register(CreateUserDto createUserDto) {
            return Optional.empty();
        }

        @Override
        public Optional<LoginResponseDto> login(LoginUserDto loginUserDto) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayUserDto> findByUsername(String username) {
            return Optional.ofNullable(currentUser);
        }

        @Override
        public Optional<DisplayUserDto> addMediaToWatchlist(String username, Long mediaId) {
            addWatchlistCalled = true;
            return Optional.empty();
        }

        @Override
        public Optional<DisplayUserDto> removeMediaFromWatchlist(String username, Long mediaId) {
            return Optional.empty();
        }

        @Override
        public List<DisplayCardMediaDto> getWatchlist(String username) {
            return List.of();
        }

        @Override
        public boolean isMediaInWatchlist(String username, Long mediaId) {
            return false;
        }

        @Override
        public Optional<DisplayUserDto> changePassword(Long userId, ChangePasswordDto changePasswordDto) {
            changePasswordCalled = true;
            return Optional.empty();
        }
    }

    private static class FakeRecommendationApplicationService implements RecommendationApplicationService {
        private boolean called;

        @Override
        public List<DisplayCardMediaDto> getRecommendationsForUser(Long userId) {
            called = true;
            return List.of();
        }
    }
}
