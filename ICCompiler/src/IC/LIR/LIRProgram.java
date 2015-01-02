package IC.LIR;

import java.util.*;

import IC.AST.*;

public final class LIRProgram
{
	private HashMap<String, String> literals = new HashMap<String, String>();
	private int literalCount;
	
	private HashMap<String, Method> methods;
	private HashMap<ICClass, String> dispatchTable;
	
	private int lockedRegister = 2;
	
	public final int ReturnRegister = 9999;
	public ICClass currentClass = null;
	public int expressionRegister = 2;
	public String breakLabel = null;
	public String continueLabel = null;
	
	public String AddLiteral(String literal)
	{
		String literalName = null;
		if (literals.containsKey(literal))
		{
			literalName = literals.get(literal);
		}
		else
		{
			literalName = "str" + literalCount;
			literalCount++;
			literals.put(literal, literalName);
		}
		
		return literalName;
	}
	
	public int GetNextRegister()
	{
		lockedRegister++;
		return lockedRegister;
	}
	
	public void UnLockRegister(int count)
	{
		lockedRegister -=count;
	}
	
	public int[] GetArgumentsRegisters(int count)
	{
		int[] argsReg = new int[count];
		for(int i = 0; i < count; i++)
		{
			argsReg[i] = 4000 + i;
		}
		
		return argsReg;
	}
	
	public String GetLabelName(ASTNode statement, String statementName)
	{
		return "_Label_" + statementName + "_" + statement.getLine();
	}
	
}
