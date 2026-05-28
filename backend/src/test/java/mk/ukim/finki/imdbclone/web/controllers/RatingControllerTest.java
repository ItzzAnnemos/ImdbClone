package mk.ukim.finki.imdbclone.web.controllers;

import mk.ukim.finki.imdbclone.model.dto.CreateRatingDto;
import mk.ukim.finki.imdbclone.model.dto.CreateUserDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRatingDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayUserDto;
import mk.ukim.finki.imdbclone.model.dto.LoginResponseDto;
import mk.ukim.finki.imdbclone.model.dto.LoginUserDto;
import mk.ukim.finki.imdbclone.service.application.RatingApplicationService;
import mk.ukim.finki.imdbclone.service.application.UserApplicationService;
import mk.ukim.finki.imdbclone.web.helpers.ControllerAuthorizationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RatingControllerTest {

    private final FakeRatingApplicationService ratingApplicationService = new FakeRatingApplicationService();
    private final FakeUserApplicationService userApplicationService = new FakeUserApplicationService();
    private final ControllerAuthorizationHelper authorizationHelper =
            new ControllerAuthorizationHelper(userApplicationService);
    private final RatingController ratingController = new RatingController(
            ratingApplicationService,
            authorizationHelper
    );

    @Test
    void rejectsRatingMutationForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);
        userApplicationService.currentUser = new DisplayUserDto(1L, "alice", "Alice", "User");

        var response = ratingController.rateMedia(new CreateRatingDto(2L, 20L, 8), authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(ratingApplicationService.saveCalled).isFalse();
    }

    @Test
    void rejectsUserSpecificRatingLookupForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);
        userApplicationService.currentUser = new DisplayUserDto(1L, "alice", "Alice", "User");

        var response = ratingController.getRating(2L, 20L, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(ratingApplicationService.findByUserAndMediaCalled).isFalse();
    }

    private static class FakeRatingApplicationService implements RatingApplicationService {
        private boolean saveCalled;
        private boolean findByUserAndMediaCalled;

        @Override
        public Optional<DisplayRatingDto> save(CreateRatingDto ratingDto) {
            saveCalled = true;
            return Optional.empty();
        }

        @Override
        public void delete(Long userId, Long mediaId) {
        }

        @Override
        public Optional<DisplayRatingDto> findByUserAndMedia(Long userId, Long mediaId) {
            findByUserAndMediaCalled = true;
            return Optional.empty();
        }

        @Override
        public List<DisplayRatingDto> findByMedia(Long mediaId) {
            return List.of();
        }

        @Override
        public List<DisplayRatingDto> findByUser(Long userId) {
            return List.of();
        }

        @Override
        public Double getAverageRating(Long mediaId) {
            return null;
        }

        @Override
        public Long getRatingCount(Long mediaId) {
            return 0L;
        }
    }

    private static class FakeUserApplicationService implements UserApplicationService {
        private DisplayUserDto currentUser;

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
    }
}
