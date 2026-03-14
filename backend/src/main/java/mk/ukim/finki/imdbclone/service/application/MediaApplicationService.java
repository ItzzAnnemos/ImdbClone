package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;

import java.util.List;
import java.util.Optional;

public interface MediaApplicationService<C, D> {

    List<D> findAll();

    Optional<D> findById(Long id);

    Optional<D> save(C createDto);

    Optional<D> update(Long id, C createDto);

    void delete(Long id);

    List<D> findTopRated();

    List<D> findRecent();

    List<DisplayCardMediaDto> findSimilar(Long id);
}