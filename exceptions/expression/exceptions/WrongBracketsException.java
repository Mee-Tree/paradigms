package expression.exceptions;

public class WrongBracketsException extends ParsingException {
    public WrongBracketsException(String message) {
        super("Wrong amount of brackets:\t\t" + message);
    }
}
