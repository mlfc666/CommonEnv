package week4.framework.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class JwtUtils {
    private static final String SECRET = "CommonEnv-Mlfc-1323230180"; // 签名密钥

    public static String createToken(String payload) {
        try {
            String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\"}".getBytes());
            String body = Base64.getUrlEncoder().encodeToString(payload.getBytes());
            String signature = hmacSha256(header + "." + body);
            return header + "." + body + "." + signature;
        } catch (Exception e) { return null; }
    }

    public static boolean validate(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String expectedSig = hmacSha256(parts[0] + "." + parts[1]);
            return expectedSig.equals(parts[2]); // 校验签名是否匹配
        } catch (Exception e) { return false; }
    }

    private static String hmacSha256(String data) throws Exception {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        sha256.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getUrlEncoder().encodeToString(sha256.doFinal(data.getBytes()));
    }
}