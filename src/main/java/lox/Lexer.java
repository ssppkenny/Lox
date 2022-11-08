package lox;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    static Yylex lex;

    static int lineNo = 0;

    static List<Token> tokens = new ArrayList<>();

    static void incrementLineNumber() {
        lineNo++;
    }

    static int scan(int cat) {
        String value = lex.yytext();
        if (cat == 36) {
            tokens.add(new Token(cat, value.substring(1, value.length()-1)));
        } else {
            tokens.add(new Token(cat, lex.yytext()));
        }
        return cat;
    }

    public static void main(String[] args) throws Exception {

        lex = new Yylex(new FileReader(args[0]));
        while (lex.yylex() != Yylex.YYEOF) {
            System.out.println(lex.yytext());
        }

        System.out.println(tokens);

    }


    static void error(String s) {
        System.out.println(s);
    }

}
