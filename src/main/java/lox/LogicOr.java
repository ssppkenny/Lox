package lox;

import java.util.List;

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
}
