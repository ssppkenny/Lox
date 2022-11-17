package lox;

import java.util.Map;

public class NormalExpression implements OptExpression {

    private Expression expression;

    public NormalExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "NormalExpression{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return expression.eval(env);
    }
}
