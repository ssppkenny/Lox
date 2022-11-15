//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";


//#line 2 "lox.y"

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import lox.Assignment;
import lox.BangOrMinusFactor;
import lox.BlockStatement;
import lox.Call;
import lox.CallEqualsExpression;
import lox.ClassDeclaration;
import lox.Comparison;
import lox.ComparisonElement;
import lox.Declaration;
import lox.Else;
import lox.EmptyElse;
import lox.EmptyExpression;
import lox.EmptyForInit;
import lox.EmptyParentDeclaration;
import lox.EmptyVariableDeclaration;
import lox.Equality;
import lox.ExprStmtForInit;
import lox.Expression;
import lox.ExpressionStatement;
import lox.ExpressionVariableDeclaration;
import lox.Factor;
import lox.FactorElement;
import lox.FirstForInit;
import lox.ForStatement;
import lox.Function;
import lox.FunctionDeclaration;
import lox.Identifier;
import lox.IdentifierEqualsExpression;
import lox.IfStatement;
import lox.LogicAnd;
import lox.LogicOr;
import lox.NormalElse;
import lox.NormalExpression;
import lox.OptExpression;
import lox.OptionalParentDeclaration;
import lox.OptionalVariableDeclaration;
import lox.Parameters;
import lox.ParentDeclaration;
import lox.Primary;
import lox.PrimaryBlock;
import lox.PrintStatement;
import lox.Program;
import lox.ReturnStatement;
import lox.Statement;
import lox.Term;
import lox.TermElement;
import lox.Unary;
import lox.UnaryCall;
import lox.UnaryElement;
import lox.VarDeclForInit;
import lox.VariableDeclaration;
import lox.WhileStatement;
//#line 24 "Parser.java"


public class Parser {

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character

    //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
    void debug(String msg) {
        if (yydebug)
            System.out.println(msg);
    }

    //########## STATE STACK ##########
    final static int YYSTACKSIZE = 500;  //maximum stack size
    int statestk[] = new int[YYSTACKSIZE]; //state stack
    int stateptr;
    int stateptrmax;                     //highest index of stackptr
    int statemax;                        //state when highest index reached

    //###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }

    //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }

    //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++)
            System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


    String yytext;//user variable to return contextual strings
    ParserVal yyval; //used to return semantic vals from action routines
    ParserVal yylval;//the 'lval' (result) I got from yylex()
    ParserVal valstk[];
    int valptr;

    //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
    void val_init() {
        valstk = new ParserVal[YYSTACKSIZE];
        yyval = new ParserVal();
        yylval = new ParserVal();
        valptr = -1;
    }

    void val_push(ParserVal val) {
        if (valptr >= YYSTACKSIZE)
            return;
        valstk[++valptr] = val;
    }

