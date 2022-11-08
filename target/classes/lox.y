%{
  import java.io.*;
  import java.lang.RuntimeException;
  import java.lang.Exception;
%}
%token LEFT_PAREN RIGHT_PAREN LEFT_BRACE RIGHT_BRACE
%token COMMA DOT MINUS PLUS
%token SEMICOLON SLASH STAR
%token BANG_EQUALS EQUALS_EQUALS EQUALS
%token GREATER_EQUALS GREATER LESS_EQUALS LESS
%token AND CLASS ELSE FALSE FUN FOR
%token IF NIL OR PRINT RETURN SUPER
%token THIS TRUE VAR WHILE
%token IDENTIFIER STRING NUMBER
%%
program : declarations {
	$$ = Parser.node("program", $1);
}

declarations: /* empty */
	| declarations declaration {
	$$ = Parser.node("declarations", $1, $2);
	}

declaration: varDecl
	| statement
{

	$$ = Parser.node("declaration", $1);
}

varDecl: VAR IDENTIFIER optVarDecl
{
	$$ = Parser.node("varDecl", $3);
}

optVarDecl : SEMICOLON
	| EQUALS expression  SEMICOLON {
	$$ = Parser.node("optVarDecl", $2);
	}

statement: exprStmt
        | ifStmt
	| printStmt
	| whileStmt
	| blockStmt


ifStmt: IF LEFT_PAREN expression RIGHT_PAREN statement optElse

optElse: /* empty */
	| ELSE statement

blockStmt:  LEFT_BRACE declarations RIGHT_BRACE

exprStmt: expression SEMICOLON

printStmt: PRINT expression SEMICOLON

whileStmt: WHILE LEFT_PAREN expression RIGHT_PAREN statement

expression: term

term: NUMBER | STRING | IDENTIFIER

%%

private Yylex lexer;

private int yylex() {
	try {
		String s = lexer.yytext();
		System.out.println(s);
		return lexer.yylex();
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

public Parser(Reader r) {
    lexer = new Yylex(r, this);
}

 public static ParserVal node(String s, ParserVal... parserValues) {

        return new ParserVal(s);

    }

public void yyerror (String error) {
    System.err.println ("Error: " + error);
}

public static void main(String args[]) throws IOException {

    Parser yyparser = null;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }

     int yyparse = yyparser.yyparse();
     ParserVal[] valstk1 = yyparser.valstk;

  }

