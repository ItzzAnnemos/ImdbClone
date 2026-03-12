package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Rating;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.RatingRepository;
import mk.ukim.finki.imdbclone.service.domain.RecommendationService;
import mk.ukim.finki.imdbclone.service.domain.UserService;
import mk.ukim.finki.imdbclone.service.domain.helper.MediaSimilarityHelper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final MediaRepository mediaRepository;
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final MediaSimilarityHelper mediaSimilarityHelper;

    @Override
    public List<Media> getPopularRecommendations() {
        return mediaRepository.findTop10ByOrderByAverageRatingDesc();
    }

    @Override
    public List<Media> getContentBasedRecommendations(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Media> likedMedia = ratingRepository.findHighlyRatedMediaByUserId(userId);
        List<Media> watchlistMedia = user.getWatchlist();

        Set<Media> baseMedia = new HashSet<>();
        baseMedia.addAll(likedMedia);
        baseMedia.addAll(watchlistMedia);

        if (baseMedia.isEmpty()) {
            return getPopularRecommendations();
        }

        Set<Long> excludedIds = getExcludedMediaIds(user);

        Map<Media, Double> scores = new HashMap<>();
        List<Media> allMedia = mediaRepository.findAll();

        for (Media candidate : allMedia) {
            if (excludedIds.contains(candidate.getId())) {
                continue;
            }

            double score = 0.0;

            for (Media source : baseMedia) {
                score += mediaSimilarityHelper.similarityScore(source, candidate);
            }

            if (score > 0.0) {
                scores.put(candidate, score);
            }
        }

        return scores.entrySet()
                .stream()
                .sorted(Map.Entry.<Media, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(10)
                .toList();
    }

    @Override
    public List<Media> getCollaborativeRecommendations(Long userId) {
        List<Media> userLikedMedia = ratingRepository.findHighlyRatedMediaByUserId(userId);

        if (userLikedMedia.isEmpty()) {
            return Collections.emptyList();
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Set<Long> excludedIds = getExcludedMediaIds(user);

        Map<Long, Integer> similarUserScores = new HashMap<>();

        for (Media media : userLikedMedia) {
            List<Long> otherUserIds = ratingRepository.findUserIdsWhoHighlyRatedMedia(media.getId());

            for (Long otherUserId : otherUserIds) {
                if (!otherUserId.equals(userId)) {
                    similarUserScores.put(
                            otherUserId,
                            similarUserScores.getOrDefault(otherUserId, 0) + 1
                    );
                }
            }
        }

        List<Long> mostSimilarUsers = similarUserScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(5)
                .toList();

        Map<Media, Integer> recommendationScores = new HashMap<>();

        for (Long similarUserId : mostSimilarUsers) {
            List<Media> similarUserLikedMedia = ratingRepository.findHighlyRatedMediaByUserId(similarUserId);

            for (Media media : similarUserLikedMedia) {
                if (!excludedIds.contains(media.getId())) {
                    recommendationScores.put(
                            media,
                            recommendationScores.getOrDefault(media, 0) + 1
                    );
                }
            }
        }

        return recommendationScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Media, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(10)
                .toList();
    }

    @Override
    public List<Media> getRecommendationsForUser(Long userId) {
        Map<Media, Double> combinedScores = new HashMap<>();

        List<Media> contentBased = getContentBasedRecommendations(userId);
        List<Media> collaborative = getCollaborativeRecommendations(userId);

        for (int i = 0; i < contentBased.size(); i++) {
            combinedScores.put(
                    contentBased.get(i),
                    combinedScores.getOrDefault(contentBased.get(i), 0.0) + (10.0 - i)
            );
        }

        for (int i = 0; i < collaborative.size(); i++) {
            combinedScores.put(
                    collaborative.get(i),
                    combinedScores.getOrDefault(collaborative.get(i), 0.0) + (10.0 - i)
            );
        }

        if (combinedScores.isEmpty()) {
            return getPopularRecommendations();
        }

        return combinedScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Media, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(10)
                .toList();
    }

    private Set<Long> getExcludedMediaIds(User user) {
        Set<Long> excludedIds = new HashSet<>();

        excludedIds.addAll(
                user.getRatings()
                        .stream()
                        .map(Rating::getMedia)
                        .filter(Objects::nonNull)
                        .map(Media::getId)
                        .collect(Collectors.toSet())
        );

        excludedIds.addAll(
                user.getWatchlist()
                        .stream()
                        .map(Media::getId)
                        .collect(Collectors.toSet())
        );

        return excludedIds;
    }
}