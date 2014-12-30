package IC.SymbolTables;

import IC.AST.Type;
import IC.SemanticChecks.ScopeVisitor;

public class LocalScope extends PrimitiveScope
{

	public LocalScope(Scope parent, String name, Type type, int line)
	{
		super(parent, name, type, line);
	}

	public LocalScope(String name, Type type, int line)
	{
		super(name, type, line);
	}

	public LocalScope(String name, int line)
	{
		super(name, null, line);
	}

	@Override
	public Object accept(ScopeVisitor visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public String getDisplayName()
	{
		return "Local variable";
	}

}
