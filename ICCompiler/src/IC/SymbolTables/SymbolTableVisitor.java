package IC.SymbolTables;

import IC.AST.*;
import IC.SemanticChecks.SemanticError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SymbolTableVisitor implements Visitor
{
	private boolean	insideWhile	= false;

	public Object visit(Program program)
	{
		SymbolTable symbolTable = new SymbolTable();
		for (ICClass icClass : program.getClasses())
		{
			ClassScope classScope = (ClassScope) icClass.accept(this);
			symbolTable.root.addSymbol(classScope, symbolTable.root.classScopes);
			if (!icClass.hasSuperClass())
				icClass.scope = symbolTable.root;
			else
			{
				if (!symbolTable.root.hasSymbol(icClass.getSuperClassName())
				    || icClass.getName().equals(icClass.getSuperClassName()))
				{
					throw new SemanticError("semantic error at line " + icClass.getLine()
					    + ": " + "class " + icClass.getSuperClassName()
					    + "is not defined when trying to extend it by class "
					    + icClass.getName());
				}
				ClassScope parent = (ClassScope) symbolTable.root.getSymbol(icClass
				    .getSuperClassName());
				icClass.scope = parent;
				parent.addDerived(classScope, icClass.getLine());
			}
			
			symbolTable.AddType(icClass.getName(), icClass);
		}

		return symbolTable;
	}

	public Object visit(ICClass icClass)
	{
		ClassScope classScope = new ClassScope(icClass.getName(),
		    icClass.getLine(), icClass);

		for (Field field : icClass.getFields())
		{
			field.scope = classScope;
			classScope.addSymbol((FieldScope) field.accept(this), classScope.fields);
		}

		for (Method method : icClass.getMethods())
		{
			method.scope = classScope;
			MethodScope methodScope = (MethodScope) method.accept(this);
			if (method instanceof StaticMethod || method instanceof LibraryMethod)
			{
				classScope.addSymbol(methodScope, classScope.methods);
			}
			else
			{
				methodScope.setVirtual(true);
				classScope.addSymbol(methodScope, classScope.methods);
			}
		}
		return classScope;
	}

	public Object visit(Field field)
	{
		FieldScope fieldScope = new FieldScope(field.scope, field.getName(),
		    field.getType(), field.getLine());
		fieldScope.setParent(field.scope);
		field.scope = fieldScope;
		return fieldScope;
	}

	public Object visit(VirtualMethod method)
	{
		return makeMethodScope(method);
	}

	public Object visit(StaticMethod method)
	{
		return makeMethodScope(method);
	}

	private Object makeMethodScope(Method method)
	{
		MethodScope methodScope = new MethodScope(method.getName(),
		    method.getType(), method.getLine(), false, method);
		methodScope.setParent(method.scope);
		method.scope = methodScope;

		for (Formal form : method.getFormals())
		{
			form.scope = methodScope;
			FormalScope formal = (FormalScope) form.accept(this);
			methodScope.addSymbol(formal, methodScope.params);
		}

		for (Statement statement : method.getStatements())
		{
			statement.scope = methodScope;

			if (statement instanceof LocalVariable)
			{
				LocalScope local = (LocalScope) statement.accept(this);
				methodScope.addSymbol(local, methodScope.locals);
			}
			else
				if (statement instanceof StatementsBlock)
				{
					BlockScope blockScope = (BlockScope) statement.accept(this);
					methodScope.addAnonymousScope(blockScope, methodScope.blocks);
				}
				else
					if (statement instanceof While)
					{
						Object w = statement.accept(this);
						if (w instanceof LocalScope)
							methodScope.addSymbol((LocalScope) w, methodScope.locals);
						if (w instanceof BlockScope)
							methodScope.addAnonymousScope((BlockScope) w, methodScope.blocks);
					}
					else
						if (statement instanceof If)
						{
							List<Scope> blocks = (List<Scope>) statement.accept(this);
							for (Scope scope : blocks)
							{
								if (scope instanceof LocalScope)
									methodScope.addSymbol((LocalScope) scope, methodScope.locals);
								else
									if (scope instanceof BlockScope)
										methodScope.addAnonymousScope((BlockScope) scope,
										    methodScope.blocks);
							}
						}
						else
							statement.accept(this);
		}

		return methodScope;
	}

	public Object visit(LibraryMethod method)
	{
		return makeMethodScope(method);
	}

	public Object visit(Formal formal)
	{
		FormalScope formalScope = new FormalScope(formal.getName(),
		    formal.getType(), formal.getLine());
		formalScope.setParent(formal.scope);
		formal.scope = formalScope;

		return formalScope;
	}

	public Object visit(If ifStatement)
	{
		List<Scope> blocks = new ArrayList<Scope>();
		if (ifStatement.getOperation() instanceof If)
			blocks.addAll((Collection<? extends Scope>) ifStatement.getOperation()
			    .accept(this));
		else
			blocks.add((Scope) ifStatement.getOperation().accept(this));

		if (ifStatement.hasElse())
		{
			ifStatement.getElseOperation().scope = ifStatement.scope;
			if (ifStatement.getElseOperation() instanceof If)
				blocks.addAll((Collection<? extends Scope>) ifStatement
				    .getElseOperation().accept(this));
			else
				blocks.add((Scope) ifStatement.getElseOperation().accept(this));
		}

		ifStatement.getOperation().scope = ifStatement.scope;
		ifStatement.getCondition().scope = ifStatement.scope;
		ifStatement.getCondition().accept(this);

		return blocks;
	}

	public Object visit(While whileStatement)
	{
		boolean out = insideWhile;
		insideWhile = true;

		Scope whileScope = null;
		if (whileStatement.getOperation() instanceof StatementsBlock)
		{
			BlockScope blockScope = new BlockScope("while", whileStatement.getLine(),
			    (StatementsBlock) whileStatement.getOperation());
			whileScope = blockScope;
		}
		else
		{
			Statement statement = whileStatement.getOperation();
			LocalScope scope = new LocalScope("while single statement",
			    statement.getLine());
			whileScope = scope;
		}
		whileScope.setParent(whileStatement.scope);

		whileStatement.getCondition().scope = whileStatement.scope;
		whileStatement.getCondition().accept(this);

		whileStatement.getOperation().scope = whileStatement.scope;
		whileStatement.getOperation().accept(this);

		whileStatement.getOperation().scope = whileStatement.scope;
		if (!out)
		{
			insideWhile = false;
		}
		return whileScope;
	}

	public Object visit(Break breakStatement)
	{
		if (!insideWhile)
		{
			throw new SemanticError("semantic error at line "
			    + breakStatement.getLine() + ": "
			    + "break statement is being used outside of a loop");
		}
		return breakStatement.scope;
	}

	public Object visit(Continue continueStatement)
	{
		if (!insideWhile)
		{
			throw new SemanticError("semantic error at line "
			    + continueStatement.getLine() + ": "
			    + "break statement is being used outside of a loop");
		}
		return continueStatement.scope;
	}

	public Object visit(StatementsBlock statementsBlock)
	{
		BlockScope blockScope = new BlockScope(statementsBlock.getLine(),
		    statementsBlock);
		blockScope.setParent(statementsBlock.scope);

		for (Statement statement : statementsBlock.getStatements())
		{
			statement.scope = blockScope;
			if (statement instanceof LocalVariable)
			{
				LocalScope local = (LocalScope) statement.accept(this);
				blockScope.addSymbol(local, blockScope.locals);

				LocalVariable localVarible = (LocalVariable) statement;
				localVarible.getType().scope = blockScope;
				localVarible.getInitValue().scope = blockScope;
			}
			else
				if (statement instanceof StatementsBlock)
				{
					BlockScope subBlockScope = (BlockScope) statement.accept(this);
					blockScope.addAnonymousScope(subBlockScope, blockScope.blocks);
				}
				else
					if (statement instanceof While)
					{
						Object w = statement.accept(this);
						if (w instanceof LocalScope)
							blockScope.addSymbol((LocalScope) w, blockScope.locals);
						if (w instanceof BlockScope)
							blockScope.addAnonymousScope((BlockScope) w, blockScope.blocks);

						((While) statement).getCondition().scope = blockScope;
						((While) statement).getOperation().scope = blockScope;
					}
					else
						if (statement instanceof If)
						{
							List<Scope> blocks = (List<Scope>) statement.accept(this);
							for (Scope scope : blocks)
							{
								if (scope instanceof LocalScope)
									blockScope.addSymbol((LocalScope) scope, blockScope.locals);
								if (scope instanceof BlockScope)
									blockScope.addAnonymousScope((BlockScope) scope,
									    blockScope.blocks);
							}
						}
						else
						{
							statement.accept(this);
						}
		}
		return blockScope;
	}

	public Object visit(LocalVariable localVariable)
	{
		LocalScope localScope = new LocalScope(localVariable.getName(),
		    localVariable.getType(), localVariable.getLine());
		localScope.setParent(localVariable.scope);
		localVariable.scope = localScope;
		localScope.symbols.put(localVariable.getName(), localVariable.scope);

		localVariable.getType().scope = localScope;

		if (localVariable.getInitValue() != null)
		{
			localVariable.getInitValue().scope = localScope;
			localVariable.getInitValue().accept(this);
		}

		return localVariable.scope;
	}

	public Object visit(PrimitiveType type)
	{
		return null;
	}

	public Object visit(Type type)
	{
		return null;
	}

	public Object visit(Assignment assignment)
	{
		assignment.getVariable().scope = assignment.scope;
		assignment.getVariable().accept(this);

		if (assignment.getAssignment() != null)
		{
			assignment.getAssignment().scope = assignment.scope;
			assignment.getAssignment().accept(this);
		}

		return assignment.scope;
	}

	public Object visit(CallStatement callStatement)
	{
		callStatement.getCall().scope = callStatement.scope;
		callStatement.getCall().accept(this);

		return callStatement.scope;
	}

	public Object visit(Return returnStatement)
	{
		if (returnStatement.getValue() != null)
		{
			returnStatement.getValue().scope = returnStatement.scope;
			returnStatement.getValue().accept(this);
		}

		return returnStatement.scope;
	}

	public Object visit(VariableLocation location)
	{
		Expression locationExpression = location.getLocation();
		if (locationExpression != null)
		{
			locationExpression.scope = location.scope;
			locationExpression.accept(this);
		}

		return location.scope;
	}

	public Object visit(ArrayLocation location)
	{
		location.getArray().scope = location.scope;
		location.getArray().accept(this);

		location.getIndex().scope = location.scope;
		location.getIndex().accept(this);

		return location.scope;
	}

	public Object visit(StaticCall call)
	{
		if (call.getArguments() != null)
		{
			for (Expression expArg : call.getArguments())
			{
				expArg.scope = call.scope;
				expArg.accept(this);
			}
		}

		return call.scope;
	}

	public Object visit(VirtualCall call)
	{
		if (call.getLocation() != null)
		{
		  call.getLocation().scope = call.scope;
		  call.getLocation().accept(this);
		}

		if (call.getArguments() != null)
		{
			for (Expression expArg : call.getArguments())
			{
				expArg.scope = call.scope;
				expArg.accept(this);
			}
		}

		return call.scope;
	}

	public Object visit(This thisExpression)
	{
		return thisExpression.scope;
	}

	public Object visit(NewClass newClass)
	{
		return newClass.scope;
	}

	public Object visit(NewArray newArray)
	{

		newArray.getSize().scope = newArray.scope;
		newArray.getSize().accept(this);

		newArray.getType().scope = newArray.scope;

		return newArray.scope;
	}

	public Object visit(Length length)
	{
		length.getArray().scope = length.scope;
		length.getArray().accept(this);

		return length.scope;
	}

	public Object visit(MathBinaryOp binaryOp)
	{
		binaryOp.getFirstOperand().scope = binaryOp.scope;
		binaryOp.getFirstOperand().accept(this);

		binaryOp.getSecondOperand().scope = binaryOp.scope;
		binaryOp.getSecondOperand().accept(this);

		return binaryOp.scope;
	}

	public Object visit(LogicalBinaryOp binaryOp)
	{
		binaryOp.getFirstOperand().scope = binaryOp.scope;
		binaryOp.getFirstOperand().accept(this);

		binaryOp.getSecondOperand().scope = binaryOp.scope;
		binaryOp.getSecondOperand().accept(this);

		return binaryOp.scope;
	}

	public Object visit(MathUnaryOp unaryOp)
	{
		unaryOp.getOperand().scope = unaryOp.scope;
		unaryOp.getOperand().accept(this);

		return unaryOp.scope;
	}

	public Object visit(LogicalUnaryOp unaryOp)
	{
		unaryOp.getOperand().scope = unaryOp.scope;
		unaryOp.getOperand().accept(this);

		return unaryOp.scope;
	}

	public Object visit(Literal literal)
	{
		return literal.scope;
	}

	public Object visit(ExpressionBlock expressionBlock)
	{
		expressionBlock.getExpression().scope = expressionBlock.scope;
		expressionBlock.getExpression().accept(this);

		return expressionBlock.scope;
	}

}
