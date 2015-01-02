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
		writer.println("str: \"\"");
		writer.println();
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
		writer.println("#" + ifStatement.toString());
		
		String endIfLable = lirProgram.GetLabelName(ifStatement, "end_if");
		String elseLabel = ifStatement.getElseOperation() != null ?
											 lirProgram.GetLabelName(ifStatement, "else") : null;
		
		ifStatement.getCondition().accept(this);
		writer.println("Compare 1, R" + lirProgram.expressionRegister);
		
		if (elseLabel != null)
			writer.println("JumpFalse " + elseLabel);
		else
			writer.println("JumpFalse " + endIfLable);
		
		ifStatement.getOperation().accept(this);
		writer.println("JumpFalse " + endIfLable);
		
		if (elseLabel != null)
		{
			writer.println("#else: " + ifStatement.toString());
			
			writer.println(elseLabel + ":");
			ifStatement.getElseOperation().accept(this);
			writer.println("JumpFalse " + endIfLable);
		}

		writer.println(endIfLable + ":");
		 
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
		
		lirProgram.breakLabel = labelWhileEnd;
		lirProgram.continueLabel = labelWhileBody;
		
		writer.println(labelWhileBody + ":");
		whileStatement.getCondition().accept(this);
		writer.println("Compare 1, R" + lirProgram.expressionRegister);
		writer.println("JumpFalse " + labelWhileEnd);
		whileStatement.getOperation().accept(this);
		writer.println("Jump " + labelWhileBody);
		writer.println(labelWhileEnd + ":");
		
		lirProgram.breakLabel = null;
		lirProgram.continueLabel = null;
		
		return null;
	}

	@Override
	public Object visit(Break breakStatement)
	{
		if (lirProgram.breakLabel == null)
		{
			writer.println("break Errorrr!!!!!!!!!!!!!!!!");
			return null;
		}
		
		writer.println("#break");
		writer.println("Jump " + lirProgram.breakLabel);
		
		return null;
	}

	@Override
	public Object visit(Continue continueStatement)
	{
		if (lirProgram.continueLabel == null)
		{
			writer.println("continue Errorrr!!!!!!!!!!!!!!!!");
			return null;
		}
		
		writer.println("#continue");
		writer.println("Jump " + lirProgram.continueLabel);
		
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
		int reg1 = lirProgram.GetNextRegister();
		int reg2 = lirProgram.GetNextRegister();
		
		location.getArray().accept(this);
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		location.getIndex().accept(this);
		writer.println("MoveArray R" + reg1 + "[R" + lirProgram.expressionRegister + "]" + ", R" + reg2);
		writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(2);
		
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
		int reg1 = lirProgram.GetNextRegister();
		
		newArray.getSize().accept(this);
		writer.println("Mul 4, R" + lirProgram.expressionRegister);
		writer.println("Library __allocateArray(R" + lirProgram.expressionRegister + "), R" + reg1);
		writer.println("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(Length length)
	{
		int reg1 = lirProgram.GetNextRegister();
		
		length.getArray().accept(this);
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		writer.println("ArrayLength R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp)
	{
		int reg1 = lirProgram.GetNextRegister();
		
		binaryOp.getFirstOperand().accept(this);
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		binaryOp.getSecondOperand().accept(this);
		
		switch (binaryOp.getOperator())
		{
			case PLUS:
				writer.println("Add R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MINUS:
				writer.println("Sub R" + lirProgram.expressionRegister + ", R" + reg1);
				writer.println("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MULTIPLY:
				writer.println("Mul R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case DIVIDE:
				writer.println("Div R" + lirProgram.expressionRegister + ", R" + reg1);
				writer.println("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MOD:
				writer.println("Mod R" + lirProgram.expressionRegister + ", R" + reg1);
				writer.println("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			default:
				writer.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
		}
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp)
	{
		writer.println("#" + binaryOp.toString());
		
		int reg1 = lirProgram.GetNextRegister();
		int reg2 = lirProgram.GetNextRegister();
		
		binaryOp.getFirstOperand().accept(this);
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		binaryOp.getSecondOperand().accept(this);
		
		String labelName = null;
		switch (binaryOp.getOperator())
		{
			case LAND:
				writer.println("And R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LOR:
				writer.println("Or R" + lirProgram.expressionRegister + ", R" + reg1);
				writer.println("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LT: // expressionRegister1 < expressionRegister
				writer.println("Move 0, R" + reg2);
				DeugRegValue(reg1);
				DeugRegValue(lirProgram.expressionRegister);
				writer.println("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less");
				writer.println("JumpLE " + labelName);
				writer.println("Move 1, R" + reg2);
				writer.println(labelName + ":");
				writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LTE: // expressionRegister1 <= expressionRegister
				writer.println("Move 0, R" + reg2);
				writer.println("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpL " + labelName);
				writer.println("Move 1, R" + reg2);
				writer.println(labelName + ":");
				writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case GT: // expressionRegister1 > expressionRegister
				writer.println("Move 0, R" + reg2);
				writer.println("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpGE " + labelName);
				writer.println("Move 1, R" + reg2);
				writer.println(labelName + ":");
				writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case GTE: // expressionRegister1 >= expressionRegister
				writer.println("Move 0, R" + reg2);
				writer.println("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpG " + labelName);
				writer.println("Move 1, R" + reg2);
				writer.println(labelName + ":");
				writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case EQUAL: // expressionRegister1 == expressionRegister
				writer.println("Move 0, R" + reg2);
				writer.println("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpFalse " + labelName);
				writer.println("Move 1, R" + reg2);
				writer.println(labelName + ":");
				writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case NEQUAL: // expressionRegister1 != expressionRegister
				writer.println("Move 0, R" + reg2);
				writer.println("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				writer.println("JumpTrue " + labelName);
				writer.println("Move 1, R" + reg2);
				writer.println(labelName + ":");
				writer.println("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			default:
				writer.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
		}
		
		lirProgram.UnLockRegister(2);
		
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp)
	{
		if (unaryOp.getOperator() != UnaryOps.UMINUS)
		{
			writer.println("Wrong unary operator ERORR!!!!!!!!!!!!!!!!!!!!!1");
		}
		
		writer.println("#" + unaryOp.toString());
		
		unaryOp.getOperand().accept(this);
		
		int reg1 = lirProgram.GetNextRegister();
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		writer.println("Move 0, R" + lirProgram.expressionRegister);
		writer.println("Sub R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp)
	{
		if (unaryOp.getOperator() != UnaryOps.LNEG)
		{
			writer.println("Wrong unary operator ERORR!!!!!!!!!!!!!!!!!!!!!1");
		}
		
		writer.println("#" + unaryOp.toString());
		
		unaryOp.getOperand().accept(this);
		
		int reg1 = lirProgram.GetNextRegister();
		writer.println("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		writer.println("Move 1, R" + lirProgram.expressionRegister);
		writer.println("Xor R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
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
			String literalName = lirProgram.AddLiteral(literal.getValue().toString());
			writer.println("Move " + literalName + ", R" + lirProgram.expressionRegister);
		}

		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void DeugRegValue(int reg)
	{
		writer.println(); // todo: remove
		writer.println("Library __printi(R" + reg + "), Rdummy");
		writer.println("Library __println(str), Rdummy");
		writer.println();
	}
}
