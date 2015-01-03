package IC.LIR;

import java.util.*;

import IC.AST.*;
import IC.SymbolTables.ClassScope;
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
	public final int thisRegister = 9998;
	
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
	
	public ICClass GetClassByName(Scope scope, String className)
	{
		ClassScope classScope = (ClassScope)programTable.getSymbol(scope, className);
		return classScope != null ? classScope.icClass : null;
	}
	
	public ICClass GetClassByName(String className)
	{
		return programTable.GetClassByName(className);
	}
	
	public boolean IsLocalVarible(VariableLocation location)
	{
		ICClass icClass = GetVaribleLocationClass(location);
		if (icClass != null)
			return !ContainsField(icClass, location.getName());

		return true;
	}
	
	public ICClass GetVaribleLocationClass(VariableLocation location)
	{
		Scope scope = programTable.getSymbol(location.scope, location.getName());
		if (scope != null)
		{
			ClassScope classScope = programTable.getEnclosingClass(scope);
			if (classScope != null)
				return classScope.icClass;
		}
		
		return null;
	}
	
	public boolean ContainsField(ICClass icClass, String name)
	{
		for (Field field : icClass.getFields())
		{
			if (field.getName().equals(name))
			{
				return true;
			}
		}
		
		return false;
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
