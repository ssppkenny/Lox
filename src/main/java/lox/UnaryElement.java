package lox;

public class UnaryElement {

    private String sign;

    private Unary unary;

    public UnaryElement(String sign, Unary unary) {
        this.sign = sign;
        this.unary = unary;
    }

    @Override
    public String toString() {
        return "UnaryElement{" +
                "sign='" + sign + '\'' +
                ", unary=" + unary +
                '}';
    }
}