    ParserVal val_pop() {
        if (valptr < 0)
            return new ParserVal();
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0)
            return;
        valptr = ptr;
    }

    ParserVal val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0)
            return new ParserVal();
        return valstk[ptr];
    }

    final ParserVal dup_yyval(ParserVal val) {
        ParserVal dup = new ParserVal();
        dup.ival = val.ival;
        dup.dval = val.dval;
        dup.sval = val.sval;
        dup.obj = val.obj;
        return dup;
    }

    //#### end semantic value section ####
    public final static short LEFT_PAREN = 257;
    public final static short RIGHT_PAREN = 258;
    public final static short LEFT_BRACE = 259;
    public final static short RIGHT_BRACE = 260;
    public final static short COMMA = 261;
    public final static short DOT = 262;
    public final static short MINUS = 263;
    public final static short PLUS = 264;
    public final static short SEMICOLON = 265;
    public final static short SLASH = 266;
    public final static short STAR = 267;
    public final static short BANG = 268;
    public final static short BANG_EQUALS = 269;
    public final static short EQUALS_EQUALS = 270;
    public final static short EQUALS = 271;
    public final static short GREATER_EQUALS = 272;
    public final static short GREATER = 273;
    public final static short LESS_EQUALS = 274;
    public final static short LESS = 275;
    public final static short AND = 276;
    public final static short CLASS = 277;
    public final static short ELSE = 278;
    public final static short FALSE = 279;
    public final static short FUN = 280;
    public final static short FOR = 281;
    public final static short IF = 282;
    public final static short NIL = 283;
    public final static short OR = 284;
    public final static short PRINT = 285;
    public final static short RETURN = 286;
    public final static short SUPER = 287;
    public final static short THIS = 288;
    public final static short TRUE = 289;
    public final static short VAR = 290;
    public final static short WHILE = 291;
    public final static short IDENTIFIER = 292;
    public final static short STRING = 293;
    public final static short NUMBER = 294;
    public final static short YYERRCODE = 256;
    final static short yylhs[] = {-1,
            0, 1, 1, 2, 2, 2, 2, 3, 7, 7,
            8, 8, 4, 9, 10, 10, 12, 12, 13, 5,
            14, 14, 11, 6, 6, 6, 6, 6, 6, 6,
            17, 20, 23, 23, 22, 22, 22, 16, 18, 24,
            24, 19, 21, 15, 25, 25, 25, 27, 29, 29,
            28, 31, 31, 30, 33, 33, 34, 34, 32, 36,
            36, 37, 37, 37, 37, 35, 39, 39, 38, 42,
            42, 41, 41, 44, 44, 43, 43, 40, 40, 45,
            45, 45, 46, 46, 26, 49, 49, 50, 50, 48,
            48, 47, 47, 47, 47, 47, 47, 47, 47, 47,
    };
    final static short yylen[] = {2,
            1, 0, 2, 1, 1, 1, 1, 6, 0, 2,
            0, 2, 2, 5, 0, 2, 0, 2, 2, 4,
            0, 2, 3, 1, 1, 1, 1, 1, 1, 1,
            8, 3, 0, 1, 1, 1, 1, 2, 6, 0,
            2, 3, 5, 1, 3, 3, 1, 2, 0, 3,
            2, 0, 3, 2, 0, 3, 1, 1, 2, 0,
            3, 1, 1, 1, 1, 2, 0, 3, 2, 0,
            3, 2, 1, 1, 1, 1, 1, 1, 1, 0,
            1, 2, 0, 3, 2, 0, 1, 3, 2, 0,
            2, 1, 1, 1, 1, 1, 1, 1, 3, 3,
    };
    final static short yydefred[] = {2,
            0, 0, 0, 2, 75, 74, 0, 93, 0, 0,
            0, 94, 0, 0, 0, 95, 92, 0, 0, 0,
            97, 96, 3, 4, 5, 6, 7, 30, 0, 24,
            25, 26, 27, 28, 29, 44, 0, 47, 49, 52,
            55, 60, 67, 70, 0, 90, 0, 0, 0, 0,
            13, 0, 0, 0, 34, 0, 0, 0, 0, 0,
            38, 0, 0, 0, 0, 0, 0, 0, 98, 73,
            72, 0, 99, 23, 0, 0, 0, 37, 35, 36,
            0, 0, 42, 32, 100, 0, 0, 0, 45, 46,
            0, 0, 57, 58, 0, 63, 62, 65, 64, 0,
            78, 79, 0, 76, 77, 0, 0, 0, 91, 10,
            11, 17, 0, 0, 0, 22, 20, 0, 50, 53,
            56, 61, 68, 71, 0, 87, 0, 89, 0, 0,
            0, 0, 0, 43, 0, 88, 8, 12, 0, 18,
            14, 0, 0, 39, 0, 19, 0, 41, 84, 31,
    };
    final static short yydgoto[] = {1,
            2, 23, 24, 25, 26, 27, 76, 129, 51, 113,
            28, 130, 140, 87, 29, 30, 31, 32, 33, 34,
            35, 81, 56, 144, 36, 37, 38, 39, 63, 40,
            64, 41, 65, 95, 42, 66, 100, 43, 67, 103,
            44, 68, 106, 45, 126, 135, 46, 72, 127, 109,
    };
    final static short yysindex[] = {0,
            0, -195, -98, 0, 0, 0, -278, 0, -271, -224,
            -210, 0, -98, -98, -204, 0, 0, -227, -178, -191,
            0, 0, 0, 0, 0, 0, 0, 0, -198, 0,
            0, 0, 0, 0, 0, 0, -182, 0, 0, 0,
            0, 0, 0, 0, -71, 0, -177, -251, -174, -155,
            0, -110, -98, -160, 0, -159, -184, -161, -98, -98,
            0, -98, -172, -163, -245, -203, -189, -190, 0, 0,
            0, -208, 0, 0, -176, -145, -175, 0, 0, 0,
            -98, -139, 0, 0, 0, -98, -144, -136, 0, 0,
            -71, -71, 0, 0, -71, 0, 0, 0, 0, -71,
            0, 0, -71, 0, 0, -71, -98, -169, 0, 0,
            0, 0, -134, -140, -148, 0, 0, -148, 0, 0,
            0, 0, 0, 0, 0, 0, -131, 0, -247, -133,
            -129, -98, -146, 0, -125, 0, 0, 0, -142, 0,
            0, -107, -148, 0, -98, 0, -148, 0, 0, 0,
    };
    final static short yyrindex[] = {0,
            0, 148, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -111, 0, 0, 0, 0, 0, -33,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 60, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, -102, 0,
            0, 0, 0, 0, 0, 0, 0, -103, 0, 0,
            0, 0, -243, -238, -158, -109, 102, 82, 0, 0,
            0, 38, 0, 0, 0, 0, -95, 0, 0, 0,
            -111, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, -94, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -254, 0, 0, 0, 0, -92,
            0, -90, 1, 0, -87, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            168, 0, 0, 0, 122, -99, 0, 0, 47, 0,
            56, 0, 0, 0, -3, 136, 0, 0, 0, 0,
            0, 0, -79, 0, 0, -40, 0, 107, 0, 101,
            0, 104, 0, 0, 100, 0, 0, -42, 0, 0,
            95, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    };
    final static int YYTABLESIZE = 386;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{47,
                40, 114, 71, 81, 70, 3, 83, 4, 74, 54,
                55, 5, 137, 49, 48, 133, 6, 48, 134, 51,
                50, 48, 51, 93, 94, 7, 51, 8, 9, 10,
                11, 12, 52, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 148, 50, 51, 53, 150, 107, 82,
                70, 70, 142, 108, 70, 88, 89, 57, 90, 70,
                123, 3, 70, 4, 58, 70, 61, 5, 96, 97,
                98, 99, 6, 101, 102, 104, 105, 55, 59, 60,
                73, 7, 116, 8, 9, 10, 11, 12, 62, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 54,
                75, 77, 54, 125, 83, 84, 54, 85, 3, 86,
                4, 91, 92, 111, 5, 110, 112, 54, 115, 6,
                117, 118, 128, 131, 132, 54, 136, 139, 55, 4,
                8, 143, 10, 11, 12, 145, 13, 14, 15, 16,
                17, 149, 19, 20, 21, 22, 3, 1, 59, 146,
                147, 59, 5, 33, 78, 59, 9, 6, 3, 59,
                59, 21, 15, 80, 5, 16, 59, 33, 8, 6,
                82, 48, 12, 79, 59, 138, 15, 16, 17, 18,
                8, 20, 21, 22, 12, 3, 141, 80, 15, 16,
                17, 5, 120, 20, 21, 22, 6, 119, 121, 122,
                124, 0, 0, 0, 0, 0, 0, 8, 0, 0,
                0, 12, 0, 0, 0, 15, 16, 17, 0, 0,
                69, 21, 22, 98, 98, 0, 0, 98, 98, 98,
                98, 98, 98, 98, 0, 98, 98, 0, 98, 98,
                98, 98, 98, 0, 0, 0, 0, 0, 0, 0,
                98, 0, 0, 0, 0, 0, 0, 40, 0, 40,
                40, 0, 0, 40, 0, 0, 0, 0, 40, 0,
                0, 0, 0, 0, 0, 0, 0, 40, 0, 40,
                40, 40, 40, 40, 0, 40, 40, 40, 40, 40,
                40, 40, 40, 40, 40, 85, 0, 0, 85, 0,
                85, 85, 85, 85, 85, 0, 85, 85, 85, 85,
                85, 85, 85, 85, 0, 0, 0, 73, 0, 0,
                73, 85, 73, 73, 73, 73, 73, 0, 73, 73,
                0, 73, 73, 73, 73, 73, 0, 0, 0, 69,
                0, 0, 69, 73, 69, 69, 69, 0, 0, 0,
                69, 69, 0, 69, 69, 69, 69, 69, 0, 66,
                0, 0, 66, 0, 0, 69, 66, 0, 0, 0,
                66, 66, 0, 66, 66, 66, 66, 66, 0, 0,
                0, 0, 0, 0, 0, 66,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{3,
                0, 81, 45, 258, 45, 257, 261, 259, 260, 13,
                14, 263, 260, 292, 258, 115, 268, 261, 118, 258,
                292, 265, 261, 269, 270, 277, 265, 279, 280, 281,
                282, 283, 257, 285, 286, 287, 288, 289, 290, 291,
                292, 293, 294, 143, 292, 284, 257, 147, 257, 53,
                91, 92, 132, 262, 95, 59, 60, 262, 62, 100,
                103, 257, 103, 259, 292, 106, 265, 263, 272, 273,
                274, 275, 268, 263, 264, 266, 267, 81, 257, 271,
                258, 277, 86, 279, 280, 281, 282, 283, 271, 285,
                286, 287, 288, 289, 290, 291, 292, 293, 294, 258,
                275, 257, 261, 107, 265, 265, 265, 292, 257, 271,
                259, 284, 276, 259, 263, 292, 292, 276, 258, 268,
                265, 258, 292, 258, 265, 284, 258, 261, 132, 259,
                279, 278, 281, 282, 283, 261, 285, 286, 287, 288,
                289, 145, 291, 292, 293, 294, 257, 0, 258, 292,
                258, 261, 263, 265, 265, 265, 259, 268, 257, 269,
                270, 265, 258, 258, 263, 258, 276, 258, 279, 268,
                258, 4, 283, 52, 284, 129, 287, 288, 289, 290,
                279, 292, 293, 294, 283, 257, 131, 52, 287, 288,
                289, 263, 92, 292, 293, 294, 268, 91, 95, 100,
                106, -1, -1, -1, -1, -1, -1, 279, -1, -1,
                -1, 283, -1, -1, -1, 287, 288, 289, -1, -1,
                292, 293, 294, 257, 258, -1, -1, 261, 262, 263,
                264, 265, 266, 267, -1, 269, 270, -1, 272, 273,
                274, 275, 276, -1, -1, -1, -1, -1, -1, -1,
                284, -1, -1, -1, -1, -1, -1, 257, -1, 259,
                260, -1, -1, 263, -1, -1, -1, -1, 268, -1,
                -1, -1, -1, -1, -1, -1, -1, 277, -1, 279,
                280, 281, 282, 283, -1, 285, 286, 287, 288, 289,
                290, 291, 292, 293, 294, 258, -1, -1, 261, -1,
                263, 264, 265, 266, 267, -1, 269, 270, 271, 272,
                273, 274, 275, 276, -1, -1, -1, 258, -1, -1,
                261, 284, 263, 264, 265, 266, 267, -1, 269, 270,
                -1, 272, 273, 274, 275, 276, -1, -1, -1, 258,
                -1, -1, 261, 284, 263, 264, 265, -1, -1, -1,
                269, 270, -1, 272, 273, 274, 275, 276, -1, 258,
                -1, -1, 261, -1, -1, 284, 265, -1, -1, -1,
                269, 270, -1, 272, 273, 274, 275, 276, -1, -1,
                -1, -1, -1, -1, -1, 284,
        };
    }

    final static short YYFINAL = 1;
    final static short YYMAXTOKEN = 294;
    final static String yyname[] = {
            "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "COMMA",
            "DOT", "MINUS", "PLUS", "SEMICOLON", "SLASH", "STAR", "BANG", "BANG_EQUALS",
            "EQUALS_EQUALS", "EQUALS", "GREATER_EQUALS", "GREATER", "LESS_EQUALS", "LESS", "AND",
            "CLASS", "ELSE", "FALSE", "FUN", "FOR", "IF", "NIL", "OR", "PRINT", "RETURN", "SUPER",
            "THIS", "TRUE", "VAR", "WHILE", "IDENTIFIER", "STRING", "NUMBER",
    };
    final static String yyrule[] = {
            "$accept : program",
            "program : declarations",
            "declarations :",
            "declarations : declarations declaration",
            "declaration : classDecl",
            "declaration : funDecl",
            "declaration : varDecl",
            "declaration : statement",
            "classDecl : CLASS IDENTIFIER optParentDecl LEFT_BRACE functions RIGHT_BRACE",
            "optParentDecl :",
            "optParentDecl : LESS IDENTIFIER",
            "functions :",
            "functions : functions function",
            "funDecl : FUN function",
            "function : IDENTIFIER LEFT_PAREN parameters RIGHT_PAREN block",
            "parameters :",
            "parameters : IDENTIFIER optIdentifiers",
            "optIdentifiers :",
            "optIdentifiers : optIdentifiers optIdentifier",
            "optIdentifier : COMMA IDENTIFIER",
            "varDecl : VAR IDENTIFIER optVarDecl SEMICOLON",
            "optVarDecl :",
            "optVarDecl : EQUALS expression",
            "block : LEFT_BRACE declarations RIGHT_BRACE",
            "statement : exprStmt",
            "statement : forStmt",
            "statement : ifStmt",
            "statement : printStmt",
            "statement : returnStmt",
            "statement : whileStmt",
            "statement : block",
            "forStmt : FOR LEFT_PAREN firstForInit optExpression SEMICOLON optExpression RIGHT_PAREN statement",
            "returnStmt : RETURN optExpression SEMICOLON",
            "optExpression :",
            "optExpression : expression",
            "firstForInit : varDecl",
            "firstForInit : exprStmt",
            "firstForInit : SEMICOLON",
            "exprStmt : expression SEMICOLON",
            "ifStmt : IF LEFT_PAREN expression RIGHT_PAREN statement optElse",
            "optElse :",
            "optElse : ELSE statement",
            "printStmt : PRINT expression SEMICOLON",
            "whileStmt : WHILE LEFT_PAREN expression RIGHT_PAREN statement",
            "expression : assignment",
            "assignment : IDENTIFIER EQUALS expression",
            "assignment : call EQUALS expression",
            "assignment : logic_or",
            "logic_or : logic_and logic_ands",
            "logic_ands :",
            "logic_ands : logic_ands OR logic_and",
            "logic_and : equality equalities",
            "equalities :",
            "equalities : equalities AND equality",
            "equality : comparison comparisons",
            "comparisons :",
            "comparisons : comparisons comparison_sign comparison",
            "comparison_sign : BANG_EQUALS",
            "comparison_sign : EQUALS_EQUALS",
            "comparison : term terms",
            "terms :",
            "terms : terms term_sign term",
            "term_sign : GREATER",
            "term_sign : GREATER_EQUALS",
            "term_sign : LESS",
            "term_sign : LESS_EQUALS",
            "term : factor factors",
            "factors :",
            "factors : factors factor_sign factor",
            "factor : unary unaries",
            "unaries :",
            "unaries : unaries unary_sign unary",
            "unary : bang_or_minus factor",
            "unary : call",
            "bang_or_minus : BANG",
            "bang_or_minus : MINUS",
            "unary_sign : SLASH",
            "unary_sign : STAR",
            "factor_sign : MINUS",
            "factor_sign : PLUS",
            "arguments :",
            "arguments : expression",
            "arguments : expression expressions",
            "expressions :",
            "expressions : expressions COMMA expression",
            "call : primary primary_blocks",
            "arguments_group :",
            "arguments_group : arguments",
            "primary_block : LEFT_PAREN arguments_group RIGHT_PAREN",
            "primary_block : DOT IDENTIFIER",
            "primary_blocks :",
            "primary_blocks : primary_blocks primary_block",
            "primary : TRUE",
            "primary : FALSE",
            "primary : NIL",
            "primary : THIS",
            "primary : NUMBER",
            "primary : STRING",
            "primary : IDENTIFIER",
            "primary : LEFT_PAREN expression RIGHT_PAREN",
            "primary : SUPER DOT IDENTIFIER",
    };

