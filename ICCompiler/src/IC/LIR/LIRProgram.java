package IC.LIR;

import java.util.*;

import IC.AST.*;

public final class LIRProgram
{
	private HashMap<String, String> literals;
	private HashMap<String, Method> methods;
	private HashMap<ICClass, String> dispatchTable;
	
	public final int ReturnRegister = 0;
	public ICClass currentClass = null;
	public int currRegister = 1;
	public int expressionRegister = 2;
	public int expressionRegister1 = 3;
	public int expressionRegister2 = 4;
	
	public int[] GetArgumentsRegisters(int count)
	{
		int[] argsReg = new int[count];
		for(int i = 0; i < count; i++)
		{
			argsReg[i] = expressionRegister2 + 1 + i;
		}
		
		return argsReg;
	}
	
	public String GetLabelName(ASTNode statement, String statementName)
	{
		return "_Label_" + statementName + "_" + statement.getLine();
	}
	
}
