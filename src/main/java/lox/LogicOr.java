package lox;

import java.util.List;
import java.util.Map;

public class LogicOr implements Assignment {

    private List<LogicAnd> logicAnds;

    public LogicOr(List<LogicAnd> logicAnds) {
        this.logicAnds = logicAnds;
    }

    @Override
    public String toString() {
        return "LogicOr{" +
                "logicAnds=" + logicAnds +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {

        if (logicAnds.size() == 1) {
            return logicAnds.get(0).eval(env);
        } else {
            for (LogicAnd logicAnd : logicAnds) {
                if ((Boolean) logicAnd.eval(env) == Boolean.TRUE) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }
}
