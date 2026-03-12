package mk.ukim.finki.imdbclone.service.domain.impl;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.service.domain.MediaService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base implementation of {@link MediaService} that provides shared
 * CRUD logic
 * for all Media subtypes. Subclasses inject their own typed repository and
 * implement
 * the type-specific queries ({@code search}, {@code getTopRated},
 * {@code getRecent}, {@code update}).
 *
 * @param <T> the concrete Media subtype
 */
@Transactional
public abstract class MediaServiceImpl<T extends Media> implements MediaService<T> {

    protected final JpaRepository<T, Long> repository;

    protected MediaServiceImpl(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Media not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // These are repository-specific and must be implemented by each concrete
    // subclass
    @Override
    public abstract T update(Long id, T entityDetails);

    @Override
    public abstract List<T> search(String title);

    @Override
    public abstract List<T> getTopRated();

    @Override
    public abstract List<T> getRecent();

    @Override
    @Transactional(readOnly = true)
    public List<T> findSimilar(Long id) {
        T target = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        return repository.findAll().stream()
                .filter(media -> !media.getId().equals(id))
                .sorted((m1, m2) ->
                        Integer.compare(
                                similarityScore(target, m2),
                                similarityScore(target, m1)
                        )
                )
                .limit(8)
                .toList();
    }

    /**
     * Calculates how similar two media items are.
     * The score increases if they share genres,
     * have close release years, or similar ratings.
     * A higher score means the media items are more similar.
     */
    protected int similarityScore(Media a, Media b) {
        int score = 0;

        long sharedGenres = a.getGenres().stream()
                .filter(b.getGenres()::contains)
                .count();
        score += sharedGenres * 3;

        if (a.getReleaseYear() != null && b.getReleaseYear() != null
                && Math.abs(a.getReleaseYear() - b.getReleaseYear()) <= 3) {
            score += 1;
        }

        if (a.getAverageRating() != null && b.getAverageRating() != null
                && Math.abs(a.getAverageRating() - b.getAverageRating()) <= 1) {
            score += 1;
        }

        if (hasSamePersonWithRole(a, b, Role.DIRECTOR)) {
            score += 5;
        }

        if (hasSamePersonWithRole(a, b, Role.MAIN_ACTOR)) {
            score += 4;
        }

        return score;
    }

    private boolean hasSamePersonWithRole(Media a, Media b, Role role) {
        return a.getCastAndCrew().stream()
                .filter(mp -> mp.getRole() == role)
                .map(mp -> mp.getPerson().getId())
                .anyMatch(personId ->
                        b.getCastAndCrew().stream()
                                .filter(mp -> mp.getRole() == role)
                                .map(mp -> mp.getPerson().getId())
                                .anyMatch(personId::equals)
                );
    }
}
