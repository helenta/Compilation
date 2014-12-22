package IC.Semantic;

import IC.AST.*;

import java.util.ArrayList;
import java.util.List;

public class MethodScope extends Scope {
	
	public List<Scope> params;
	public List<Scope> locals;
	public List<Scope> blocks;
	protected Type returnType;
	protected boolean virtual;
	public Method method;
	
	public MethodScope(String name, Type retType, int line, boolean virtual, Method method) {
		super(name, line);
		this.returnType = retType;
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
		params = new ArrayList<Scope>();
		this.virtual = virtual;
		this.method = method;
	}

	public Type getReturnType() {
		return returnType;
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

	public String signature(boolean onlyFormals){
		StringBuffer output = new StringBuffer();
		if (!onlyFormals) {
			output.append(this.name + " ");
		}
		output.append("{");
		if (this.params.size() != 0){
			for (Scope param : this.params)
				output.append(((PrimitiveScope) param).getTypeName() + ", ");
			output.delete(output.length()-2, output.length());
		}
		output.append(" -> " + returnType.getName());
		for (int i=0;i<returnType.getDimension();i++){
			output.append("[]");
		}
		output.append("}");
		return output.toString();
	}
}
