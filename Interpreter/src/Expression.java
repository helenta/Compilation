import java.util.*;

class Expression

// Can hold var OR number OR BinaryOP for to integers
{
	public ExpType	 m_Type;

	private String[]	m_Token;
	private String	 exp;	   // todo: remove

	public Expression(PolishProgram program, String strExp)
	{
		// Receive the PARTIAL string of the expression. The first char should not
		// be whitespace!
		// Should parse the string into the object
		// Should update the error list in the program in case there is an error
		// strExp = strExp.trim();
		m_Token = strExp.split("\\s+");
		exp = strExp;
	}

	// *** should run when we want to run syntax error. Sending dummy values to
	// execute, in order to check the syntax of the expression ***/
	public boolean CheckSyntax(PolishProgram program)
	{
		String[] templateExp = m_Token.clone();
		for (int i = 0; i < templateExp.length; i++)
		{
			if (isLiteral(templateExp[i]))
			{
				templateExp[i] = "1";
			}
		}

		// todo: remove
		try
		{
			boolean result = Execute(program, templateExp) != -1;
			return result;
		}
		catch (Exception e)
		{
			// System.out.println("7b : " + exp);
		}
		return false;
	}

	// should be called from the Command class using the m_token that was created
	// in the constructor
	public int Execute(PolishProgram program)
	{
		return Execute(program, m_Token);
	}

	private int Execute(PolishProgram program, String[] token)
	{
		int result = -1;
		Stack<Integer> literalStack = new Stack<Integer>();

		// implements the stack expression fetching algorithm of the Polish notation
		for (int i = token.length - 1; i >= 0; i--)
		{
			if (isVariable(token[i]))
			{
				int value = program.GetVarValue(token[i].charAt(0));
				if (value == -1)
				{
					program.AddError(PolishProgram.ErrorCodeInitalization, "The '"
					    + token[i].charAt(0) + "' variable is not assigned.");
				}
				literalStack.add(value);
			}
			else
				if (isNumber(token[i]))
				{
					literalStack.add(Integer.parseInt(token[i]));
				}
				else
					if (expressionOp(token[i]) != BinaryOp.None)
					{
						result = 0;
						try
						{
							result = processOperator(expressionOp(token[i]),
							    literalStack.pop(), literalStack.pop());
						}
						catch (EmptyStackException e)
						{
							program.AddError(PolishProgram.ErrorCodeGrammer,
							    "expression syntax error ");
							return -1;
						}
						literalStack.push(result);

					}
					else
					{
						program.AddError(PolishProgram.ErrorCodeGrammer,
						    "expression syntax error - illegal term");
						return -1;
					}

		}
		result = literalStack.pop();
		if (!literalStack.isEmpty())
		{
			program.AddError(PolishProgram.ErrorCodeGrammer,
			    "expression syntax error - stack isnt empty ");
			return -1;
		}
		return result;

	}

	private int processOperator(BinaryOp expressionOp, Integer pop, Integer pop2)
	{
		if (expressionOp == BinaryOp.plus)
		{
			return pop + pop2;
		}
		else
			if (expressionOp == BinaryOp.minus)
			{
				return pop - pop2;
			}
			else
				if (expressionOp == BinaryOp.mult)
				{
					return pop * pop2;
				}
				else
					if (expressionOp == BinaryOp.devide)
					{
						return pop / pop2;
					}
		return -1;
	}

	public BinaryOp expressionOp(String op)
	{
		if (op.length() != 1)
		{
			return BinaryOp.None;
		}

		char c = op.charAt(0);
		if (c == '+')
		{
			return BinaryOp.plus;
		}
		else
			if (c == '-')
			{
				return BinaryOp.minus;
			}
			else
				if (c == '*')
				{
					return BinaryOp.mult;
				}
				else
					if (c == '\\')
					{
						return BinaryOp.devide;
					}
		return BinaryOp.None;
	}

	public boolean isNumber(String str)
	{

		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) < '0' || str.charAt(i) > '9')
			{
				return false;
			}
		}
		return true;
	}

	public boolean isVariable(String str)
	{
		if (str.length() != 1)
		{
			return false;
		}
		if (str.charAt(0) < 'a' || str.charAt(0) > 'z')
		{
			return false;
		}
		return true;
	}

	public boolean isLiteral(String str)
	{
		return (isVariable(str) || isNumber(str));
	}

}
