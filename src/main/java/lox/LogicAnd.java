package lox;

import java.util.List;

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
}
