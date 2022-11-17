package lox;

import java.util.Map;

public class Expression {

    private Assignment assignment;


    public Expression(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "assignment=" + assignment +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        return assignment.eval(env);
    }
}
