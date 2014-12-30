package IC.AST;

import IC.SymbolTables.LocalScope;
import IC.SymbolTables.SymbolTable;

/**
 * Pretty printing visitor - travels along the AST and prints info about each
 * node, in an easy-to-comprehend format.
 * 
 * @author Tovi Almozlino
 */
public class PrettyPrinter implements Visitor
{

	private int	        depth	= 0;	// depth of indentation

	private String	    ICFilePath;

	private SymbolTable	table;

	/**
	 * Constructs a new pretty printer visitor.
	 * 
	 * @param ICFilePath
	 *          The path + name of the IC file being compiled.
	 */
	public PrettyPrinter(String ICFilePath, SymbolTable table)
	{
		this.ICFilePath = ICFilePath;
		this.table = table;
	}

	private void indent(StringBuffer output, ASTNode node)
	{
		output.append("\n");
		for (int i = 0; i < depth; ++i)
			output.append(" ");
		if (node != null)
			output.append(node.getLine() + ": ");
	}

	private void indent(StringBuffer output)
	{
		indent(output, null);
	}

	public Object visit(Program program)
	{
		StringBuffer output = new StringBuffer();

		indent(output);
		output.append("Abstract Syntax Tree: " + ICFilePath + "\n");
		for (ICClass icClass : program.getClasses())
			output.append(icClass.accept(this));
		return output.toString();
	}

