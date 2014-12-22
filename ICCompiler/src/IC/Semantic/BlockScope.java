package IC.Semantic;

import java.util.ArrayList;
import java.util.List;

import IC.AST.*;

public class BlockScope extends Scope {

	public List<Scope> locals;
	public List<Scope> blocks;
	StatementsBlock statementsBlock;
	
	BlockScope(Scope parent, String name, int line, StatementsBlock statementsBlock) {
		super(parent, name, line);
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
		this.statementsBlock = statementsBlock;
	}
	
	public BlockScope(String name, int line, StatementsBlock statementsBlock) {
		super(name, line);
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
		this.statementsBlock = statementsBlock;
	}
	

	public BlockScope(int line, StatementsBlock statementsBlock) {
		super("", line);
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
		this.statementsBlock = statementsBlock;
	}

	@Override
	public Object accept(ScopeVisitor visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public String getName(){
		if (parent.getParent().getParent() instanceof BlockScope)
		{
			return "statement block in statement block in statement block in " + parent.getName();
		}
		if (parent.getParent() instanceof BlockScope)
		{
			return "statement block in statement block in " + parent.getName();
		}
		if ((parent instanceof BlockScope) ||
			(parent instanceof MethodScope))
		{
			return "statement block in " + parent.getName();
		}
		return parent.getName();
	}
}
