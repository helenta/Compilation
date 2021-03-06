package IC.AST;

/**
 * Abstract base class for data type AST nodes.
 * 
 * @author Tovi Almozlino
 */
public class Type extends ASTNode
{

	/**
	 * Number of array 'dimensions' in data type. For example, int[][] ->
	 * dimension = 2.
	 */
	private int	   dimension	= 0;

	private String	name;

	protected Type(int line)
	{
		super(line);
	}

	public Type(int line, String name)
	{
		super(line);
		this.name = name;
	}

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public String getName()
	{
		return name;
	}

	public int getDimension()
	{
		return dimension;
	}

	public void incrementDimension()
	{
		++dimension;
	}

	public boolean IsPimitive()
	{
		return false;
	}

	public boolean IsIntegerOrBoolean()
	{
		return false;
	}

	public boolean IsInteger()
	{
		return false;
	}

	public boolean IsBoolean()
	{
		return false;
	}
	
	public String toString()
	{	
		if (dimension == 0)
			return name ;
		else
			return name + "[]x" + dimension;
	}

}
