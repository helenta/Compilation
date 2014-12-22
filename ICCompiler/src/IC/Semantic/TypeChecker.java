package IC.Semantic;

import java.util.List;

import IC.AST.*;
import IC.*;

public class TypeChecker implements Visitor
{
	
	private SymbolTable table;
	private boolean inStatic = false;
	
	public TypeChecker(SymbolTable table) 
	{
		this.table = table;
	}
	
	public Object visit(Program program)
	{
		for (ICClass cls : program.getClasses())
		{
			cls.accept(this);
		}
		
		return null;
	}

	public Object visit(ICClass icClass) 
	{
		for (Field field : icClass.getFields())
		{
			field.accept(this);
			field.semType = field.getType();
		}
		
		for (Method method : icClass.getMethods())
		{
			method.accept(this);
		}
		
		return null;
	}

	public Object visit(VirtualMethod method) 
	{
		for (Formal formal : method.getFormals())
		{
			formal.accept(this);
			formal.semType = formal.getType();
		}
		
		for (Statement stmt : method.getStatements())
		{
			stmt.accept(this);
		}
		
		return null;
	}

	public Object visit(StaticMethod method) 
	{
		inStatic = true;
		
		for (Formal formal : method.getFormals())
		{
			formal.accept(this);
			formal.semType = formal.getType();
		}
		
		for (Statement stmt : method.getStatements())
		{
			stmt.accept(this);
		}
		
		inStatic = false;
		
		return null;
	}

	public Object visit(PrimitiveType type) 
	{
		return type.semType = type;
	}

	public Object visit(Assignment assignment) 
	{
		Type refType = (Type) assignment.getVariable().accept(this);
		Type exprType = (Type) assignment.getAssignment().accept(this);
		
		if (!isSameType(refType, exprType))
		{
			throw new SemanticError("semantic error at line " + assignment.getLine() + ": " + 
		"cannot assign type " + printType(exprType) +  " to a variable of type " + printType(refType));
		}
		
		assignment.semType = refType;
		assignment.getVariable().semType = refType;
		assignment.getAssignment().semType = exprType;
		
		return refType;
	}
	
	String printType(Type type)
	{
		if (type.getName().equals("int"))
		{
			return "integer";
		}
		
		return type.getName();
	}
	
