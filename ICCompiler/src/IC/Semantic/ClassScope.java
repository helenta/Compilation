package IC.Semantic;

import java.util.ArrayList;
import java.util.List;

import IC.AST.*;

public class ClassScope extends Scope {
	public List<Scope> methods;
	public List<Scope> fields;
	public List<Scope> derived;
	
	public ICClass icClass;
	
	public ClassScope(Scope parent, String name, int line, ICClass icClass) {
		super(parent, name, line);
		methods = new ArrayList<Scope>();
		fields = new ArrayList<Scope>();
		derived = new ArrayList<Scope>();
		this.icClass = icClass;
	}
	
	public ClassScope(String name, int line, ICClass icClass){
		super(name, line);
		methods = new ArrayList<Scope>();
		fields = new ArrayList<Scope>();
		derived = new ArrayList<Scope>();
		this.icClass = icClass;
	}
	
	@Override
	public Object accept(ScopeVisitor visitor) {
		return visitor.visit(this);
	}
	
	public void addDerived(ClassScope subClass, int line){
		checkDerived(subClass, this);
		addSymbol(subClass, this.derived);
	}

	private void checkDerived(ClassScope subClass, ClassScope classScope) {
		for (Scope method : subClass.methods){
			if (classScope.hasSymbol(method.getName())){
				Scope sym = classScope.getSymbol(method.getName());
				if (sym instanceof FieldScope)
					throw new SemanticError("semantic error at line " + method.getLine() + ": " + 
				"method " + sym.getName() + " shadows a field with the same name");
				if (sym instanceof MethodScope && !((MethodScope) sym).signature(false).equals(((MethodScope) method).signature(false)))
					throw new SemanticError("semantic error at line " + method.getLine() + ": " + 
				"method " + sym.getName() + " overloads a method with the same name");
			}
		}
		for (Scope field : subClass.fields){
			if (classScope.hasSymbol(field.getName())){
				Scope sym = classScope.getSymbol(field.getName());
				if (sym instanceof FieldScope)
					throw new SemanticError("semantic error at line " + field.getLine() + ": " + 
				"field " + sym.getName() + " shadows a field with the same name");
			}
		}
	}
}
