package IC.Semantic;

import IC.AST.Type;

import java.util.ArrayList;
import java.util.List;

public class MethodScope extends Scope {
	
	public List<Scope> params;
	public List<Scope> locals;
	public List<Scope> blocks;
	protected Type retType;
	protected boolean virtual;
	
	MethodScope(Scope parent, String name, Type retType, int line, boolean virtual) {
		super(parent, name, line);
		this.retType = retType;
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
		params = new ArrayList<Scope>();
		this.virtual = virtual;
	}
	
	public MethodScope(String name, Type retType, int line, boolean virtual) {
		super(name, line);
		this.retType = retType;
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
		params = new ArrayList<Scope>();
		this.virtual = virtual;
	}

	public Type getRetType() {
		return retType;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	@Override
	public Object accept(ScopeVisitor visitor) {
		return visitor.visit(this);
	}

	public String signature(){
		StringBuffer output = new StringBuffer();
		output.append(this.name + " {");
		if (this.params.size() == 0){
			//output.append("void");
		}
		else{
			for (Scope param : this.params)
				output.append(((PrimitiveScope) param).getTypeName() + ", ");
			output.delete(output.length()-2, output.length());
		}
		output.append(" -> " + retType.getName());
		for (int i=0;i<retType.getDimension();i++){
			output.append("[]");
		}
		output.append("}");
		return output.toString();
	}
}
