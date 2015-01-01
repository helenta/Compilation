package IC.AST;

import java.util.List;

/**
 * Static method call AST node.
 * 
 * @author Tovi Almozlino
 */
public class StaticCall extends Call
{
	private String	className;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public StaticCall(int line, String className, String name,
	    List<Expression> arguments)
	{
		super(line, name, arguments);
		this.className = className;
	}

	public String getClassName()
	{
		return className;
	}
}
