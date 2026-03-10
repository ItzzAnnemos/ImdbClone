package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreatePersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayPersonDto;

import java.util.List;
import java.util.Optional;

public interface PersonApplicationService {

    List<DisplayPersonDto> findAll();

    Optional<DisplayPersonDto> findById(Long id);

    Optional<DisplayPersonDto> save(CreatePersonDto personDto);

    Optional<DisplayPersonDto> update(Long id, CreatePersonDto personDto);

    void delete(Long id);

    List<DisplayPersonDto> search(String name);
}