package IC.Semantic;

public class SymbolTable {
	
	public GlobalScope root;
	
	public SymbolTable() {
		this.root = new GlobalScope();
	}
	
	public String toString(){
		SymbolTablePrettyPrint pp = new SymbolTablePrettyPrint();
		return (String)pp.visit(root);
		
	}
	
	public boolean isDerived(String derName, String supName){
		// todo: remove
		if (this.root == null)
		{
			int t = 0;
			t = 1;
		}
		Scope derScope = this.root.getSymbol(derName);
		// todo: remove
		if (derScope == null)
		{
			int t = 0;
			t = 1;
			derScope = this.root.getSymbol(derName);
		}
		Scope parentScope = derScope.getParent();
		if (parentScope instanceof GlobalScope){
			return false;
		}
		
		if (derScope.getParent().getName().equals(supName)){
			return true;
		}
		return isDerived(parentScope.getName(), supName);
	}
	
	public boolean isEnclosedInVirtualMethod(Scope scope){
		MethodScope method = getEnclosingMethod(scope);
		if (method == null){
			return false;
		}
		return method.isVirtual();
	}
	
	public MethodScope getEnclosingMethod(Scope scope){
		if (scope instanceof GlobalScope){
			return null;
		}
		if (scope instanceof MethodScope){
			return (MethodScope) scope;
		}
		return getEnclosingMethod(scope.getParent());
	}
	
	public Scope getSymbol(Scope scope, String name){
		if (scope.hasSymbol(name)){
			return scope.getSymbol(name);
		}
		if (scope.getParent() == null){
			return null;
		}
		return getSymbol(scope.getParent(), name);
	}
	
	public ClassScope getEnclosingClass(Scope scope){
		if (scope instanceof GlobalScope){
			return null;
		}
		if (scope instanceof ClassScope){
			return (ClassScope) scope;
		}
		return getEnclosingClass(scope.getParent());
	}
	
	public void addLibTable(SymbolTable libTable){
		Scope libScope = libTable.root.classScopes.get(0);
		this.root.classScopes.add(0, libScope);
		this.root.symbols.put(libScope.getName(), libScope);
		libScope.setParent(this.root);
	}
}
