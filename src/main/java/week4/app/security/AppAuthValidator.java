package week4.app.security;

import com.google.gson.JsonObject;
import week4.app.services.UserService;
import week4.framework.core.AuthValidator;
import week4.framework.exception.UnauthorizedException;

public class AppAuthValidator implements AuthValidator {
    private final UserService userService;

    public AppAuthValidator(UserService userService) {
        this.userService = userService; // 注入 Service
    }

    @Override
    public void validate(JsonObject payload) {
        Integer userId = payload.get("uid").getAsInt();
        long iat = payload.get("iat").getAsLong();

        // 统一调用 Service 层的方法
        if (!userService.isTokenValid(userId, iat)) {
            throw new UnauthorizedException("登录已失效，请重新登录");
        }
    }
}