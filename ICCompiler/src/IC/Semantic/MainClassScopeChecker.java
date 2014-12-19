package IC.Semantic;

import IC.DataTypes;
import IC.AST.PrimitiveType;

public class MainClassScopeChecker implements IScopeChecker 
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
		
			if (!(methodScope.retType instanceof PrimitiveType) ||
				(((PrimitiveType)methodScope.retType).getType() != DataTypes.VOID))
			{
				errorMainMethod = true;
			}
			
			if (methodScope.virtual)
				errorMainMethod = true;
		}
	}

	public boolean IsSucess() 
	{
		return !errorMainMethod && (mainMethodTimes == 1);
	}

}
