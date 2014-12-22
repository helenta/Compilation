package IC.Semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VirtualMap {
	private String clsName;
	private Scope scope;
	private List<String> fields;
	private HashMap<String,String> methods;
	private HashMap<String,MethodScope> methodScops;
	
	
	public VirtualMap(String clsName,Scope scope2) {
		this.scope = scope2;
		this.clsName = clsName;
		this.fields = new ArrayList<String>();
		this.methods = new HashMap<String,String>();
		this.methodScops = new HashMap<String,MethodScope>();
	}
	
	public Scope getScope() {
		return scope;
	}

	public void setScope(ClassScope scope) {
		this.scope = scope;
	}

	/*
	 * this method calculates the total size of a class object
	 * by adding the total size of each field
	 */
	public int getClassAllocSize(){
		return fields.size() + 1;
		
	}

	
	public List<String> getFields() {
		return fields;
	}

	public HashMap<String, String> getMethods() {
		return methods;
	}

	public void addMethod(String name) {
		
		methods.put(name,clsName);
		
	}

	public void addField(String name) {
		fields.add(name);
		
	}
	public static VirtualMap createExtendsCls(String clsName, VirtualMap clsSuper,Scope scope2){
		VirtualMap cls =new VirtualMap(clsName,scope2);
		for(String method : clsSuper.getMethods().keySet()){
			cls.addMethod(method,clsSuper.getMethods().get(method),clsSuper.getScopeOfMethod(method));
		}
		for(String field : clsSuper.getFields()){
			cls.addField(field);
		}
		return cls;
	}

	public MethodScope getScopeOfMethod(String method) {
		return methodScops.get(method);
	}

	public void addMethod(String method, String clsNameSuper) {
		methods.put(method,clsNameSuper);
		
	}

	public void addMethod(String method, String clsNameSuper, MethodScope scope2) {
		methods.put(method,clsNameSuper);
		methodScops.put(method,scope2);
		
	}
	public void addMethodScope(String method,MethodScope scope) {
		methodScops.put(method,scope);
		
	}
	
	public String getClsName() {
		return clsName;
	}

	public int findMethodIndex(String method) {
		int index=0;
		for(String m : methods.keySet()){
			if (m.equals(method)){
				return index;
			}
			index++;
		}
		return -1;
	}

	public int getFildOffset(String field) {
		int index=1;
		for(String f : fields){
			if (f.equals(field)){
				return index;
			}
			index++;
		}
		return -1;
	}

	public void addSuperMethodsAndFields(VirtualMap superCls) {
		HashMap<String, String> superMethods = superCls.getMethods();
		List<String> superFields = superCls.getFields();
		
		for (String f : superFields) {
			if(!this.fields.contains(f)){
				fields.add(f);
			}
		}
		for (String m : superMethods.keySet()) {
			if(!this.methods.containsKey(m)){
				methods.put(m, superCls.getMethods().get(m));
				methodScops.put(m, superCls.getScopeOfMethod(m));
					
				}
			}
		
	}

	public HashMap<String, MethodScope> getMethodScops() {
		return methodScops;
	}

}
