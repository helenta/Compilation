package IC.AST;

/**
 * Logical binary operation AST node.
 * 
 * @author Tovi Almozlino
 */
public class LogicalBinaryOp extends BinaryOp
{
	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public LogicalBinaryOp(Expression operand1, BinaryOps operator,
	    Expression operand2)
	{
		super(operand1, operator, operand2);
	}

}
