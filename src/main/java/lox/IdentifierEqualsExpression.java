package lox;

import java.util.Map;

public class IdentifierEqualsExpression implements Assignment {

    private Expression expression;

    private String identifier;

    public IdentifierEqualsExpression(String identifier, Expression expression) {
        this.expression = expression;
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "IdentifierEqualsExpression{" +
                "expression=" + expression +
                ", identifier='" + identifier + '\'' +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        Object value = expression.eval(env);
        env.put(identifier, value);
        return value;
    }
}
