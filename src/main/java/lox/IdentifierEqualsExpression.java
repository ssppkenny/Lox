package lox;

public class IdentifierEqualsExpression implements Assignment {

    private Expression expression;

    public IdentifierEqualsExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "IdentifierEqualsExpression{" +
                "expression=" + expression +
                '}';
    }
}
