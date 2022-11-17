package lox;

import java.util.Map;

public class ExpressionVariableDeclaration implements OptionalVariableDeclaration {

    private final Expression expression;

    public ExpressionVariableDeclaration(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExpressionVariableDeclaration{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return expression.eval(env);
    }
}
