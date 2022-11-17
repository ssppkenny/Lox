package lox;

import java.util.List;
import java.util.Map;

public class Term {

    private Factor factor;

    private List<FactorElement> factors;

    public Term(Factor factor, List<FactorElement> factors) {
        this.factor = factor;
        this.factors = factors;
    }

    @Override
    public String toString() {
        return "Term{" +
                "factor=" + factor +
                ", factors=" + factors +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        Object d = factor.eval(env);

        for (FactorElement elt : factors) {
            String sign = elt.getSign();
            switch (sign) {
                case "-":
                    Double d1 = (Double) elt.getFactor().eval(env);
                    d = (Double) d - d1;
                    break;
                case "+":
                    Double d2 = (Double) elt.getFactor().eval(env);
                    d = (Double) d + d2;
                    break;
                default:
                    break;

            }
        }
        return d;
    }
}
