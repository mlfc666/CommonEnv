package week4.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class PasswordUtils {

    // 严禁直接存储，必须加盐
    private static final String SALT = "CommonEnv_Mlfc_Salt_XiaoATask";

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // 将盐值与输入拼接
            String salted = input + SALT;
            byte[] hash = md.digest(salted.getBytes(StandardCharsets.UTF_8));

            // 转为十六进制
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found");
        }
    }
    public static boolean verify(String input, String hashed) {
        return hash(input).equals(hashed);
    }
}