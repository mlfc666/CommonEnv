package week4.app.dto;

public record LoginDTO(
        String username,
        String password,
        String cfToken
) {}