//#line 604 "lox.y"

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

    public void yyerror(String error) {
        System.err.println("Error: " + error);
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
        if (args.length > 0) {
            // parse a file
            yyparser = new Parser(new FileReader(args[0]));
        }

        int yyparse = yyparser.yyparse();
        ParserVal[] valstk1 = yyparser.valstk;

    }

    //#line 489 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) //check index bounds
            s = yyname[ch];    //now get it
        if (s == null)
            s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }


    //The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string


    //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
    int yyparse() {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;          //impossible char forces a read
        yystate = 0;            //initial state
        state_push(yystate);  //save it
        val_push(yylval);     //save empty value
        while (true) //until parsing is done, either correctly, or w/error
        {
            doaction = true;
            if (yydebug) debug("loop");
            //#### NEXT ACTION (from reduction table)
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0)      //we want a char?
                {
                    yychar = yylex();  //get next token
                    if (yydebug) debug(" next yychar:" + yychar);
                    //#### ERROR CHECK ####
                    if (yychar < 0)    //it it didn't work/error
                    {
                        yychar = 0;      //change it to default string (no -1!)
                        if (yydebug)
                            yylexdebug(yystate, yychar);
                    }
                }//yychar<0
                yyn = yysindex[yystate];  //get amount to shift by (shift index)
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug)
                        debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    //#### NEXT STATE ####
                    yystate = yytable[yyn];//we are in a new state
                    state_push(yystate);   //save it
                    val_push(yylval);      //push our lval as the input for next rule
                    yychar = -1;           //since we have 'eaten' a token, say we need another
                    if (yyerrflag > 0)     //have we recovered an error?
                        --yyerrflag;        //give ourselves credit
                    doaction = false;        //but don't process yet
                    break;   //quit the yyn=0 loop
                }

                yyn = yyrindex[yystate];  //reduce
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true; //get ready to execute
                    break;         //drop down to actions
                } else //ERROR RECOVERY
                {
                    if (yyerrflag == 0) {
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                yyerror("stack underflow. aborting...");  //note lower case 's'
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug)
                                    debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug)
                                    debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0)   //check for under & overflow here
                                {
                                    yyerror("Stack underflow. aborting...");  //capital 'S'
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else            //discard this token
                    {
                        if (yychar == 0)
                            return 1; //yyabort
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;  //read another
                    }
                }//end error recovery
            }//yyn=0 loop
            if (!doaction)   //any reason not to proceed?
                continue;      //skip action
            yym = yylen[yyn];          //get count of terminals on rhs
            if (yydebug)
                debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0)                 //if count of rhs not 'nil'
                yyval = val_peek(yym - 1); //get current semantic value
            yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
            switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
                case 1:
