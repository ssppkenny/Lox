package lox;

public class BangOrMinusFactor implements Unary {

    private String sign;
    private Factor factor;

    public BangOrMinusFactor(String sign, Factor factor) {
        this.sign = sign;
        this.factor = factor;
    }

    @Override
    public String toString() {
        return "BangOrMinusFactor{" +
                "sign='" + sign + '\'' +
                ", factor=" + factor +
                '}';
    }
}
