package IC.AST;

/**
 * Abstract base class for variable reference AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Location extends Expression
{
	protected Location(int line)
	{
		super(line);
	}
}
