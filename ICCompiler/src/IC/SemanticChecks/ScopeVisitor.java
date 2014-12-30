package IC.SemanticChecks;

import IC.SymbolTables.BlockScope;
import IC.SymbolTables.ClassScope;
import IC.SymbolTables.FieldScope;
import IC.SymbolTables.FormalScope;
import IC.SymbolTables.GlobalScope;
import IC.SymbolTables.LocalScope;
import IC.SymbolTables.MethodScope;

public interface ScopeVisitor
{

	public Object visit(ClassScope classScope);

	public Object visit(GlobalScope globalScope);

	public Object visit(FieldScope fieldScope);

	public Object visit(FormalScope formalScope);

	public Object visit(LocalScope localScope);

	public Object visit(BlockScope blockScope);

	public Object visit(MethodScope methodScope);

}
