package week4.framework.models;

public record MultipartFile(String fileName, String contentType, byte[] data) {
}