package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserApplicationService {

    Optional<DisplayUserDto> register(CreateUserDto createUserDto);

    Optional<LoginResponseDto> login(LoginUserDto loginUserDto);

    Optional<DisplayUserDto> findByUsername(String username);

    Optional<DisplayUserDto> addMediaToWatchlist(String username, Long mediaId);

    Optional<DisplayUserDto> removeMediaFromWatchlist(String username, Long mediaId);

    List<DisplayCardMediaDto> getWatchlist(String username);

    boolean isMediaInWatchlist(String username, Long mediaId);
}