//#line 23 "lox.y"
                {
                    List<Declaration> declarations = (List) val_peek(0).obj;
                    yyval = new ParserVal(new Program(declarations));
                }
                break;
                case 2:
//#line 28 "lox.y"
                {
                    Parser.rule("declarations: empty");
                    List<Declaration> declarations = new ArrayList<>();
                    yyval = new ParserVal(declarations);
                }
                break;
                case 3:
//#line 34 "lox.y"
                {
                    Parser.rule("declarations:  declarations declaration", val_peek(1), val_peek(0));
                    List<Declaration> declarations = (List) val_peek(1).obj;
                    Declaration declaration = (Declaration) val_peek(0).obj;
                    declarations.add(declaration);
                    yyval = new ParserVal(declarations);
                }
                break;
                case 4:
//#line 43 "lox.y"
                {
                    Parser.rule("declaration: classDecl", val_peek(0));
                    yyval = new ParserVal(val_peek(0).obj);
                }
                break;
                case 5:
//#line 48 "lox.y"
                {
                    Parser.rule("declaration: funDecl", val_peek(0));
                    yyval = new ParserVal(val_peek(0).obj);
                }
                break;
                case 6:
//#line 53 "lox.y"
                {
                    Parser.rule("declaration: varDecl", val_peek(0));
                    yyval = new ParserVal(val_peek(0).obj);
                }
                break;
                case 7:
//#line 58 "lox.y"
                {
                    Parser.rule("declaration: statement", val_peek(0));
                    yyval = new ParserVal(val_peek(0).obj);
                }
                break;
                case 8:
