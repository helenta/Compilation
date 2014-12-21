package IC.Semantic;

import IC.AST.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SymbolTableBuilder implements Visitor {
	
	private boolean inWhile = false;
	
	public Object visit(Program program) {
		SymbolTable symbolTable = new SymbolTable();
		// Put base classes
		for (ICClass decl : program.getClasses()){
			ClassScope clsScope = (ClassScope) decl.accept(this);
			symbolTable.root.addSymbol(clsScope, symbolTable.root.classScopes);
			if (!decl.hasSuperClass())
			// Set root as the scope for DeclClasses AST nodes.
				decl.scope = symbolTable.root;
			else{
				if (!symbolTable.root.hasSymbol(decl.getSuperClassName()) || decl.getName().equals(decl.getSuperClassName())){
					 throw new SemanticException(decl.getLine() + ": semantic error; Class " + decl.getName() + " cannot extend " + decl.getSuperClassName() + ", since it's not yet defined");
				}
				ClassScope parent = (ClassScope) symbolTable.root.getSymbol(decl.getSuperClassName());
				// Set parent class as scope for those sub classes.
				decl.scope = parent;
				parent.addDerived(clsScope, decl.getLine());
			}
		}
		
		return symbolTable;
	}

	public Object visit(ICClass icClass) {
		ClassScope clsScope = new ClassScope(icClass.getName(), icClass.getLine());
		for (Field field : icClass.getFields()){
			clsScope.addSymbol((FieldScope)field.accept(this), clsScope.fields);
			field.scope = clsScope;
		}
		for (Method method : icClass.getMethods()){
			MethodScope methodScope = (MethodScope) method.accept(this);
			if (method instanceof StaticMethod || method instanceof LibraryMethod){
				clsScope.addSymbol(methodScope, clsScope.methods);
			}
			else {
				methodScope.setVirtual(true);
				clsScope.addSymbol(methodScope, clsScope.methods);
			}
			method.scope = clsScope;
		}
		return clsScope;
	}

	public Object visit(Field field) {
		FieldScope fieldScope = new FieldScope(field.getName(), field.getType(), field.getLine());
		return fieldScope;
		
	}

	public Object visit(VirtualMethod method) {
		return makeMethodScope(method);
	}

	public Object visit(StaticMethod method) {
		return makeMethodScope(method);
	}

	private Object makeMethodScope(Method method){
		MethodScope methodScope = new MethodScope(method.getName(), method.getType(), method.getLine(), false, method);
		for (Formal form : method.getFormals()){
			FormalScope formal = (FormalScope) form.accept(this);
			methodScope.addSymbol(formal, methodScope.params);
			form.scope = methodScope;
		}
		for (Statement stmt : method.getStatements()){
			if (stmt instanceof LocalVariable){
				LocalScope local = (LocalScope) stmt.accept(this);
				methodScope.addSymbol(local, methodScope.locals);
			}
			else if (stmt instanceof StatementsBlock){
				BlockScope blkScope = (BlockScope) stmt.accept(this);
				methodScope.addAnonymousScope(blkScope, methodScope.blocks);
			}
			else if (stmt instanceof While){
				Object wh = stmt.accept(this);
				if (wh instanceof LocalScope){
					methodScope.addSymbol((LocalScope)wh, methodScope.locals);
				}
				if (wh instanceof BlockScope){
					methodScope.addAnonymousScope((BlockScope) wh, methodScope.blocks);
				}
			}
			else if (stmt instanceof If){
				@SuppressWarnings("unchecked")
				List<Scope> blocks = (List<Scope>) stmt.accept(this);
				for (Scope scope : blocks){
					if (scope instanceof LocalScope){
						methodScope.addSymbol((LocalScope)scope, methodScope.locals);
					}
					if (scope instanceof BlockScope){
						methodScope.addAnonymousScope((BlockScope) scope, methodScope.blocks);
					}
				}
			}
			else{
				stmt.accept(this);
			}
			stmt.scope = methodScope;
		}
		return methodScope;
	}
	
	public Object visit(LibraryMethod method) {
		return makeMethodScope(method);
	}

	public Object visit(Formal formal) {
		return new FormalScope(formal.getName(), formal.getType(), formal.getLine());
	}

	@SuppressWarnings("unchecked")
	public Object visit(If ifStatement) {
		List<Scope> blocks = new ArrayList<Scope>();
		if (ifStatement.getOperation() instanceof If){
			blocks.addAll((Collection<? extends Scope>) ifStatement.getOperation().accept(this));
		}
		else{
			blocks.add((Scope) ifStatement.getOperation().accept(this));
		}
		if (ifStatement.hasElse()){
			if (ifStatement.getElseOperation() instanceof If){
				blocks.addAll((Collection<? extends Scope>) ifStatement.getElseOperation().accept(this));
			}
			else{
				blocks.add((Scope) ifStatement.getElseOperation().accept(this));
			}
		}	
		return blocks;
	}

	public Object visit(While whileStatement) {
		boolean outer = inWhile;
		inWhile = true;
		Scope whScope = (Scope) whileStatement.getOperation().accept(this);
		if (!outer){
			inWhile = false;
		}
		return whScope;
	}

	public Object visit(Break breakStatement) {
		if (!inWhile){
			throw new SemanticException(breakStatement.getLine()+ ": semantic error; Use of 'break' statement outside of loop not allowed");
		}
		return null;
	}

	public Object visit(Continue continueStatement) {
		if (!inWhile){
			throw new SemanticException(continueStatement.getLine()+ ": semantic error; Use of 'continue' statement outside of loop not allowed");
		}
		return null;
	}

	public Object visit(StatementsBlock statementsBlock) {
		BlockScope blkScope = new BlockScope(statementsBlock.getLine(), statementsBlock);
		for (Statement stmt : statementsBlock.getStatements()){
			if (stmt instanceof LocalVariable){
				LocalScope local = (LocalScope) stmt.accept(this);
				blkScope.addSymbol(local, blkScope.locals);
				
				LocalVariable localVarible = (LocalVariable)stmt;
				localVarible.getType().scope = blkScope;
				localVarible.getInitValue().scope = blkScope;
			}
			else if (stmt instanceof StatementsBlock){
				BlockScope subBlkScope = (BlockScope) stmt.accept(this);
				blkScope.addAnonymousScope(subBlkScope, blkScope.blocks);
			}
			else if (stmt instanceof While){
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
			else if (stmt instanceof If){
				@SuppressWarnings("unchecked")
				List<Scope> blocks = (List<Scope>) stmt.accept(this);
				for (Scope scope : blocks){
					if (scope instanceof LocalScope){
						blkScope.addSymbol((LocalScope)scope, blkScope.locals);
					}
					if (scope instanceof BlockScope){
						blkScope.addAnonymousScope((BlockScope) scope, blkScope.blocks);
					}
				}
				
				((If)stmt).getCondition().scope = blkScope;
				((If)stmt).getOperation().scope = blkScope;
				
				if (((If)stmt).getElseOperation() != null){
					((If)stmt).getElseOperation().scope = blkScope;
				}
			}
			else{
				stmt.accept(this);
			}
			stmt.scope = blkScope;
		}
		return blkScope;
	}

	public Object visit(LocalVariable localVariable) 
	{
		LocalScope localScope = new LocalScope(localVariable.getName(), localVariable.getType(), localVariable.getLine());
		localVariable.scope = localScope;
		return localScope;
	}

	public Object visit(PrimitiveType type) 
	{
		//type.scope = type.p
		return null;
	}

	public Object visit(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Assignment assignment) 
	{
		//LocalScope local = new LocalScope(assignment.)
		return null;
	}

	public Object visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Return returnStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(VariableLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(ArrayLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(StaticCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(VirtualCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(This thisExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(NewClass newClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(NewArray newArray) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Length length) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(MathBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(LogicalBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(MathUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(LogicalUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Literal literal) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(ExpressionBlock expressionBlock) {
		// TODO Auto-generated method stub
		return null;
	}
}

