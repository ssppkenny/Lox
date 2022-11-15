package lox;

import java.util.List;

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
}
