package lox;

public class NormalElse implements Else {

    private Statement statement;

    public NormalElse(Statement statement) {
        this.statement = statement;
    }
}
