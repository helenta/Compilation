package IC.Semantic;

import java.util.List;

import IC.AST.*;
import IC.*;

public class TypeChecker implements Visitor {
	
	private SymbolTable table;
	private boolean mainDeclared;
	private boolean inStatic = false;
	
	public TypeChecker(SymbolTable table) {
		this.table = table;
		this.mainDeclared = false;
	}
	
	public Object visit(Program program) {
		for (ICClass cls : program.getClasses()){
			cls.accept(this);
		}
		return null;
	}

	public Object visit(ICClass icClass) {
		for (Method method : icClass.getMethods()){
			method.accept(this);
		}
		return null;
	}

	public Object visit(VirtualMethod method) {
		for (Statement stmt : method.getStatements()){
			stmt.accept(this);
		}
		return null;
	}

	public Object visit(StaticMethod method) {
		if (isMain(method)){
			if (mainDeclared){
				throw new SemanticException(method.getLine() + ": semantic error; Found more than one main in the file");
			}
			mainDeclared = true;
		}
		inStatic = true;
		for (Statement stmt : method.getStatements()){
			stmt.accept(this);
		}
		inStatic = false;
		return null;
	}

	private boolean isMain(StaticMethod method) {
		if (method.getName().equals("main")){
			if (!(method.getType().getName().equals("void"))){
				throw new SemanticException(method.getLine() + ": semantic error; Main method should have 'void' return type");
			}
			if (method.getFormals().size() == 1){
				Formal param = method.getFormals().get(0);
				String name = param.getName();
				int dim = param.getType().getDimension();
				String typeName = param.getType().getName();
				if (name.equals("args") && dim == 1 &&  typeName.equals("string")){
					return true;
				}
			throw new SemanticException(method.getLine() + ": semantic error; Argument for main method should be 'string[] args'");	
			}
		}
		return false;
	}

	public Object visit(PrimitiveType type) {
		return type.semType = type;
	}

	public Object visit(Assignment assignment) {
		assignment.getVariable().scope = assignment.scope;
		assignment.getAssignment().scope = assignment.scope;
		Type refType = (Type) assignment.getVariable().accept(this);
		Type exprType = (Type) assignment.getAssignment().accept(this);
		// todo: remove
		if (!isSameType(refType, exprType))
		{
			int t = 0;
			t = 1;
			refType = (Type) assignment.getVariable().accept(this);
			exprType = (Type) assignment.getAssignment().accept(this);
		}
		if (!isSameType(refType, exprType)){
			throw new SemanticException(assignment.getLine() + ": semantic error; Invalid assignment of type " + printType(exprType) +  " to variable of type " + printType(refType));
		}
		return null;
	}
	
	String printType(Type type){
		if (type.getName().equals("int")){
			return "integer";
		}
		return type.getName();
	}
	private boolean isSameType(Type a, Type b) {
		//todo: remove
		if (b == null){
			if (a == null){
				return true;
			}
			else if (!a.IsPimitive() || a.IsIntegerOrBoolean() || ((PrimitiveType)a).getType() != DataTypes.VOID){
				return true;
			}
			else{
				return false;
			}
		}
		if (b.IsPimitive()){
			if (((PrimitiveType)b).getName() == null){
				return true;
			}
		}	
		if (a.getName().equals(b.getName())){
			return a.getDimension() == b.getDimension();
		}
		if (!(a instanceof Type) || !(b instanceof Type)){
			return false;
		}
		if (a.getDimension() == b.getDimension() && table.isDerived(b.getName(), a.getName())){
			return true;
		}
		return false;
	}

	public Object visit(Call callStatement) {
		callStatement.accept(this);
		// TODO: finish this.
		return null;
	}

	public Object visit(Return returnStatement) {
		MethodScope method = table.getEnclosingMethod(returnStatement.scope);
		Type retType = method.getRetType();
		String retValueTypeName = retType.getName(); 
		if (!returnStatement.hasValue()){
			if (!retValueTypeName.equals("void")){
				throw new SemanticException(returnStatement.getLine() + ": semantic error; Return statement is not of type " + printType(retType));
			}
			return null;
		}
		
		returnStatement.getValue().scope = returnStatement.scope;
		Type expType = (Type) returnStatement.getValue().accept(this);
		if (expType == null && !retType.IsIntegerOrBoolean()){
			return null;
		}
		else if (expType == null && retType.IsIntegerOrBoolean()){
			throw new SemanticException(returnStatement.getLine() + ": semantic error; Return statement is not of type " + printType(retType));
		}
		
		if (!method.getRetType().IsPimitive() && !expType.IsPimitive()){
			if (!(table.isDerived(expType.getName(), retValueTypeName))){
				throw new SemanticException(returnStatement.getLine() + ": semantic error; Return statement is not of type " + printType(retType));

			}
		}
		if (!retValueTypeName.equals(expType.getName())){
			throw new SemanticException(returnStatement.getLine() + ": semantic error; Return statement is not of type " + printType(retType));
		}
		return null;
	}

