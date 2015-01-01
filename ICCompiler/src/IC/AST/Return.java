package IC.AST;

/**
 * Return statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Return extends Statement
{

	private Expression	value	= null;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public Return(int line)
	{
		super(line);
	}

	public Return(int line, Expression value)
	{
		this(line);
		this.value = value;
	}

	public boolean hasValue()
	{
		return (value != null);
	}

	public Expression getValue()
	{
		return value;
	}
	
	public String toString()
	{	
		return "return";
	}

}
