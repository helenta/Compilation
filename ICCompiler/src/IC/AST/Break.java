package IC.AST;

/**
 * Break statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Break extends Statement
{
	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public Break(int line)
	{
		super(line);
	}

	public String toString()
	{
		return "break";
	}
}
