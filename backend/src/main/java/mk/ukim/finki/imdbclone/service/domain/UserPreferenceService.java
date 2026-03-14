package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.model.domain.UserPreference;

import java.util.List;

public interface UserPreferenceService {
    void processRatingPreference(User user, Media media, Integer rating);
    void processWatchlistPreference(User user, Media media);
    List<UserPreference> getUserPreferences(User user);
}