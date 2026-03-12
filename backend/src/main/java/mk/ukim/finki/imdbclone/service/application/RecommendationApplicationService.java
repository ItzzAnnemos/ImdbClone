package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;

import java.util.List;

public interface RecommendationApplicationService {
    List<DisplayCardMediaDto> getRecommendationsForUser(Long userId);
}