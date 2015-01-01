package IC.AST;

/**
 * Mathematical binary operation AST node.
 * 
 * @author Tovi Almozlino
 */
public class MathBinaryOp extends BinaryOp
{
	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public MathBinaryOp(Expression operand1, BinaryOps operator,
	    Expression operand2)
	{
		super(operand1, operator, operand2);
	}

}
