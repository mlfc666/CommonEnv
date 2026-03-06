package week4.framework.models;

public record ApiResponse(
        int code,
        String message,
        Object data
) {
}