package week4.utils;

public class PathUtils {
    // 统一剥离 /web-xxxx 前缀并映射到 index.html
    public static String sanitizePath(String path) {
        if (path != null && path.startsWith("/web-")) {
            path = path.replaceFirst("^/web-[^/]+", "");
        }
        if (path == null || path.trim().isEmpty() || path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}