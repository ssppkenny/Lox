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
                    Object d1 = elt.getFactor().eval(env);
                    d = (Double) d - (Double) d1;
                    break;
                case "+":
                    Object d2 = elt.getFactor().eval(env);
                    if (d instanceof String) {
                        if (d2 instanceof String) {
                            d = d + (String) d2;
                        } else {
                            d = d + d2.toString();
                        }
                    } else {
                        if (d2 instanceof String) {
                            d = d + (String) d2;
                        } else {
                            d = d + d2.toString();
                        }
                    }
                    break;
                default:
                    break;

            }
        }
        return d;
    }
}
