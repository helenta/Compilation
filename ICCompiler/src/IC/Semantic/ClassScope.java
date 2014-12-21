package IC.Semantic;

import java.util.ArrayList;
import java.util.List;

public class ClassScope extends Scope {
	public List<Scope> methods;
	public List<Scope> fields;
	public List<Scope> derived;
	
	public ClassScope(Scope parent, String name, int line) {
		super(parent, name, line);
		methods = new ArrayList<Scope>();
		fields = new ArrayList<Scope>();
		derived = new ArrayList<Scope>();
	}
	
	public ClassScope(String name, int line){
		super(name, line);
		methods = new ArrayList<Scope>();
		fields = new ArrayList<Scope>();
		derived = new ArrayList<Scope>();
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
				if (sym instanceof FieldScope){
					throw new SemanticException(method.getLine() + ": semantic error; Method " + sym.getName() + " is shadowing a field with the same name");
				}
				if (sym instanceof MethodScope && !((MethodScope) sym).signature(false).equals(((MethodScope) method).signature(false))){
					throw new SemanticException(method.getLine() + ": semantic error; method '" + sym.getName() + "' overloads a different method with the same name");
				}
			}
		}
		for (Scope field : subClass.fields){
			if (classScope.hasSymbol(field.getName())){
				Scope sym = classScope.getSymbol(field.getName());
				if (sym instanceof FieldScope){
					throw new SemanticException(field.getLine() + ": semantic error; Field " + sym.getName() + " is shadowing a field with the same name");
				}
			}
		}
	
	}
}
