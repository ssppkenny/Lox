package lox;

public class TermElement {

    private String sign;

    private Term term;

    public TermElement(String sign, Term term) {
        this.sign = sign;
        this.term = term;
    }

    @Override
    public String toString() {
        return "TermElement{" +
                "sign='" + sign + '\'' +
                ", term=" + term +
                '}';
    }

    public String getSign() {
        return sign;
    }

    public Term getTerm() {
        return term;
    }
}
