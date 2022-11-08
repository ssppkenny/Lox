package lox;

public class Token {

    int cat;

    String text;

    public Token(int cat , String text) {
        this.cat = cat;
        this.text = text;
    }

    public int getCat() {
        return cat;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Token{" +
                "cat=" + cat +
                ", text='" + text + '\'' +
                '}';
    }
}
