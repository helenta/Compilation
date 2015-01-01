package IC.AST;

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

	private List<Method>	methods;

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
	}

	public ICClass(int line, String name, String superClassName,
	    List<Field> fields, List<Method> methods)
	{
		this(line, name, fields, methods);
		this.superClassName = superClassName;
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
