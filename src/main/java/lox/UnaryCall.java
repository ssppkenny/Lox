package lox;

public class UnaryCall implements Unary {

    private Call call;

    public UnaryCall(Call call) {
        this.call = call;
    }
}
