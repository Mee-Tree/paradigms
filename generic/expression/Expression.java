package generic.expression;

import exceptions.EvaluatingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Expression<T> extends ToMiniString {
    T evaluate(T x) throws EvaluatingException;
}