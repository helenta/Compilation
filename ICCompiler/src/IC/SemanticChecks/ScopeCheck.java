package IC.SemanticChecks;

import IC.SymbolTables.BlockScope;
import IC.SymbolTables.ClassScope;
import IC.SymbolTables.FieldScope;
import IC.SymbolTables.FormalScope;
import IC.SymbolTables.GlobalScope;
import IC.SymbolTables.LocalScope;
import IC.SymbolTables.MethodScope;
import IC.SymbolTables.Scope;

public final class ScopeCheck implements ScopeVisitor
{
	private IScopeCheck	scopeChecker;

	public ScopeCheck(IScopeCheck checker)
	{
		this.scopeChecker = checker;
	}

	public Object visit(ClassScope classScope)
	{
		this.scopeChecker.Check(classScope);

		for (Scope methodScope : classScope.methods)
		{
			visit((MethodScope) methodScope);
		}

		for (Scope fieldScope : classScope.fields)
		{
			visit((FieldScope) fieldScope);
		}

		return null;
	}

	public Object visit(GlobalScope globalScope)
	{
		this.scopeChecker.Check(globalScope);

		for (Scope classScopes : globalScope.classScopes)
		{
			visit((ClassScope) classScopes);
		}

		return this.scopeChecker.isSuccess();
	}

	public Object visit(FieldScope fieldScope)
	{
		this.scopeChecker.Check(fieldScope);

		return null;
	}

	public Object visit(FormalScope formalScope)
	{
		this.scopeChecker.Check(formalScope);

		return null;
	}

	public Object visit(LocalScope localScope)
	{
		this.scopeChecker.Check(localScope);

		return null;
	}

	public Object visit(BlockScope blockScope)
	{
		this.scopeChecker.Check(blockScope);

		for (Scope childeBlockScope : blockScope.blocks)
		{
			visit((BlockScope) childeBlockScope);
		}

		for (Scope localScope : blockScope.locals)
		{
			visit((LocalScope) localScope);
		}

		return null;
	}

	public Object visit(MethodScope methodScope)
	{
		this.scopeChecker.Check(methodScope);

		return null;
	}

}