//#line 64 "lox.y"
                {
                    Parser.rule("classDecl: CLASS IDENTIFIER optParentDecl LEFT_BRACE functions RIGHT_BRACE", val_peek(5), val_peek(4), val_peek(3), val_peek(2), val_peek(1));
                    String s = ((Tree) val_peek(4).obj).sym;
                    OptionalParentDeclaration optParentDecl = (OptionalParentDeclaration) val_peek(3).obj;
                    List<Function> functions = (List) val_peek(1).obj;
                    yyval = new ParserVal(new ClassDeclaration(s, optParentDecl, functions));
                }
                break;
                case 9:
//#line 72 "lox.y"
                {
                    Parser.rule("optParentDecl: empty");
                    yyval = new ParserVal(new EmptyParentDeclaration());
                }
                break;
                case 10:
//#line 77 "lox.y"
                {
                    Parser.rule("optParentDecl: LESS IDENTIFIER", val_peek(1), val_peek(0));
                    String s = ((Tree) val_peek(1).obj).sym;
                    yyval = new ParserVal(new ParentDeclaration(s));
                }
                break;
                case 11:
//#line 83 "lox.y"
                {
                    Parser.rule("functions: empty");
                    List<Function> functions = new ArrayList<>();
                    yyval = new ParserVal(functions);
                }
                break;
                case 12:
//#line 89 "lox.y"
                {
                    Parser.rule("functions: functions function", val_peek(1), val_peek(0));
                    List<Function> functions = (List) val_peek(1).obj;
                    Function function = (Function) val_peek(0).obj;
                    functions.add(function);
                    yyval = new ParserVal(functions);
                }
                break;
                case 13:
//#line 98 "lox.y"
                {
                    Parser.rule("funDecl: FUN function", val_peek(1), val_peek(0));
                    yyval = new ParserVal(new FunctionDeclaration(val_peek(0).obj));
                }
                break;
                case 14:
//#line 104 "lox.y"
                {
                    Parser.rule("function: IDENTIFIER LEFT_PAREN parameters RIGHT_PAREN block", val_peek(4), val_peek(3), val_peek(2), val_peek(1), val_peek(0));
                    yyval = new ParserVal(new Function(val_peek(2).obj, val_peek(0).obj));
                }
                break;
                case 15:
//#line 109 "lox.y"
                {
                    Parser.rule("parameters: empty");
                    List<Identifier> identifiers = new ArrayList<>();
                    yyval = new ParserVal(new Parameters(identifiers));
                }
                break;
                case 16:
//#line 115 "lox.y"
                {
                    Parser.rule("parameters: IDENTIFIER optIdentifiers", val_peek(1), val_peek(0));
                    String s = ((Tree) val_peek(1).obj).sym;
                    List<Identifier> identifiers = (List) val_peek(0).obj;
                    identifiers.add(0, new Identifier(s));
                    yyval = new ParserVal(identifiers);
                }
                break;
                case 17:
//#line 123 "lox.y"
                {
                    Parser.rule("optIdentifiers: empty");
                    List<Identifier> identifiers = new ArrayList<>();
                    yyval = new ParserVal(identifiers);
                }
                break;
                case 18:
//#line 129 "lox.y"
                {
                    Parser.rule("optIdentifiers: optIdentifiers optIdentifier", val_peek(1), val_peek(0));
                    List<Identifier> identifiers = (List) val_peek(1).obj;
                    Identifier identifier = (Identifier) val_peek(0).obj;
                    identifiers.add(identifier);
                    yyval = new ParserVal(identifiers);

                }
                break;
                case 19:
//#line 139 "lox.y"
                {
                    Parser.rule("optIdentifier: COMMA IDENTIFIER", val_peek(1), val_peek(0));
                    String s = ((Tree) val_peek(0).obj).sym;
                    yyval = new ParserVal(new Identifier(s));
                }
                break;
                case 20:
//#line 146 "lox.y"
                {
                    Parser.rule("varDecl: VAR IDENTIFIER optVarDecl SEMICOLON", val_peek(3), val_peek(2), val_peek(1), val_peek(0));
                    String s = ((Tree) val_peek(2).obj).sym;
                    OptionalVariableDeclaration optVarDecl = (OptionalVariableDeclaration) val_peek(1).obj;
                    yyval = new ParserVal(new VariableDeclaration(s, optVarDecl));
                }
                break;
                case 21:
//#line 153 "lox.y"
                {
                    Parser.rule("optVarDecl: empty");
                    yyval = new ParserVal(new EmptyVariableDeclaration());
                }
                break;
                case 22:
//#line 158 "lox.y"
                {
                    Parser.rule("optVarDecl: EQUALS expression", val_peek(1), val_peek(0));
                    Expression expr = (Expression) val_peek(0).obj;
                    yyval = new ParserVal(new ExpressionVariableDeclaration(expr));
                }
                break;
                case 23:
//#line 165 "lox.y"
                {
                    Parser.rule("block: LEFT_BRACE declarations RIGHT_BRACE", val_peek(2), val_peek(1), val_peek(0));
                    yyval = new ParserVal(new BlockStatement((List) val_peek(1).obj));
                }
                break;
                case 24:
//#line 170 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 25:
//#line 173 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 26:
//#line 176 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 27:
//#line 179 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 28:
//#line 182 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 29:
//#line 185 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 30:
//#line 188 "lox.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 31:
//#line 193 "lox.y"
                {
                    Parser.rule("forStmt: FOR LEFT_PAREN firstForInit optExpression SEMICOLON optExpression RIGHT_PAREN statement", val_peek(7), val_peek(6), val_peek(5), val_peek(4), val_peek(3), val_peek(2), val_peek(1), val_peek(0));

                    FirstForInit init = (FirstForInit) val_peek(5).obj;
                    OptExpression optExpr1 = (OptExpression) val_peek(4).obj;
                    OptExpression optExpr2 = (OptExpression) val_peek(2).obj;
                    Statement stmt = (Statement) val_peek(0).obj;
                    yyval = new ParserVal(new ForStatement(init, optExpr1, optExpr2, stmt));

                }
                break;
                case 32:
//#line 205 "lox.y"
                {
                    Parser.rule("returnStmt: RETURN optExpression SEMICOLON", val_peek(2), val_peek(1), val_peek(0));
                    yyval = new ParserVal(new ReturnStatement((OptExpression) val_peek(1).obj));
                }
                break;
                case 33:
