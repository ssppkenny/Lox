package lox;


import java.util.HashMap;
import java.util.Map;

public class Function {

    private String identifier;

    private Parameters parameters;
    private BlockStatement block;

    public Function(String identifier, Parameters parameters, BlockStatement block) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.block = block;
    }

    @Override
    public String toString() {
        return "Function{" +
                "identifier='" + identifier + '\'' +
                ", parameters=" + parameters +
                ", block=" + block +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public BlockStatement getBlock() {
        return block;
    }

    public Object eval(Map<String, Object> env, Map<String, Object> fargs) {
        Map<String, Object> newEnv = new HashMap<>();
        for (String key : env.keySet()) {
            newEnv.put(key, env.get(key));
        }
        for (String key : fargs.keySet()) {
            newEnv.put(key, fargs.get(key));
        }

        env.putAll(newEnv);

        return block.eval(env);
    }
}
