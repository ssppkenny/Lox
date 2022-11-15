package lox;

import java.util.List;

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
}
