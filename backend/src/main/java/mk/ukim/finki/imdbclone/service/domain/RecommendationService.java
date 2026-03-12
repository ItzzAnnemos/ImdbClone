package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Media;

import java.util.List;

public interface RecommendationService {
    List<Media> getPopularRecommendations();
    List<Media> getContentBasedRecommendations(Long userId);
    List<Media> getCollaborativeRecommendations(Long userId);
    List<Media> getRecommendationsForUser(Long userId);
}