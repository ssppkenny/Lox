package lox;

public class NormalExpression implements OptExpression {

    private Expression expression;

    public NormalExpression(Expression expression) {
        this.expression = expression;
    }

}
