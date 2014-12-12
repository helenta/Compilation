
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20140808 (SVN rev 54)
//----------------------------------------------------

package IC.Parser;

import IC.AST.*;
import IC.DataTypes;
import java_cup.runtime.*;
import java.util.*;
import java.io.*;
import java_cup.runtime.XMLElement;

/** CUP v0.11b 20140808 (SVN rev 54) generated parser.
  */
@SuppressWarnings({"rawtypes"})
public class LibParser extends java_cup.runtime.lr_parser {

 public final Class getSymbolContainer() {
    return sym.class;
}

  /** Default constructor. */
  public LibParser() {super();}

  /** Constructor which sets the default scanner. */
  public LibParser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public LibParser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\024\000\002\003\003\000\002\002\004\000\002\003" +
    "\004\000\002\004\010\000\002\004\007\000\002\005\004" +
    "\000\002\005\003\000\002\006\010\000\002\006\011\000" +
    "\002\010\003\000\002\010\005\000\002\007\004\000\002" +
    "\011\003\000\002\011\003\000\002\013\003\000\002\013" +
    "\005\000\002\012\003\000\002\012\003\000\002\012\003" +
    "\000\002\012\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\044\000\004\006\005\001\002\000\006\002\001\006" +
    "\001\001\002\000\004\061\011\001\002\000\006\002\010" +
    "\006\005\001\002\000\006\002\uffff\006\uffff\001\002\000" +
    "\004\002\000\001\002\000\004\032\012\001\002\000\004" +
    "\017\014\001\002\000\006\017\014\031\044\001\002\000" +
    "\014\004\024\012\025\016\023\023\017\061\022\001\002" +
    "\000\006\017\ufffb\031\ufffb\001\002\000\004\060\030\001" +
    "\002\000\006\033\uffef\060\uffef\001\002\000\006\033\026" +
    "\060\ufff3\001\002\000\004\060\ufff5\001\002\000\006\033" +
    "\uffee\060\uffee\001\002\000\004\060\ufff4\001\002\000\006" +
    "\033\ufff0\060\ufff0\001\002\000\006\033\ufff1\060\ufff1\001" +
    "\002\000\004\034\027\001\002\000\004\060\ufff2\001\002" +
    "\000\004\027\031\001\002\000\014\004\024\012\025\023" +
    "\017\030\035\061\022\001\002\000\004\030\042\001\002" +
    "\000\006\030\ufff8\036\040\001\002\000\004\060\037\001" +
    "\002\000\004\035\036\001\002\000\006\017\ufffa\031\ufffa" +
    "\001\002\000\006\030\ufff6\036\ufff6\001\002\000\012\004" +
    "\024\012\025\023\017\061\022\001\002\000\004\030\ufff7" +
    "\001\002\000\004\035\043\001\002\000\006\017\ufff9\031" +
    "\ufff9\001\002\000\010\002\ufffd\006\ufffd\035\046\001\002" +
    "\000\006\017\ufffc\031\ufffc\001\002\000\006\002\ufffe\006" +
    "\ufffe\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\044\000\006\003\005\004\003\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\004\006\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\006\005" +
    "\012\006\014\001\001\000\004\006\044\001\001\000\010" +
    "\011\015\012\017\013\020\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\012\007\032\010\031\012\017\013" +
    "\033\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\012\007\032\010\040\012\017\013\033\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$LibParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$LibParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$LibParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {

