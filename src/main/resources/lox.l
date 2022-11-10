%%

%byaccj

%{
    private Parser yyparser;

    public Yylex(java.io.Reader r, Parser yyparser) {
        this(r);
        this.yyparser = yyparser;
    }

    public int scan(int cat) {
        Tree t = new Tree("token", 0, new Token(cat, yytext(), yylineno()));
        yyparser.yylval = new ParserVal(t);
        return cat;
    }
%}
%%
"("     {return scan(Parser.LEFT_PAREN); }
")"     {return scan(Parser.RIGHT_PAREN); }
"{"     {return scan(Parser.LEFT_BRACE); }
"}"     {return scan(Parser.RIGHT_BRACE); }
","     {return scan(Parser.COMMA); }
"."     {return scan(Parser.DOT); }
"-"     {return scan(Parser.MINUS); }
"+"     {return scan(Parser.PLUS); }
";"     {return scan(Parser.SEMICOLON); }
"/"     {return scan(Parser.SLASH); }
"*"     {return scan(Parser.STAR); }
"!="    {return scan(Parser.BANG_EQUALS); }
"=="    {return scan(Parser.EQUALS_EQUALS); }
"="     {return scan(Parser.EQUALS); }
">="    {return scan(Parser.GREATER_EQUALS); }
">"     {return scan(Parser.GREATER); }
"<="    {return scan(Parser.LESS_EQUALS); }
"<"     {return scan(Parser.LESS); }
"!"     {return scan(Parser.BANG);}
"and"   {return scan(Parser.AND); }
"class" {return scan(Parser.CLASS); }
"else"  {return scan(Parser.ELSE); }
"false" {return scan(Parser.FALSE); }
"fun"   {return scan(Parser.FUN); }
"for"   {return scan(Parser.FOR); }
"if"    {return scan(Parser.IF); }
"nil"   {return scan(Parser.NIL); }
"or"    {return scan(Parser.OR); }
"print" {return scan(Parser.PRINT); }
"return" {return  scan(Parser.RETURN); }
"super"  {return  scan(Parser.SUPER); }
"this"   {return  scan(Parser.THIS); }
"true"   {return  scan(Parser.TRUE); }
"var"    {return  scan(Parser.VAR); }
"while"  {return  scan(Parser.WHILE); }
[a-zA-Z_][a-zA-Z0-9_]*   {return  scan(Parser.IDENTIFIER); }
\".*\"  { return scan(Parser.STRING); }
[0-9]+  { return scan(Parser.NUMBER); }
[ \t\r]+ { }
\n      { Lexer.incrementLineNumber(); }
.       { Lexer.error("unrecognized character"); }
