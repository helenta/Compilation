package IC.Parser;

import java_cup.runtime.Symbol;

public class Token extends Symbol {
	int id;
	String text;
	String tag;
	int row;
	int column;
	
    public Token(int id, String tag, int col, int line, String t) {
    	super(id, line+1, col, t);
    	this.text = t;
        this.column = col + 1;
        this.row = line + 1;
        this.tag = tag;
    }
    
    public String getLine(){
    	return text + "\t" + tag + "\t" + row + ":" + column;
    }
    
    public int getRow()
    {
    	return this.row;
    }
    
    public int getColumn()
    {
    	return this.column;
    }
    
    public String getText(){
    	return this.text;
    }
    
    public String getTag(){
    	return this.tag;
    }
    
	//public static void PrintToken(String token, String tag , int line , int column) {
		//System.out.println (token + "\t" + tag + "\t" + line + ":" + column);
		//}
}

