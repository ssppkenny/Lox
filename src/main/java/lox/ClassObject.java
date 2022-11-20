package lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassObject {

    private String className;

    private List<Function> functions;

    private Map<String, Object> env;

    public ClassObject(String className, List<Function> functions, Map<String, Object> env) {
        this.className = className;
        this.functions = functions;
        this.env = new HashMap<>();
        this.env.putAll(env);
    }

    public Object callFunction(String functionName, Map<String, Object> fArgs) {
        Function function = findFunction(functionName);
        return function.eval(env, fArgs);
    }

    public Function getFunction(String functionName) {
        Function function = findFunction(functionName);
        return function;
    }

    private Function findFunction(String functionName) {
        Function function = functions.stream().filter(f -> f.getIdentifier().equals(functionName)).findFirst().orElse(null);
        return function;
    }


    public void setProperty(String name, Object value) {
        env.put(name, value);
    }

    public Object getProperty(String name) {
        return env.get(name);
    }
}
