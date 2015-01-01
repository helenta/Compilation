package IC.AST;

import IC.SymbolTables.Scope;

/**
 * Abstract AST node base class.
 * 
 * @author Tovi Almozlino
 */
public abstract class ASTNode
{
	private int	 line;

	public Scope	scope;
	public Type	 semType;

	public abstract Object accept(Visitor visitor);

	protected ASTNode(int line)
	{
		this.line = line;
	}

	public int getLine()
	{
		return line;
	}

}
