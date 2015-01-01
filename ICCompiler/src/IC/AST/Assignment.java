package IC.AST;

/**
 * Assignment statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Assignment extends Statement
{
	private Location	 variable;

	private Expression	assignment;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public Assignment(Location variable, Expression assignment)
	{
		super(variable.getLine());
		this.variable = variable;
		this.assignment = assignment;
	}

	public Location getVariable()
	{
		return variable;
	}

	public Expression getAssignment()
	{
		return assignment;
	}

	public String toString()
	{
		return variable.toString() + 
				   ((assignment != null) ? " = " + assignment.toString() 
				  		                   : "");
	}
}
