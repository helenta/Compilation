package IC.AST;

/**
 * Class field AST node.
 * 
 * @author Tovi Almozlino
 */
public class Field extends ASTNode
{
	private Type	 type;

	private String	name;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public Field(Type type, String name)
	{
		super(type.getLine());
		this.type = type;
		this.name = name;
	}

	public Type getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return type.toString() + " " + name;
	}
}
