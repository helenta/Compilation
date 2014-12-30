package IC.SymbolTables;

import java.util.ArrayList;
import java.util.List;

import IC.SemanticChecks.ScopeVisitor;

public class GlobalScope extends Scope
{

	public List<Scope>	classScopes;

	public GlobalScope()
	{
		super(null, "", 0);
		classScopes = new ArrayList<Scope>();
	}

	@Override
	public Object accept(ScopeVisitor visitor)
	{
		return visitor.visit(this);
	}
}
