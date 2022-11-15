package lox;

public class UnaryElement {

    private String sign;

    private Unary unary;

    public UnaryElement(String sign, Unary unary) {
        this.sign = sign;
        this.unary = unary;
    }
}
