package IC.AST;

import java.util.List;

/**
 * Abstract base class for method call AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Call extends Expression
{
	private String	         name;
	private List<Expression>	arguments;

	protected Call(int line, String name, List<Expression> arguments)
	{
		super(line);
		this.name = name;
		this.arguments = arguments;
	}

	public String getName()
	{
		return name;
	}

	public List<Expression> getArguments()
	{
		return arguments;
	}

	public String toString()
	{
		StringBuffer args = new StringBuffer();
		for (int i = 0; i < arguments.size(); i++)
		{
			Expression exp = arguments.get(i);
			args.append(exp.toString());
			
			if (i < arguments.size() - 1)
				args.append(", ");
		}
		
		return name + "(" + args.toString() + ")";
	}
	
}
