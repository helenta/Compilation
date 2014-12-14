package IC.Semantic;

import IC.AST.Type;

public abstract class PrimitiveScope extends Scope {
	
	protected Type type;
	
	public PrimitiveScope(Scope parent, String name, Type type, int line) {
		super(parent, name, line);
		this.type = type;
	}
	
	public PrimitiveScope(String name, Type type, int line) {
		super(name, line);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

	public String getTypeName() {
		StringBuffer output = new StringBuffer();
		output.append(type.getName());
		for (int i=0;i<type.getDimension();i++){
			output.append("[]");
		}
		return output.toString();
	}
	
	public String toString(){
		return this.getDisplayName() + ": " + this.getName() + " : " + this.getTypeName();
	}
	
	public abstract String getDisplayName();
}
