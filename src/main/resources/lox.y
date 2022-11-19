%{
  import java.io.*;
  import java.util.*;
  import java.lang.RuntimeException;
  import java.lang.Exception;
  import java.text.MessageFormat;
  import lox.*;
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

%left PLUS MINUS
%left STAR SLASH

%%
program : declarations {
	List<Declaration> declarations = (List)$1.obj;
	yyval = new ParserVal(new Program(declarations));
}

declarations: /* empty */ {
	Parser.rule("declarations: empty");
	List<Declaration> declarations = new ArrayList<>();
	yyval = new ParserVal(declarations);
}
	| declarations declaration
	 {
	    Parser.rule("declarations:  declarations declaration", $1, $2);
	     List<Declaration> declarations = (List) $1.obj;
	     Declaration declaration = (Declaration)$2.obj;
	     declarations.add(declaration);
	     yyval = new ParserVal(declarations);
	 }

declaration: classDecl
	 {
	    Parser.rule("declaration: classDecl", $1);
	    yyval = new ParserVal($1.obj);
	 }
	| funDecl
	 {
	    Parser.rule("declaration: funDecl", $1);
	    yyval = new ParserVal($1.obj);
	 }
	| varDecl
	 {
	    Parser.rule("declaration: varDecl", $1);
	    yyval = new ParserVal($1.obj);
	 }
	| statement
	 {
	    Parser.rule("declaration: statement", $1);
	    yyval = new ParserVal($1.obj);
	 }

classDecl: CLASS IDENTIFIER optParentDecl LEFT_BRACE functions RIGHT_BRACE
	 {
	    Parser.rule("classDecl: CLASS IDENTIFIER optParentDecl LEFT_BRACE functions RIGHT_BRACE", $1, $2, $3,$4,$5);
	    String s = ((Tree)$2.obj).sym;
	    OptionalParentDeclaration optParentDecl = (OptionalParentDeclaration)$3.obj;
	    List<Function> functions = (List)$5.obj;
	    yyval = new ParserVal(new ClassDeclaration(s, optParentDecl, functions));
	 }

optParentDecl: {
	Parser.rule("optParentDecl: empty");
	yyval = new ParserVal(new EmptyParentDeclaration());
}
	| LESS IDENTIFIER
	 {
	    Parser.rule("optParentDecl: LESS IDENTIFIER", $1, $2);
	    String s = ((Tree)$1.obj).sym;
	    yyval = new ParserVal(new ParentDeclaration(s));
	 }

functions: {
	Parser.rule("functions: empty");
	List<Function> functions = new ArrayList<>();
	yyval = new ParserVal(functions);
}
	| functions function
	 {
	    Parser.rule("functions: functions function", $1, $2);
	    List<Function> functions = (List)$1.obj;
	    Function function = (Function)$2.obj;
	    functions.add(function);
	    yyval = new ParserVal(functions);
	 }

funDecl: FUN function
	 {
	    Parser.rule("funDecl: FUN function", $1, $2);
	    Function f = (Function)$2.obj;
	    yyval = new ParserVal(new FunctionDeclaration(f));
	 }

function: IDENTIFIER LEFT_PAREN parameters RIGHT_PAREN block
	 {
	    Parser.rule("function: IDENTIFIER LEFT_PAREN parameters RIGHT_PAREN block", $1,$2,$3,$4,$5);
	    String identifier = ((Tree)$1.obj).sym;
	    Parameters params = new Parameters((List)$3.obj);
	    yyval = new ParserVal(new Function(identifier, params, (BlockStatement)$5.obj));
	 }

parameters: {
	Parser.rule("parameters: empty");
	List<Identifier> identifiers = new ArrayList<>();
	yyval = new ParserVal(new Parameters(identifiers));
}
	| IDENTIFIER optIdentifiers
	 {
	    Parser.rule("parameters: IDENTIFIER optIdentifiers", $1, $2);
	    String s = ((Tree)$1.obj).sym;
	    List<Identifier> identifiers = (List)$2.obj;
	    identifiers.add(0, new Identifier(s));
	    yyval = new ParserVal(identifiers);
	 }

optIdentifiers: /*empty*/ {
	Parser.rule("optIdentifiers: empty");
	List<Identifier> identifiers = new ArrayList<>();
	yyval = new ParserVal(identifiers);
}
	| optIdentifiers optIdentifier
	 {
	   Parser.rule("optIdentifiers: optIdentifiers optIdentifier", $1, $2);
	   List<Identifier> identifiers = (List)$1.obj;
	   Identifier identifier = (Identifier)$2.obj;
	   identifiers.add(identifier);
	   yyval = new ParserVal(identifiers);

	 }

optIdentifier: COMMA IDENTIFIER
	 {
	    Parser.rule("optIdentifier: COMMA IDENTIFIER", $1, $2);
	    String s = ((Tree)$2.obj).sym;
	    yyval = new ParserVal(new Identifier(s));
	 }

varDecl: VAR IDENTIFIER optVarDecl SEMICOLON
	 {
	    Parser.rule("varDecl: VAR IDENTIFIER optVarDecl SEMICOLON", $1, $2, $3, $4);
	    String s = ((Tree)$2.obj).sym;
	    OptionalVariableDeclaration optVarDecl = (OptionalVariableDeclaration)$3.obj;
	    yyval = new ParserVal(new VariableDeclaration(s, optVarDecl));
	 }

optVarDecl: {
	Parser.rule("optVarDecl: empty");
	yyval = new ParserVal(new EmptyVariableDeclaration());
}
	| EQUALS expression
	 {
	    Parser.rule("optVarDecl: EQUALS expression", $1, $2);
	    Expression expr = (Expression)$2.obj;
	    yyval = new ParserVal(new ExpressionVariableDeclaration(expr));
	 }

block: LEFT_BRACE declarations RIGHT_BRACE
	 {
	    Parser.rule("block: LEFT_BRACE declarations RIGHT_BRACE", $1, $2,$3);
	    yyval = new ParserVal(new BlockStatement((List)$2.obj));
	 }

statement: exprStmt {
	$$ = $1;
	}
	| forStmt {
	$$ = $1;
	}
	| ifStmt {
	$$ = $1;
	}
	| printStmt {
	 $$ = $1;
	}
	| returnStmt {
	 $$ = $1;
	}
	| whileStmt {
	$$ = $1;
	}
	| block {
	$$ = $1;
	}

forStmt: FOR LEFT_PAREN firstForInit optExpression SEMICOLON optExpression RIGHT_PAREN statement
	 {
	    Parser.rule("forStmt: FOR LEFT_PAREN firstForInit optExpression SEMICOLON optExpression RIGHT_PAREN statement", $1,$2,$3,$4,$5,$6,$7,$8);

	    FirstForInit init = (FirstForInit)$3.obj;
	    OptExpression optExpr1 = (OptExpression)$4.obj;
	    OptExpression optExpr2 = (OptExpression)$6.obj;
	    Statement stmt = (Statement)$8.obj;
	    yyval = new ParserVal(new ForStatement(init, optExpr1, optExpr2, stmt));

	 }

returnStmt: RETURN optExpression SEMICOLON
	 {
	    Parser.rule("returnStmt: RETURN optExpression SEMICOLON", $1, $2, $3);
	    yyval = new ParserVal(new ReturnStatement((OptExpression)$2.obj));
	 }

optExpression: /*empty*/ {
	Parser.rule("optExpression: empty");
	yyval = new ParserVal(new EmptyExpression());
}
	| expression
	 {
	    Parser.rule("optExpression: expression", $1);
	    Expression expr = (Expression)$1.obj;
	    yyval = new ParserVal(new NormalExpression(expr));
	 }

firstForInit: varDecl {
	VariableDeclaration varDecl = (VariableDeclaration)$1.obj;
	yyval = new ParserVal(new VarDeclForInit(varDecl));
}
	| exprStmt {
	ExpressionStatement exprStmt = (ExpressionStatement)$1.obj;
	yyval = new ParserVal(new ExprStmtForInit(exprStmt));
	}
	| SEMICOLON {
	yyval = new ParserVal(new EmptyForInit());
	}

exprStmt: expression SEMICOLON
	 {
	    Parser.rule("exprStmt: expression SEMICOLON", $1, $2);
	    Expression expr = (Expression)$1.obj;
	    yyval = new ParserVal(new ExpressionStatement(expr));
	 }

ifStmt: IF LEFT_PAREN expression RIGHT_PAREN statement optElse
	 {
	    Parser.rule("ifStmt: IF LEFT_PAREN expression RIGHT_PAREN statement optElse", $1, $2,$3,$4,$5,$6);
	    Else elsePart = (Else)$6.obj;
	    Statement stmt = (Statement)$5.obj;
	    Expression expr = (Expression)$3.obj;
	    yyval = new ParserVal(new IfStatement(expr, stmt, elsePart));
	 }

optElse: {
	Parser.rule("optElse: empty");
	yyval = new ParserVal(new EmptyElse());
}
	| ELSE statement
	 {
	    Parser.rule("optElse: ELSE statement", $1, $2);
	    Statement stmt = (Statement)$2.obj;
	    yyval = new ParserVal(new NormalElse(stmt));
	 }

printStmt: PRINT expression SEMICOLON
	 {
	    Parser.rule("printStmt: PRINT expression SEMICOLON", $1, $2, $3);
	    Expression expr = (Expression)$2.obj;
	    yyval = new ParserVal(new PrintStatement(expr));
	 }

whileStmt: WHILE LEFT_PAREN expression RIGHT_PAREN statement
	 {
	    Parser.rule("whileStmt: WHILE LEFT_PAREN expression RIGHT_PAREN statement", $1, $2, $3, $4, $5);
	    Expression expr = (Expression)$3.obj;
	    Statement stmt = (Statement)$5.obj;
	    yyval = new ParserVal(new WhileStatement(expr, stmt));
	 }

expression: assignment
	 {
	    Parser.rule("expression: assignment", $1);
	    Assignment assign = (Assignment)$1.obj;
	    yyval = new ParserVal(new Expression(assign));
	 }

assignment: IDENTIFIER EQUALS expression
	 {
	    Parser.rule("assignment: IDENTIFIER EQUALS expression", $1, $2, $3);
	    Strig identifier = ((Tree)$1.obj).sym;
	    yyval = new ParserVal(new IdentifierEqualsExpression(identifier, (Expression)$3.obj));
	 }
	| call EQUALS expression
	 {
	    Parser.rule("assignment: call EQUALS expression", $1, $2, $3);
	    yyval = new ParserVal(new CallEqualsExpression((Expression)$3.obj));
	 }
	| logic_or
	 {
	    Parser.rule("assignment: logic_or", $1);
	    yyval = new ParserVal($1.obj);
	 }

logic_or: logic_and logic_ands
	 {
	    Parser.rule("logic_or: logic_and logic_ands", $1, $2);
	    List<LogicAnd> logicAnds = (List)$2.obj;
	    LogicAnd logicAnd = (LogicAnd)$1.obj;
	    logicAnds.add(0, logicAnd);
	    yyval = new ParserVal(new LogicOr(logicAnds));
	 }

logic_ands: {
	Parser.rule("logic_ands: empty");
	List<LogicAnd> logicAnds = new ArrayList<>();
	yyval = new ParserVal(logicAnds);
}
	| logic_ands OR logic_and
	 {
	    Parser.rule("logic_ands: logic_ands OR logic_and", $1, $2, $3);
	    List<LogicAnd> logicAnds = (List)$1.obj;
	    LogicAnd logicAnd = (LogicAnd)$3.obj;
	    logicAnds.add(logicAnd);
	    yyval = new ParserVal(logicAnds);
	 }

logic_and: equality equalities
	 {
	    Parser.rule("logic_and: equality equalities", $1, $2);
	    Equality equality = (Equality)$1.obj;
	    List<Equality> equalities = (List)$2.obj;
	    equalities.add(0, equality);
	    yyval = new ParserVal(new LogicAnd(equalities));
	 }

equalities: {
	Parser.rule("equalities: empty");
	List<Equality> equalities = new ArrayList<>();
	yyval = new ParserVal(equalities);
}
	| equalities AND equality
	 {
	    Parser.rule("equalities: equalities AND equality", $1, $2, $3);
	    List<Equality> equalities = (List)$1.obj;
	    Equality equality = (Equality)$3.obj;
	    equalities.add(equality);
	    yyval = new ParserVal(equalities);
	 }

equality: comparison comparisons
	 {
	    Parser.rule("equality: comparison comparisons", $1, $2);
	    Comparison comparison = (Comparison)$1.obj;
	    List<ComparisonElement> comparisons = (List)$2.obj;
	    yyval = new ParserVal(new Equality(comparison, comparisons));
	 }

comparisons: {
	Parser.rule("comparisons: empty");
	List<ComparisonElement> comparisons = new ArrayList<>();
	yyval = new ParserVal(comparisons);
}
	| comparisons comparison_sign comparison
	 {
	    Parser.rule("comparisons: comparisons comparison_sign comparison", $1, $2, $3);

	    ComparisonElement comparisonElement = new ComparisonElement(((Tree)$2.obj).sym, (Comparison)$3.obj);

	    List<ComparisonElement> comparisons = (List)$1.obj;
	    comparisons.add(comparisonElement);
	    yyval = new ParserVal(comparisons);

	 }

comparison_sign: BANG_EQUALS
	| EQUALS_EQUALS

comparison: term terms
	 {
	    Parser.rule("comparison: term terms", $1, $2);
	    Term term = (Term)$1.obj;
	    List<TermElement> terms = (List)$2.obj;
	    yyval = new ParserVal(new Comparison(term, terms));
	 }

terms: {
	Parser.rule("terms: empty");
	List<TermElement> terms = new ArrayList<>();
	yyval = new ParserVal(terms);
}
	| terms term_sign term
	 {
	    Parser.rule("terms term_sign term", $1, $2, $3);
	    List<TermElement> terms = (List)$1.obj;
	    String sign = ((Tree)$2.obj).sym;
	    Term term = (Term)$3.obj;
	    terms.add(new TermElement(sign, term));
	    yyval = new ParserVal(terms);
	 }

term_sign: GREATER
	| GREATER_EQUALS
	| LESS
	| LESS_EQUALS

term: factor factors
	 {
	    Parser.rule("term: factor factors", $1, $2);
	    Factor factor = (Factor)$1.obj;
	    List<FactorElement> factors = (List)$2.obj;
	    yyval = new ParserVal(new Term(factor, factors));
	 }

factors: {
	Parser.rule("factors: empty");
	yyval = new ParserVal(new ArrayList<FactorElement>());
}
	| factors factor_sign factor
	 {
	    Parser.rule("factors: factors factor_sign factor", $1, $2, $3);
	    FactorElement factor = new FactorElement(((Tree)$2.obj).sym, (Factor)$3.obj);
	    List<FactorElement> factors = (List)$1.obj;
	    factors.add(factor);
	    yyval = new ParserVal(factors);
	 }

factor: unary unaries
	 {
	    Parser.rule("factor: unary unaries", $1, $2);
	    Unary unary = (Unary)$1.obj;
	    List<UnaryElement> unaries = (List)$2.obj;
	    Factor factor = new Factor(unary, unaries);
	    yyval = new ParserVal(factor);
	 }

unaries: /* empty */ {
	Parser.rule("unaries: empty");
	List<UnaryElement> unaries = new ArrayList<>();
	yyval = new ParserVal(unaries);
}
	| unaries unary_sign unary
	 {
	    Parser.rule("unaries: unaries unary_sign unary", $1, $2, $3);
	    List<UnaryElement> unaries = (List)$1.obj;
	    String sign = ((Tree)$2.obj).sym;
	    Unary unary = (Unary)$3.obj;
	    unaries.add(new UnaryElement(sign, unary));
	    yyval = new ParserVal(unaries);
	 }

unary: bang_or_minus unary
	 {
	    Parser.rule("unary: bang_or_minus factor", $1, $2);
	    yyval = new ParserVal(new BangOrMinusUnary(((Tree)$1.obj).sym, (Unary)$2.obj));
	 }
	| call
	 {
	    Parser.rule("unary: call", $1);
	    yyval = new ParserVal(new UnaryCall((Call)$1.obj));
	 }

bang_or_minus: BANG
	| MINUS

unary_sign: SLASH
	| STAR

factor_sign: MINUS
	| PLUS

arguments: {
	Parser.rule("arguments: empty");
	List<Expression> expressions = new ArrayList<>();
	yyval = new ParserVal(expressions);
}
	| expression
	 {
	    Parser.rule("arguments: expression", $1);
	    Expression expression = (Expression)$1.obj;
	    List<Expression> expressions = new ArrayList<>();
	    expressions.add(expression);
	    yyval = new ParserVal(expressions);
	 }
	| expression expressions
	 {
	    Parser.rule("arguments: expression expressions", $1, $2);
	    List<Expression> expressions = (List)$2.obj;
	    Expression expression = (Expression)$1.obj;
	    expressions.add(0, expression);
	    yyval = new ParserVal(expressions);
	 }

expressions: {
   Parser.rule("empty expressions");
   List<Expression> expressions = new ArrayList<>();
   yyval = new ParserVal(expressions);
}
	| expressions COMMA expression
	 {
	    Parser.rule("expressions COMMA expression", $1, $2, $3);
	    List<Expression> expressions = (List)$1.obj;
	    Expression expression = (Expression)$3.obj;
	    expressions.add(expression);
	    yyval = new ParserVal(expressions);
	 }

call: primary primary_blocks
	 {
	    Parser.rule("call: primary primary_blocks", $1, $2);
	    Primary primary = (Primary)$1.obj;
	    List<PrimaryBlock> primaryBlocks = (List)$2.obj;
	    yyval = new ParserVal(new Call(primary, primaryBlocks));
	 }


arguments_group: {
	Parser.rule("arguments_group: empty");
	List<Expression> arguments = new ArrayList<>();
	yyval = new ParserVal(arguments);
}
	| arguments
	 {
	    Parser.rule("arguments_group: arguments", $1);
	    List<Expression> expressions = (List)$1.obj;
	    yyval = new ParserVal(expressions);
	 }

primary_block: LEFT_PAREN arguments_group RIGHT_PAREN
	 {
	    Parser.rule("primary_block: LEFT_PAREN arguments_group RIGHT_PAREN", $1, $2, $3);
	    List<Expression> expressions = (List)$2.obj;
	    PrimaryBlock.ArgumentsGroup ag = new PrimaryBlock.ArgumentsGroup(expressions);
	    yyval = new ParserVal(ag);
	 }
	| DOT IDENTIFIER
	 {
	    Parser.rule("DOT IDENTIFIER", $1, $2);
	    String s = ((Tree)$2.obj).sym;
	    yyval = new ParserVal(new PrimaryBlock.DotIdentifier(s));

	 }

primary_blocks: /*empty*/ {
	    Parser.rule("primary_blocks /*empty*/");
	    List<PrimaryBlock> primaryBlocks = new ArrayList<>();
	    yyval = new ParserVal(primaryBlocks);
    }
	| primary_blocks primary_block
	 {
	    Parser.rule("primary_blocks primary_block", $1, $2);
	    List<PrimaryBlock> primaryBlocks = (List)$1.obj;
	    PrimaryBlock primaryBlock = (PrimaryBlock)$2.obj;
	    primaryBlocks.add(primaryBlock);
	    yyval = new ParserVal(primaryBlocks);
	 }

primary: TRUE
	 {
	    Parser.rule("TRUE", $1);
	    yyval = new ParserVal(new Primary.True());
	 }
	| FALSE
	 {
	    Parser.rule("FALSE", $1);
	    yyval = new ParserVal(new Primary.False());
	 }
	| NIL
	 {
	    Parser.rule("NIL", $1);
	    yyval = new ParserVal(new Primary.Nil());
	 }
	| THIS
	 {
	    Parser.rule("THIS", $1);
	    yyval = new ParserVal(new Primary.This());
	 }
	| NUMBER
	 {
	    Parser.rule("NUMBER", $1);
	    Double d = Double.parseDouble(((Tree)$1.obj).sym);
	    yyval = new ParserVal(new Primary.Number(d));
	 }
	| STRING
	 {
	    Parser.rule("STRING", $1);
	    String s = ((Tree)$1.obj).sym;
	    int len = s.length();
	    yyval = new ParserVal(new Primary.String(s,substring(1,len-1)));
	 }
	| IDENTIFIER
	 {
	    Parser.rule("IDENTIFIER", $1);
	    String s = ((Tree)$1.obj).sym;
	    yyval = new ParserVal(new Primary.Identifier(s));
	 }
	| LEFT_PAREN expression RIGHT_PAREN
	 {
	    Parser.rule("LEFT_PAREN expression RIGHT_PAREN", $1, $2, $3);
	    Expression expression = (Expression)$2.obj;
	    yyval = new ParserVal(new Primary.ExpressionPrimary(expression));

	 }
	| SUPER DOT IDENTIFIER
	 {
	    Parser.rule("SUPER DOT IDENTIFIER", $1, $2, $3);
	    String s = ((Tree)$3.obj).sym;
	    yyval = new ParserVal(new Primary.SuperIdentifier(s));
	 }


%%

private Yylex lexer;

private int yylex() {
	try {
		String s = lexer.yytext();
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

public static void rule(String s, ParserVal... pvs) {
    StringBuilder b = new StringBuilder(s).append(" ");
    for (ParserVal pv : pvs) {
        b.append(pv).append(";");
    }
    System.out.println(b.toString());
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

