package lox;

import java.util.Map;

public class UnaryCall implements Unary {

    private Call call;

    public UnaryCall(Call call) {
        this.call = call;
    }

    @Override
    public String toString() {
        return "UnaryCall{" +
                "call=" + call +
                '}';
    }


    @Override
    public Object eval(Map<String, Object> env) {
        return call.eval(env);
    }
}
