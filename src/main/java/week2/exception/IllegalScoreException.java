package week2.exception;

public class IllegalScoreException extends RuntimeException {

    public IllegalScoreException(String message) {
        super(message);
    }
}