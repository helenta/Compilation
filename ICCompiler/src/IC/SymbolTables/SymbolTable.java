package IC.SymbolTables;

import java.util.HashMap;

import IC.AST.*;

public class SymbolTable
{
	public GlobalScope	root;
	
	private HashMap<String, ICClass> typeToClass;

	public SymbolTable()
	{
		this.root = new GlobalScope();
		this.typeToClass = new HashMap<String, ICClass>();
	}
	
	public void AddType(String name, ICClass icClass)
	{
		typeToClass.put(name, icClass);
	}
	
	public ICClass GetClassByName(String name)
	{
		ICClass icClass = null;
		if (typeToClass.containsKey(name))
		{
			icClass = typeToClass.get(name);
		}
		
		return icClass;
	}
	
	public ClassScope GetClassScopeByName(String name)
	{
		ClassScope classScope = null;
		for (Scope scope : this.root.classScopes)
		{
			if (((ClassScope)scope).icClass.getName().equals(name))
			{
				classScope = (ClassScope)scope;
				break;
			}
		}
		
		return classScope;
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
