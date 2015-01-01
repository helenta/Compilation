package IC.AST;

/**
 * Abstract base class for statement AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Statement extends ASTNode
{

	protected Statement(int line)
	{
		super(line);
	}

}