	public Object visit(If ifStatement) {
		ifStatement.getCondition().scope = ifStatement.scope;
		Type cond = (Type) ifStatement.getCondition().accept(this);
		if (!cond.getName().equals("boolean")){
			throw new SemanticException(ifStatement.getLine() + ": semantic error; Non boolean condition for if statement");
		}
		ifStatement.getOperation().scope = ifStatement.scope;
		ifStatement.getOperation().accept(this);
		if (ifStatement.hasElse()){
			ifStatement.getElseOperation().scope = ifStatement.scope;
			ifStatement.getElseOperation().accept(this);
		}
		return null;
	}

	public Object visit(While whileStatement) {
		whileStatement.getCondition().scope = whileStatement.scope;
		Type cond = (Type) whileStatement.getCondition().accept(this);
		// todo: remove
		if (cond == null)
		{
			int t = 0;
			t = 1;
			cond = (Type) whileStatement.getCondition().accept(this);
		}
		if (!cond.getName().equals("boolean")){
			throw new SemanticException(whileStatement.getLine() + ": semantic error; Non boolean condition for while statement");
		}
		whileStatement.getOperation().scope = whileStatement.scope;
		whileStatement.getOperation().accept(this); 
		return null;
	}

	public Object visit(StatementsBlock statementsBlock) {
		for (Statement stmt : statementsBlock.getStatements()){
			stmt.accept(this);
		}
		return null;
	}

	public Object visit(LocalVariable localVariable) {
		//if (localVariable.isInitialized()){
		//	localVariable.getInitialValue().scope = localVariable.scope;
		//	localVariable.getInitialValue().accept(this);
		//}
		return localVariable.semType = localVariable.getType();
	}

	public Object visit(VariableLocation location) {
		Scope varScope = table.getSymbol(location.scope, location.getName());
		if (varScope == null){
			throw new SemanticException(location.getLine() + ": semantic error; " + location.getName() + " not found in symbol table");
		}

		if (!location.isExternal() && inStatic && varScope instanceof FieldScope){
			throw new SemanticException(location.getLine() + ": semantic error; Use of field inside static method is not allowed");
		}
		return location.semType = ((PrimitiveScope)varScope).getType();
	}

	public Object visit(Field location) {
		//location.getObject().scope = location.scope;
		Type objType = (Type) location.accept(this);
		Scope objScope = table.getSymbol(location.scope, objType.getName());
		String fieldName = location.getName();
		if (objScope.getSymbol(fieldName) == null || !(objScope.getSymbol(fieldName) instanceof FieldScope)){
			// TODO: some error
		}
		if (inStatic){
			//if (table.getEnclosingClass(location.scope).getName().equals(objScope.getName())){
			//	throw new SemanticException(location.getLine() + ": semantic error; Use of field inside static method is not allowed");
			//}
		}
		ClassScope objcls  = (ClassScope) table.getSymbol(location.scope, objType.getName());
		Scope fieldScope = table.getSymbol(objcls, fieldName);
		return location.semType =  ((PrimitiveScope)fieldScope).getType();
	}

	public Object visit(ArrayLocation location) {
		location.getArray().scope = location.scope;
		location.getIndex().scope = location.scope;
		Type arrType = (Type) location.getArray().accept(this);
		Type indexType = (Type) location.getIndex().accept(this);
		if (!indexType.getName().equals("int")){
			throw new SemanticException("Non int for index");
		}
		// copy type and give 1 dimension lower:
		Type copied = null;
		if (arrType instanceof PrimitiveType){
			copied = new PrimitiveType(arrType.getLine(), ((PrimitiveType) arrType).getType());
		}
		else if (arrType instanceof Type){
			copied = new Type(arrType.getLine(), ((Type)arrType).getName());
		}
		
		for (int i=0; i<arrType.getDimension() - 1;i++){
			copied.incrementDimension();
		}
		return location.semType = copied;
	}

