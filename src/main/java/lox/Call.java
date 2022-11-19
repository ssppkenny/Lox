package lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Call {

    private Primary primary;
    private List<PrimaryBlock> primaryBlock;

    public Call(Primary primary, List<PrimaryBlock> primaryBlock) {
        this.primary = primary;
        this.primaryBlock = primaryBlock;
    }

    @Override
    public String toString() {
        return "Call{" +
                "primary=" + primary +
                ", primaryBlock=" + primaryBlock +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        if (primaryBlock.isEmpty()) {
            return primary.eval(env);
        } else if (primary instanceof Primary.Identifier) {
            PrimaryBlock bl = primaryBlock.get(0);
            if (bl instanceof PrimaryBlock.ArgumentsGroup) {
                PrimaryBlock.ArgumentsGroup ag = (PrimaryBlock.ArgumentsGroup) bl;
                List<Object> paramValues = (List) ag.eval(env);
                String fName = ((Primary.Identifier) primary).getIdentifier();
                Function f = (Function) env.get("function:" + fName);
                List<String> parameterNames = f.getParameters().getParameterNames();
                Map<String, Object> fArgs = new HashMap<>();
                for (int i = 0; i < parameterNames.size(); i++) {
                    fArgs.put(parameterNames.get(i), paramValues.get(i));
                }

                Object o = FunctionCache.get(fName, paramValues);
                if (o == null) {
                    try {
                        Object retVal = f.eval(env, fArgs);
                        FunctionCache.put(fName, paramValues, retVal);
                        return retVal;
                    } catch (StackOverflowError e) {
                        throw new RuntimeException("StackOverflowError");
                    }
                } else {
                    return o;
                }

            }


            return null;
        }
        return null;

    }
}
