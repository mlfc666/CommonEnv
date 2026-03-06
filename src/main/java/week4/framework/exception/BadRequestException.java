package week4.framework.exception;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) { super(400, message); }
}