	public Object visit(ICClass icClass)
	{
		StringBuffer output = new StringBuffer();
		String scope = icClass.scope.getName();

		indent(output, icClass);
		output.append("Declaration of class: " + icClass.getName());
		if (icClass.hasSuperClass())
			output.append(", subclass of " + icClass.getSuperClassName());
		depth += 4;
		if (scope == "")
		{
			scope = "Global";
		}
		output.append(", Type: " + icClass.getName() + ", Symbol table: " + scope);
		for (Field field : icClass.getFields())
			output.append(field.accept(this));
		for (Method method : icClass.getMethods())
			output.append(method.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(PrimitiveType type)
	{
		StringBuffer output = new StringBuffer();

		indent(output, type);
		output.append("Primitive data type: ");
		if (type.getDimension() > 0)
			output.append(type.getDimension() + "-dimensional array of ");
		output.append(type.getName());
		return output.toString();
	}

	public Object visit(Type type)
	{
		StringBuffer output = new StringBuffer();

		indent(output, type);
		output.append("User-defined data type: ");
		if (type.getDimension() > 0)
			output.append(type.getDimension() + "-dimensional array of ");
		output.append(type.getName());
		return output.toString();
	}

	private String SimpleType(Type type)
	{
		StringBuffer output = new StringBuffer();

		output.append(type.getName());
		for (int dim = 0; dim < type.getDimension(); dim++)
			output.append("[]");
		return output.toString();
	}

	public Object visit(Field field)
	{
		StringBuffer output = new StringBuffer();

		indent(output, field);
		output.append("Declaration of field: " + field.getName());
		output.append(", Type: " + field.getType().getName() + ", Symbol table: "
		    + field.scope.getParent().getName());
		++depth;
		field.getType().accept(this);
		--depth;
		return output.toString();
	}

	public Object visit(LibraryMethod method)
	{
		StringBuffer output = new StringBuffer();

		indent(output, method);
		output.append("Declaration of library method: " + method.getName());
		output.append(", Type: " + method.getType().getName() + ", Symbol table: "
		    + method.scope.getName());
		depth += 4;
		output.append(method.getType().accept(this));
		for (Formal formal : method.getFormals())
			output.append(formal.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(Formal formal)
	{
		StringBuffer output = new StringBuffer();

		indent(output, formal);
		output.append("Parameter: " + formal.getName());
		output.append(", Type: " + SimpleType(formal.getType())
		    + ", Symbol table: " + formal.scope.getParent().getName());
		++depth;
		// output.append(formal.getType().accept(this));
		--depth;
		return output.toString();
	}

	public Object visit(VirtualMethod method)
	{
		StringBuffer output = new StringBuffer();

		indent(output, method);
		output.append("Declaration of virtual method: " + method.getName());
		output.append(", Type: " + "{");
		for (Formal formal : method.getFormals())
			output.append(formal.getType().getName() + ", ");
		if (method.getFormals().size() > 0)
			output.deleteCharAt(output.length() - 2);
		output.append("-> " + method.getType().getName() + "}" + ", Symbol table: "
		    + method.scope.getParent().getName());
		depth += 4;
		method.getType().accept(this);
		for (Formal formal : method.getFormals())
			output.append(formal.accept(this));
		for (Statement statement : method.getStatements())
			output.append(statement.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(StaticMethod method)
	{
		StringBuffer output = new StringBuffer();

		indent(output, method);
		output.append("Declaration of static method: " + method.getName());
		output.append(", Type: " + "{");
		for (Formal formal : method.getFormals())
			output.append(SimpleType(formal.getType()));
		output.append(" -> " + SimpleType(method.getType()) + "}"
		    + ", Symbol table: " + method.scope.getParent().getName());
		depth += 4;
		method.getType().accept(this);
		for (Formal formal : method.getFormals())
			output.append(formal.accept(this));
		for (Statement statement : method.getStatements())
			output.append(statement.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(Assignment assignment)
	{
		StringBuffer output = new StringBuffer();

		indent(output, assignment);
		output.append("Assignment statement");
		output.append(", Symbol table: " + assignment.scope.getName());
		depth += 4;
		output.append(assignment.getVariable().accept(this));
		output.append(assignment.getAssignment().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(CallStatement callStatement)
	{
		StringBuffer output = new StringBuffer();

		indent(output, callStatement);
		output.append("Method call statement");
		++depth;
		output.append(callStatement.getCall().accept(this));
		--depth;
		return output.toString();
	}

	public Object visit(Return returnStatement)
	{
		StringBuffer output = new StringBuffer();

		indent(output, returnStatement);
		output.append("Return statement");
		if (returnStatement.hasValue())
			output.append(", with return value");
		output.append(", Symbol table: " + returnStatement.scope.getName());
		if (returnStatement.hasValue())
		{
			depth += 4;
			output.append(returnStatement.getValue().accept(this));
			depth -= 4;
		}
		return output.toString();
	}

	public Object visit(If ifStatement)
	{
		StringBuffer output = new StringBuffer();

		indent(output, ifStatement);
		output.append("If statement");
		if (ifStatement.hasElse())
			output.append(", with Else operation");
		depth += 4;
		output.append(", Symbol table: " + ifStatement.scope.getName());
		output.append(ifStatement.getCondition().accept(this));
		output.append(ifStatement.getOperation().accept(this));
		if (ifStatement.hasElse())
			output.append(ifStatement.getElseOperation().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(While whileStatement)
	{
		StringBuffer output = new StringBuffer();

		indent(output, whileStatement);
		output.append("While statement");
		output.append(", Symbol table: " + whileStatement.scope.getName());
		depth += 4;
		output.append(whileStatement.getCondition().accept(this));
		output.append(whileStatement.getOperation().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(Break breakStatement)
	{
		StringBuffer output = new StringBuffer();

		indent(output, breakStatement);
		output.append("Break statement");
		output.append(", Symbol table: " + breakStatement.scope.getName());
		return output.toString();
	}

	public Object visit(Continue continueStatement)
	{
		StringBuffer output = new StringBuffer();

		indent(output, continueStatement);
		output.append("Continue statement");
		output.append(", Symbol table: " + continueStatement.scope.getName());
		return output.toString();
	}

	public Object visit(StatementsBlock statementsBlock)
	{
		StringBuffer output = new StringBuffer();

		indent(output, statementsBlock);
		output.append("Block of statements");
		output.append(", Symbol table: " + statementsBlock.scope.getName());
		depth += 4;
		for (Statement statement : statementsBlock.getStatements())
			output.append(statement.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(LocalVariable localVariable)
	{
		StringBuffer output = new StringBuffer();

		indent(output, localVariable);
		output.append("Declaration of local variable: " + localVariable.getName());

		if (localVariable.hasInitValue())
		{
			output.append(", with initial value");
			output.append(", Type: " + SimpleType(localVariable.getType())
			    + ", Symbol table: " + localVariable.scope.getParent().getName());
			depth += 2;
		}
		else
		{
			output.append(", Type: ");
			output.append(SimpleType(localVariable.getType()));
			output.append(", Symbol table: ");
			output.append(localVariable.scope.getParent().getName());
		}

		depth += 2;
		localVariable.getType().accept(this);
		if (localVariable.hasInitValue())
		{
			output.append(localVariable.getInitValue().accept(this));
			depth -= 2;
		}
		depth -= 2;
		return output.toString();
	}

	public Object visit(VariableLocation location)
	{
		StringBuffer output = new StringBuffer();

		indent(output, location);
		output.append("Reference to variable: " + location.getName());
		if (location.isExternal())
			output.append(", in external scope");
		output.append(", Type: " + SimpleType(location.semType));

		if (location.scope instanceof LocalScope)
			output.append(", Symbol table: " + location.scope.getParent().getName());
		else
			output.append(", Symbol table: " + location.scope.getName());

		if (location.isExternal())
		{
			depth += 4;
			output.append(location.getLocation().accept(this));
			depth -= 4;
		}
		return output.toString();
	}

	public Object visit(ArrayLocation location)
	{
		StringBuffer output = new StringBuffer();

		indent(output, location);
		output.append("Reference to array");

		output.append(", Type: " + SimpleType(location.semType));
		output.append(", Symbol table: " + location.scope.getName());

		depth += 4;
		output.append(location.getArray().accept(this));
		output.append(location.getIndex().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(StaticCall call)
	{
		StringBuffer output = new StringBuffer();

		indent(output, call);
		output.append("Call to static method: " + call.getName() + ", in class "
		    + call.getClassName());
		depth += 4;
		for (Expression argument : call.getArguments())
			output.append(argument.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(VirtualCall call)
	{
		StringBuffer output = new StringBuffer();

		indent(output, call);
		output.append("Call to virtual method: " + call.getName());
		if (call.isExternal())
			output.append(", in external scope");
		depth += 4;
		if (call.isExternal())
			output.append(call.getLocation().accept(this));
		for (Expression argument : call.getArguments())
			output.append(argument.accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(This thisExpression)
	{
		StringBuffer output = new StringBuffer();

		indent(output, thisExpression);
		output.append("Reference to 'this' instance");
		return output.toString();
	}

	public Object visit(NewClass newClass)
	{
		StringBuffer output = new StringBuffer();

		indent(output, newClass);
		output.append("Instantiation of class: " + newClass.getName());
		output.append(", Type: " + newClass.getName());
		output.append(", Symbol table: " + newClass.scope.getName());
		return output.toString();
	}

	public Object visit(NewArray newArray)
	{
		StringBuffer output = new StringBuffer();

		indent(output, newArray);
		output.append("Array allocation");
		output.append(", Type: " + SimpleType(newArray.getType()));
		output.append(", Symbol table: ");
		output.append(newArray.scope.getParent().getName());
		depth += 4;
		newArray.getType().accept(this);
		output.append(newArray.getSize().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(Length length)
	{
		StringBuffer output = new StringBuffer();

		indent(output, length);
		output.append("Reference to array length");
		++depth;
		output.append(length.getArray().accept(this));
		--depth;
		return output.toString();
	}

	public Object visit(MathBinaryOp binaryOp)
	{
		StringBuffer output = new StringBuffer();

		indent(output, binaryOp);
		output.append("Mathematical binary operation: "
		    + binaryOp.getOperator().getDescription());
		output.append(", Type: int");
		output.append(", Symbol table: " + binaryOp.scope.getName());
		depth += 4;
		output.append(binaryOp.getFirstOperand().accept(this));
		output.append(binaryOp.getSecondOperand().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(LogicalBinaryOp binaryOp)
	{
		StringBuffer output = new StringBuffer();

		indent(output, binaryOp);
		output.append("Logical binary operation: "
		    + binaryOp.getOperator().getDescription());
		output.append(", Type: boolean");
		output.append(", Symbol table: ");
		output.append(binaryOp.scope.getName());
		depth += 4;
		output.append(binaryOp.getFirstOperand().accept(this));
		output.append(binaryOp.getSecondOperand().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(MathUnaryOp unaryOp)
	{
		StringBuffer output = new StringBuffer();

		indent(output, unaryOp);
		output.append("Mathematical unary operation: "
		    + unaryOp.getOperator().getDescription());
		output.append(", Type: boolean ");
		depth += 4;
		output.append(unaryOp.getOperand().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(LogicalUnaryOp unaryOp)
	{
		StringBuffer output = new StringBuffer();

		indent(output, unaryOp);
		output.append("Logical unary operation: "
		    + unaryOp.getOperator().getDescription());
		output.append(", Type: boolean ");
		depth += 4;
		output.append(unaryOp.getOperand().accept(this));
		depth -= 4;
		return output.toString();
	}

	public Object visit(Literal literal)
	{
		StringBuffer output = new StringBuffer();
		String type = "";
		switch (literal.getType())
		{
			case TRUE:
				type = "boolean";
			case FALSE:
				type = "boolean";
				break;
			case INTEGER:
				type = "int";
				break;
			case STRING:
				type = "string";
				break;
			case NULL:
				type = "null";
				break;
			default:
				break;

		}
		indent(output, literal);
		output.append(literal.getType().getDescription() + ": "
		    + literal.getType().toFormattedString(literal.getValue()));
		output.append(", Type: " + type);
		output.append(", Symbol table: ");

		if (literal.scope instanceof LocalScope)
			output.append(literal.scope.getParent().getName());
		else
			output.append(literal.scope.getName());

		return output.toString();
	}

	public Object visit(ExpressionBlock expressionBlock)
	{
		StringBuffer output = new StringBuffer();

		indent(output, expressionBlock);
		output.append("Parenthesized expression");
		++depth;
		output.append(expressionBlock.getExpression().accept(this));
		--depth;
		return output.toString();
	}
}
