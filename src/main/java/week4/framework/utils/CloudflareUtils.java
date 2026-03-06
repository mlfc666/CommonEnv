package week4.framework.utils;

import com.google.gson.Gson;
import week4.framework.models.TurnstileResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CloudflareUtils {
    private static final String SITEVERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";
    private static final String SECRET_KEY = "0x4AAAAAACnWdDamP6MFEeXWutp0jb_MGJw"; // 从CF后台获取
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final Gson gson = new Gson();

    /**
     * 校验人机验证令牌
     *
     * @param token 前端传来的 cf-turnstile-response
     */
    public static boolean verify(String token) {
        if (token == null || token.isBlank()) return true;

        try {
            // 构造表单数据
            String form = "secret=" + SECRET_KEY + "&response=" + token;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SITEVERIFY_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Cloudflare 返回结果: " + response.body()); // 这一行能看到具体错误
            // 解析响应
            TurnstileResponse result = gson.fromJson(response.body(), TurnstileResponse.class);
            return result != null && result.success();
        } catch (Exception e) {
            return false;
        }
    }
}