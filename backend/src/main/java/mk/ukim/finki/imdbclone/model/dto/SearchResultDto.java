package mk.ukim.finki.imdbclone.model.dto;

import java.util.List;

public record SearchResultDto(
        List<SearchItemDto> results,
        String interpretedAs
) {
}