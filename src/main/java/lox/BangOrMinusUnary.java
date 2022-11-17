package lox;

import java.util.Map;

public class BangOrMinusUnary implements Unary {

    private String sign;
    private Unary unary;

    public BangOrMinusUnary(String sign, Unary unary) {
        this.sign = sign;
        this.unary = unary;
    }

    @Override
    public String toString() {
        return "BangOrMinusFactor{" +
                "sign='" + sign + '\'' +
                ", unary=" + unary +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        if (sign.equals("!")) {
            Boolean b = (Boolean) unary.eval(env);
            return !b;
        } else {
            Double d = (Double) unary.eval(env);
            return -d;
        }
    }
}
