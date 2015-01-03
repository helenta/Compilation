package IC.LIR;

import java.io.*;
import java.util.List;
import java.util.Map;

import IC.AST.*;
import IC.SymbolTables.MethodScope;

public class EmitVisitor implements Visitor
{
	private File file;
	private StringBuffer writer;
	
	public LIRProgram lirProgram;
	
	public EmitVisitor(File file, LIRProgram program) throws FileNotFoundException
  {
		this.file = file;
		
		lirProgram = program;
  }
	
	private void AppendLine(String line)
	{
		writer.append(line + "\n");
	}
	
	private void AppendLine()
	{
		writer.append("\n");
	}
	
	@Override
	public Object visit(Program program)
	{
		writer = new StringBuffer();
		
		for (ICClass icClass : program.getClasses())
		{
			icClass.accept(this);
		}
		
		try
		{
			PrintWriter f = new PrintWriter(file, "UTF-8");
			
			f.println("# program");
			f.println();
			
			f.println("############literals#############");
			for (Map.Entry<String, String> entry : lirProgram.literals.entrySet())
			{
				f.println(entry.getValue() + " : " + entry.getKey());
			}
			f.println("#################################");
			f.println();
			
			f.println("############dispatch table#######");
			for (Map.Entry<String, List<String>> entry : lirProgram.dispatchTables.entrySet())
			{
				//_DV_Foo: [_Foo_shine,_Foo_rise]
				if (entry.getValue().size() > 0)
				{
					f.print(entry.getKey() + ": [");
					for (int i = 0; i < entry.getValue().size(); i++)
					{
						String labelName = entry.getValue().get(i);
						f.print(labelName);
						
						if (i < entry.getValue().size() - 1)
							f.print(", ");
					}
					f.println("]");
				}
			}
			f.println("#################################");
			f.println();
			
			f.print(writer.toString());
			
			f.close();
		}
		catch (Throwable th)
		{
			System.err.println(th.getMessage());
			return null;
		}
			
		return null;
	}

	@Override
	public Object visit(ICClass icClass)
	{
		lirProgram.currentClass = icClass;
		
		AppendLine();
		AppendLine("# class " + icClass.getName());
		AppendLine();
		
		lirProgram.AddDispatchTable(icClass);
		for (Method method : icClass.getMethods())
		{
			if (method instanceof VirtualMethod)
			{
				VirtualMethod virtualMethod = (VirtualMethod)method;
				lirProgram.AddVirtualMethodToDispatchTable(virtualMethod);
			}
			
			method.accept(this);
		}
		
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
		return AcceptMetod(method, "virtual");
	}
	
	private Object AcceptMetod(Method method, String st)
	{
		AppendLine();
		AppendLine("#" + st + " method: " + method.getName() + ", " + lirProgram.currentClass.getName());
		
		String labelName = lirProgram.GetMethodLabel(method);
		AppendLine(labelName + ":");
		
		boolean exsistRetunStatement = false;
		for (Statement statement : method.getStatements())
	  {
			exsistRetunStatement = statement instanceof Return;
	  	statement.accept(this);
	  }
		
		if (method.IsMain())
			AppendLine("Library __exit(0),R0");
		
		if (!exsistRetunStatement)
			AppendLine("Return " + lirProgram.ReturnRegister);
		
	  AppendLine();
		
		return null;
	}

	@Override
	public Object visit(StaticMethod method)
	{
		return AcceptMetod(method, "static");
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
		AppendLine("#" + assignment.toString());
		
		int reg1 = lirProgram.GetNextRegister();
		int reg2 = lirProgram.GetNextRegister();
		int reg3 = lirProgram.GetNextRegister();
		
		assignment.getAssignment().accept(this);
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		Location location = assignment.getVariable();
		
		if (location instanceof VariableLocation)
		{
			VariableLocation variableLocation = (VariableLocation)location;
			if (variableLocation.getLocation() == null)
			{
				boolean isLocal = lirProgram.IsLocalVarible(variableLocation);
				if (isLocal)
				{
					AppendLine("Move R" + reg1 + ", " + variableLocation.getName());
				}
				else
				{
					ICClass icClass = lirProgram.GetVaribleLocationClass(variableLocation);
					int fieldIndex = icClass.GetFieldIndex(variableLocation.getName());
					
					AppendLine("MoveField R" + reg1 + ", R" + lirProgram.thisRegister + "." + fieldIndex);
				}
			}
			else
			{
				variableLocation.getLocation().accept(this);
				
				Type expressionType = variableLocation.getLocation().semType;
				ICClass icClass = lirProgram.GetClassByName(expressionType.getName());
				int fieldIndex = icClass.GetFieldIndex(variableLocation.getName());
				
				AppendLine("MoveField R" + reg1 + ", R" + lirProgram.expressionRegister + "." + fieldIndex);
			}
		}
		else
		{
			ArrayLocation arrayLocation = (ArrayLocation)location;
			
			arrayLocation.getArray().accept(this);
			AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg2);
			
			arrayLocation.getIndex().accept(this);
			AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg3);
			
