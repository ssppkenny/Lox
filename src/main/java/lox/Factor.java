package lox;

import java.util.List;
import java.util.Map;

public class Factor {

    private Unary unary;

    private List<UnaryElement> unaries;

    public Factor(Unary unary, List<UnaryElement> unaries) {
        this.unary = unary;
        this.unaries = unaries;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "unary=" + unary +
                ", unaries=" + unaries +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        Object d = unary.eval(env);

        for (UnaryElement elt : unaries) {
            String sign = elt.getSign();
            switch (sign) {
                case "/":
                    Double d1 = (Double) elt.getUnary().eval(env);
                    d = (Double) d / d1;
                    break;
                case "*":
                    Double d2 = (Double) elt.getUnary().eval(env);
                    d = (Double) d * d2;
                    break;
                default:
                    break;

            }
        }
        return d;
    }
}
