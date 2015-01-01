package IC.AST;

/**
 * Abstract base class for object creation AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class New extends Expression
{

	protected New(int line)
	{
		super(line);
	}

}
