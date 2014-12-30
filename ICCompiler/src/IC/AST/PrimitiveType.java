package IC.AST;

/**
 * Primitive data type AST node.
 * 
 * @author Tovi Almozlino
 */
public class PrimitiveType extends Type
{

	private DataTypes	type;

	/**
	 * Constructs a new primitive data type node.
	 * 
	 * @param line
	 *          Line number of type declaration.
	 * @param type
	 *          Specific primitive data type.
	 */
	public PrimitiveType(int line, DataTypes type)
	{
		super(line, type.toString());
		this.type = type;
	}

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public String getName()
	{
		return type.getDescription();
	}

	public DataTypes getType()
	{
		return type;
	}

	public boolean IsPimitive()
	{
		return true;
	}

	public boolean IsIntegerOrBoolean()
	{
		return type == DataTypes.BOOLEAN || type == DataTypes.INT;
	}

	public boolean IsInteger()
	{
		return type == DataTypes.INT;
	}

	public boolean IsBoolean()
	{
		return type == DataTypes.BOOLEAN;
	}
}
