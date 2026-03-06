package week4.framework.exception;

public class InternalErrorException extends BusinessException {
    public InternalErrorException(String message) { super(500, message); }
}
