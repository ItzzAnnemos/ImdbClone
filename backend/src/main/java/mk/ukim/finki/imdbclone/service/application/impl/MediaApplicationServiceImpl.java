package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.service.application.MediaApplicationService;
import mk.ukim.finki.imdbclone.service.domain.MediaService;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class MediaApplicationServiceImpl<T extends Media, C, D>
        implements MediaApplicationService<C, D> {

    protected final MediaService<T> mediaService;
    protected final Function<T, D> toDisplayDto;
    protected final Function<C, T> toEntity;

    protected MediaApplicationServiceImpl(MediaService<T> mediaService,
                                          Function<T, D> toDisplayDto,
                                          Function<C, T> toEntity) {
        this.mediaService = mediaService;
        this.toDisplayDto = toDisplayDto;
        this.toEntity = toEntity;
    }

    @Override
    public List<D> findAll() {
        return mediaService.getAll()
                .stream()
                .map(toDisplayDto)
                .toList();
    }

    @Override
    public Optional<D> findById(Long id) {
        return mediaService.getById(id)
                .map(toDisplayDto);
    }

    @Override
    public Optional<D> save(C createDto) {
        T entity = toEntity.apply(createDto);
        return Optional.of(
                toDisplayDto.apply(mediaService.create(entity))
        );
    }

    @Override
    public Optional<D> update(Long id, C createDto) {
        T entity = toEntity.apply(createDto);
        return Optional.of(
                toDisplayDto.apply(mediaService.update(id, entity))
        );
    }

    @Override
    public void delete(Long id) {
        mediaService.delete(id);
    }

    @Override
    public List<D> search(String title) {
        return mediaService.search(title)
                .stream()
                .map(toDisplayDto)
                .toList();
    }

    @Override
    public List<D> findTopRated() {
        return mediaService.getTopRated()
                .stream()
                .map(toDisplayDto)
                .toList();
    }

    @Override
    public List<D> findRecent() {
        return mediaService.getRecent()
                .stream()
                .map(toDisplayDto)
                .toList();
    }

    @Override
    public List<DisplayCardMediaDto> findSimilar(Long id) {
        return mediaService.findSimilar(id)
                .stream()
                .map(DisplayCardMediaDto::from)
                .toList();
    }
}