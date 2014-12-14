package IC.Semantic;

import java.util.ArrayList;
import java.util.List;

public class GlobalScope extends Scope {
	
	public List<Scope> classScopes;
	
	public GlobalScope() {
		super(null, "", 0);
		classScopes = new ArrayList<Scope>();
	}

	@Override
	public Object accept(ScopeVisitor visitor) {
		return visitor.visit(this);
	}
}
