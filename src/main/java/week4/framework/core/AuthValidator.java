package week4.framework.core;

import com.google.gson.JsonObject;

// 允许业务层自定义校验逻辑
public interface AuthValidator {
    // 校验 Token 负载信息
    void validate(JsonObject payload);
}