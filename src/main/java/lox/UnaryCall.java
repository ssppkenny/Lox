package lox;

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
}