	public Object visit(StaticCall call) {
		String methodName = call.getName();
		String clsName = call.getClassName();
		Scope clsScope = table.getSymbol(call.scope, clsName);
		if (clsScope == null){
			throw new SemanticException(call.getLine() + ": semantic error; Class " + clsName + " doesn't exist");
		}
		MethodScope methodScope = (MethodScope) table.getSymbol(clsScope, methodName);
		if (methodScope == null){
			throw new SemanticException(call.getLine() + ": semantic error; Method " + methodName + " doesn't exist");
		}
		if (call.getArguments().size() != methodScope.params.size()){
			throw new SemanticException(call.getLine() + ": semantic error; Invalid number of arguments for " + clsName + "." + methodName);
		}
		if (!isSameSignature(call, methodScope)){
			throw new SemanticException(call.getLine() + ": semantic error; Method " + methodName + " is not applicable for the arguments given");
		}
		return call.semType = methodScope.getRetType();
	}
	
	@SuppressWarnings({ "unused", "null" })
	public Object visit(VirtualCall call) {
		Type objType = null;
		if (call == null){
			objType = new Type(0, table.getEnclosingClass(call.scope).getName());
		}
		else{
			//call.getObject().scope = call.scope;
			objType = (Type) call.accept(this);	
		}
		Scope objScope = table.getSymbol(call.scope, objType.getName());

		String methodName = call.getName();
		//MethodScope methodScope = (MethodScope) table.getSymbol(call.scope, methodName);
		MethodScope methodScope = (MethodScope) table.getSymbol(objScope, methodName);
		MethodScope encMethod = table.getEnclosingMethod(call.scope);
		if (methodScope == null){
			if (call == null){
				throw new SemanticException(call.getLine() + ": semantic error; " + methodName + " not found in symbol table");
			}
			else{
				throw new SemanticException(call.getLine() + ": semantic error; Method " + objType.getName() + "." + methodName + " not found in type table");
			}
		}
		if (!table.isEnclosedInVirtualMethod(call.scope) && !encMethod.getName().equals("main") && methodScope.isVirtual() && call == null){
			throw new SemanticException(call.getLine() + ": semantic error; Calling a local virtual method from inside a static method is not allowed");
		}
		if (call.getArguments().size() != methodScope.params.size()){
			if (call == null){
				throw new SemanticException(call.getLine() + ": semantic error; Invalid number of arguments for " + methodName);
			}
			else{
				throw new SemanticException(call.getLine() + ": semantic error; Invalid number of arguments for " + objType.getName() + "." + methodName);
			}
		}
		if (!isSameSignature(call, methodScope)){
			if (call == null){
				throw new SemanticException(call.getLine() + ": semantic error; Method " + methodName + " is not applicable for the arguments given");
			}
			else{
				throw new SemanticException(call.getLine() + ": semantic error; Method" + objType.getName() + "." + methodName + " is not applicable for the arguments given");
			}
		}
		return call.semType = methodScope.getRetType();
	}
	
	public boolean isSameSignature(Call call, MethodScope scope){
		int params = call.getArguments().size();
		List<Expression> callArgs = call.getArguments();
		List<Scope> methodArgs = scope.params;
		for (int i=0; i < params; i++){
			callArgs.get(i).scope = call.scope;
			Type argType = (Type) callArgs.get(i).accept(this);
			Type paramType = ((PrimitiveScope)methodArgs.get(i)).getType();
			if (!isSameType(paramType, argType)){
				return false;
			}
		}
		return true;
	}
	
	public Object visit(This thisExpression) {
		if (inStatic){
			throw new SemanticException(thisExpression.getLine() + ": semantic error; Use of 'this' expression inside static method is not allowed");
		}
		ClassScope cls = table.getEnclosingClass(thisExpression.scope);
		return thisExpression.semType = new Type(cls.getLine(), cls.getName());
	}

	public Object visit(NewClass newClass) {
		return newClass.semType = new Type(newClass.getLine(), newClass.getName());
	}

	public Object visit(NewArray newArray) {
		newArray.getSize().scope = newArray.scope;
		newArray.getSize().accept(this);
		if (newArray.getSize().semType.getName().equals("int")){
			// TODO: not great, better copy but ?
			Type arr = newArray.getType();
			arr.incrementDimension();
			return newArray.semType = arr;
		}
		//TODO: some error
		return null;
	}

	public Object visit(Length length) {
		length.getArray().scope = length.scope;
		length.getArray().accept(this);
		// all is fine return an int.
		
		return length.semType = new PrimitiveType(length.getLine(), DataTypes.INT);
	}

