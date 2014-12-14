package IC.Semantic;

import IC.AST.Type;

public class FormalScope extends PrimitiveScope {
	
	public FormalScope(Scope parent, String name, Type type, int line) {
		super(parent, name, type, line);
	}
	
	public FormalScope(String name, Type type, int line) {
		super(name, type, line);
	}

	@Override
	public Object accept(ScopeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public String getDisplayName() {
		return "Parameter";
	}
}
