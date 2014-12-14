package IC.Semantic;

public class SymbolTablePrettyPrint implements ScopeVisitor {

	public Object visit(ClassScope classScope) {
		StringBuffer output = new StringBuffer();
		if (classScope.parent instanceof GlobalScope){
			output.append("Class Symbol Table: " + classScope.getName() + "\n");
		}
		else{
			output.append("Class Symbol Table: " + classScope.getName() + " (parent = " + classScope.getParent().getName() + ")\n");
		}
		// Fields
		for (Scope field : classScope.fields)
			output.append("\t" + field.accept(this) + "\n");
		// Method signatures
		for (Scope method : classScope.methods)
			if (!((MethodScope)method).isVirtual()) 
				output.append("\tStatic method: " + ((MethodScope) method).signature() + "\n");
			else
				output.append("\tVirtual method: " + ((MethodScope) method).signature() + "\n");
		output.append("\n");
		// Method displays
		for (Scope method: classScope.methods)
			output.append(method.accept(this));
		output.append("\n");
		return output.toString();
	}

	public Object visit(GlobalScope globalScope) {
		StringBuffer output = new StringBuffer();
		output.append("\nGlobal Symbol Table\n");
		for (Scope clsScope : globalScope.classScopes)
			output.append("\tClass: " + clsScope.getName() + "\n");
		output.append("\n");
		for (Scope clsScope : globalScope.classScopes)
			output.append(clsScope.accept(this));
		return output.toString();
	}

	public Object visit(FieldScope fieldScope) {
		return fieldScope.toString();
	}

	public Object visit(FormalScope formalScope) {
		return formalScope.toString();
	}

	public Object visit(LocalScope localScope) {
		return localScope.toString();
	}

	public Object visit(BlockScope blockScope) {
		StringBuffer output = new StringBuffer();
		output.append("Statement Block Symbol Table: " + blockScope.getName() + " (parent = " + blockScope.getParent().getName() + ")\n");
		// Locals
		for (Scope local : blockScope.locals)
			output.append("\t" + local.accept(this) + "\n");
		output.append("\n");
		for (Scope block : blockScope.blocks)
			output.append(block.accept(this));
		return output.toString();
	}

	public Object visit(MethodScope methodScope) {
		StringBuffer output = new StringBuffer();
		output.append("Method Symbol Table: " + methodScope.getName() + " (parent = " + methodScope.getParent().getName() + ")\n");
		// Params
		for (Scope formal : methodScope.params)
			output.append("\t" + formal.accept(this) + "\n");
		// Locals
		for (Scope local : methodScope.locals)
			output.append("\t" + local.accept(this) + "\n");
		output.append("\n");	
		// Blocks
		for (Scope block : methodScope.blocks)
			output.append(block.accept(this) + "\n");
		return output.toString();
	}

}
