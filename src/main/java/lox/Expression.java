package lox;

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
}
