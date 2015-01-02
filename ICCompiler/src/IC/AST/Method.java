package IC.AST;

import java.util.*;

/**
 * Abstract base class for method AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Method extends ASTNode
{
	protected Type	          type;

	protected String	        name;

	protected List<Formal>	  formals;

	protected List<Statement>	statements;

	protected Method(Type type, String name, List<Formal> formals,
	    List<Statement> statements)
	{
		super(type.getLine());
		this.type = type;
		this.name = name;
		if (formals != null)
		{
			Collections.reverse(formals);
		}
		this.formals = formals;
		this.statements = statements;
	}

	public Type getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public List<Formal> getFormals()
	{
		return formals;
	}

	public List<Statement> getStatements()
	{
		return statements;
	}

	public String toString()
	{
		StringBuffer formalsBuffer = new StringBuffer();
		for (int i = 0; i < formals.size(); i++)
		{
			Formal formal = formals.get(i);
			formalsBuffer.append(formal.toString());
			
			if (i < formals.size() - 1)
				formalsBuffer.append(",");
		}
		
		return type.toString() + " " + name + "(" + formalsBuffer.toString() + ")";
	}
}