	private boolean isSameType(Type a, Type b) 
	{
		if (b == null)
		{
			if (a == null)
			{
				return true;
			}
			else if (!a.IsPimitive() || a.IsIntegerOrBoolean() || ((PrimitiveType)a).getType() != DataTypes.VOID)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		if (b.IsPimitive())
		{
			if (((PrimitiveType)b).getName() == null)
			{
				return true;
			}
		}	
		if (a.getName().equals(b.getName()))
		{
			return a.getDimension() == b.getDimension();
		}
		if (!(a instanceof Type) || !(b instanceof Type))
		{
			return false;
		}
		if (a.getDimension() == b.getDimension() && table.isDerived(b.getName(), a.getName()))
		{
			return true;
		}
		return false;
	}

	public Object visit(Call callStatement) 
	{
		if (callStatement.getArguments() == null)
			return null;
		
		for (Expression arg : callStatement.getArguments())
		{
			arg.accept(this);
		}
		
		return null;
	}

	public Object visit(Return returnStatement) 
	{
		MethodScope method = table.getEnclosingMethod(returnStatement.scope);
		Type retType = method.getRetType();
		String retValueTypeName = retType.getName();
		
		if (!returnStatement.hasValue())
		{
			if (!retValueTypeName.equals("void"))
			{
				throw new SemanticError("semantic error at line " + returnStatement.getLine() + ": " + 
			printType(retType) + "is not the type of the return statment");
			}
			return null;
		}
		
		returnStatement.getValue().scope = returnStatement.scope;
		Type expType = (Type) returnStatement.getValue().accept(this);
		
		if (expType == null && !retType.IsIntegerOrBoolean())
		{
			return null;
		}
		
		else if (expType == null && retType.IsIntegerOrBoolean())
		{
			throw new SemanticError("semantic error at line " + returnStatement.getLine() + ": " + 
		printType(retType) + "is not the type of the return statment");
		}
		
		if (!method.getRetType().IsPimitive() && !expType.IsPimitive())
		{
			if (!(table.isDerived(expType.getName(), retValueTypeName)))
			{
				throw new SemanticError("semantic error at line " + returnStatement.getLine() + ": " + 
			printType(retType) + "is not the type of the return statment");
			}
		}
		
		if (!retValueTypeName.equals(expType.getName()))
		{
			throw new SemanticError("semantic error at line " + returnStatement.getLine() + ": " + 
		printType(retType) + "is not the type of the return statment");
		}
		
		returnStatement.getValue().semType = retType;
		
		return retType;
	}

	public Object visit(If ifStatement) 
	{
		Type cond = (Type) ifStatement.getCondition().accept(this);
		
		if (!cond.IsBoolean())
		{
			throw new SemanticError("semantic error at line " + ifStatement.getLine() + ": " + 
		"if statment has no condition of type boolean");
		}
		
		ifStatement.getCondition().semType = cond;
		ifStatement.getOperation().accept(this);
		
		if (ifStatement.hasElse())
		{
			ifStatement.getElseOperation().accept(this);
		}
		
		return null;
	}

	public Object visit(While whileStatement) 
	{
		Type cond = (Type) whileStatement.getCondition().accept(this);
	
		if (!cond.getName().equals("boolean"))
		{
			throw new SemanticError("semantic error at line " + whileStatement.getLine() + ": " + 
		"while statment has no condition of type boolean");
		}
		
		whileStatement.getCondition().semType = cond;
		
		whileStatement.getOperation().accept(this); 
		
		return null;
	}

	public Object visit(StatementsBlock statementsBlock) 
	{
		for (Statement stmt : statementsBlock.getStatements())
		{
			stmt.accept(this);
		}
		
		return null;
	}

	public Object visit(LocalVariable localVariable) 
	{
		localVariable.getType().semType = localVariable.getType();
		
		if (localVariable.hasInitValue())
		{
			Type initType = (Type)localVariable.getInitValue().accept(this);
			
			if (!isSameType(initType, localVariable.getType()))
			{
				throw new SemanticError("semantic error at line " + localVariable.getLine() + ": " + 
			"init value of local variable is not compatible");
			}
			
			localVariable.getInitValue().semType = initType;
		}

		return localVariable.semType = localVariable.getType();
	}

	public Object visit(VariableLocation location) 
	{
		Scope varScope = table.getSymbol(location.scope, location.getName());
		if (varScope == null)
		{
			throw new SemanticError("semantic error at line " + location.getLine() + ": " + 
		location.getName() + "is not in symbol table");
		}

		if (!location.isExternal() && inStatic && varScope instanceof FieldScope)
		{
			throw new SemanticError("semantic error at line " + location.getLine() + ": " + 
		"field cannot be used inside static method");
		}
		
		location.semType = ((PrimitiveScope)varScope).getType();
		
		if (location.getLocation() != null)
		{
			location.getLocation().accept(this);
		}
		
		return location.semType;
	}

	public Object visit(Field location) 
	{
		Type objType = (Type) location.getType();
		Scope objTypeScope = table.root;
		if (!objType.IsPimitive())
		{
			objTypeScope = table.getSymbol(location.scope, objType.getName());
			if (objTypeScope == null)
				throw new SemanticError("semantic error at line " + location.getLine() + ": " + 
			"field cannot be used inside static method");		
		}
		
	 	String fieldName = location.getName();
		
		if (inStatic)
		{
			if (table.getEnclosingClass(location.scope).getName().equals(objTypeScope.getName()))
			{
				throw new SemanticError("semantic error at line " + location.getLine() + ": " + 
			"enclosing type unknown");
			}
		}
		
		ClassScope objScope = table.getEnclosingClass(location.scope);
		Scope fieldScope = table.getSymbol(objScope, fieldName);
		
		return location.semType =  ((PrimitiveScope)fieldScope).getType();
	}

	public Object visit(ArrayLocation location) 
	{
		Type arrType = (Type) location.getArray().accept(this);
		Type indexType = (Type) location.getIndex().accept(this);
		
		if (!indexType.getName().equals("int"))
		{
			throw new SemanticError("semantic error at line " + location.getLine() + ": " + 
		"index type is different from int");
		}
		
		Type copied = null;
		if (arrType instanceof PrimitiveType)
		{
			copied = new PrimitiveType(arrType.getLine(), ((PrimitiveType) arrType).getType());
		}
		else if (arrType instanceof Type)
		{
			copied = new Type(arrType.getLine(), ((Type)arrType).getName());
		}
		
		for (int i=0; i<arrType.getDimension() - 1;i++)
		{
			copied.incrementDimension();
		}
		
		return location.semType = copied;
	}

	public Object visit(StaticCall call) 
	{
		String methodName = call.getName();
		String clsName = call.getClassName();
		Scope clsScope = table.getSymbol(call.scope, clsName);
		
		if (clsScope == null)
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"class " + clsName + " does not exist");
		}
		
