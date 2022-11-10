%{
  import java.io.*;
  import java.lang.RuntimeException;
  import java.lang.Exception;
%}
%token LEFT_PAREN RIGHT_PAREN LEFT_BRACE RIGHT_BRACE
%token COMMA DOT MINUS PLUS
%token SEMICOLON SLASH STAR
%token BANG BANG_EQUALS EQUALS_EQUALS EQUALS
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

declaration: classDecl
	| funDecl
	| varDecl
	| statement
{

	$$ = Parser.node("declaration", $1);
}

classDecl: CLASS IDENTIFIER optParentDecl LEFT_BRACE functions RIGHT_BRACE

optParentDecl:
	| LESS IDENTIFIER

functions:
	| functions function

funDecl: FUN function

function: IDENTIFIER LEFT_PAREN parameters RIGHT_PAREN block

parameters:
	| IDENTIFIER optIdentifiers

optIdentifiers: /*empty*/
	| optIdentifiers optIdentifier

optIdentifier: COMMA IDENTIFIER

varDecl: VAR IDENTIFIER optVarDecl SEMICOLON
{
	$$ = Parser.node("varDecl", $3);
}

optVarDecl :
	| EQUALS expression {
	$$ = Parser.node("optVarDecl", $2);
	}

block: LEFT_BRACE declarations RIGHT_BRACE

statement: exprStmt
	| forStmt
	| ifStmt
	| printStmt
	| returnStmt
	| whileStmt
	| block

forStmt: FOR LEFT_PAREN firstForInit optExpression SEMICOLON optExpression RIGHT_PAREN statement

returnStmt: RETURN optExpression SEMICOLON

optExpression: /*empty*/
	| expression

firstForInit: varDecl
	| exprStmt
	| SEMICOLON

exprStmt: expression SEMICOLON

ifStmt: IF LEFT_PAREN expression RIGHT_PAREN statement optElse

optElse: /* empty */
	| ELSE statement

printStmt: PRINT expression SEMICOLON

whileStmt: WHILE LEFT_PAREN expression RIGHT_PAREN statement

expression: assignment

assignment: IDENTIFIER EQUALS expression
	| call EQUALS expression
	| logic_or

logic_or: logic_and logic_ands

logic_ands: /* empty */
	| logic_ands OR logic_and

logic_and: equality equalities

equalities: /* empty */
	| equalities AND equality

equality: comparison comparisons

comparisons:
	| comparisons comparison_sign comparison

comparison_sign: BANG_EQUALS
	| EQUALS_EQUALS

comparison: term terms

terms: /* empty */
	| terms term_sign term

term_sign: GREATER
	| GREATER_EQUALS
	| LESS
	| LESS_EQUALS

term: factor factors

factors:
	| factors factor_sign factor

factor: unary unaries

unaries: /* empty */
	| unaries unary_sign unary

unary: bang_or_minus factor
	| call

bang_or_minus: BANG
	| MINUS

unary_sign: SLASH
	| STAR

factor_sign: MINUS
	| PLUS

arguments:
	| expression
	| expression expressions

expressions: /* empty */
	| expressions COMMA expression

call: primary primary_blocks


arguments_group: /*empty*/
	| arguments

primary_block: LEFT_PAREN arguments_group RIGHT_PAREN
	| DOT IDENTIFIER

primary_blocks: /*empty*/
	| primary_blocks primary_block

primary: TRUE
	| FALSE
	| NIL
	| THIS
	| NUMBER
	| STRING
	| IDENTIFIER
	| LEFT_PAREN expression RIGHT_PAREN
	| SUPER DOT IDENTIFIER


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

