package exceptions;

public class IncorrectFormatException extends Exception {
    public IncorrectFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFormatException(String message) {
        super(message);
    }
}
