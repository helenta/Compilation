package IC.SymbolTables;

public class SymbolTable
{

	public GlobalScope	root;

	public SymbolTable()
	{
		this.root = new GlobalScope();
	}

	public String toString()
	{
		SymbolTablePrettyPrint prettyPrint = new SymbolTablePrettyPrint();
		return (String) prettyPrint.visit(root);

	}

	public MethodScope getEnclosingMethod(Scope scope)
	{
		if (scope instanceof GlobalScope)
			return null;
		if (scope instanceof MethodScope)
			return (MethodScope) scope;
		return getEnclosingMethod(scope.getParent());
	}

	public Scope getSymbol(Scope scope, String name)
	{
		if (scope.hasSymbol(name))
			return scope.getSymbol(name);
		if (scope.getParent() == null)
			return null;
		return getSymbol(scope.getParent(), name);
	}

	public ClassScope getEnclosingClass(Scope scope)
	{
		if (scope instanceof GlobalScope)
			return null;
		if (scope instanceof ClassScope)
			return (ClassScope) scope;
		return getEnclosingClass(scope.getParent());
	}

	public boolean isDerivedName(String derivedName, String superName)
	{
		Scope derScope = this.root.getSymbol(derivedName);
		if (derScope == null)
		{
			return false;
		}
		Scope parentScope = derScope.getParent();
		if (parentScope instanceof GlobalScope)
			return false;
		if (derScope.getParent().getName().equals(superName))
			return true;
		return isDerivedName(parentScope.getName(), superName);
	}

	public void addLibraryTable(SymbolTable libraryTable)
	{
		Scope libraryScope = libraryTable.root.classScopes.get(0);
		this.root.classScopes.add(0, libraryScope);
		this.root.symbols.put(libraryScope.getName(), libraryScope);
		libraryScope.setParent(this.root);
	}
}
