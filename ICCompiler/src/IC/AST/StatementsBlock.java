package IC.AST;

import java.util.List;

/**
 * Statements block AST node.
 * 
 * @author Tovi Almozlino
 */
public class StatementsBlock extends Statement
{
	private List<Statement>	statements;

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public StatementsBlock(int line, List<Statement> statements)
	{
		super(line);
		this.statements = statements;
	}

	public List<Statement> getStatements()
	{
		return statements;
	}
}
