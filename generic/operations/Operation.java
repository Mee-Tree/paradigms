package generic.operations;

import exceptions.EvaluatingException;
import exceptions.ParsingException;

public interface Operation<T extends Number> {
    T parse(String a) throws ParsingException;
    T add(T a, T b) throws EvaluatingException;
    T subtract(T a, T b) throws EvaluatingException;
    T multiply(T a, T b) throws EvaluatingException;
    T divide(T a, T b) throws EvaluatingException;
    T negate(T a) throws EvaluatingException;

    T count(T a);
    T max(T a, T b);
    T min(T a, T b);
}
