package lox;

import java.util.List;
import java.util.Map;

public class Comparison {

    private Term term;

    private List<TermElement> termElements;

    public Comparison(Term term, List<TermElement> termElements) {
        this.term = term;
        this.termElements = termElements;
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "term=" + term +
                ", termElements=" + termElements +
                '}';
    }


    public Object eval(Map<String, Object> env) {
        Object eval = term.eval(env);
        boolean first = Utils.isTruthy(eval);
        if (termElements.isEmpty()) {
            return eval;
        } else {
            for (TermElement termElement : termElements) {
                String sign = termElement.getSign();
                Term term1 = termElement.getTerm();
                switch (sign) {
                    case ">=":
                        eval = (Double) eval >= (Double) term1.eval(env);
                        if (!(Boolean) eval) {
                            return false;
                        }
                        eval = term1.eval(env);
                        break;
                    case "<=":
                        eval = (Double) eval <= (Double) term1.eval(env);
                        if (!(Boolean) eval) {
                            return false;
                        }
                        eval = term1.eval(env);
                        break;
                    case "<":
                        eval = (Double) eval < (Double) term1.eval(env);
                        if (!(Boolean) eval) {
                            return false;
                        }
                        eval = term1.eval(env);
                        break;
                    case ">":
                        eval = (Double) eval > (Double) term1.eval(env);
                        if (!(Boolean) eval) {
                            return false;
                        }
                        eval = term1.eval(env);
                        break;

                }
            }
        }
        return true;
    }
}
