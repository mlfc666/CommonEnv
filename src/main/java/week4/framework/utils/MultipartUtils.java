package week4.framework.utils;

import week4.framework.models.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MultipartUtils {

    // 解析 Multipart 请求体
    public static Map<String, MultipartFile> parse(byte[] body, String boundary) {
        Map<String, MultipartFile> files = new HashMap<>();
        String bodyStr = new String(body, StandardCharsets.ISO_8859_1);
        String[] parts = bodyStr.split("--" + boundary);

        for (String part : parts) {
            if (part.contains("filename=\"")) {
                String name = extractAttribute(part, "name=\"");
                String filename = extractAttribute(part, "filename=\"");
                String contentType = part.split("Content-Type: ")[1].split("\r\n")[0];

                int start = part.indexOf("\r\n\r\n") + 4;
                int end = part.lastIndexOf("\r\n");

                String dataStr = part.substring(start, end);
                byte[] data = dataStr.getBytes(StandardCharsets.ISO_8859_1);

                files.put(name, new MultipartFile(filename, contentType, data));
            }
        }
        return files;
    }

    private static String extractAttribute(String part, String attr) {
        int start = part.indexOf(attr) + attr.length();
        int end = part.indexOf("\"", start);
        return part.substring(start, end);
    }
}