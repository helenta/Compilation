/**
 * 
 */
package IC.Semantic;

public final class ScopeChecker implements ScopeVisitor 
{
	private IScopeChecker scope_checker;
	
	public ScopeChecker(IScopeChecker checker)
	{
		this.scope_checker = checker;
	}

	public Object visit(ClassScope classScope) 
	{
		this.scope_checker.Check(classScope);
		
		for (Scope methodScope : classScope.methods)
		{
			visit((MethodScope)methodScope);
		}
		
		for (Scope fieldScope : classScope.fields)
		{
			visit((FieldScope)fieldScope);
		}
		
		return null;
	}


	public Object visit(GlobalScope globalScope) 
	{
		this.scope_checker.Check(globalScope);
		
		for (Scope classScopes : globalScope.classScopes)
		{
			visit((ClassScope)classScopes);
		}
		
		return this.scope_checker.IsSucess();
	}

	public Object visit(FieldScope fieldScope) 
	{
		this.scope_checker.Check(fieldScope);
		
		return null;
	}

	public Object visit(FormalScope formalScope) 
	{
		this.scope_checker.Check(formalScope);
	
		return null;
	}

	public Object visit(LocalScope localScope) 
	{
		this.scope_checker.Check(localScope);
		
		return null;
	}

	public Object visit(BlockScope blockScope) 
	{
		this.scope_checker.Check(blockScope);
		
		for (Scope childeBlockScope : blockScope.blocks)
		{
			visit((BlockScope)childeBlockScope);
		}
		
		for (Scope localScope : blockScope.locals)
		{
			visit((LocalScope)localScope);
		}
		
		return null;
	}
	
	public Object visit(MethodScope methodScope) 
	{
		this.scope_checker.Check(methodScope);
		
		return null;
	}

}