			AppendLine("MoveArray R" + reg1 + ", R" + reg2 + "[R" + reg3 + "]");
		}
		
		lirProgram.UnLockRegister(3);
		
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
		returnStatement.getValue().accept(this);
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + lirProgram.ReturnRegister);
		AppendLine("Return R" + lirProgram.ReturnRegister);
		
		return null;
	}

	@Override
	public Object visit(If ifStatement)
	{
		AppendLine("#" + ifStatement.toString());
		
		String endIfLable = lirProgram.GetLabelName(ifStatement, "end_if");
		String elseLabel = ifStatement.getElseOperation() != null ?
											 lirProgram.GetLabelName(ifStatement, "else") : null;
		
		ifStatement.getCondition().accept(this);
		AppendLine("Compare 1, R" + lirProgram.expressionRegister);
		
		if (elseLabel != null)
			AppendLine("JumpFalse " + elseLabel);
		else
			AppendLine("JumpFalse " + endIfLable);
		
		ifStatement.getOperation().accept(this);
		AppendLine("JumpFalse " + endIfLable);
		
		if (elseLabel != null)
		{
			AppendLine("#else: " + ifStatement.toString());
			
			AppendLine(elseLabel + ":");
			ifStatement.getElseOperation().accept(this);
			AppendLine("JumpFalse " + endIfLable);
		}

		AppendLine(endIfLable + ":");
		 
		return null;
	}

	@Override
	public Object visit(While whileStatement)
	{
		AppendLine("#" + whileStatement.toString());
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
		
		AppendLine(labelWhileBody + ":");
		whileStatement.getCondition().accept(this);
		AppendLine("Compare 1, R" + lirProgram.expressionRegister);
		AppendLine("JumpFalse " + labelWhileEnd);
		whileStatement.getOperation().accept(this);
		AppendLine("Jump " + labelWhileBody);
		AppendLine(labelWhileEnd + ":");
		
		lirProgram.breakLabel = null;
		lirProgram.continueLabel = null;
		
		return null;
	}

	@Override
	public Object visit(Break breakStatement)
	{
		if (lirProgram.breakLabel == null)
		{
			AppendLine("break Errorrr!!!!!!!!!!!!!!!!");
			return null;
		}
		
		AppendLine("#break");
		AppendLine("Jump " + lirProgram.breakLabel);
		
		return null;
	}

	@Override
	public Object visit(Continue continueStatement)
	{
		if (lirProgram.continueLabel == null)
		{
			AppendLine("continue Errorrr!!!!!!!!!!!!!!!!");
			return null;
		}
		
		AppendLine("#continue");
		AppendLine("Jump " + lirProgram.continueLabel);
		
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock)
	{
		AppendLine("#Statement block line " + statementsBlock.getLine());
		for (Statement statement : statementsBlock.getStatements())
		{
			statement.accept(this);
		}
		
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable)
	{
		AppendLine("#" + localVariable.toString());
		
		if (localVariable.getInitValue() == null)
		{
			AppendLine("Move " + localVariable.getName() + ", 0");
		}
		else
		{
			localVariable.getInitValue().accept(this);
			AppendLine("Move R" + lirProgram.expressionRegister + ", " + localVariable.getName());
		}
		return null;
	}

	@Override
	public Object visit(VariableLocation location)
	{
		boolean isLocalVariable = lirProgram.IsLocalVarible(location);
		if (location.getLocation() == null)
		{
			if (isLocalVariable)
			{
				AppendLine("Move " + location.getName() + ", R" + lirProgram.expressionRegister);
			}
			else
			{
				ICClass icClass = lirProgram.GetVaribleLocationClass(location);
				int index = icClass.GetFieldIndex(location.getName());
				
				AppendLine("MoveField R" + lirProgram.thisRegister + "." + index + ", R" + lirProgram.expressionRegister);
			}
		}
		else
		{
			location.getLocation().accept(this);
			
			ICClass icClass = lirProgram.GetClassByName(location.getLocation().semType.getName());
			int index = icClass.GetFieldIndex(location.getName());

			AppendLine("MoveField R" + lirProgram.expressionRegister + "." + index + ", R" + lirProgram.expressionRegister);	
		}
		
		return null;
	}

	@Override
	public Object visit(ArrayLocation location)
	{
		int reg1 = lirProgram.GetNextRegister();
		int reg2 = lirProgram.GetNextRegister();
		
		location.getArray().accept(this);
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		location.getIndex().accept(this);
		AppendLine("MoveArray R" + reg1 + "[R" + lirProgram.expressionRegister + "]" + ", R" + reg2);
		AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(2);
		
		return null;
	}

	@Override
	public Object visit(StaticCall call)
	{
		AppendLine("# Call " + call.toString());
		
		List<Expression> callArguments = call.getArguments();
		StringBuffer argsBuffer = new StringBuffer();
		int[] argumentsRegs = lirProgram.GetArgumentsRegisters(callArguments.size());
		
		if (call.getClassName().equalsIgnoreCase("Library"))
		{
			// todo: check if there are some methods
			// set arguments
			for (int i = 0; i < callArguments.size(); i++)
			{
				AppendLine("# evaluate arg " + i);
				
				Expression exp = callArguments.get(i);
				int reg = argumentsRegs[i];
				exp.accept(this);
				
				AppendLine("Move R" + lirProgram.expressionRegister + ", " + "R" + reg);
				
				argsBuffer.append("R" + reg);
				if (i < callArguments.size() - 1)
					argsBuffer.append(", ");
			}
			
			AppendLine("Library __" + call.getName() + "(" + argsBuffer.toString() + "), Rdummy");
		}
		else
		{
			// StaticCall _func1(x=R1,y=R2),R8
			// set arguments
			
			Method method = lirProgram.GetMethodFromCall(call);
			List<Formal> formals = method.getFormals();
			
			String methodLabel = lirProgram.GetMethodLabel(method);
			
			for (int i = 0; i < callArguments.size(); i++)
			{
				AppendLine("# evaluate arg " + i);
				
				Expression exp = callArguments.get(i);
				int reg = argumentsRegs[i];
				exp.accept(this);
				
				AppendLine("Move R" + lirProgram.expressionRegister + ", " + "R" + reg);
				
				Formal formal = formals.get(i);
				
				argsBuffer.append(formal.getName() + " = R" + reg);
				if (i < callArguments.size() - 1)
					argsBuffer.append(", ");
			}
			
			AppendLine("StaticCall " + methodLabel + "(" + argsBuffer.toString() + "), R" + lirProgram.expressionRegister);
		}
		
		return null;
	}

	@Override
	public Object visit(VirtualCall call)
	{
		List<Expression> callArguments = call.getArguments();
		StringBuffer argsBuffer = new StringBuffer();
		int[] argumentsRegs = lirProgram.GetArgumentsRegisters(callArguments.size());
		Method method = lirProgram.GetMethodFromCall(call);
		List<Formal> formals = method.getFormals();
		String methodLabel = lirProgram.GetMethodLabel(method);
		
		if (method instanceof StaticMethod)
		{
			for (int i = 0; i < callArguments.size(); i++)
			{
				AppendLine("# evaluate arg " + i);
				
				Expression exp = callArguments.get(i);
				int reg = argumentsRegs[i];
				exp.accept(this);
				
				AppendLine("Move R" + lirProgram.expressionRegister + ", " + "R" + reg);
				
				Formal formal = formals.get(i);
				
				argsBuffer.append(formal.getName() + " = R" + reg);
				if (i < callArguments.size() - 1)
					argsBuffer.append(", ");
			}
			
			AppendLine("StaticCall " + methodLabel + "(" + argsBuffer.toString() + "), R" + lirProgram.expressionRegister);
		}
		else
		{	
			int reg1 = lirProgram.GetNextRegister();
			call.getLocation().accept(this);
			AppendLine("Move R" + lirProgram.expressionRegister + ", " + "R" + reg1);
			
			ICClass icClass = lirProgram.GetClassByName(call.getLocation().semType.getName());
			int methodIndex = icClass.virtualMethods.indexOf((VirtualMethod)method);
			
			for (int i = 0; i < callArguments.size(); i++)
			{
				AppendLine("# evaluate arg " + i);
				
				Expression exp = callArguments.get(i);
				int reg = argumentsRegs[i];
				exp.accept(this);
				
				AppendLine("Move R" + lirProgram.expressionRegister + ", " + "R" + reg);
				
				Formal formal = formals.get(i);
				
				argsBuffer.append(formal.getName() + " = R" + reg);
				if (i < callArguments.size() - 1)
					argsBuffer.append(", ");
			}
			
			//VirtualCall R1.0(x=R2),Rdummy
			AppendLine("Move R" + reg1 + ", " + "R" + lirProgram.thisRegister);
			AppendLine("VirtualCall R" + reg1 + "." + methodIndex + "(" + argsBuffer.toString() + "), R" + lirProgram.expressionRegister);
			
		  lirProgram.UnLockRegister(1);
		}
		
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
		AppendLine("#new " + newClass.getName() + "()");
		
		int reg1 = lirProgram.GetNextRegister();
		
		ICClass icClass = lirProgram.GetClassByName(newClass.scope, newClass.getName());
		String dispatchTable = lirProgram.GetDispatchTableName(icClass);
		
		int fieldsCount = icClass.getFields().size();
		AppendLine("Library __allocateObject(" + (fieldsCount+1)*4 + "), R" + reg1);
		
		if (icClass.virtualMethods.size() > 0)
			AppendLine("MoveField " + dispatchTable + ", R" + reg1 + ".0");

		if (icClass.ctorMethod != null)
		{
			icClass.ctorMethod.accept(this);
		}
		
		AppendLine("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(NewArray newArray)
	{
		int reg1 = lirProgram.GetNextRegister();
		
		newArray.getSize().accept(this);
		AppendLine("Mul 4, R" + lirProgram.expressionRegister);
		AppendLine("Library __allocateArray(R" + lirProgram.expressionRegister + "), R" + reg1);
		AppendLine("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(Length length)
	{
		int reg1 = lirProgram.GetNextRegister();
		
		length.getArray().accept(this);
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		AppendLine("ArrayLength R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp)
	{
		int reg1 = lirProgram.GetNextRegister();
		
		binaryOp.getFirstOperand().accept(this);
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		binaryOp.getSecondOperand().accept(this);
		
		switch (binaryOp.getOperator())
		{
			case PLUS:
				AppendLine("Add R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MINUS:
				AppendLine("Sub R" + lirProgram.expressionRegister + ", R" + reg1);
				AppendLine("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MULTIPLY:
				AppendLine("Mul R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case DIVIDE:
				AppendLine("Div R" + lirProgram.expressionRegister + ", R" + reg1);
				AppendLine("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case MOD:
				AppendLine("Mod R" + lirProgram.expressionRegister + ", R" + reg1);
				AppendLine("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			default:
				AppendLine("Error!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
		}
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp)
	{
		AppendLine("#" + binaryOp.toString());
		
		int reg1 = lirProgram.GetNextRegister();
		int reg2 = lirProgram.GetNextRegister();
		
		binaryOp.getFirstOperand().accept(this);
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		
		binaryOp.getSecondOperand().accept(this);
		
		String labelName = null;
		switch (binaryOp.getOperator())
		{
			case LAND:
				AppendLine("And R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LOR:
				AppendLine("Or R" + lirProgram.expressionRegister + ", R" + reg1);
				AppendLine("Move R" + reg1 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LT: // expressionRegister1 < expressionRegister
				AppendLine("Move 0, R" + reg2);
				DeugRegValue(reg1);
				DeugRegValue(lirProgram.expressionRegister);
				AppendLine("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less");
				AppendLine("JumpLE " + labelName);
				AppendLine("Move 1, R" + reg2);
				AppendLine(labelName + ":");
				AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case LTE: // expressionRegister1 <= expressionRegister
				AppendLine("Move 0, R" + reg2);
				AppendLine("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				AppendLine("JumpL " + labelName);
				AppendLine("Move 1, R" + reg2);
				AppendLine(labelName + ":");
				AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case GT: // expressionRegister1 > expressionRegister
				AppendLine("Move 0, R" + reg2);
				AppendLine("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				AppendLine("JumpGE " + labelName);
				AppendLine("Move 1, R" + reg2);
				AppendLine(labelName + ":");
				AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case GTE: // expressionRegister1 >= expressionRegister
				AppendLine("Move 0, R" + reg2);
				AppendLine("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				AppendLine("JumpG " + labelName);
				AppendLine("Move 1, R" + reg2);
				AppendLine(labelName + ":");
				AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case EQUAL: // expressionRegister1 == expressionRegister
				AppendLine("Move 0, R" + reg2);
				AppendLine("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				AppendLine("JumpFalse " + labelName);
				AppendLine("Move 1, R" + reg2);
				AppendLine(labelName + ":");
				AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			case NEQUAL: // expressionRegister1 != expressionRegister
				AppendLine("Move 0, R" + reg2);
				AppendLine("Compare R" + reg1 + ", R" + lirProgram.expressionRegister);
				labelName = lirProgram.GetLabelName(binaryOp, "less_equal");
				AppendLine("JumpTrue " + labelName);
				AppendLine("Move 1, R" + reg2);
				AppendLine(labelName + ":");
				AppendLine("Move R" + reg2 + ", R" + lirProgram.expressionRegister);
				break;
				
			default:
				AppendLine("Error!!!!!!!!!!!!!!!!!!!!!!!!!");
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
			AppendLine("Wrong unary operator ERORR!!!!!!!!!!!!!!!!!!!!!1");
		}
		
		AppendLine("#" + unaryOp.toString());
		
		unaryOp.getOperand().accept(this);
		
		int reg1 = lirProgram.GetNextRegister();
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		AppendLine("Move 0, R" + lirProgram.expressionRegister);
		AppendLine("Sub R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp)
	{
		if (unaryOp.getOperator() != UnaryOps.LNEG)
		{
			AppendLine("Wrong unary operator ERORR!!!!!!!!!!!!!!!!!!!!!1");
		}
		
		AppendLine("#" + unaryOp.toString());
		
		unaryOp.getOperand().accept(this);
		
		int reg1 = lirProgram.GetNextRegister();
		AppendLine("Move R" + lirProgram.expressionRegister + ", R" + reg1);
		AppendLine("Move 1, R" + lirProgram.expressionRegister);
		AppendLine("Xor R" + reg1 + ", R" + lirProgram.expressionRegister);
		
		lirProgram.UnLockRegister(1);
		
		return null;
	}

	@Override
	public Object visit(Literal literal)
	{
		LiteralTypes literalType = literal.getType();
		if (literalType == LiteralTypes.INTEGER)
		{
			AppendLine("Move " + literal.getValue() + ", R" + lirProgram.expressionRegister);
		}
		else if (literalType == LiteralTypes.TRUE)
		{
			AppendLine("Move 1, R" + lirProgram.expressionRegister);
		}
		else if ((literalType == LiteralTypes.FALSE) || (literal.getType() == LiteralTypes.NULL))
		{
			AppendLine("Move 0, R" + lirProgram.expressionRegister);
		}
		else if (literalType == LiteralTypes.STRING)
		{
			String literalName = lirProgram.AddLiteral(literal.getValue().toString());
			AppendLine("Move " + literalName + ", R" + lirProgram.expressionRegister);
		}

		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock)
	{
		expressionBlock.getExpression().accept(this);
		return null;
	}

	private void DeugRegValue(int reg)
	{
		AppendLine(); // todo: remove
		AppendLine("Library __printi(R" + reg + "), Rdummy");
		AppendLine("Library __println(str), Rdummy");
		AppendLine();
	}
}