	public Object visit(Literal literal) {
		switch(literal.getType()) {
		case INTEGER:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.INT);
		case STRING:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.STRING);
		case NULL:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.VOID);
		case TRUE:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.BOOLEAN);
		case FALSE:
			return literal.semType = new PrimitiveType(literal.getLine(), DataTypes.BOOLEAN);
		}
		return null;
	}

	public Object visit(UnaryOp unaryOp) {
		unaryOp.getOperand().scope = unaryOp.scope;
		unaryOp.getOperand().accept(this);
		Type operand = unaryOp.getOperand().semType;
		UnaryOps op = unaryOp.getOperator();
		switch(op){
		case UMINUS:
			if (operand.getName().equals("int")){
				return unaryOp.semType = new PrimitiveType(operand.getLine(),DataTypes.INT);
			}
			break;	
		case LNEG:
			if (operand.getName().equals("boolean")){
				return unaryOp.semType = new PrimitiveType(operand.getLine(),DataTypes.BOOLEAN);
			}
			break;
		}
		return null;
	}

	public Object visit(BinaryOp binaryOp) {
		binaryOp.getFirstOperand().scope = binaryOp.scope;
		binaryOp.getSecondOperand().scope = binaryOp.scope;
		binaryOp.getFirstOperand().accept(this);
		binaryOp.getSecondOperand().accept(this);
		//System.out.println(binaryOp.getFirstOperand());
		//System.out.println(binaryOp.getSecondOperand());
		Type temp = null;
		Type a = binaryOp.getFirstOperand().semType;
		Type b = binaryOp.getSecondOperand().semType;
		BinaryOps op = binaryOp.getOperator();
		
		switch(op){
		case PLUS:
			temp = add(a,b);
			if (temp == null){
				throw new SemanticException(binaryOp.getLine() + ": semantic error; Invalid arithmatic binary op (" + op + ") on non-integer and non-string expression");
			}
			return binaryOp.semType = temp;
		case MINUS:
		case MULTIPLY:
		case DIVIDE:
		case MOD:
			temp = arith(a,b);
			if (temp == null){
				throw new SemanticException(binaryOp.getLine() + ": semantic error; Invalid arithmatic binary op (" + op + ") on non-integer expression");
			}
			return binaryOp.semType = temp;
		case LTE:
		case GTE:
		case GT:
		case LT:
			temp = comp(a,b);
			if (temp == null){
				throw new SemanticException(binaryOp.getLine() + ": semantic error; Invalid logical binary op (" + op + ") on non-integer expression");
			}
			return binaryOp.semType = temp;
		case EQUAL:
		case NEQUAL:
			temp = eq(a,b);
			if (temp == null){
				throw new SemanticException(binaryOp.getLine() + ": semantic error; Invalid comparison binary op (" + op + ") on expressions of different types");
			}
			return binaryOp.semType = temp;
		case LOR:
		case LAND:
			temp = logic(a,b);
			if (temp == null){
				throw new SemanticException(binaryOp.getLine() + ": semantic error; Invalid logical binary op (" + op + ") on non-boolean expression");
			}
			return binaryOp.semType = temp;
		default:
			throw new SemanticException("Bad op");
		}
	}

	private Type logic(Type a, Type b) {
		if (a.getName().equals(b.getName()) && a.getName().equals("boolean")){
			return new PrimitiveType(a.getLine(),DataTypes.BOOLEAN);
		}
		// TODO: some error.
		return null;
	}

	private Type eq(Type a, Type b) {
		if (isSameType(a,b)){
			return new PrimitiveType(a.getLine(),DataTypes.BOOLEAN);
		}
		// TODO: some error
		return null;
	}

	private Type comp(Type a, Type b) {
		if (a.getName().equals(b.getName()) && a.getName().equals("int")){
			return new PrimitiveType(a.getLine(), DataTypes.BOOLEAN);
		}
		// TODO: some error.
		return null;
	}

	private Type arith(Type a, Type b) {
		if (a.getName().equals(b.getName()) && a.getName().equals("int")){
			return new PrimitiveType(a.getLine(),DataTypes.INT);
		}
		// TODO: some error.
		return null;
	}

	private Type add(Type a, Type b) {
		if (a instanceof PrimitiveType && b instanceof PrimitiveType){
			if (a.getName().equals(b.getName())){
				switch (a.getName()){
				case "int":
					return new PrimitiveType(a.getLine(),DataTypes.INT);
				case "string":
					return new PrimitiveType(a.getLine(),DataTypes.STRING);
				default:
					//TODO: some error
				}
			}	
		}
		return null;
			
	}

	@Override
	public Object visit(LibraryMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Formal formal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return new PrimitiveType(binaryOp.getLine(), DataTypes.BOOLEAN);
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		// TODO Auto-generated method stub
		return null;
	}
}
