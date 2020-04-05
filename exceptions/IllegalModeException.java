package exceptions;

public class IllegalModeException extends Exception {
    public IllegalModeException(final String mode) {
        super("Unknown mode: " + mode);
    }
}
