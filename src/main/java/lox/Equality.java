package lox;

import java.util.List;
import java.util.Map;

public class Equality {

    private Comparison comparison;
    private List<ComparisonElement> comparisons;

    public Equality(Comparison comparison, List<ComparisonElement> comparisons) {
        this.comparisons = comparisons;
        this.comparison = comparison;
    }

    @Override
    public String toString() {
        return "Equality{" +
                "comparison=" + comparison +
                ", comparisons=" + comparisons +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        Object eval = comparison.eval(env);
        boolean first = Utils.isTruthy(eval);
        if (comparisons.isEmpty()) {
            return eval;
        } else {
            for (ComparisonElement comparisonElement : comparisons) {
                String sign = comparisonElement.getComparisonSign();
                Comparison comparison1 = comparisonElement.getComparison();
                switch (sign) {
                    case "!=":
                        eval = !eval.equals(comparison1.eval(env));
                        if (!(Boolean) eval) {
                            return false;
                        }
                        eval = comparison1.eval(env);
                        break;
                    case "==":
                        eval = eval.equals(comparison1.eval(env));
                        if (!(Boolean) eval) {
                            return false;
                        }
                        eval = comparison1.eval(env);
                        break;
                }
            }
        }
        return true;
    }
}