//#line 210 "lox.y"
                {
                    Parser.rule("optExpression: empty");
                    yyval = new ParserVal(new EmptyExpression());
                }
                break;
                case 34:
//#line 215 "lox.y"
                {
                    Parser.rule("optExpression: expression", val_peek(0));
                    Expression expr = (Expression) val_peek(0).obj;
                    yyval = new ParserVal(new NormalExpression(expr));
                }
                break;
                case 35:
//#line 221 "lox.y"
                {
                    VariableDeclaration varDecl = (VariableDeclaration) val_peek(0).obj;
                    yyval = new ParserVal(new VarDeclForInit(varDecl));
                }
                break;
                case 36:
//#line 225 "lox.y"
                {
                    ExpressionStatement exprStmt = (ExpressionStatement) val_peek(0).obj;
                    yyval = new ParserVal(new ExprStmtForInit(exprStmt));
                }
                break;
                case 37:
//#line 229 "lox.y"
                {
                    yyval = new ParserVal(new EmptyForInit());
                }
                break;
                case 38:
//#line 234 "lox.y"
                {
                    Parser.rule("exprStmt: expression SEMICOLON", val_peek(1), val_peek(0));
                    Expression expr = (Expression) val_peek(1).obj;
                    yyval = new ParserVal(new ExpressionStatement(expr));
                }
                break;
                case 39:
//#line 241 "lox.y"
                {
                    Parser.rule("ifStmt: IF LEFT_PAREN expression RIGHT_PAREN statement optElse", val_peek(5), val_peek(4), val_peek(3), val_peek(2), val_peek(1), val_peek(0));
                    Else elsePart = (Else) val_peek(0).obj;
                    Statement stmt = (Statement) val_peek(1).obj;
                    Expression expr = (Expression) val_peek(3).obj;
                    yyval = new ParserVal(new IfStatement(expr, stmt, elsePart));
                }
                break;
                case 40:
//#line 249 "lox.y"
                {
                    Parser.rule("optElse: empty");
                    yyval = new ParserVal(new EmptyElse());
                }
                break;
                case 41:
//#line 254 "lox.y"
                {
                    Parser.rule("optElse: ELSE statement", val_peek(1), val_peek(0));
                    Statement stmt = (Statement) val_peek(0).obj;
                    yyval = new ParserVal(new NormalElse(stmt));
                }
                break;
                case 42:
//#line 261 "lox.y"
                {
                    Parser.rule("printStmt: PRINT expression SEMICOLON", val_peek(2), val_peek(1), val_peek(0));
                    Expression expr = (Expression) val_peek(1).obj;
                    yyval = new ParserVal(new PrintStatement(expr));
                }
                break;
                case 43:
//#line 268 "lox.y"
                {
                    Parser.rule("whileStmt: WHILE LEFT_PAREN expression RIGHT_PAREN statement", val_peek(4), val_peek(3), val_peek(2), val_peek(1), val_peek(0));
                    Expression expr = (Expression) val_peek(2).obj;
                    Statement stmt = (Statement) val_peek(0).obj;
                    yyval = new ParserVal(new WhileStatement(expr, stmt));
                }
                break;
                case 44:
//#line 276 "lox.y"
                {
                    Parser.rule("expression: assignment", val_peek(0));
                    Assignment assign = (Assignment) val_peek(0).obj;
                    yyval = new ParserVal(new Expression(assign));
                }
                break;
                case 45:
//#line 283 "lox.y"
                {
                    Parser.rule("assignment: IDENTIFIER EQUALS expression", val_peek(2), val_peek(1), val_peek(0));
                    yyval = new ParserVal(new IdentifierEqualsExpression((Expression) val_peek(0).obj));
                }
                break;
                case 46:
//#line 288 "lox.y"
                {
                    Parser.rule("assignment: call EQUALS expression", val_peek(2), val_peek(1), val_peek(0));
                    yyval = new ParserVal(new CallEqualsExpression((Expression) val_peek(0).obj));
                }
                break;
                case 47:
//#line 293 "lox.y"
                {
                    Parser.rule("assignment: logic_or", val_peek(0));
                    yyval = new ParserVal(val_peek(0).obj);
                }
                break;
                case 48:
//#line 299 "lox.y"
                {
                    Parser.rule("logic_or: logic_and logic_ands", val_peek(1), val_peek(0));
                    List<LogicAnd> logicAnds = (List) val_peek(0).obj;
                    LogicAnd logicAnd = (LogicAnd) val_peek(1).obj;
                    logicAnds.add(0, logicAnd);
                    yyval = new ParserVal(new LogicOr(logicAnds));
                }
                break;
                case 49:
//#line 307 "lox.y"
                {
                    Parser.rule("logic_ands: empty");
                    List<LogicAnd> logicAnds = new ArrayList<>();
                    yyval = new ParserVal(logicAnds);
                }
                break;
                case 50:
//#line 313 "lox.y"
                {
                    Parser.rule("logic_ands: logic_ands OR logic_and", val_peek(2), val_peek(1), val_peek(0));
                    List<LogicAnd> logicAnds = (List) val_peek(2).obj;
                    LogicAnd logicAnd = (LogicAnd) val_peek(0).obj;
                    logicAnds.add(logicAnd);
                    yyval = new ParserVal(logicAnds);
                }
                break;
                case 51:
//#line 322 "lox.y"
                {
                    Parser.rule("logic_and: equality equalities", val_peek(1), val_peek(0));
                    Equality equality = (Equality) val_peek(1).obj;
                    List<Equality> equalities = (List) val_peek(0).obj;
                    equalities.add(0, equality);
                    yyval = new ParserVal(new LogicAnd(equalities));
                }
                break;
                case 52:
//#line 330 "lox.y"
                {
                    Parser.rule("equalities: empty");
                    List<Equality> equalities = new ArrayList<>();
                    yyval = new ParserVal(equalities);
                }
                break;
                case 53:
//#line 336 "lox.y"
                {
                    Parser.rule("equalities: equalities AND equality", val_peek(2), val_peek(1), val_peek(0));
                    List<Equality> equalities = (List) val_peek(2).obj;
                    Equality equality = (Equality) val_peek(0).obj;
                    equalities.add(equality);
                    yyval = new ParserVal(equalities);
                }
                break;
                case 54:
