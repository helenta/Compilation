package IC;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java_cup.runtime.Symbol;
import IC.AST.*;
import IC.LIR.EmitVisitor;
import IC.LIR.LIRProgram;
import IC.Parser.*;
import IC.SemanticChecks.*;
import IC.SymbolTables.SymbolTable;
import IC.SymbolTables.SymbolTablePrettyPrint;
import IC.SymbolTables.SymbolTableVisitor;

public class Compiler
{

	public static void main(String[] args) throws Exception
	{

		if (args.length < 1)
		{
			System.out.println("Error arguments number");
			return;
		}

		String icFileName = args[0];

		icFileName = args[0];
		Program program = (Program) CreateICTree(icFileName, false);
		System.out.println("Parsed " + icFileName + " successfully!");

		if (program == null)
			return;

		if (args.length == 1)
			return;

		String libaryFileName = null;
		String[] printOptions = null;

		if (args[1].startsWith("-L"))
		{
			libaryFileName = args[1].substring(2, args[1].length());
			if (args.length > 2)
				printOptions = Arrays.copyOfRange(args, 2, args.length);

		}
		else
		{
			printOptions = Arrays.copyOfRange(args, 1, args.length);;

		}

		List<ICClass> libaryClass = null;
		if (libaryFileName != null){
			libaryClass = (List<ICClass>) CreateICTree(libaryFileName, true);
			System.out.println("Parsed " + libaryFileName + " successfully!");
		}
		
		for (String option: printOptions){
			ProcessArgument(option, icFileName, program, libaryClass);
		}
	

	}

	private static void ProcessArgument(String arg, String icFileName,
	    																Program program, List<ICClass> libaryClass)
	{
		try
		{
			SymbolTable table = (SymbolTable) new SymbolTableVisitor().visit(program);

			if (libaryClass != null && libaryClass.size() > 0)
			{
				Program libaryProgram = new Program(libaryClass);
				SymbolTable libTable = (SymbolTable) new SymbolTableVisitor()
				    .visit(libaryProgram);
				table.addLibraryTable(libTable);
			}

			TypeCheck checker = new TypeCheck(table);
			checker.visit(program);

			MainClassScopeChecker mainClassScopeChecker = new MainClassScopeChecker();
			ScopeCheck mainChecker = new ScopeCheck(mainClassScopeChecker);
			boolean success = (boolean) mainChecker.visit(table.root);
			if (!success)
			{
				throw new SemanticError(
				    "semantic error at some line: wrong main method");
			}
			
			//Method mainMethod = mainClassScopeChecker.GetMainMethod();

			if (arg.equals("-print-ast"))
			{
				PrettyPrinter printer = new PrettyPrinter(icFileName, table);
				Object output = printer.visit(program);
				System.out.println(output);
			}

			if (arg.equals("-dump-symtab"))
			{
				System.out.println(table);

				System.out.println("Type Table");
				Map<String, Integer> map = SymbolTablePrettyPrint.typeTable;
				Map<Integer, String> typeTable = new TreeMap<Integer, String>();
				int i = 0;

				for (String name : map.keySet())
					typeTable.put(map.get(name), name);

				for (Integer id : typeTable.keySet())
				{
					String value = typeTable.get(id).toString();
					System.out.println("    " + (++i) + ": " + value);
				}
			}
			
			// Emit to LIR file
			if (arg.equals("-print-lir")){
				
				Path p = Paths.get(icFileName);
				String newFileName = p.getFileName().toString();
				newFileName = newFileName.substring(0, newFileName.length() - 3) + ".lir";
				
				File file = new File(newFileName);
				
				LIRProgram lirProgram = new LIRProgram(table);
				
				EmitVisitor emitVisitor = new EmitVisitor(file, lirProgram);
				
				emitVisitor.visit(program);
				
				System.out.println(newFileName +" created successfully!");

				
			}

		}
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

	private static Object CreateICTree(String fileName, boolean isLibary)
	    throws IOException
	{
		FileReader txtFile = null;
		try
		{
			txtFile = new FileReader(fileName);
			Scanner lex = new Scanner(txtFile);

			if (isLibary)
			{
				LibParser parser = new LibParser(lex);
				parser.printTokens = false;
				Symbol symbol = parser.parse();
				return symbol.value;
			}
			else
			{
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
