package IC.Semantic;

public interface ScopeVisitor {

	public Object visit(ClassScope classScope);

	public Object visit(GlobalScope globalScope);

	public Object visit(FieldScope fieldScope);

	public Object visit(FormalScope formalScope);

	public Object visit(LocalScope localScope);

	public Object visit(BlockScope blockScope);

	public Object visit(MethodScope methodScope);

}

