package IC.SymbolTables;

import IC.AST.Type;
import IC.SemanticChecks.ScopeVisitor;

public class FieldScope extends PrimitiveScope
{
	public FieldScope(Scope parent, String name, Type type, int line)
	{
		super(parent, name, type, line);
	}

	public Object accept(ScopeVisitor visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public String getDisplayName()
	{
		return "Field";
	}

}
