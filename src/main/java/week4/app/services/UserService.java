package week4.app.services;

import week4.app.dto.LoginDTO;
import week4.framework.models.MultipartFile;

import java.util.Map;

public interface UserService {
    // 验证参数并执行用户注册逻辑
    Map<String, Object> register(LoginDTO dto);

    // 校验身份凭证并签发JWT令牌
    Map<String, Object> login(LoginDTO dto);

    // 处理二进制头像上传并关联至用户
    String uploadAvatar(Integer userId, MultipartFile file);

    // 处理用户退出登录逻辑
    void logout(Integer userId);

    // 校验 Token 的签发时间是否早于最后登出时间
    boolean isTokenValid(Integer userId, Long iat);
}