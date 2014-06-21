package edu.uap.tripla;

import java_cup.runtime.*;


/**
 * Tokens for the TRIPLA-Parser.
 *
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */


%%

/* Options */
%class Tokenizer // name of lexer class
%cup             // CUP compatibility mode


/* Macro Declarations */
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

Id = [A-Za-z_][A-Za-z0-9_]*
Const = 0 | [1-9][0-9]*
Op = "+" | "-" | "*" | "/" | "==" | "!=" | "<" | ">"

%%

/* Lexical Rules */
<YYINITIAL> {

    {Const}       { return new Symbol(sym.CONST, new Integer(yytext())); }
    {Op}          { return new Symbol(sym.OP, yytext()); }

    {WhiteSpace}  { /* do nothing */ }
    
    ","           { return new Symbol(sym.COMMA); }
    ";"           { return new Symbol(sym.SEMIC); }
    let           { return new Symbol(sym.LET); }
    in            { return new Symbol(sym.IN); }
    "("           { return new Symbol(sym.LPAREN); }
    ")"           { return new Symbol(sym.RPAREN); }
    "{"           { return new Symbol(sym.LCPAREN); }
    "}"           { return new Symbol(sym.RCPAREN); }
    "="           { return new Symbol(sym.EQ); }
    if            { return new Symbol(sym.IF); }
    then          { return new Symbol(sym.THEN); }
    else          { return new Symbol(sym.ELSE); }

    {Id}          { return new Symbol(sym.ID, yytext()); }
}

[^]  { throw new Error("Illegal character <" + yytext() + ">"); }

