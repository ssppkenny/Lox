package lox;

public class FactorElement {

    private String sign;

    private Factor factor;

    public FactorElement(String sign, Factor factor) {
        this.sign = sign;
        this.factor = factor;
    }
}
