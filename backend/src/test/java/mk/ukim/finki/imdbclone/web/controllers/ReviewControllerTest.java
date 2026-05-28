package mk.ukim.finki.imdbclone.web.controllers;

import mk.ukim.finki.imdbclone.model.dto.CreateReviewDto;
import mk.ukim.finki.imdbclone.model.dto.CreateUserDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayReviewDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayUserDto;
import mk.ukim.finki.imdbclone.model.dto.LoginResponseDto;
import mk.ukim.finki.imdbclone.model.dto.LoginUserDto;
import mk.ukim.finki.imdbclone.service.application.ReviewApplicationService;
import mk.ukim.finki.imdbclone.service.application.UserApplicationService;
import mk.ukim.finki.imdbclone.web.helpers.ControllerAuthorizationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewControllerTest {

    private final FakeReviewApplicationService reviewApplicationService = new FakeReviewApplicationService();
    private final FakeUserApplicationService userApplicationService = new FakeUserApplicationService();
    private final ControllerAuthorizationHelper authorizationHelper =
            new ControllerAuthorizationHelper(userApplicationService);
    private final ReviewController reviewController = new ReviewController(
            reviewApplicationService,
            authorizationHelper
    );

    @Test
    void rejectsReviewCreationForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);
        userApplicationService.currentUser = new DisplayUserDto(1L, "alice", "Alice", "User");

        var response = reviewController.createReview(new CreateReviewDto(2L, 20L, "Review text"), authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(reviewApplicationService.saveCalled).isFalse();
    }

    @Test
    void rejectsReviewEditForDifferentAuthenticatedUser() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", null);
        reviewApplicationService.review =
                new DisplayReviewDto(100L, "bob", "Movie", "Old", null, null);

        var response = reviewController.updateReview(
                100L,
                new CreateReviewDto(2L, 20L, "Changed"),
                authentication
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(reviewApplicationService.updateCalled).isFalse();
    }

    private static class FakeReviewApplicationService implements ReviewApplicationService {
        private DisplayReviewDto review;
        private boolean saveCalled;
        private boolean updateCalled;

        @Override
        public Optional<DisplayReviewDto> save(CreateReviewDto reviewDto) {
            saveCalled = true;
            return Optional.empty();
        }

        @Override
        public Optional<DisplayReviewDto> update(Long reviewId, String newText) {
            updateCalled = true;
            return Optional.empty();
        }

        @Override
        public void delete(Long reviewId) {
        }

        @Override
        public Optional<DisplayReviewDto> findByUserAndMedia(Long userId, Long mediaId) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayReviewDto> findById(Long reviewId) {
            return Optional.ofNullable(review);
        }

        @Override
        public List<DisplayReviewDto> findByMedia(Long mediaId) {
            return List.of();
        }

        @Override
        public List<DisplayReviewDto> findByUser(Long userId) {
            return List.of();
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
