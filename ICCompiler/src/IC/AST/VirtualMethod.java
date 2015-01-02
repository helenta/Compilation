package IC.AST;

import java.util.List;

/**
 * Virtual method AST node.
 * 
 * @author Tovi Almozlino
 */
public class VirtualMethod extends Method
{

	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public VirtualMethod(Type type, String name, List<Formal> formals,
	    List<Statement> statements)
	{
		super(type, name, formals, statements);
	}

  public boolean IsMain()
  {
	  return false;
  }

}
