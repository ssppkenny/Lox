package lox;

import java.util.List;

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
}
