package lox;

import java.util.List;
import java.util.Map;

public class LogicAnd {

    List<Equality> equalities;

    public LogicAnd(List<Equality> equalities) {
        this.equalities = equalities;
    }

    @Override
    public String toString() {
        return "LogicAnd{" +
                "equalities=" + equalities +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        if (equalities.size() == 1) {
            return equalities.get(0).eval(env);
        } else {
            for (Equality equality : equalities) {
                if (!(Boolean) equality.eval(env)) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }
}
