package lox;

public class CallEqualsExpression implements Assignment {

    private Expression expression;

    public CallEqualsExpression(Expression expression) {
        this.expression = expression;
    }
}
