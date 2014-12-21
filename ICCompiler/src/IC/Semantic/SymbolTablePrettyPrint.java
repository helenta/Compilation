package IC.Semantic;

public class SymbolTablePrettyPrint implements ScopeVisitor {

	public Object visit(ClassScope classScope) {
		StringBuffer output = new StringBuffer();
		output.append("Class Symbol Table: " + classScope.getName() + "\n");
		
		// Fields
		for (Scope field : classScope.fields) {
			output.append("    ");
			output.append(field.accept(this) + "\n");
		}
		// Method signatures
		for (Scope method : classScope.methods)
			if (!((MethodScope)method).isVirtual()) {
				output.append("    ");
				output.append("Static method: " + ((MethodScope) method).signature() + "\n");
			}
			else {
				output.append("    ");
				output.append("Virtual method: " + ((MethodScope) method).signature() + "\n");
			}
		output.append("Children tables: ");
		for (Scope method : classScope.methods) {
			if (method.parent.equals(classScope))
				output.append(method.getName() +", ");
		}
		output.deleteCharAt(output.length() - 2);
		output.append("\n\n");
		// Method displays
		for (Scope method: classScope.methods)
			output.append(method.accept(this));
		return output.toString();
	}

	public Object visit(GlobalScope globalScope) {
		StringBuffer output = new StringBuffer();
		output.append("\nGlobal Symbol Table\n");
		for (Scope clsScope : globalScope.classScopes) {
			output.append("    ");
			output.append("Class: " + clsScope.getName() + "\n");
		}
		output.append("Children tables: ");
		for (Scope clsScope : globalScope.classScopes) {
			if (clsScope.parent.equals(globalScope))
				output.append(clsScope.getName() +", ");
		}
		output.deleteCharAt(output.length() - 2);
		output.append("\n\n");
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
		output.append("Statement Block Symbol Table: ( located in " + blockScope.getParent().getName() + " )\n");
		// Locals
		for (Scope local : blockScope.locals) {
			output.append("    ");
			output.append(local.accept(this) + "\n");
		}
		if (!blockScope.blocks.isEmpty()) {
			output.append("Children tables: ");
			for (Scope block : blockScope.blocks) {
				if (block.parent.equals(blockScope))
					output.append(block.getName() +", ");
			}
			output.deleteCharAt(output.length() - 2);
			output.append("\n\n	");
		}
		for (Scope block : blockScope.blocks)
			output.append(block.accept(this));
		return output.toString();
	}

	public Object visit(MethodScope methodScope) {
		StringBuffer output = new StringBuffer();
		output.append("Method Symbol Table: " + methodScope.getName() + "\n");
		// Params
		for (Scope formal : methodScope.params) {
			output.append("    ");
			output.append(formal.accept(this) + "\n");
		}
		// Locals
		for (Scope local : methodScope.locals) {
			output.append("    ");
			output.append(local.accept(this) + "\n");
		}
		if (!methodScope.blocks.isEmpty()) {
			output.append("Children tables: ");
			for (Scope block : methodScope.blocks) {
				if (block.parent.equals(methodScope))
					output.append(block.getName() +", ");
			}
			output.deleteCharAt(output.length() - 2);
			output.append("\n");
		}
		output.append("\n");	
		// Blocks
		for (Scope block : methodScope.blocks)
			output.append(block.accept(this) + "\n");
		return output.toString();
	}

}
