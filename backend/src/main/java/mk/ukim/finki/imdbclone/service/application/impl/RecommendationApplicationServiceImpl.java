package mk.ukim.finki.imdbclone.service.application.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.service.application.RecommendationApplicationService;
import mk.ukim.finki.imdbclone.service.domain.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationApplicationServiceImpl implements RecommendationApplicationService {

    private final RecommendationService recommendationService;

    @Override
    public List<DisplayCardMediaDto> getRecommendationsForUser(Long userId) {
        return recommendationService.getRecommendationsForUser(userId)
                .stream()
                .map(DisplayCardMediaDto::from)
                .toList();
    }
}