	Token t = next_token();
	if (printTokens)
		System.out.println(t.getLine() + ":" + t);
	return t; 

    }



	public boolean printTokens;
	
	private Scanner lexer;
	private ArrayList<Token> all_tokens;
	private int curr_token_index = -1;
	
	private Token currentToken;

	public LibParser(Scanner lexer) {
		super(lexer);
		this.lexer = lexer;
		this.all_tokens = new ArrayList<Token>();
	}
	
	public Token next_token() throws IOException{
	    curr_token_index++;
		currentToken = lexer.next_token();
		all_tokens.add(currentToken);
		return currentToken;
	}
	
	public int getLine() {
		return lexer.getLineNumber();
	}
	
	public void syntax_error(Symbol s) 
	{
		Token tok = (Token)s;
	
		List<Integer> ids = this.expected_token_ids();
		StringBuffer expectedTokens = new StringBuffer();
		for (Integer id : ids)
		{
			expectedTokens.append('\'');
   			expectedTokens.append(sym.terminalNames[id]);
   			expectedTokens.append('\'');
   			expectedTokens.append(" or ");
		}
		
		int length = expectedTokens.length();
		if (length > 0)
		{
			expectedTokens.delete(length-4, length);
		}
	
		String userMessage = tok.getRow() + ":" + tok.getColumn() + " : " +
							 "syntax error; expected " + expectedTokens.toString() + 
							 ", but found \'" + tok.getTag() + "\'";
							 
		System.out.println(userMessage);
	}


