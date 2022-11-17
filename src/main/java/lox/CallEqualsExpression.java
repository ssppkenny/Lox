package lox;

import java.util.Map;

public class CallEqualsExpression implements Assignment {

    private Expression expression;

    public CallEqualsExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "CallEqualsExpression{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }
}
