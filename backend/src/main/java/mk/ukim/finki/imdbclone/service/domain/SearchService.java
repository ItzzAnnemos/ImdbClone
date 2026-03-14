package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;

public interface SearchService {

    SearchResultDto search(String query);

}