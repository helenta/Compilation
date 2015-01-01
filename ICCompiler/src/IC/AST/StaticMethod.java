package IC.AST;

import java.util.List;

/**
 * Static method AST node.
 * 
 * @author Tovi Almozlino
 */
public class StaticMethod extends Method
{
	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public StaticMethod(Type type, String name, List<Formal> formals,
	    List<Statement> statements)
	{
		super(type, name, formals, statements);
	}

	public String toString()
	{	
		return "static " + super.toString();
	}

}
