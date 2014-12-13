/* Compile me with:  jflex IC.lex  */
package IC.Parser;
import java_cup.runtime.Symbol;

%%

%public
%class Scanner
%column
%line
%unicode
%cup
%type Token

%{
public int getLineNumber(){
	return yyline;
}

Token tok(int id, String text) { 
	return new Token(id, text, yycolumn, yyline, yytext());
}

Error error(String message, boolean isPrintable){
	int line = yyline + 1;
	int column = yycolumn + 1;
	if (isPrintable){
		return new Error(line + ":" + column + " : lexical error; " + message + "'" + yytext() + "'");
	}
	else{
		return new Error(line + ":" + column + " : lexical error; " + message);
	}
}
%}

%eofval{ 
	return tok(sym.EOF,"EOF");
%eofval}

/* main character classes */
LINE_TERMINATOR = \r|\n|\r\n
INPUT_CHARACTER = [^\r\n]

WHITE_SPACE = {LINE_TERMINATOR} | [ \t\f]

/* comments */
COMMENT = {TRADITIONAL_COMMENT} | {END_OF_LINE_COMMENT} | {DOCUMENTATION_COMMENT}

TRADITIONAL_COMMENT = "/*" [^*] ~"*/" | "/*" "*"+ "/"
END_OF_LINE_COMMENT = "//" {INPUT_CHARACTER}* {LINE_TERMINATOR}?
DOCUMENTATION_COMMENT = "/*" "*"+ [^/*] ~"*/"
UNTERMINATED_COMMENT = "/*"[^/*]*
ALLOWED_IC_STRING_CHARS = ([ !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ "\[" "\]" "\^" _`abcdefghijklmnopqrstuvwxyz{|}] |"\\\\" |"\\t"| "\\n")
ALPHANUMERIC = [a-zA-Z0-9_]
LOWER = [a-z]
UPPER = [A-Z]
DIGIT = [0-9]
DIGIT_NO_ZERO = [1-9]
ZERO = 0
DQUOTE = [\"]
INT = {DIGIT_NO_ZERO} {DIGIT}*

INTEGER = {ZERO} | {INT}
ID = {LOWER} {ALPHANUMERIC}*
CLASS_ID = {UPPER} {ALPHANUMERIC}* 
UNTERMINATED_STRING = ({DQUOTE}((\\(\"))|.)*])
INVALID_STRING =({UNTERMINATED_STRING}?({DQUOTE}))
STRING = ({DQUOTE}((\\(\"))|{ALLOWED_IC_STRING_CHARS})*?({DQUOTE}))


%%
/* keywords */
"boolean"                      { return tok(sym.BOOLEAN,"boolean"); }
"break"                        { return tok(sym.BREAK,"break"); }
"class"                        { return tok(sym.CLASS,"class"); }
"continue"                     { return tok(sym.CONTINUE,"continue"); }
"else"                         { return tok(sym.ELSE,"else"); }
"extends"                      { return tok(sym.EXTENDS,"extends"); }
"int"                          { return tok(sym.INT,"int"); }
"new"                          { return tok(sym.NEW,"new"); }
"if"                           { return tok(sym.IF,"if"); }
"return"                       { return tok(sym.RETURN,"return"); }
"void"                         { return tok(sym.VOID,"void"); }
"static"                       { return tok(sym.STATIC,"static"); }
"while"                        { return tok(sym.WHILE,"while"); }
"this"                         { return tok(sym.THIS,"this"); }
"length"					   { return tok(sym.LENGTH,"length"); }
"string"					   { return tok(sym.TYPE_STRING,"type_string"); }
  
/* boolean literals */
"true"                         { return tok(sym.TRUE,"true"); }
"false"                        { return tok(sym.FALSE,"false"); }
  
 /* null literal */
 "null"                        { return tok(sym.NULL,"null"); }
 
 /* separators */
 "("                            { return tok(sym.LP,"("); }
 ")"                            { return tok(sym.RP,")"); }
 "{"                            { return tok(sym.LCP,"{"); }
 "}"                            { return tok(sym.RCP,"}"); }
 "["                            { return tok(sym.LSP,"["); }
 "]"                            { return tok(sym.RSP,"]"); }
 ";"                            { return tok(sym.SEMI_COLONS,";"); }
 ","                            { return tok(sym.COMMA,","); }
 "."                            { return tok(sym.DOT,"."); }
  
 /* operators */
 "="                            { return tok(sym.ASSIGN,"="); }
 ">"                            { return tok(sym.BT,">"); }
 "<"                            { return tok(sym.ST,"<"); }
 "!"                            { return tok(sym.NOT,"!"); }
 "=="                           { return tok(sym.EQUALS,"=="); }
 "<="                           { return tok(sym.BTE,"<="); }
 ">="                           { return tok(sym.STE,">="); }
 "!="                           { return tok(sym.NE,"!="); }
 "&&"                           { return tok(sym.AND,"&&"); }
 "||"                           { return tok(sym.OR,"||"); }
 "+"                            { return tok(sym.PLUS,"+"); }
 "-"                            { return tok(sym.MINUS,"-"); }
 "*"                            { return tok(sym.MULT,"*"); }
 "/"                            { return tok(sym.DIV,"/"); }
 "%"                            { return tok(sym.MOD,"%"); }
   
{WHITE_SPACE}					{ /* nothing; skip */ }
{INTEGER}						{ return tok(sym.INTEGER,"INTEGER"); }
{ID}        					{ return tok(sym.ID,"ID"); }
{CLASS_ID}						{ return tok(sym.CLASS_ID,"CLASS_ID"); }
{COMMENT}   					{ /* nothing; skip */ }
{UNTERMINATED_COMMENT}			{ throw error("unterminated comment", false); }
{STRING}    					{ return tok(sym.STRING,"STRING"); }
{INVALID_STRING}				{ throw error("malformed string literal", false); }
"_"								{ throw error("an identifier cannot start with", true); }
.           					{ throw error("invalid character", true); }
