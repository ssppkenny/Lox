package lox;

public class Function {

    private Object parameters;
    private Object block;

    public Function(Object parameters, Object block) {
        this.parameters = parameters;
        this.block = block;
    }
}
