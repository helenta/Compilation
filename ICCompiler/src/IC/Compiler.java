package IC;

import java.io.*;
import java.util.List;

import java_cup.runtime.Symbol;
import IC.AST.*;
import IC.Parser.*;

public class Compiler{
	
    public static void main(String[] args) throws Exception{
    	
    	if (args.length < 1 || args.length > 2){
    		System.out.println("Error arguments number");
    		return;
    	}
    	
    	FileReader txtFile;
    	Scanner lex;
    	Symbol symbol;
    	String libFileName = null, icFileName = args[0];
    	Program program = null, programLib = null;
    	PrettyPrinter printer, printerLib;
    	Object output, outputLib;
    	
    	if (args.length == 2 && args[1].startsWith("-L")) 
    	{
    		libFileName = args[1].substring(2);
	    	txtFile = new FileReader(libFileName);
			lex = new Scanner(txtFile);
			LibParser libParser = new LibParser(lex);
			symbol = libParser.parse();
			programLib = new Program((List<ICClass>)symbol.value);
			txtFile.close();
    	}
    	
    	txtFile = new FileReader(icFileName);
		lex = new Scanner(txtFile);
		Parser parser = new Parser(lex);
		symbol = parser.parse();
		program = (Program)symbol.value;
		txtFile.close();
		
		if (libFileName != null) 
		{
			printerLib = new PrettyPrinter(libFileName);
			outputLib = printerLib.visit(programLib);
			
			System.out.println(outputLib);
		}
		
		printer = new PrettyPrinter(icFileName);
		output = printer.visit(program);
		
	    System.out.println(output);	    
	}

}