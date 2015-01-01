package IC.AST;

/**
 * Abstract base class for binary operation AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class BinaryOp extends Expression
{

	private Expression	operand1;

	private BinaryOps	 operator;

	private Expression	operand2;

	protected BinaryOp(Expression operand1, BinaryOps operator,
	    Expression operand2)
	{
		super(operand1.getLine());
		this.operand1 = operand1;
		this.operator = operator;
		this.operand2 = operand2;
	}

	public BinaryOps getOperator()
	{
		return operator;
	}

	public Expression getFirstOperand()
	{
		return operand1;
	}

	public Expression getSecondOperand()
	{
		return operand2;
	}

	public String toString()
	{
		return operand1.toString() + " " + operator.getOperatorString() + " " + operand2.toString();
	}
}
