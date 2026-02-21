package mk.ukim.finki.imdbclone.service.domain.impl;

import mk.ukim.finki.imdbclone.model.domain.Media;
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
}
