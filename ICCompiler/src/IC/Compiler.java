package IC;

import java.io.*;
import java.util.List;

import java_cup.runtime.Symbol;
import IC.AST.*;
import IC.Parser.*;
import IC.Semantic.*;

public class Compiler{
	
    public static void main(String[] args) throws Exception{
    	
    	if (args.length < 1 || args.length > 2){
    		System.out.println("Error arguments number");
    		return;
    	}
    	
    	FileReader txtFile;
    	Scanner lex;
    	Symbol symbol;
    	String icFileName = args[0];
    	Program program = null;
    	PrettyPrinter printer;
    	Object output;
    	
    	if (args.length == 2) 
    	{
    		icFileName = args[0];
    		txtFile = new FileReader(icFileName);
			lex = new Scanner(txtFile);
			Parser parser = new Parser(lex);
			symbol = parser.parse();
			program = (Program)symbol.value;
			txtFile.close();
    	}
    	
    	if (args[1].equals("-print-ast"))
    	{
    		printer = new PrettyPrinter(icFileName);
    		output = printer.visit(program);
    	    System.out.println(output);	    
    	}

    	if (args[1].equals("-dump-symtab")) 
    	{
			SymbolTable table = (SymbolTable) new SymbolTableBuilder().visit(program);
			System.out.println(table);
			TypeChecker checker = new TypeChecker(table);
			//checker.visit(program); 
    	}
	}
    
}