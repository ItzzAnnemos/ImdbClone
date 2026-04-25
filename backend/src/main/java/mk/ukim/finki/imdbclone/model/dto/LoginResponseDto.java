package mk.ukim.finki.imdbclone.model.dto;

public record LoginResponseDto(
        String token,
        DisplayUserDto user
) {
}