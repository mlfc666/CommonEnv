package week4.framework.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import week4.framework.exception.InternalErrorException;
import week4.framework.exception.UnauthorizedException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class JwtUtils {
    private static final String SECRET = "CommonEnv_Mlfc_XiaoATask";
    private static final Gson gson = new Gson();

    /**
     * 创建 JWTToken
     *
     * @param subject          用户唯一标识 (sub)
     * @param ttlSeconds       有效期（秒）
     * @param additionalClaims 额外的自定义信息
     */
    public static String createToken(String subject, long ttlSeconds, Map<String, Object> additionalClaims) {
        try {
            // HS256 算法
            String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String encodedHeader = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));

            // 构造 Payload
            long nowMillis = System.currentTimeMillis();
            Map<String, Object> payload = new HashMap<>();

            // 注入额外 Claims
            if (additionalClaims != null) {
                payload.putAll(additionalClaims);
            }

            // 注入 JWT 标准字段
            payload.put("sub", subject);
            payload.put("iat", nowMillis / 1000);
            payload.put("exp", (nowMillis / 1000) + ttlSeconds);

            String encodedPayload = base64UrlEncode(gson.toJson(payload).getBytes(StandardCharsets.UTF_8));

            // 签名
            String signature = hmacSha256(encodedHeader + "." + encodedPayload);

            return encodedHeader + "." + encodedPayload + "." + signature;
        } catch (Exception e) {
            throw new InternalErrorException("Token 生成失败: " + e.getMessage());
        }
    }

    /**
     * 校验 Token 有效性
     *
     * @param token 原始 JWT 字符串
     */
    public static void validate(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new Exception(); // 格式错，进 catch

            // 签名校验
            String expectedSignature = hmacSha256(parts[0] + "." + parts[1]);
            if (!expectedSignature.equals(parts[2])) throw new Exception(); // 签名错，进 catch

            // 解析 Payload
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            JsonObject json = gson.fromJson(new String(payloadBytes, StandardCharsets.UTF_8), JsonObject.class);

            // 过期校验
            if (json.has("exp")) {
                long exp = json.get("exp").getAsLong();
                if ((System.currentTimeMillis() / 1000) > exp) {
                    // 只有过期可以考虑单独给个提示，或者同样统一处理
                    throw new UnauthorizedException("登录已过期");
                }
            }
        } catch (UnauthorizedException e) {
            throw e; // 转发过期异常
        } catch (Exception e) {
            // 所有的格式错误、签名错误、解密失败，统一报这个
            throw new UnauthorizedException("身份验证失败");
        }
    }

    /**
     * 从 Token 中提取 Payload 的特定字段
     */
    public static JsonObject getPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            byte[] bytes = Base64.getUrlDecoder().decode(parts[1]);
            return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class);
        } catch (Exception e) {
            throw new UnauthorizedException("解析 Token 失败");
        }
    }

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static String hmacSha256(String data) throws Exception {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        sha256.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = sha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return base64UrlEncode(hash);
    }
}