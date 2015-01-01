package IC.LIR;

import java.io.*;
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		if (call.getClassName().equalsIgnoreCase("Library"))
		{
			writer.println("Library __" + call.getName() + "(R" + lirProgram.expressionRegister + "), Rdummy");
		}
		else
		{
			
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp)
	{
		// TODO Auto-generated method stub
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
