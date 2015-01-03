package IC.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class declaration AST node.
 * 
 * @author Tovi Almozlino
 */
public class ICClass extends ASTNode
{
	private String	     name;

	private String	     superClassName	= null;

	private List<Field>	 fields;

	private List<Method> methods;
	public List<VirtualMethod> virtualMethods;
	
	public  Method       ctorMethod;
	
	private HashMap<String, Integer> fiedsIndex;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public ICClass(int line, String name, List<Field> fields, List<Method> methods)
	{
		super(line);
		this.name = name;
		this.fields = fields;
		this.methods = methods;
		
		ctorMethod = null;
		virtualMethods = new ArrayList<VirtualMethod>();
		for (Method method : methods)
		{
			if (method.getName().endsWith(name)   &&
					method.getName().startsWith(name) && 
					name.length() == method.getName().length())
			{
				ctorMethod = method;
			}
			
			if (method instanceof VirtualMethod)
			{
				virtualMethods.add((VirtualMethod)method);
			}
		}
		
		fiedsIndex = new HashMap<String, Integer>();
		int index = 1;
		for (Field field : fields)
		{
			fiedsIndex.put(field.getName(), index);
			index++;
		}
	}

	public ICClass(int line, String name, String superClassName,
	    List<Field> fields, List<Method> methods)
	{
		this(line, name, fields, methods);
		this.superClassName = superClassName;
	}
	
	public int GetFieldIndex(String fieldName)
	{
		if (fiedsIndex.containsKey(fieldName))
			return fiedsIndex.get(fieldName);
		else
			return -1;
	}
	
	public Field GetFieldByName(String fieldName)
	{
		Field field = null;
		for (Field f : fields)
		{
			if (f.getName().equals(fieldName))
			{
				field = f;
				break;
			}
		}
		
		return field;
	}

	public String getName()
	{
		return name;
	}

	public boolean hasSuperClass()
	{
		return (superClassName != null);
	}

	public String getSuperClassName()
	{
		return superClassName;
	}

	public List<Field> getFields()
	{
		return fields;
	}

	public List<Method> getMethods()
	{
		return methods;
	}
	
	public String toString()
	{
		return "calss " + name;
	}

}
