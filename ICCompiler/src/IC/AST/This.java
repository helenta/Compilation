package IC.AST;

/**
 * 'This' expression AST node.
 * 
 * @author Tovi Almozlino
 */
public class This extends Expression
{

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public This(int line)
	{
		super(line);
	}

	public String toString()
	{	
		return "this";
	}

}
