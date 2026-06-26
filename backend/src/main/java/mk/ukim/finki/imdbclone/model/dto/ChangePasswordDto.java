package mk.ukim.finki.imdbclone.model.dto;

public record ChangePasswordDto(
        String currentPassword,
        String newPassword,
        String repeatNewPassword
) {
}