//#line 345 "lox.y"
                {
                    Parser.rule("equality: comparison comparisons", val_peek(1), val_peek(0));
                    Comparison comparison = (Comparison) val_peek(1).obj;
                    List<ComparisonElement> comparisons = (List) val_peek(0).obj;
                    yyval = new ParserVal(new Equality(comparison, comparisons));
                }
                break;
                case 55:
//#line 352 "lox.y"
                {
                    Parser.rule("comparisons: empty");
                    List<ComparisonElement> comparisons = new ArrayList<>();
                    yyval = new ParserVal(comparisons);
                }
                break;
                case 56:
//#line 358 "lox.y"
                {
                    Parser.rule("comparisons: comparisons comparison_sign comparison", val_peek(2), val_peek(1), val_peek(0));

                    ComparisonElement comparisonElement = new ComparisonElement(((Tree) val_peek(1).obj).sym, (Comparison) val_peek(0).obj);

                    List<ComparisonElement> comparisons = (List) val_peek(2).obj;
                    comparisons.add(comparisonElement);
                    yyval = new ParserVal(comparisons);

                }
                break;
                case 59:
//#line 373 "lox.y"
                {
                    Parser.rule("comparison: term terms", val_peek(1), val_peek(0));
                    Term term = (Term) val_peek(1).obj;
                    List<TermElement> terms = (List) val_peek(0).obj;
                    yyval = new ParserVal(new Comparison(term, terms));
                }
                break;
                case 60:
//#line 380 "lox.y"
                {
                    Parser.rule("terms: empty");
                    List<TermElement> terms = new ArrayList<>();
                    yyval = new ParserVal(terms);
                }
                break;
                case 61:
//#line 386 "lox.y"
                {
                    Parser.rule("terms term_sign term", val_peek(2), val_peek(1), val_peek(0));
                    List<TermElement> terms = (List) val_peek(2).obj;
                    String sign = ((Tree) val_peek(1).obj).sym;
                    Term term = (Term) val_peek(0).obj;
                    yyval = new ParserVal(new TermElement(sign, term));
                }
                break;
                case 66:
//#line 400 "lox.y"
                {
                    Parser.rule("term: factor factors", val_peek(1), val_peek(0));
                    Factor factor = (Factor) val_peek(1).obj;
                    List<FactorElement> factors = (List) val_peek(0).obj;
                    yyval = new ParserVal(new Term(factor, factors));
                }
                break;
                case 67:
//#line 407 "lox.y"
                {
                    Parser.rule("factors: empty");
                    yyval = new ParserVal(new ArrayList<FactorElement>());
                }
                break;
                case 68:
//#line 412 "lox.y"
                {
                    Parser.rule("factors: factors factor_sign factor", val_peek(2), val_peek(1), val_peek(0));
                    FactorElement factor = new FactorElement(((Tree) val_peek(1).obj).sym, (Factor) val_peek(0).obj);
                    List<FactorElement> factors = (List) val_peek(2).obj;
                    factors.add(factor);
                    yyval = new ParserVal(factors);
                }
                break;
                case 69:
//#line 421 "lox.y"
                {
                    Parser.rule("factor: unary unaries", val_peek(1), val_peek(0));
                    Unary unary = (Unary) val_peek(1).obj;
                    List<UnaryElement> unaries = (List) val_peek(0).obj;
                    Factor factor = new Factor(unary, unaries);
                    yyval = new ParserVal(factor);
                }
                break;
                case 70:
//#line 429 "lox.y"
                {
                    Parser.rule("unaries: empty");
                    List<UnaryElement> unaries = new ArrayList<>();
                    yyval = new ParserVal(unaries);
                }
                break;
                case 71:
//#line 435 "lox.y"
                {
                    Parser.rule("unaries: unaries unary_sign unary", val_peek(2), val_peek(1), val_peek(0));
                    List<UnaryElement> unaries = (List) val_peek(2).obj;
                    String sign = ((Tree) val_peek(1).obj).sym;
                    Unary unary = (Unary) val_peek(0).obj;
                    unaries.add(new UnaryElement(sign, unary));
                    yyval = new ParserVal(unaries);
                }
                break;
                case 72:
//#line 445 "lox.y"
                {
                    Parser.rule("unary: bang_or_minus factor", val_peek(1), val_peek(0));
                    yyval = new ParserVal(new BangOrMinusFactor(((Tree) val_peek(1).obj).sym, (Factor) val_peek(0).obj));
                }
                break;
                case 73:
//#line 450 "lox.y"
                {
                    Parser.rule("unary: call", val_peek(0));
                    yyval = new ParserVal(new UnaryCall((Call) val_peek(0).obj));
                }
                break;
                case 80:
//#line 464 "lox.y"
                {
                    Parser.rule("arguments: empty");
                    List<Expression> expressions = new ArrayList<>();
                    yyval = new ParserVal(expressions);
                }
                break;
                case 81:
//#line 470 "lox.y"
                {
                    Parser.rule("arguments: expression", val_peek(0));
                    Expression expression = (Expression) val_peek(0).obj;
                    List<Expression> expressions = new ArrayList<>();
                    expressions.add(expression);
                    yyval = new ParserVal(expressions);
                }
                break;
                case 82:
//#line 478 "lox.y"
                {
                    Parser.rule("arguments: expression expressions", val_peek(1), val_peek(0));
                    List<Expression> expressions = (List) val_peek(0).obj;
                    Expression expression = (Expression) val_peek(1).obj;
                    expressions.add(0, expression);
                    yyval = new ParserVal(expressions);
                }
                break;
                case 83:
//#line 486 "lox.y"
                {
                    Parser.rule("empty expressions");
                    List<Expression> expressions = new ArrayList<>();
                    yyval = new ParserVal(expressions);
                }
                break;
                case 84:
//#line 492 "lox.y"
                {
                    Parser.rule("expressions COMMA expression", val_peek(2), val_peek(1), val_peek(0));
                    List<Expression> expressions = (List) val_peek(2).obj;
                    Expression expression = (Expression) val_peek(0).obj;
                    expressions.add(expression);
                    yyval = new ParserVal(expressions);
                }
                break;
                case 85:
