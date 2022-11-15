package lox;

public class ReturnStatement implements Statement {
    private OptExpression optExpression;

    public ReturnStatement(OptExpression optExpression) {
        this.optExpression = optExpression;
    }
}
