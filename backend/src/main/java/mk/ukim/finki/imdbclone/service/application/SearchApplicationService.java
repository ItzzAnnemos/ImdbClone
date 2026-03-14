package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;

public interface SearchApplicationService {
    SearchResultDto search(String query);
}