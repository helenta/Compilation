package IC.LIR;

import java.io.*;
import java.util.List;

import IC.AST.*;

public class EmitVisitor implements Visitor
{
	private PrintWriter writer;
	
	public LIRProgram lirProgram;
	
	public EmitVisitor(PrintWriter printWriter, LIRProgram program)
  {
		writer = printWriter;
		lirProgram = program;
  }
	
	@Override
	public Object visit(Program program)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ICClass icClass)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Field field)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualMethod method)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticMethod method)
	{
    writer.println("# main in " + lirProgram.currentClass.getName());
	  writer.println("_ic_main:");
	  
	  for (Statement statement : method.getStatements())
	  {
	  	statement.accept(this);
	  }
	  
	  writer.println("Library __exit(0),R0");
	  writer.println("Return 9999");
		
		return null;
	}

	@Override
	public Object visit(LibraryMethod method)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Formal formal)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(PrimitiveType type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Type type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Assignment assignment)
	{
		writer.println("#" + assignment.toString());
		assignment.getAssignment().accept(this);
		
		Location location = assignment.getVariable();
		if (location instanceof VariableLocation)
		{
			VariableLocation varLocation = (VariableLocation)location;
			if (varLocation.getLocation() == null)
			{
				writer.println("Move R" + lirProgram.expressionRegister + ", " + varLocation.getName());
			}
			// todo: else field location
		}
		// todo: else array location
		
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement)
	{
		return callStatement.getCall().accept(this);
	}

	@Override
	public Object visit(Return returnStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement)
	{
		writer.println("#" + whileStatement.toString());
		/*_test_label:
		Move x,R1
		Compare 0,R1
		JumpLE _end_label 
		Move x,R1
		# Extra code to print "x="+x
		Library __print(str1),Rdummy
		Jump _test_label*/
		String labelWhileBody = lirProgram.GetLabelName(whileStatement, "while_body");
		String labelWhileEnd = lirProgram.GetLabelName(whileStatement, "while_end");
		
		writer.println(labelWhileBody + ":");
		whileStatement.getCondition().accept(this);
		writer.println("Compare 1, R" + lirProgram.expressionRegister);
		writer.println("JumpFalse " + labelWhileEnd);
		whileStatement.getOperation().accept(this);
		writer.println("Jump " + labelWhileBody);
		writer.println(labelWhileEnd + ":");
		
		return null;
	}

	@Override
	public Object visit(Break breakStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock)
	{
		writer.println("#Statement block line " + statementsBlock.getLine());
		for (Statement statement : statementsBlock.getStatements())
		{
			statement.accept(this);
		}
		
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable)
	{
		writer.println("#" + localVariable.toString());
		
		if (localVariable.getInitValue() == null)
		{
			writer.println("Move " + localVariable.getName() + ", 0");
		}
		else
		{
			localVariable.getInitValue().accept(this);
			writer.println("Move R" + lirProgram.expressionRegister + ", " + localVariable.getName());
		}
		return null;
	}

	@Override
	public Object visit(VariableLocation location)
	{
		if (location.getLocation() == null)
		{
			writer.println("Move " + location.getName() + ", R" + lirProgram.expressionRegister);
		}
		else
		{
			// todo:
		}
		
		return null;
	}

	@Override
	public Object visit(ArrayLocation location)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call)
	{
		writer.println("# Call " + call.toString());
		if (call.getClassName().equalsIgnoreCase("Library"))
		{
			// todo: check if there are some methods
			// set arguments
			List<Expression> callArguments = call.getArguments();
			StringBuffer argsBuffer = new StringBuffer();
			int[] argumentsRegs = lirProgram.GetArgumentsRegisters(callArguments.size());
			
			for (int i = 0; i < callArguments.size(); i++)
			{
				writer.println("# evaluate arg " + i);
				
				Expression exp = callArguments.get(i);
				int reg = argumentsRegs[i];
				exp.accept(this);
				
				writer.println("Move R" + lirProgram.expressionRegister + ", " + "R" + reg);
				
				argsBuffer.append("R" + reg);
				if (i < callArguments.size() - 1)
					argsBuffer.append(", ");
			}
			
			writer.println("Library __" + call.getName() + "(" + argsBuffer.toString() + "), Rdummy");
		}
		else
		{
			//writer.println("StaticCal " + call.get + call.getName() + "(R" + lirProgram.expressionRegister + "), Rdummy");
		}
		
		return null;
	}

	@Override
	public Object visit(VirtualCall call)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewArray newArray)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Length length)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp)
	{
		binaryOp.getFirstOperand().accept(this);
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + lirProgram.expressionRegister1);
		
		binaryOp.getSecondOperand().accept(this);
		
		switch (binaryOp.getOperator())
		{
			case PLUS:
				writer.println("Add R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MINUS:
				writer.println("Sub R" + lirProgram.expressionRegister + ", R" + lirProgram.expressionRegister1);
				writer.println("Move R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MULTIPLY:
				writer.println("Mul R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case DIVIDE:
				writer.println("Div R" + lirProgram.expressionRegister + ", R" + lirProgram.expressionRegister1);
				writer.println("Move R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MOD:
				writer.println("Mod R" + lirProgram.expressionRegister + ", R" + lirProgram.expressionRegister1);
				writer.println("Move R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			default:
				writer.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
		}
		
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp)
	{
		binaryOp.getFirstOperand().accept(this);
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + lirProgram.expressionRegister1);
		
		binaryOp.getSecondOperand().accept(this);
		
		String labelName = null;
		switch (binaryOp.getOperator())
		{
			case LAND:
				writer.println("And R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LOR:
				writer.println("Or R" + lirProgram.expressionRegister + ", R" + lirProgram.expressionRegister1);
				writer.println("Move R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LT: // expressionRegister1 < expressionRegister
				writer.println("Move 0, R" + lirProgram.expressionRegister2);
				writer.println("Compare R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less");
				writer.println("JumpLE " + labelName);
				writer.println("Move 1, R" + lirProgram.expressionRegister2);
				writer.println(labelName + ":");
				writer.println("Move R" + lirProgram.expressionRegister2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LTE: // expressionRegister1 <= expressionRegister
				writer.println("Move 0, R" + lirProgram.expressionRegister2);
				writer.println("Compare R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpL " + labelName);
				writer.println("Move 1, R" + lirProgram.expressionRegister2);
				writer.println(labelName + ":");
				writer.println("Move R" + lirProgram.expressionRegister2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case GT: // expressionRegister1 > expressionRegister
				writer.println("Move 0, R" + lirProgram.expressionRegister2);
				writer.println("Compare R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpGE " + labelName);
				writer.println("Move 1, R" + lirProgram.expressionRegister2);
				writer.println(labelName + ":");
				writer.println("Move R" + lirProgram.expressionRegister2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case GTE: // expressionRegister1 >= expressionRegister
				writer.println("Move 0, R" + lirProgram.expressionRegister2);
				writer.println("Compare R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpG " + labelName);
				writer.println("Move 1, R" + lirProgram.expressionRegister2);
				writer.println(labelName + ":");
				writer.println("Move R" + lirProgram.expressionRegister2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case EQUAL: // expressionRegister1 == expressionRegister
				writer.println("Move 0, R" + lirProgram.expressionRegister2);
				writer.println("Compare R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpFalse " + labelName);
				writer.println("Move 1, R" + lirProgram.expressionRegister2);
				writer.println(labelName + ":");
				writer.println("Move R" + lirProgram.expressionRegister2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case NEQUAL: // expressionRegister1 != expressionRegister
				writer.println("Move 0, R" + lirProgram.expressionRegister2);
				writer.println("Compare R" + lirProgram.expressionRegister1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpTrue " + labelName);
				writer.println("Move 1, R" + lirProgram.expressionRegister2);
				writer.println(labelName + ":");
				writer.println("Move R" + lirProgram.expressionRegister2 + ", R" + lirProgram.expressionRegister);
				break;
				
			default:
				writer.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
		}
		
		
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Literal literal)
	{
		LiteralTypes literalType = literal.getType();
		if (literalType == LiteralTypes.INTEGER)
		{
			writer.println("Move " + literal.getValue() + ", R" + lirProgram.expressionRegister);
		}
		else if (literalType == LiteralTypes.TRUE)
		{
			writer.println("Move 1, R" + lirProgram.expressionRegister);
		}
		else if ((literalType == LiteralTypes.FALSE) || (literal.getType() == LiteralTypes.NULL))
		{
			writer.println("Move 0, R" + lirProgram.expressionRegister);
		}
		else if (literalType == LiteralTypes.STRING)
		{
			// todo: locate string name, add it to the head of the program
			return "str:" + literal.getValue().toString();
		}

		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
