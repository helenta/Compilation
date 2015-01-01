package IC.LIR;

import java.util.*;

import IC.AST.*;

public final class LIRProgram
{
	public HashMap<String, String> literals;
	public HashMap<String, Method> methods;
	public HashMap<ICClass, String> dispatchTable;
	
	public final int ReturnRegister = 0;
	public ICClass currentClass = null;
	public int currRegister = 1;
	public int expressionRegister = 2;
	
	private static void EmitNewObject(ICClass icClass)
	{
		// todo:
	}
}
