package week4.utils;

public class MimeUtils {
    public static String getContentType(String path) {
        String p = path.toLowerCase();
        if (p.endsWith(".html")) return "text/html; charset=utf-8";
        if (p.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (p.endsWith(".css")) return "text/css; charset=utf-8";
        if (p.endsWith(".json")) return "application/json; charset=utf-8";
        if (p.endsWith(".webp")) return "image/webp";
        if (p.endsWith(".png")) return "image/png";
        return "application/octet-stream";
    }
}