package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.helpers.JwtHelper;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.model.dto.*;
import mk.ukim.finki.imdbclone.service.application.UserApplicationService;
import mk.ukim.finki.imdbclone.service.domain.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserService userService;
    private final JwtHelper jwtHelper;

    public UserApplicationServiceImpl(UserService userService,
                                      JwtHelper jwtHelper) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Optional<DisplayUserDto> register(CreateUserDto createUserDto) {
        User user = userService.register(
                createUserDto.username(),
                createUserDto.password(),
                createUserDto.repeatPassword(),
                createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.email()
        );
        return Optional.of(DisplayUserDto.from(user));
    }

    @Override
    public Optional<LoginResponseDto> login(LoginUserDto loginUserDto) {
        User user = userService.login(
                loginUserDto.username(),
                loginUserDto.password()
        );

        String token = jwtHelper.generateToken(user);

        return Optional.of(new LoginResponseDto(token));
    }

    @Override
    public Optional<DisplayUserDto> findByUsername(String username) {
        return userService.getUserByUsername(username)
                .map(DisplayUserDto::from);
    }

    @Override
    public Optional<DisplayUserDto> addMediaToWatchlist(String username, Long mediaId) {
        User user = userService.addMediaToWatchlist(username, mediaId);
        return Optional.of(DisplayUserDto.from(user));
    }

    @Override
    public Optional<DisplayUserDto> removeMediaFromWatchlist(String username, Long mediaId) {
        User user = userService.removeMediaFromWatchlist(username, mediaId);
        return Optional.of(DisplayUserDto.from(user));
    }

    @Override
    public List<DisplayCardMediaDto> getWatchlist(String username) {
        return userService.getWatchlist(username).stream()
                .map(DisplayCardMediaDto::from)
                .toList();
    }

    @Override
    public boolean isMediaInWatchlist(String username, Long mediaId) {
        return userService.isMediaInWatchlist(username, mediaId);
    }
}