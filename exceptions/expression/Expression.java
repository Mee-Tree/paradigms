package expression;

import expression.exceptions.EvaluatingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Expression extends ToMiniString {
    int evaluate(int x) throws EvaluatingException;
}