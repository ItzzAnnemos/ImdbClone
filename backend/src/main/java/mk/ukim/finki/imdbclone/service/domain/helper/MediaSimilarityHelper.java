package mk.ukim.finki.imdbclone.service.domain.helper;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.repository.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MediaSimilarityHelper {

    private final GenreRepository genreRepository;

    public double similarityScore(Media a, Media b) {
        double[] vectorA = generateFeatureVector(a);
        double[] vectorB = generateFeatureVector(b);

        double score = cosineSimilarity(vectorA, vectorB);

        if (hasSamePersonWithRole(a, b, Role.DIRECTOR)) {
            score += 0.25;
        }

        if (hasSamePersonWithRole(a, b, Role.MAIN_ACTOR)) {
            score += 0.20;
        }

        return score;
    }

    public double[] generateFeatureVector(Media media) {
        List<Double> features = new ArrayList<>();

        List<String> genre = genreRepository.findAll()
                .stream()
                .map(Genre::getName)
                .sorted()
                .toList();

        for (String genreName : genre) {
            boolean hasGenre = media.getGenres() != null &&
                    media.getGenres().stream()
                            .map(Genre::getName)
                            .anyMatch(name -> name.equalsIgnoreCase(genreName));

            features.add(hasGenre ? 1.0 : 0.0);
        }

        features.add(normalizeYear(media.getReleaseYear()));
        features.add(normalizeRating(media.getAverageRating()));

        return features.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public double cosineSimilarity(double[] a, double[] b) {

        if (a.length != b.length) {
            throw new IllegalArgumentException("Feature vectors must have the same length.");
        }

        double dotProduct = 0.0; // Measures how much the two vectors overlap
        double normA = 0.0;      // Sum of squares of vector A elements (used to compute magnitude)
        double normB = 0.0;      // Sum of squares of vector B elements (used to compute magnitude)

        for (int i = 0; i < a.length; i++) {

            dotProduct += a[i] * b[i]; // This captures how similar the feature values are at each position
            normA += a[i] * a[i]; // These values will later be square-rooted to obtain vector lengths
            normB += b[i] * b[i];
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }

        // Cosine similarity formula:
        // similarity = (A · B) / (|A| * |B|)
        // where |A| and |B| are the magnitudes (lengths) of the vectors
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private double normalizeYear(Integer year) {
        if (year == null) {
            return 0.0;
        }
        return year / 2100.0;
    }

    private double normalizeRating(Double rating) {
        if (rating == null) {
            return 0.0;
        }
        return rating / 10.0;
    }

    private boolean hasSamePersonWithRole(Media a, Media b, Role role) {
        if (a.getCastAndCrew() == null || b.getCastAndCrew() == null) {
            return false;
        }

        return a.getCastAndCrew().stream()
                .filter(mp -> mp.getRole() == role)
                .map(MediaPerson::getPerson)
                .filter(person -> person != null && person.getId() != null)
                .anyMatch(personA ->
                        b.getCastAndCrew().stream()
                                .filter(mp -> mp.getRole() == role)
                                .map(MediaPerson::getPerson)
                                .filter(person -> person != null && person.getId() != null)
                                .anyMatch(personB -> personA.getId().equals(personB.getId()))
                );
    }
}