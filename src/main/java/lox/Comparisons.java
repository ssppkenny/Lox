package lox;

import java.util.ArrayList;
import java.util.List;

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
}
