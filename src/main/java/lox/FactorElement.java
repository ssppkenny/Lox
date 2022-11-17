package lox;

public class FactorElement {

    private String sign;

    private Factor factor;

    public FactorElement(String sign, Factor factor) {
        this.sign = sign;
        this.factor = factor;
    }

    @Override
    public String toString() {
        return "FactorElement{" +
                "sign='" + sign + '\'' +
                ", factor=" + factor +
                '}';
    }

    public String getSign() {
        return sign;
    }

    public Factor getFactor() {
        return factor;
    }
}
