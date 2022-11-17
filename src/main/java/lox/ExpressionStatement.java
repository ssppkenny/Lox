package lox;

import java.util.Map;

public class ExpressionStatement implements Statement {

    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExpressionStatement{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return expression.eval(env);
    }
}
