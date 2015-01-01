package IC.AST;

/**
 * Abstract base class for expression AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Expression extends ASTNode
{
	protected Expression(int line)
	{
		super(line);
	}
}
