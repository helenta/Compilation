package IC.SemanticChecks;

import IC.AST.*;
import IC.AST.Method;
import IC.AST.PrimitiveType;
import IC.SymbolTables.MethodScope;
import IC.SymbolTables.Scope;

public class MainClassScopeChecker implements IScopeCheck
{
	int	    mainMethodTimes	= 0;
	boolean	errorMainMethod	= false;
	Method  mainMethod      = null;

	public void Check(Scope scope)
	{
		if (errorMainMethod || (mainMethodTimes > 1))
			return;

		if (!(scope instanceof MethodScope))
			return;

		MethodScope methodScope = (MethodScope) scope;
		if (methodScope.getName().equals("main"))
		{
			mainMethodTimes++;
			mainMethod = methodScope.method;

			if (!(methodScope.getReturnType() instanceof PrimitiveType)
			    || (((PrimitiveType) methodScope.getReturnType()).getType() != DataTypes.VOID))
			{
				errorMainMethod = true;
			}

			if (methodScope.isVirtual())
				errorMainMethod = true;
		}
	}

	public boolean isSuccess()
	{
		return !errorMainMethod && (mainMethodTimes == 1);
	}

	public Method GetMainMethod()
	{
		return mainMethod;
	}
}
