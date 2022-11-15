package lox;

public class ComparisonElement {
    private String comparisonSign;

    private Comparison comparison;

    public ComparisonElement(String comparisonSign, Comparison comparison) {
        this.comparisonSign = comparisonSign;
        this.comparison = comparison;
    }

    @Override
    public String toString() {
        return "ComparisonElement{" +
                "comparisonSign='" + comparisonSign + '\'' +
                ", comparison=" + comparison +
                '}';
    }
}
