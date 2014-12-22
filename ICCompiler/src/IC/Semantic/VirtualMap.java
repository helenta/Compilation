package IC.Semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VirtualMap {
	private String className;
	private Scope scope;
	private List<String> fields;
	private HashMap<String,String> methods;
	private HashMap<String,MethodScope> methodScopes;
	
	
	public VirtualMap(String clsName,Scope scope) {
		this.scope = scope;
		this.className = clsName;
		this.fields = new ArrayList<String>();
		this.methods = new HashMap<String,String>();
		this.methodScopes = new HashMap<String,MethodScope>();
	}
	
	public Scope getScope() {
		return scope;
	}

	public void setScope(ClassScope scope) {
		this.scope = scope;
	}
	
	public List<String> getFields() {
		return fields;
	}

	public HashMap<String, String> getMethods() {
		return methods;
	}

	public void addField(String name) {
		fields.add(name);	
	}
	
	public static VirtualMap createExtendsClass(String className, VirtualMap superClass,Scope scope){
		VirtualMap cls = new VirtualMap(className,scope);
		for(String method : superClass.getMethods().keySet()){
			cls.addMethod(method,superClass.getMethods().get(method),superClass.getScopeOfMethod(method));
		}
		for(String field : superClass.getFields()){
			cls.addField(field);
		}
		return cls;
	}

	public MethodScope getScopeOfMethod(String method) {
		return methodScopes.get(method);
	}

	public void addMethod(String method, String superClassName, MethodScope scope) {
		methods.put(method,superClassName);
		methodScopes.put(method,scope);	
	}

	public void addSuperMethodsAndFields(VirtualMap superClass) {
		HashMap<String, String> superMethods = superClass.getMethods();
		List<String> superFields = superClass.getFields();
		
		for (String f : superFields) {
			if(!this.fields.contains(f)){
				fields.add(f);
			}
		}
		for (String m : superMethods.keySet()) {
			if(!this.methods.containsKey(m)){
				methods.put(m, superClass.getMethods().get(m));
				methodScopes.put(m, superClass.getScopeOfMethod(m));
					
				}
			}		
	}
	
}
