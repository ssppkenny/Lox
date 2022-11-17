package lox;

import java.util.Map;

public class NormalElse implements Else {

    private Statement statement;

    public NormalElse(Statement statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "NormalElse{" +
                "statement=" + statement +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return statement.eval(env);
    }
}
