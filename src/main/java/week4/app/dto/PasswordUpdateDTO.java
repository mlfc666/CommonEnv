package week4.app.dto;

public record PasswordUpdateDTO(
        String oldPassword,
        String newPassword
) {
}