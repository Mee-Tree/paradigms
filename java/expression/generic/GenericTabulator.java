package expression.generic;

import expression.TripleExpression;
import expression.operations.*;
import expression.parser.*;
import expression.exceptions.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Operation<?>> MODES = Map.of(
    		"s",    new ShortOperation(),
    		"l",    new LongOperation(),
            "d",    new DoubleOperation(),
            "f",    new FloatOperation(),
            "b",    new ByteOperation(),
            "i",    new IntegerOperation(true),
            "u",    new IntegerOperation(false),
            "bi",   new BigIntegerOperation()

    );

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws IllegalModeException, ParsingException {
        return getTable(expression, getOperation(mode), x1, x2, y1, y2, z1, z2);
    }

    private Operation<?> getOperation(final String mode) throws IllegalModeException {
        if (MODES.containsKey(mode)) {
            return MODES.get(mode);
        }
        throw new IllegalModeException(mode);
    }

    private <T extends Number> Object[][][] getTable(String expression, Operation<T> operation, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        Object[][][] tab = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        Parser<T> parser = new ExpressionParser<>(operation);
        TripleExpression<T> exp = parser.parse(expression);

        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    try {
                        tab[x - x1][y - y1][z - z1] = exp.evaluate(
                                operation.parse(Integer.toString(x)),
                                operation.parse(Integer.toString(y)),
                                operation.parse(Integer.toString(z))
                        );
                    } catch (EvaluatingException ignored) {}
                }
            }
        }
        return tab;
    }
}
