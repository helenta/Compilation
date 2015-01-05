package IC.SemanticChecks;

import java.util.List;

import IC.AST.*;
import IC.SymbolTables.ClassScope;
import IC.SymbolTables.FieldScope;
import IC.SymbolTables.MethodScope;
import IC.SymbolTables.PrimitiveScope;
import IC.SymbolTables.Scope;
import IC.SymbolTables.SymbolTable;

public class TypeCheck implements Visitor
{
	private SymbolTable	table;
	private boolean	    insideStatic	= false;

	public TypeCheck(SymbolTable table)
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
			stmt.accept(this);

		return null;
	}

	public Object visit(StaticMethod method)
	{
		insideStatic = true;

		for (Formal formal : method.getFormals())
		{
			formal.accept(this);
			formal.semType = formal.getType();
		}

		for (Statement stmt : method.getStatements())
			stmt.accept(this);

		insideStatic = false;
		return null;
	}

	public Object visit(PrimitiveType type)
	{
		return type.semType = type;
	}

	public Object visit(Assignment assignment)
	{
		Type referenceType = (Type) assignment.getVariable().accept(this);
		Type expressionType = (Type) assignment.getAssignment().accept(this);

		if (!isSameType(referenceType, expressionType))
			throw new SemanticError("semantic error at line " + assignment.getLine()
			    + ": " + "cannot assign type " + expressionType.getName()
			    + " to a variable of type " + referenceType.getName());

		assignment.semType = referenceType;
		assignment.getVariable().semType = referenceType;
		assignment.getAssignment().semType = expressionType;

		return referenceType;
	}

	private boolean isSameType(Type a, Type b)
	{
		if (b == null)
		{
			if (a == null)
				return true;
			else
				if (!a.IsPimitive() || a.IsIntegerOrBoolean()
				    || ((PrimitiveType) a).getType() != DataTypes.VOID)
					return true;
				else
					return false;
		}
		if (b.IsPimitive())
		{
			if (((PrimitiveType) b).getName() == null)
				return true;
		}
		if (a.getName().equals(b.getName()))
			return a.getDimension() == b.getDimension();
		if (!(a instanceof Type) || !(b instanceof Type))
			return false;
		if (a.getDimension() == b.getDimension()
		    && table.isDerivedName(b.getName(), a.getName()))
			return true;
		return false;
	}

	public Object visit(Call callStatement)
	{
		if (callStatement.getArguments() == null)
			return null;

		for (Expression arg : callStatement.getArguments())
			arg.accept(this);
		return null;
	}

	public Object visit(Return returnStatement)
	{
		MethodScope method = table.getEnclosingMethod(returnStatement.scope);
		Type returnType = method.getReturnType();
		String returnValueTypeName = returnType.getName();

		if (!returnStatement.hasValue())
		{
			if (!returnValueTypeName.equals("void"))
				throw new SemanticError("semantic error at line "
				    + returnStatement.getLine() + ": " + returnType.getName()
				    + "is not the type of the return statment");
			
			returnStatement.semType = new PrimitiveType(returnStatement.getLine(), DataTypes.VOID);

			return null;
		}

		returnStatement.getValue().scope = returnStatement.scope;
		Type expressionType = (Type) returnStatement.getValue().accept(this);

		if (expressionType == null && !returnType.IsIntegerOrBoolean())
			return null;

		else
			if (expressionType == null && returnType.IsIntegerOrBoolean())
				throw new SemanticError("semantic error at line "
				    + returnStatement.getLine() + ": " + returnType.getName()
				    + "is not the type of the return statment");

		if (!method.getReturnType().IsPimitive() && !expressionType.IsPimitive())
		{
			if (!(table.isDerivedName(expressionType.getName(), returnValueTypeName)))
				throw new SemanticError("semantic error at line "
				    + returnStatement.getLine() + ": " + returnType.getName()
				    + "is not the type of the return statment");
		}

		if (!returnValueTypeName.equals(expressionType.getName()))
			throw new SemanticError("semantic error at line "
			    + returnStatement.getLine() + ": " + returnType.getName()
			    + "is not the type of the return statment");

		returnStatement.getValue().semType = returnType;

		return returnType;
	}

	public Object visit(If ifStatement)
	{
		Type ifType = (Type) ifStatement.getCondition().accept(this);

		if (!ifType.IsBoolean())
			throw new SemanticError("semantic error at line " + ifStatement.getLine()
			    + ": " + "if statment has no condition of type boolean");

		ifStatement.getCondition().semType = ifType;
		ifStatement.getOperation().accept(this);

		if (ifStatement.hasElse())
		{
			ifStatement.getElseOperation().accept(this);
		}

		return null;
	}

	public Object visit(While whileStatement)
	{
		Type whileType = (Type) whileStatement.getCondition().accept(this);

		if (!whileType.getName().equals("boolean"))
			throw new SemanticError("semantic error at line "
			    + whileStatement.getLine() + ": "
			    + "while statment has no condition of type boolean");

		whileStatement.getCondition().semType = whileType;

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
			Type initialType = (Type) localVariable.getInitValue().accept(this);

			if (!isSameType(initialType, localVariable.getType()))
			{
				throw new SemanticError("semantic error at line "
				    + localVariable.getLine() + ": "
				    + "init value of local variable is not compatible");
			}

			localVariable.getInitValue().semType = initialType;
		}

		return localVariable.semType = localVariable.getType();
	}

	public Object visit(VariableLocation location)
	{
		Scope varScope = null;
		
		Expression innerExpression = location.getLocation();
		if (innerExpression != null)
		{
			innerExpression.accept(this);
			
			varScope = table.GetClassScopeByName(innerExpression.semType.getName());
		}
		else
		{
			varScope = table.getSymbol(location.scope, location.getName());
		}
		
		if (varScope == null)
			throw new SemanticError("semantic error at line " + location.getLine()
			    + ": " + location.getName() + " is not in symbol table");

		if (!location.isExternal() && insideStatic
		    && varScope instanceof FieldScope)
			throw new SemanticError("semantic error at line " + location.getLine()
			    + ": " + "field cannot be used inside static method");

		if (varScope instanceof PrimitiveScope)
		{
			location.semType = ((PrimitiveScope) varScope).getType();
		}
		else
		{
			ICClass icClass = ((ClassScope)varScope).icClass;
			Field field = icClass.GetFieldByName(location.getName());
			if (field == null)
			{
				throw new SemanticError("semantic error at line " + location.getLine()
				    + ": " + location.getName() + " is not in symbol table");
			}
			
			location.semType = field.getType();
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
				throw new SemanticError("semantic error at line " + location.getLine()
				    + ": " + "field cannot be used inside static method");
		}

		String fieldName = location.getName();

		if (insideStatic)
			if (table.getEnclosingClass(location.scope).getName()
			    .equals(objTypeScope.getName()))
				throw new SemanticError("semantic error at line " + location.getLine()
				    + ": " + "enclosing type unknown");

		ClassScope objScope = table.getEnclosingClass(location.scope);
		Scope fieldScope = table.getSymbol(objScope, fieldName);

		return location.semType = ((PrimitiveScope) fieldScope).getType();
	}

	public Object visit(ArrayLocation location)
	{
		Type arrayType = (Type) location.getArray().accept(this);
		Type indexType = (Type) location.getIndex().accept(this);

		if (!indexType.getName().equals("int"))
			throw new SemanticError("semantic error at line " + location.getLine()
			    + ": " + "index type is different from int");

		Type tempType = null;

		if (arrayType instanceof PrimitiveType)
		{
			tempType = new PrimitiveType(arrayType.getLine(),
			    ((PrimitiveType) arrayType).getType());
		}
		else
			if (arrayType instanceof Type)
			{
				tempType = new Type(arrayType.getLine(), ((Type) arrayType).getName());
			}

		for (int i = 0; i < arrayType.getDimension() - 1; i++)
		{
			tempType.incrementDimension();
		}

		return location.semType = tempType;
	}

	public Object visit(StaticCall call)
	{
		String methodName = call.getName();
		String className = call.getClassName();
		Scope classScope = table.getSymbol(call.scope, className);

		if (classScope == null)
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "class " + className + " does not exist");

		MethodScope methodScope = (MethodScope) table.getSymbol(classScope,
		    methodName);

		if (methodScope == null)
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "method " + methodName + " does not exist");

		if (call.getArguments().size() != methodScope.params.size())
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "method " + methodName + "has wrong number of arguments");

		if (!isSameSignature(call, methodScope))
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "method " + methodName + " can not be called with these arguments");

		return call.semType = methodScope.getReturnType();
	}

	public Object visit(VirtualCall call)
	{
		String methodName = call.getName();

		String className = null;
		Expression location = call.getLocation();
		ClassScope classScope = null;

		if (location == null)
		{
			classScope = table.getEnclosingClass(call.scope);
			if (classScope != null)
				className = classScope.icClass.getName();
		}
		else
		{
			className = ((Type) location.accept(this)).getName();
			classScope = (ClassScope) table.getSymbol(call.scope, className);
		}

		if (classScope == null)
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "class " + className + " does not exist");

		MethodScope methodScope = (MethodScope) table.getSymbol(classScope,
		    methodName);
		if (methodScope == null)
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "method " + methodName + " does not exist");

		if (call.getArguments().size() != methodScope.params.size())
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "method " + methodName + "has wrong number of arguments");

		if (!isSameSignature(call, methodScope))
			throw new SemanticError("semantic error at line " + call.getLine() + ": "
			    + "method " + methodName + " can not be called with these arguments");

		return call.semType = methodScope.getReturnType();
	}

	public boolean isSameSignature(Call call, MethodScope scope)
	{
		int arguments = call.getArguments().size();
		List<Expression> callArguments = call.getArguments();
		List<Scope> methodArgs = scope.params;

		for (int i = 0; i < arguments; i++)
		{
			callArguments.get(i).scope = call.scope;
			Type argumentType = (Type) callArguments.get(i).accept(this);
			Type paramType = ((PrimitiveScope) methodArgs.get(i)).getType();

			if (!isSameType(paramType, argumentType))
				return false;
		}
		return true;
	}

	public Object visit(This thisExpression)
	{
		if (insideStatic)
			throw new SemanticError("semantic error at line "
			    + thisExpression.getLine() + ": "
			    + "'this' expression can not be used inside static method");

		ClassScope classScope = table.getEnclosingClass(thisExpression.scope);
		return thisExpression.semType = new Type(classScope.getLine(),
		    classScope.getName());
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
			Type arrayType = newArray.getType();
			arrayType.incrementDimension();
			return newArray.semType = arrayType;
		}
		else
			throw new SemanticError("semantic error at line " + newArray.getLine()
			    + ": " + "size expression is not of type int");
	}

	public Object visit(Length length)
	{
		length.getArray().accept(this);

		return length.semType = new PrimitiveType(length.getLine(), DataTypes.INT);
	}

	public Object visit(Literal literal)
	{
		switch (literal.getType())
		{
			case INTEGER:
				return literal.semType = new PrimitiveType(literal.getLine(),
				    DataTypes.INT);
			case STRING:
				return literal.semType = new PrimitiveType(literal.getLine(),
				    DataTypes.STRING);
			case NULL:
				return literal.semType = null;
			case TRUE:
				return literal.semType = new PrimitiveType(literal.getLine(),
				    DataTypes.BOOLEAN);
			case FALSE:
				return literal.semType = new PrimitiveType(literal.getLine(),
				    DataTypes.BOOLEAN);
		}

		return null;
	}

	public Object visit(UnaryOp unaryOp)
	{
		unaryOp.getOperand().scope = unaryOp.scope;
		unaryOp.getOperand().accept(this);
		Type operand = unaryOp.getOperand().semType;
		UnaryOps op = unaryOp.getOperator();

		switch (op)
		{
			case UMINUS:
				if (operand.getName().equals("int"))
					return unaryOp.semType = new PrimitiveType(operand.getLine(),
					    DataTypes.INT);
				break;
			case LNEG:
				if (operand.getName().equals("boolean"))
					return unaryOp.semType = new PrimitiveType(operand.getLine(),
					    DataTypes.BOOLEAN);
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
		Type firstOperandType = binaryOp.getFirstOperand().semType;
		Type secondOperandType = binaryOp.getSecondOperand().semType;
		BinaryOps op = binaryOp.getOperator();

		switch (op)
		{
			case PLUS:
				temp = add(firstOperandType, secondOperandType, binaryOp);
				if (temp == null)
					throw new SemanticError("semantic error at line "
					    + binaryOp.getLine() + ": "
					    + "wrong arithmatic binary operation (" + op + ")");
				return binaryOp.semType = temp;
			case MINUS:
			case MULTIPLY:
			case DIVIDE:
			case MOD:
				temp = math(firstOperandType, secondOperandType);
				if (temp == null)
					throw new SemanticError("semantic error at line "
					    + binaryOp.getLine() + ": "
					    + "wrong arithmatic binary operation (" + op + ")");
				return binaryOp.semType = temp;
			case LTE:
			case GTE:
			case GT:
			case LT:
				temp = compare(firstOperandType, secondOperandType);
				if (temp == null)
					throw new SemanticError("semantic error at line "
					    + binaryOp.getLine() + ": " + "wrong logical binary operation ("
					    + op + ")");
				return binaryOp.semType = temp;
			case EQUAL:
			case NEQUAL:
				temp = equal(firstOperandType, secondOperandType);
				if (temp == null)
					throw new SemanticError("semantic error at line "
					    + binaryOp.getLine() + ": " + "wrong logical binary operation ("
					    + op + ")");
				return binaryOp.semType = temp;
			case LOR:
			case LAND:
				temp = logicalAnd(firstOperandType, secondOperandType);
				if (temp == null)
					throw new SemanticError("semantic error at line "
					    + binaryOp.getLine() + ": " + "wrong logical binary operation ("
					    + op + ")");
				return binaryOp.semType = temp;
			default:
				throw new SemanticError("Bad op");
		}
	}

	private Type logicalAnd(Type firstType, Type secondType)
	{
		if (firstType.getName().equals(secondType.getName())
		    && firstType.getName().equals("boolean"))
			return new PrimitiveType(firstType.getLine(), DataTypes.BOOLEAN);

		return null;
	}

	private Type equal(Type firstType, Type secondType)
	{
		if (isSameType(firstType, secondType))
		{
			return new PrimitiveType(firstType.getLine(), DataTypes.BOOLEAN);
		}
		return null;
	}

	private Type compare(Type firstType, Type secondType)
	{
		if (firstType.getName().equals(secondType.getName())
		    && firstType.getName().equals("int"))
		{
			return new PrimitiveType(firstType.getLine(), DataTypes.BOOLEAN);
		}
		return null;
	}

	private Type math(Type firstType, Type secondType)
	{
		if (firstType.getName().equals(secondType.getName())
		    && firstType.getName().equals("int"))
		{
			return new PrimitiveType(firstType.getLine(), DataTypes.INT);
		}
		return null;
	}

	private Type add(Type firstType, Type secondType, BinaryOp binaryOp)
	{
		if (firstType instanceof PrimitiveType
		    && secondType instanceof PrimitiveType)
		{
			if (firstType.getName().equals(secondType.getName()))
			{
				switch (firstType.getName())
				{
					case "int":
						return new PrimitiveType(firstType.getLine(), DataTypes.INT);
					case "string":
						return new PrimitiveType(firstType.getLine(), DataTypes.STRING);
					default:
						throw new SemanticError("semantic error at line "
						    + binaryOp.getLine() + ": " + "add operation cannot be used");
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
		Type type = (Type) callStatement.getCall().accept(this);
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
		Type firstOperandtype = (Type) binaryOp.getFirstOperand().accept(this);
		Type secondOperandtype = (Type) binaryOp.getSecondOperand().accept(this);

		if (!firstOperandtype.IsInteger() || !secondOperandtype.IsInteger())
			throw new SemanticError("semantic error at line " + binaryOp.getLine()
			    + ": " + "wrong logical binary operation");

		return new PrimitiveType(binaryOp.getLine(), DataTypes.INT);
	}

	public Object visit(LogicalBinaryOp binaryOp)
	{
		Type firstOpType = (Type) binaryOp.getFirstOperand().accept(this);
		Type secondOpType = (Type) binaryOp.getSecondOperand().accept(this);

		if (!isSameType(firstOpType, secondOpType)
		    && (firstOpType != null || !firstOpType.IsIntegerOrBoolean()))
			throw new SemanticError("semantic error at line " + binaryOp.getLine()
			    + ": " + "wrong logical binary operation");

		return new PrimitiveType(binaryOp.getLine(), DataTypes.BOOLEAN);
	}

	public Object visit(MathUnaryOp unaryOp)
	{
		Type op = (Type) unaryOp.getOperand().accept(this);
		if (!op.IsInteger())
			throw new SemanticError("semantic error at line " + unaryOp.getLine()
			    + ": " + "math operation can not be used on expression of type "
			    + op.getName());

		return op;
	}

	public Object visit(LogicalUnaryOp unaryOp)
	{
		Type op = (Type) unaryOp.getOperand().accept(this);
		if (!op.IsBoolean())
			throw new SemanticError("semantic error at line " + unaryOp.getLine()
			    + ": " + "math operation can not be used on expression of type "
			    + op.getName());

		return op;
	}

	public Object visit(ExpressionBlock expressionBlock)
	{
		Object value = expressionBlock.getExpression().accept(this);
		expressionBlock.semType =  expressionBlock.getExpression().semType;
		
		return value;
	}
}
