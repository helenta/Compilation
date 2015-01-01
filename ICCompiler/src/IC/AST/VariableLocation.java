package IC.AST;

/**
 * Variable reference AST node.
 * 
 * @author Tovi Almozlino
 */
public class VariableLocation extends Location
{
	private Expression	location	= null;

	private String	   name;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public VariableLocation(int line, String name)
	{
		super(line);
		this.name = name;
	}

	public VariableLocation(int line, Expression location, String name)
	{
		super(line);
		this.location = location;
		this.name = name;
	}

	public boolean isExternal()
	{
		return (location != null);
	}

	public Expression getLocation()
	{
		return location;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{	
		if (location != null)
			return name + "." + location.toString();
		else
			return name;
	}

}
