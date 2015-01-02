package IC.LIR;

import java.util.*;

import IC.AST.*;
import IC.SymbolTables.MethodScope;
import IC.SymbolTables.Scope;
import IC.SymbolTables.SymbolTable;

public final class LIRProgram
{
	public HashMap<String, String> literals = new HashMap<String, String>();
	private int literalCount;
	
	private HashMap<Method, String> methods = new HashMap<Method, String>();
	private HashMap<ICClass, String> dispatchTable;
	
	private SymbolTable programTable;
	
	public ICClass currentClass = null;
	
	private int lockedRegister = 2;
	
	public final int ReturnRegister = 9999;
	public int expressionRegister = 2;
	
	public String breakLabel = null;
	public String continueLabel = null;
	
	public LIRProgram(SymbolTable symbolTable)
	{
		programTable = symbolTable;
	}
	
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
		lockedRegister -= count;
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
	
	public String GetMethodLabel(Method method)
	{
		String label = null;
		if (methods.containsKey(method))
		{
			label = methods.get(method);
		}
		else
		{
			label = "_ic_main";
			if (!method.IsMain())
			{
				label = "_" + currentClass.getName() + "_" + method.getName();
			}
			methods.put(method, label);
		}
		
		return label;
	}
	
	public Method GetMethodFromCall(StaticCall staticCall)
	{
		String methodName = staticCall.getName();
		String className = staticCall.getClassName();
		Scope classScope = programTable.getSymbol(staticCall.scope, className);
		MethodScope methodScope = (MethodScope) programTable.getSymbol(classScope, methodName);
		
		return methodScope.method;
	}
	
	public Method GetMethodFromCall(VirtualCall virtualCall)
	{
		String methodName = virtualCall.getName();
		
		String className = null;
		if (virtualCall.getLocation() != null)
			className = virtualCall.getLocation().semType.getName();
		else
			className = virtualCall.scope.GetParentClassScope().icClass.getName();
		
		Scope classScope = programTable.getSymbol(virtualCall.scope, className);
		MethodScope methodScope = (MethodScope) programTable.getSymbol(classScope, methodName);
		
		return methodScope.method;
	}
}
