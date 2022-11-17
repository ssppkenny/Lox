package lox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Comparisons {

    List<Comparisons> comparisons;

    String sign;

    Comparison comparison;

    public Comparisons() {
        this.comparisons = new ArrayList<>();
    }

    public Comparisons(List<Comparisons> comparisons, String sign, Comparison comparison) {
        this.comparisons = comparisons;
        this.sign = sign;
        this.comparison = comparison;
    }

    public boolean isEmpty() {
        return comparisons.isEmpty();
    }

    @Override
    public String toString() {
        return "Comparisons{" +
                "comparisons=" + comparisons +
                ", sign='" + sign + '\'' +
                ", comparison=" + comparison +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        if (comparisons.isEmpty()) {
            return comparison.eval(env);
        }

        Boolean v = Boolean.TRUE;
        for (Comparisons elt : comparisons) {

        }
        return Boolean.TRUE;
    }
}
