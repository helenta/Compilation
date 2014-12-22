package IC.Semantic;

import IC.AST.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SymbolTableVisitor implements Visitor 
{	
	private boolean inWhile = false;
	
	public Object visit(Program program) 
	{
		SymbolTable symbolTable = new SymbolTable();
		for (ICClass decl : program.getClasses()){
			ClassScope clsScope = (ClassScope) decl.accept(this);
			symbolTable.root.addSymbol(clsScope, symbolTable.root.classScopes);
			if (!decl.hasSuperClass())
				decl.scope = symbolTable.root;
			else{
				if (!symbolTable.root.hasSymbol(decl.getSuperClassName()) || decl.getName().equals(decl.getSuperClassName())){
					 throw new SemanticError("semantic error at line " + decl.getLine() + ": " + 
				"class " + decl.getSuperClassName() + "is not defined when trying to extend it by class " + decl.getName());
				}
				ClassScope parent = (ClassScope) symbolTable.root.getSymbol(decl.getSuperClassName());
				decl.scope = parent;
				parent.addDerived(clsScope, decl.getLine());
			}
		}
		
		return symbolTable;
	}

	public Object visit(ICClass icClass) 
	{
		ClassScope clsScope = new ClassScope(icClass.getName(), icClass.getLine(), icClass);
		for (Field field : icClass.getFields())
		{
			field.scope = clsScope;
			clsScope.addSymbol((FieldScope)field.accept(this), clsScope.fields);
		}
		
		for (Method method : icClass.getMethods())
		{
			method.scope = clsScope;
			MethodScope methodScope = (MethodScope) method.accept(this);
			if (method instanceof StaticMethod || method instanceof LibraryMethod)
			{
				clsScope.addSymbol(methodScope, clsScope.methods);
			}
			else
			{
				methodScope.setVirtual(true);
				clsScope.addSymbol(methodScope, clsScope.methods);
			}
		}
		return clsScope;
	}

	public Object visit(Field field) 
	{
		FieldScope fieldScope = new FieldScope(field.scope, field.getName(), field.getType(), field.getLine());
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
		MethodScope methodScope = new MethodScope(method.getName(), method.getType(), method.getLine(), false, method);
		methodScope.setParent(method.scope);
		method.scope = methodScope;
		
		for (Formal form : method.getFormals())
		{
			form.scope = methodScope;
			FormalScope formal = (FormalScope) form.accept(this);
			methodScope.addSymbol(formal, methodScope.params);
		}
		
		for (Statement stmt : method.getStatements())
		{
			stmt.scope = methodScope;
			
			if (stmt instanceof LocalVariable)
			{
				LocalScope local = (LocalScope) stmt.accept(this);
				methodScope.addSymbol(local, methodScope.locals);
			}
			else if (stmt instanceof StatementsBlock)
			{
				BlockScope blkScope = (BlockScope) stmt.accept(this);
				methodScope.addAnonymousScope(blkScope, methodScope.blocks);
			}
			else if (stmt instanceof While)
			{
				Object wh = stmt.accept(this);
				if (wh instanceof LocalScope)
				{
					methodScope.addSymbol((LocalScope)wh, methodScope.locals);
				}
				if (wh instanceof BlockScope)
				{
					methodScope.addAnonymousScope((BlockScope) wh, methodScope.blocks);
				}
			}
			else if (stmt instanceof If)
			{
				List<Scope> blocks = (List<Scope>) stmt.accept(this);
				for (Scope scope : blocks)
				{
					if (scope instanceof LocalScope)
					{
						methodScope.addSymbol((LocalScope)scope, methodScope.locals);
					}
					else if (scope instanceof BlockScope)
					{
						methodScope.addAnonymousScope((BlockScope) scope, methodScope.blocks);
					}
				}
			}
			else
			{
				stmt.accept(this);
			}
		}
		
		return methodScope;
	}
	
	public Object visit(LibraryMethod method) 
	{
		return makeMethodScope(method);
	}

	public Object visit(Formal formal) 
	{
		FormalScope formalScope = new FormalScope(formal.getName(), formal.getType(), formal.getLine());
		formalScope.setParent(formal.scope);
		formal.scope = formalScope;
		
		return formalScope;
	}

	public Object visit(If ifStatement) 
	{
		List<Scope> blocks = new ArrayList<Scope>();
		if (ifStatement.getOperation() instanceof If)
		{
			blocks.addAll((Collection<? extends Scope>) ifStatement.getOperation().accept(this));
		}
		else
		{
			blocks.add((Scope) ifStatement.getOperation().accept(this));
		}
		
		if (ifStatement.hasElse())
		{
			ifStatement.getElseOperation().scope = ifStatement.scope;
			if (ifStatement.getElseOperation() instanceof If)
			{
				blocks.addAll((Collection<? extends Scope>) ifStatement.getElseOperation().accept(this));
			}
			else
			{
				blocks.add((Scope) ifStatement.getElseOperation().accept(this));
			}
		}	
		
		ifStatement.getOperation().scope = ifStatement.scope;
		ifStatement.getCondition().scope = ifStatement.scope;
		ifStatement.getCondition().accept(this);
		
		return blocks;
	}

	public Object visit(While whileStatement) 
	{
		boolean outer = inWhile;
		inWhile = true;
		
		Scope whileScope = null;
		if (whileStatement.getOperation() instanceof StatementsBlock)
		{
			BlockScope blockScope = new BlockScope("while", whileStatement.getLine(), (StatementsBlock)whileStatement.getOperation());
			whileScope = blockScope;
		}
		else
		{
			Statement statement = whileStatement.getOperation();
			LocalScope scope = new LocalScope("while singke statement", statement.getLine());
			whileScope = scope;
		}
		whileScope.setParent(whileStatement.scope);
		
		whileStatement.getCondition().scope = whileStatement.scope;
		whileStatement.getCondition().accept(this);
		
		whileStatement.getOperation().scope = whileStatement.scope;
		whileStatement.getOperation().accept(this);
		
		whileStatement.getOperation().scope = whileStatement.scope;
		if (!outer)
		{
			inWhile = false;
		}
		return whileScope;
	}

	public Object visit(Break breakStatement) 
	{
		if (!inWhile)
		{
			throw new SemanticError(breakStatement.getLine()+ ": semantic error; Use of 'break' statement outside of loop not allowed");
		}
		return breakStatement.scope;
	}

	public Object visit(Continue continueStatement) 
	{
		if (!inWhile)
		{
			throw new SemanticError(continueStatement.getLine()+ ": semantic error; Use of 'continue' statement outside of loop not allowed");
		}
		return continueStatement.scope;
	}

	public Object visit(StatementsBlock statementsBlock) 
	{
		BlockScope blkScope = new BlockScope(statementsBlock.getLine(), statementsBlock);
		blkScope.setParent(statementsBlock.scope);
		for (Statement stmt : statementsBlock.getStatements())
		{	
			stmt.scope = blkScope;
			if (stmt instanceof LocalVariable)
			{
				LocalScope local = (LocalScope) stmt.accept(this);
				blkScope.addSymbol(local, blkScope.locals);
				
				LocalVariable localVarible = (LocalVariable)stmt;
				localVarible.getType().scope = blkScope;
				localVarible.getInitValue().scope = blkScope;
			}
			else if (stmt instanceof StatementsBlock)
			{
				BlockScope subBlkScope = (BlockScope) stmt.accept(this);
				blkScope.addAnonymousScope(subBlkScope, blkScope.blocks);
			}
			else if (stmt instanceof While)
			{
				Object wh = stmt.accept(this);
				if (wh instanceof LocalScope){
					blkScope.addSymbol((LocalScope)wh, blkScope.locals);
				}
				if (wh instanceof BlockScope){
					blkScope.addAnonymousScope((BlockScope) wh, blkScope.blocks);
				}
				
				((While)stmt).getCondition().scope = blkScope;
				((While)stmt).getOperation().scope = blkScope;
			}
			else if (stmt instanceof If)
			{
				List<Scope> blocks = (List<Scope>) stmt.accept(this);
				for (Scope scope : blocks)
				{
					if (scope instanceof LocalScope)
					{
						blkScope.addSymbol((LocalScope)scope, blkScope.locals);
					}
					if (scope instanceof BlockScope)
					{
						blkScope.addAnonymousScope((BlockScope) scope, blkScope.blocks);
					}
				}
			}
			else
			{
				stmt.accept(this);
			}
		}
		return blkScope;
	}

	public Object visit(LocalVariable localVariable) 
	{
		LocalScope localScope = new LocalScope(localVariable.getName(), localVariable.getType(), localVariable.getLine());
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
		returnStatement.getValue().scope = returnStatement.scope;
		returnStatement.getValue().accept(this);
		
		return returnStatement.scope;
	}

	public Object visit(VariableLocation location) 
	{	
		if (location.getLocation() != null)
		{
			location.getLocation().scope = location.scope;
			location.getLocation().accept(this);
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
		call.getLocation().scope = call.scope;
		call.getLocation().accept(this);
		
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

	public Object visit(NewArray newArray) {
		
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

