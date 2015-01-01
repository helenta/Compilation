package IC.AST;

/**
 * Logical unary operation AST node.
 * 
 * @author Tovi Almozlino
 */
public class LogicalUnaryOp extends UnaryOp
{
	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public LogicalUnaryOp(UnaryOps operator, Expression operand)
	{
		super(operator, operand);
	}

}
