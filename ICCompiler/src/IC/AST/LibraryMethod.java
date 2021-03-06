package IC.AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Library method declaration AST node.
 * 
 * @author Tovi Almozlino
 */
public class LibraryMethod extends Method
{
	public Object accept(Visitor visitor)
	{
		return visitor.visit(this);
	}

	public LibraryMethod(Type type, String name, List<Formal> formals)
	{
		super(type, name, formals, new ArrayList<Statement>());
		Collections.reverse(this.formals);
	}
	
	public String toString()
	{
		return "Library." + name;
	}
	
	public boolean IsMain()
	{
		return false;
	}
}
