package IC.AST;

/**
 * If statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class If extends Statement
{

	private Expression	condition;

	private Statement	 operation;

	private Statement	 elseOperation	= null;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public If(Expression condition, Statement operation, Statement elseOperation)
	{
		this(condition, operation);
		this.elseOperation = elseOperation;
	}

	public If(Expression condition, Statement operation)
	{
		super(condition.getLine());
		this.condition = condition;
		this.operation = operation;
	}

	public Expression getCondition()
	{
		return condition;
	}

	public Statement getOperation()
	{
		return operation;
	}

	public boolean hasElse()
	{
		return (elseOperation != null);
	}

	public Statement getElseOperation()
	{
		return elseOperation;
	}

	public String toString()
	{
		return "if";
	}
}
