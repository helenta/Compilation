package IC.AST;

/**
 * Continue statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Continue extends Statement
{

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public Continue(int line)
	{
		super(line);
	}

	public String toString()
	{
		return "continue";
	}
}
