package exceptions;

public class WrongConstException extends ParsingException {
    public WrongConstException(String message) {
        super("Const is incorrect. " + message);
    }
}
