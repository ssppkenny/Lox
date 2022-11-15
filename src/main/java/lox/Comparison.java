package lox;

import java.util.List;

public class Comparison {

    private Term term;

    private List<TermElement> termElements;

    public Comparison(Term term, List<TermElement> termElements) {
        this.term = term;
        this.termElements = termElements;
    }
}
