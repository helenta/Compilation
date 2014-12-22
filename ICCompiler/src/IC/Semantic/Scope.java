package IC.Semantic;

import java.util.HashMap;
import java.util.List;

public abstract class Scope {
	
	public HashMap<String, Scope> symbols;
	protected Scope parent;
	protected String name;
	protected int line;
	
	Scope(String name, int line){
		this.parent = null;
		this.name = name;
		this.symbols = new HashMap<String, Scope>();
		this.line = line;
	}
	
	Scope(Scope parent, String name, int line){
		this.parent = parent;
		this.name = name;
		this.symbols = new HashMap<String, Scope>();
		this.line = line;
	}
	
	public Scope getParent() {
		return parent;
	}
	
	public void setParent(Scope parent){
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLine(){
		return line;
	}
	public abstract Object accept(ScopeVisitor visitor);

	public boolean hasSymbol(String key){
		return symbols.containsKey(key);
	}
	
	public void addSymbol(Scope scope, List<Scope> collection)
	{
		if (symbols.containsKey(scope.getName()))
		{
			Scope contained = symbols.get(scope.getName());
			if (scope instanceof MethodScope && contained instanceof FieldScope)
			{
				throw new SemanticException(scope.getLine() + ": semantic error; Method " + scope.getName() + " is shadowing a field with the same name");
			}
			if (scope instanceof LocalScope && contained instanceof FormalScope)
			{
				throw new SemanticException(scope.getLine() + ": semantic error; Local variable " + scope.getName() + " is shadowing a parameter");
			}
			
			throw new SemanticException(scope.getLine() + ": semantic error; Id " + scope.getName() + " already defined in current scope");
		}
		symbols.put(scope.getName(), scope);
		if (collection != null){
			collection.add(scope);
		}
		scope.setParent(this);
	}
	
	public void addAnonymousScope(Scope scope, List<Scope> collection){
		if (collection != null){
			collection.add(scope);
		}
		scope.setParent(this);
	}
	
	public Scope getSymbol(String name){
		return symbols.get(name);
	}
}
