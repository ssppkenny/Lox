package lox;

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
}
