package week4.framework.models;

public class ApiResponse {
    private final int code;
    private final String message;
    private final Object data;

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}