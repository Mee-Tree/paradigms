package expression.parser;

import expression.CommonExpression;
import expression.exceptions.ParsingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser<T> {
    CommonExpression<T> parse(String expression) throws ParsingException;
}