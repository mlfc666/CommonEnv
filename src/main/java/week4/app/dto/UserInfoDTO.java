package week4.app.dto;

public record UserInfoDTO(
        String username,
        long createTime,
        Integer id,
        String avatar
) {
}