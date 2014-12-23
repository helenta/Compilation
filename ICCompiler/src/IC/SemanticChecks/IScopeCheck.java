package IC.SemanticChecks;

import IC.SymbolTables.Scope;

public interface IScopeCheck 
{	
	void Check(Scope scope);
	
	boolean isSuccess();
}