//#line 501 "lox.y"
                {
                    Parser.rule("call: primary primary_blocks", val_peek(1), val_peek(0));
                    Primary primary = (Primary) val_peek(1).obj;
                    List<PrimaryBlock> primaryBlocks = (List) val_peek(0).obj;
                    yyval = new ParserVal(new Call(primary, primaryBlocks));
                }
                break;
                case 86:
//#line 509 "lox.y"
                {
                    Parser.rule("arguments_group: empty");
                    List<Expression> arguments = new ArrayList<>();
                    yyval = new ParserVal(arguments);
                }
                break;
                case 87:
//#line 515 "lox.y"
                {
                    Parser.rule("arguments_group: arguments", val_peek(0));
                    List<Expression> expressions = (List) val_peek(0).obj;
                    yyval = new ParserVal(expressions);
                }
                break;
                case 88:
//#line 522 "lox.y"
                {
                    Parser.rule("primary_block: LEFT_PAREN arguments_group RIGHT_PAREN", val_peek(2), val_peek(1), val_peek(0));
                    List<Expression> expressions = (List) val_peek(1).obj;
                    PrimaryBlock.ArgumentsGroup ag = new PrimaryBlock.ArgumentsGroup(expressions);
                    yyval = new ParserVal(ag);
                }
                break;
                case 89:
//#line 529 "lox.y"
                {
                    Parser.rule("DOT IDENTIFIER", val_peek(1), val_peek(0));
                    String s = ((Tree) val_peek(0).obj).sym;
                    new ParserVal(new PrimaryBlock.DotIdentifier(s));

                }
                break;
                case 90:
//#line 536 "lox.y"
                {
                    Parser.rule("primary_blocks /*empty*/");
                    List<PrimaryBlock> primaryBlocks = new ArrayList<>();
                    yyval = new ParserVal(primaryBlocks);
                }
                break;
                case 91:
//#line 542 "lox.y"
                {
                    Parser.rule("primary_blocks primary_block", val_peek(1), val_peek(0));
                    List<PrimaryBlock> primaryBlocks = (List) val_peek(1).obj;
                    PrimaryBlock primaryBlock = (PrimaryBlock) val_peek(0).obj;
                    primaryBlocks.add(primaryBlock);
                    yyval = new ParserVal(primaryBlocks);
                }
                break;
                case 92:
//#line 551 "lox.y"
                {
                    Parser.rule("TRUE", val_peek(0));
                    yyval = new ParserVal(new Primary.True());
                }
                break;
                case 93:
//#line 556 "lox.y"
                {
                    Parser.rule("FALSE", val_peek(0));
                    yyval = new ParserVal(new Primary.False());
                }
                break;
                case 94:
//#line 561 "lox.y"
                {
                    Parser.rule("NIL", val_peek(0));
                    yyval = new ParserVal(new Primary.Nil());
                }
                break;
                case 95:
//#line 566 "lox.y"
                {
                    Parser.rule("THIS", val_peek(0));
                    yyval = new ParserVal(new Primary.This());
                }
                break;
                case 96:
//#line 571 "lox.y"
                {
                    Parser.rule("NUMBER", val_peek(0));
                    Double d = Double.parseDouble(((Tree) val_peek(0).obj).sym);
                    yyval = new ParserVal(new Primary.Number(d));
                }
                break;
                case 97:
//#line 577 "lox.y"
                {
                    Parser.rule("STRING", val_peek(0));
                    String s = ((Tree) val_peek(0).obj).sym;
                    yyval = new ParserVal(new Primary.String(s));
                }
                break;
                case 98:
//#line 583 "lox.y"
                {
                    Parser.rule("IDENTIFIER", val_peek(0));
                    String s = ((Tree) val_peek(0).obj).sym;
                    yyval = new ParserVal(new Primary.Identifier(s));
                }
                break;
                case 99:
//#line 589 "lox.y"
                {
                    Parser.rule("LEFT_PAREN expression RIGHT_PAREN", val_peek(2), val_peek(1), val_peek(0));
                    Expression expression = (Expression) val_peek(1).obj;
                    yyval = new ParserVal(new Primary.ExpressionPrimary(expression));

                }
                break;
                case 100:
//#line 596 "lox.y"
                {
                    Parser.rule("SUPER DOT IDENTIFIER", val_peek(2), val_peek(1), val_peek(0));
                    String s = ((Tree) val_peek(0).obj).sym;
                    yyval = new ParserVal(new Primary.SuperIdentifier(s));
                }
                break;
//#line 1358 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
            }//switch
            //#### Now let's reduce... ####
            if (yydebug) debug("reduce");
            state_drop(yym);             //we just reduced yylen states
            yystate = state_peek(0);     //get new state
            val_drop(yym);               //corresponding value drop
            yym = yylhs[yyn];            //select next TERMINAL(on lhs)
            if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
            {
                if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;         //explicitly say we're done
                state_push(YYFINAL);       //and save it
                val_push(yyval);           //also save the semantic value of parsing
                if (yychar < 0)            //we want another character?
                {
                    yychar = yylex();        //get next character
                    if (yychar < 0) yychar = 0;  //clean, if necessary
                    if (yydebug)
                        yylexdebug(yystate, yychar);
                }
                if (yychar == 0)          //Good exit (if lex returns 0 ;-)
                    break;                 //quit the loop--all DONE
            }//if yystate
            else                        //else not done yet
            {                         //get next state and push, for next yydefred[]
                yyn = yygindex[yym];      //find out where to go
                if ((yyn != 0) && (yyn += yystate) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
                    yystate = yytable[yyn]; //get new state
                else
                    yystate = yydgoto[yym]; //else go to new defred
                if (yydebug)
                    debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
                state_push(yystate);     //going again, so push state & val...
                val_push(yyval);         //for next action
            }
        }//main loop
        return 0;//yyaccept!!
    }
//## end of method parse() ######################################


//## run() --- for Thread #######################################

    /**
     * A default run method, used for operating this parser
     * object in the background.  It is intended for extending Thread
     * or implementing Runnable.  Turn off with -Jnorun .
     */
    public void run() {
        yyparse();
    }
//## end of method run() ########################################


//## Constructors ###############################################

    /**
     * Default constructor.  Turn off with -Jnoconstruct .
     */
    public Parser() {
        //nothing to do
    }


    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe) {
        yydebug = debugMe;
    }
//###############################################################


}
//################### END OF CLASS ##############################