		MethodScope methodScope = (MethodScope) table.getSymbol(clsScope, methodName);
		if (methodScope == null)
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"method " + methodName + " does not exist");
		}
		
		if (call.getArguments().size() != methodScope.params.size())
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"method " + methodName + "has wrong number of arguments");
		}
		
		if (!isSameSignature(call, methodScope))
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"method " + methodName + " can not be called with these arguments");
			
		}
		
		return call.semType = methodScope.getRetType();
	}
	
	public Object visit(VirtualCall call) 
	{
		String methodName = call.getName();
		
		String clsName = null;
		Expression location = call.getLocation();
		ClassScope clsScope = null;
		if (location == null)
		{
			clsScope = table.getEnclosingClass(call.scope);
			if (clsScope != null)
				clsName = clsScope.icClass.getName();
		}
		else
		{
			clsName = ((Type)location.accept(this)).getName();
			clsScope = (ClassScope)table.getSymbol(call.scope, clsName);
		}
		
		if (clsScope == null)
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"class " + clsName + " does not exist");
		}
		
		MethodScope methodScope = (MethodScope) table.getSymbol(clsScope, methodName);
		if (methodScope == null)
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"method " + methodName + " does not exist");
		}
		
		if (call.getArguments().size() != methodScope.params.size())
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"method " + methodName + "has wrong number of arguments");
		}
		
		if (!isSameSignature(call, methodScope))
		{
			throw new SemanticError("semantic error at line " + call.getLine() + ": " + 
		"method " + methodName + " can not be called with these arguments");
		}
		
		return call.semType = methodScope.getRetType();
	}
	
	public boolean isSameSignature(Call call, MethodScope scope)
	{
		int params = call.getArguments().size();
		List<Expression> callArgs = call.getArguments();
		List<Scope> methodArgs = scope.params;
		
		for (int i=0; i < params; i++)
		{
			callArgs.get(i).scope = call.scope;
			Type argType = (Type) callArgs.get(i).accept(this);
			Type paramType = ((PrimitiveScope)methodArgs.get(i)).getType();
			
			if (!isSameType(paramType, argType))
			{
				return false;
			}
		}
		return true;
	}
	
	public Object visit(This thisExpression) 
	{
		if (inStatic)
		{
			throw new SemanticError("semantic error at line " + thisExpression.getLine() + ": " + 
		"'this' expression can not be used inside static method");
		}
		
		ClassScope cls = table.getEnclosingClass(thisExpression.scope);
		return thisExpression.semType = new Type(cls.getLine(), cls.getName());
	}

	public Object visit(NewClass newClass)
	{
		return newClass.semType = new Type(newClass.getLine(), newClass.getName());
	}

	public Object visit(NewArray newArray) 
	{
		newArray.getSize().accept(this);
		if (newArray.getSize().semType.getName().equals("int"))
		{
			Type arr = newArray.getType();
			arr.incrementDimension();
			return newArray.semType = arr;
		}
		else
		{
			throw new SemanticError("semantic error at line " + newArray.getLine() + ": " + 
					"size expression is not of type int");
		}
	}

	public Object visit(Length length) 
	{
		length.getArray().accept(this);
		
		return length.semType = new PrimitiveType(length.getLine(), DataTypes.INT);
	}

	public Object visit(Literal literal) 
	{
		switch(literal.getType()) 
		{
		case INTEGER:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.INT);
		case STRING:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.STRING);
		case NULL:
			return literal.semType = null;
		case TRUE:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.BOOLEAN);
		case FALSE:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.BOOLEAN);
		}
		
		return null;
	}

	public Object visit(UnaryOp unaryOp) 
	{
		unaryOp.getOperand().scope = unaryOp.scope;
		unaryOp.getOperand().accept(this);
		Type operand = unaryOp.getOperand().semType;
		UnaryOps op = unaryOp.getOperator();
		
		switch(op)
		{
			case UMINUS:
				if (operand.getName().equals("int"))
				{
					return unaryOp.semType = new PrimitiveType(operand.getLine(),DataTypes.INT);
				}
				break;	
			case LNEG:
				if (operand.getName().equals("boolean"))
				{
					return unaryOp.semType = new PrimitiveType(operand.getLine(),DataTypes.BOOLEAN);
				}
				break;
		}
		return null;
	}

	public Object visit(BinaryOp binaryOp) 
	{
		binaryOp.getFirstOperand().scope = binaryOp.scope;
		binaryOp.getSecondOperand().scope = binaryOp.scope;
		binaryOp.getFirstOperand().accept(this);
		binaryOp.getSecondOperand().accept(this);

		Type temp = null;
		Type a = binaryOp.getFirstOperand().semType;
		Type b = binaryOp.getSecondOperand().semType;
		BinaryOps op = binaryOp.getOperator();
		
		switch(op)
		{
			case PLUS:
				temp = add(a,b, binaryOp);
				if (temp == null)
				{
					throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
				"wrong arithmatic binary operation (" + op + ")");
				}
				return binaryOp.semType = temp;
			case MINUS:
			case MULTIPLY:
			case DIVIDE:
			case MOD:
				temp = arith(a,b);
				if (temp == null)
				{
					throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
				"wrong arithmatic binary operation (" + op + ")");
				}
				return binaryOp.semType = temp;
			case LTE:
			case GTE:
			case GT:
			case LT:
				temp = comp(a,b);
				if (temp == null)
				{
					throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
				"wrong logical binary operation (" + op + ")");				
				}
				return binaryOp.semType = temp;
			case EQUAL:
			case NEQUAL:
				temp = eq(a,b);
				if (temp == null)
				{
					throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
				"wrong logical binary operation (" + op + ")");	
				}
				return binaryOp.semType = temp;
			case LOR:
			case LAND:
				temp = logic(a,b);
				if (temp == null)
				{
					throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
				"wrong logical binary operation (" + op + ")");	
				}
				return binaryOp.semType = temp;
			default:
				throw new SemanticError("Bad op");
		}
	}

	private Type logic(Type a, Type b)
	{
		if (a.getName().equals(b.getName()) && a.getName().equals("boolean"))
		{
			return new PrimitiveType(a.getLine(),DataTypes.BOOLEAN);
		}
		
		return null;
	}

	private Type eq(Type a, Type b) 
	{
		if (isSameType(a,b))
		{
			return new PrimitiveType(a.getLine(),DataTypes.BOOLEAN);
		}
		return null;
	}

	private Type comp(Type a, Type b)
	{
		if (a.getName().equals(b.getName()) && a.getName().equals("int"))
		{
			return new PrimitiveType(a.getLine(), DataTypes.BOOLEAN);
		}
		return null;
	}

	private Type arith(Type a, Type b) 
	{
		if (a.getName().equals(b.getName()) && a.getName().equals("int"))
		{
			return new PrimitiveType(a.getLine(),DataTypes.INT);
		}
		return null;
	}

	private Type add(Type a, Type b, BinaryOp binaryOp) 
	{
		if (a instanceof PrimitiveType && b instanceof PrimitiveType)
		{
			if (a.getName().equals(b.getName()))
			{
				switch (a.getName())
				{
				case "int":
					return new PrimitiveType(a.getLine(),DataTypes.INT);
				case "string":
					return new PrimitiveType(a.getLine(),DataTypes.STRING);
				default:
					throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
				"add operation cannot be used");
				}
			}	
		}
		return null;
			
	}

	public Object visit(LibraryMethod method) 
	{
		return null;
	}

	public Object visit(Formal formal) 
	{
		return formal.semType = formal.getType();
	}

	public Object visit(Type type) 
	{
		return type.semType = type;
	}

	public Object visit(CallStatement callStatement) 
	{
		Type type = (Type)callStatement.getCall().accept(this);
		return callStatement.semType = type;
	}

	public Object visit(Break breakStatement)
	{
		return null;
	}

	public Object visit(Continue continueStatement) 
	{
		return null;
	}

	public Object visit(MathBinaryOp binaryOp) 
	{
		Type firstOperandtype = (Type)binaryOp.getFirstOperand().accept(this);
		Type secondOperandtype = (Type)binaryOp.getSecondOperand().accept(this);
		
		if (!firstOperandtype.IsInteger() ||
			!secondOperandtype.IsInteger())
		{
			throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " + 
		"wrong logical binary operation");
		}
		
		return new PrimitiveType(binaryOp.getLine(), DataTypes.INT);
	}

	public Object visit(LogicalBinaryOp binaryOp) 
	{
		Type firstOpType = (Type)binaryOp.getFirstOperand().accept(this);
		Type secondOpType = (Type)binaryOp.getSecondOperand().accept(this);
		
		if (!isSameType(firstOpType, secondOpType) && (firstOpType != null || !firstOpType.IsIntegerOrBoolean()))
		{
			throw new SemanticError("semantic error at line " + binaryOp.getLine() + ": " +
		"wrong logical binary operation");
		}
		
		return new PrimitiveType(binaryOp.getLine(), DataTypes.BOOLEAN);
	}

	public Object visit(MathUnaryOp unaryOp) 
	{
		Type op = (Type)unaryOp.getOperand().accept(this);
		if (!op.getName().equals("INT"))
		{
			throw new SemanticError("semantic error at line " + unaryOp.getLine() + ": " +
		"math operation can not be used on expression of type " + op.getName());
		}
		
		return op;
	}

	public Object visit(LogicalUnaryOp unaryOp) 
	{
		Type op = (Type)unaryOp.getOperand().accept(this);
		if (!op.getName().equals("BOOLEAN"))
		{
			throw new SemanticError("semantic error at line " + unaryOp.getLine() + ": " +
		"math operation can not be used on expression of type " + op.getName());
		}
		
		return op;
	}

	public Object visit(ExpressionBlock expressionBlock) 
	{
		return expressionBlock.getExpression().accept(this);
	}
}
