package IC;

import java.io.*;
import java.util.List;

import java_cup.runtime.Symbol;
import IC.AST.*;
import IC.Parser.*;
import IC.Semantic.*;

public class Compiler{
	
    public static void main(String[] args) throws Exception{
    	
    	if (args.length < 1 || args.length > 3){
    		System.out.println("Error arguments number");
    		return;
    	}
    	
    	String icFileName = args[0];
    	
    	icFileName = args[0];
    	Program program = (Program)CreateICTree(icFileName, false);
    	if (program == null)
    		return;
		
		// find libary arg
		if (args.length == 1)
			return;
		
		String libaryFileName = null, printOption = null;
		if (args[1].startsWith("-L") && args.length > 2)
		{
			libaryFileName = args[1].substring(2, args[1].length());
			printOption = args[2];
		}
		else
		{
			printOption = args[1];
			if (args.length > 2 && args[2].startsWith("-L"))
				libaryFileName = args[2].substring(2, args[2].length());
		}
			
		List<ICClass> libaryClass = null;
		if (libaryFileName != null)
			libaryClass = (List<ICClass>)CreateICTree(libaryFileName, true);
    	
		if (printOption != null)
			ProcessArgument(printOption, icFileName, program, libaryClass);
	}
    
    private static void ProcessArgument(String arg, String icFileName, Program program, List<ICClass> libaryClass)
    {
    	try
		{
			SymbolTable table = (SymbolTable) new SymbolTableBuilder().visit(program);
			
			if (libaryClass != null && libaryClass.size() > 0)
			{
				Program libaryProgram = new Program(libaryClass);
				SymbolTable libTable = (SymbolTable)new SymbolTableBuilder().visit(libaryProgram);
				table.addLibTable(libTable);
			}
			
	    	if (arg.equals("-print-ast"))
	    	{
	    		PrettyPrinter printer = new PrettyPrinter(icFileName,table);
	    		Object output = printer.visit(program);
	    	    System.out.println(output);	    
	    	}
	
	    	if (arg.equals("-dump-symtab")) 
	    	{
				System.out.println(table);

				TypeChecker checker = new TypeChecker(table);
				checker.visit(program); 
	    	}
		}
		// todo: remove
		catch (NullPointerException ex)
		{
			for (StackTraceElement elem : ex.getStackTrace())
				System.err.println(elem.toString());
		}
		catch (Throwable ex)
		{
			System.out.println(ex.getMessage());
		}
    }

    private static Object CreateICTree(String fileName, boolean isLibary) throws IOException
    {
    	FileReader txtFile = null;
    	try
		{
			txtFile = new FileReader(fileName);
			Scanner lex = new Scanner(txtFile);
			
			if (isLibary){
				LibParser parser = new LibParser(lex);
				parser.printTokens = false;
				Symbol symbol = parser.parse();
				return symbol.value;
			}
			else{
				Parser parser = new Parser(lex);
				Symbol symbol = parser.parse();
				return symbol.value;
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			return null;
		}
		finally
		{
			if (txtFile != null)
				txtFile.close();
		}    
    }
}