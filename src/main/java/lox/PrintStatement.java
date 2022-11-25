package lox;

import java.util.Map;

public class PrintStatement implements Statement {

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "PrintStatement{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        Object o = expression.eval(env);
        if (o instanceof Double) {
            Double d = (Double) o;
            System.out.println(Utils.show(d));
        } else {
            System.out.println(o);
        }
        return null;
    }
}