/** Cup generated class to encapsulate user supplied action code.*/
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class CUP$LibParser$actions {
  private final LibParser parser;

  /** Constructor */
  CUP$LibParser$actions(LibParser parser) {
    this.parser = parser;
  }

  /** Method 0 with the actual generated action code for actions 0 to 300. */
  public final java_cup.runtime.Symbol CUP$LibParser$do_action_part00000000(
    int                        CUP$LibParser$act_num,
    java_cup.runtime.lr_parser CUP$LibParser$parser,
    java.util.Stack            CUP$LibParser$stack,
    int                        CUP$LibParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$LibParser$result;

      /* select the action based on the action number */
      switch (CUP$LibParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // multiLibic ::= libic 
            {
              Object RESULT =null;
		int libleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int libright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object lib = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		 
	 			List<ICClass> libList = new ArrayList<ICClass>();
				libList.add((ICClass)lib);
				RESULT = libList;
			
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("multiLibic",1, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= multiLibic EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
		RESULT = start_val;
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$LibParser$parser.done_parsing();
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // multiLibic ::= multiLibic libic 
            {
              Object RESULT =null;
		int libListleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
		int libListright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
		Object libList = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
		int libleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int libright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object lib = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
 				((List<ICClass>)libList).add((ICClass)lib);
 				RESULT = libList;
 			
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("multiLibic",1, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // libic ::= CLASS CLASS_ID LCP multylibmethod RCP SEMI_COLONS 
            {
              Object RESULT =null;
		int tokleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).left;
		int tokright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).right;
		Object tok = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).right;
		Object id = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).value;
		int lib_method_listleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
		int lib_method_listright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
		Object lib_method_list = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
		
			RESULT = new ICClass(tokleft, id.toString(), new ArrayList<Field>(), (List<Method>)lib_method_list);
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libic",2, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // libic ::= CLASS CLASS_ID LCP multylibmethod RCP 
            {
              Object RESULT =null;
		int tokleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).left;
		int tokright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).right;
		Object tok = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).right;
		Object id = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).value;
		int lib_method_listleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
		int lib_method_listright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
		Object lib_method_list = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
		
			RESULT = new ICClass(tokleft, id.toString(), new ArrayList<Field>(), (List<Method>)lib_method_list);
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libic",2, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // multylibmethod ::= multylibmethod libmethod 
            {
              Object RESULT =null;
		int method_listleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
		int method_listright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
		Object method_list = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
		int methodleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int methodright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object method = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
			((List<Method>)method_list).add((Method)method);
			RESULT = method_list;
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("multylibmethod",3, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // multylibmethod ::= libmethod 
            {
              Object RESULT =null;
		int methodleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int methodright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object method = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
			List<LibraryMethod> methodList = new ArrayList<LibraryMethod>();
			methodList.add((LibraryMethod)method);
			RESULT = methodList;
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("multylibmethod",3, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // libmethod ::= STATIC typeV ID LP RP SEMI_COLONS 
            {
              Object RESULT =null;
		int typeleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).left;
		int typeright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).right;
		Object type = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).right;
		Object id = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).value;
		
			int currIndex = curr_token_index;
			ArrayList<Token> allTokens = all_tokens;
			String fg = currentToken.getText();
			RESULT = new LibraryMethod((Type)type, id.toString(), new ArrayList<Formal>());//(List<Formal>)formal_list);
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libmethod",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // libmethod ::= STATIC typeV ID LP libMethodArgumentsList RP SEMI_COLONS 
            {
              Object RESULT =null;
		int typeleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).left;
		int typeright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).right;
		Object type = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).right;
		Object id = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).value;
		int args_listleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
		int args_listright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
		Object args_list = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
		
			int currIndex = curr_token_index;
			ArrayList<Token> allTokens = all_tokens;
			String fg = currentToken.getText();
			RESULT = new LibraryMethod((Type)type, id.toString(), (List<Formal>)args_list);
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libmethod",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-6)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // libMethodArgumentsList ::= libMethodArgument 
            {
              Object RESULT =null;
		int argleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int argright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object arg = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
			List<Formal> myList = new ArrayList<Formal>();
			myList.add((Formal)arg);
			RESULT = myList; 
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libMethodArgumentsList",6, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // libMethodArgumentsList ::= libMethodArgument COMMA libMethodArgumentsList 
            {
              Object RESULT =null;
		int argleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
		int argright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
		Object arg = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
		int args_listleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int args_listright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object args_list = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
			((List<Formal>)args_list).add((Formal)arg);
			RESULT = args_list; 
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libMethodArgumentsList",6, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // libMethodArgument ::= type ID 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int idright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object id = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
			RESULT = new Formal((Type)t, id.toString());
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libMethodArgument",5, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // typeV ::= type 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		 
			RESULT = t;
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("typeV",7, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // typeV ::= VOID 
            {
              Object RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int vright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object v = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		 
			RESULT = new PrimitiveType(vleft, DataTypes.VOID); 
		
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("typeV",7, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // type ::= typeNoBrc 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
			RESULT = t;
		 
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",9, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // type ::= typeNoBrc LSP RSP 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
		
		 	((Type)t).incrementDimension();
		 	RESULT = t;
		 
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",9, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // typeNoBrc ::= INT 
            {
              Object RESULT =null;
		int tokleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int tokright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object tok = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
		RESULT = new PrimitiveType(tokleft, DataTypes.INT);
	
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("typeNoBrc",8, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // typeNoBrc ::= BOOLEAN 
            {
              Object RESULT =null;
		int tokleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int tokright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object tok = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
		RESULT = new PrimitiveType(tokleft, DataTypes.BOOLEAN);
	
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("typeNoBrc",8, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // typeNoBrc ::= TYPE_STRING 
            {
              Object RESULT =null;
		int tokleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int tokright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object tok = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
		RESULT = new PrimitiveType(tokleft, DataTypes.STRING);
	
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("typeNoBrc",8, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // typeNoBrc ::= CLASS_ID 
            {
              Object RESULT =null;
		int tokleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
		int tokright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
		Object tok = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
		
		RESULT = new Type(tokleft, tok.toString());
	
              CUP$LibParser$result = parser.getSymbolFactory().newSymbol("typeNoBrc",8, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
            }
          return CUP$LibParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number "+CUP$LibParser$act_num+"found in internal parse table");

        }
    } /* end of method */

  /** Method splitting the generated action code into several parts. */
  public final java_cup.runtime.Symbol CUP$LibParser$do_action(
    int                        CUP$LibParser$act_num,
    java_cup.runtime.lr_parser CUP$LibParser$parser,
    java.util.Stack            CUP$LibParser$stack,
    int                        CUP$LibParser$top)
    throws java.lang.Exception
    {
              return CUP$LibParser$do_action_part00000000(
                               CUP$LibParser$act_num,
                               CUP$LibParser$parser,
                               CUP$LibParser$stack,
                               CUP$LibParser$top);
    }
}

}
