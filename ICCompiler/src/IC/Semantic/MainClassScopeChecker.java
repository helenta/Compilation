package IC.Semantic;

import IC.DataTypes;
import IC.AST.PrimitiveType;

public class MainClassScopeChecker implements IScopeCheck 
{
	int mainMethodTimes = 0;
	boolean errorMainMethod = false;

	public void Check(Scope scope) 
	{
		if (errorMainMethod || (mainMethodTimes > 1))
			return;
		
		if (!(scope instanceof MethodScope))
			return;
		
		MethodScope methodScope = (MethodScope)scope;
		if (methodScope.name.equals("main"))
		{
			mainMethodTimes++;
		
			if (!(methodScope.returnType instanceof PrimitiveType) ||
				(((PrimitiveType)methodScope.returnType).getType() != DataTypes.VOID))
			{
				errorMainMethod = true;
			}
			
			if (methodScope.virtual)
				errorMainMethod = true;
		}
	}

	public boolean isSuccess() 
	{
		return !errorMainMethod && (mainMethodTimes == 1);
	}

}
