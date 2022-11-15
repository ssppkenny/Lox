package lox;

public class Terms {

    private Terms terms;

    private String sign;

    private Term term;

    public Terms(Terms terms, String sign, Term term) {
        this.terms = terms;
        this.sign = sign;
        this.term = term;
    }

    @Override
    public String toString() {
        return "Terms{" +
                "terms=" + terms +
                ", sign='" + sign + '\'' +
                ", term=" + term +
                '}';
    }
}
