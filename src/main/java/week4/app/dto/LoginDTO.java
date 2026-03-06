package week4.app.dto;

/**
 * 登录数据传输对象 Record
 *
 * @param username 用户名
 * @param password 密码
 * @param cfToken  cloudflare 令牌
 */
public record LoginDTO(
        String username,
        String password,
        String cfToken
) {}