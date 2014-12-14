package IC.Semantic;

import java.util.ArrayList;
import java.util.List;

public class BlockScope extends Scope {

	public List<Scope> locals;
	public List<Scope> blocks;
	
	BlockScope(Scope parent, String name, int line) {
		super(parent, name, line);
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
	}
	
	public BlockScope(String name, int line) {
		super(name, line);
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
	}
	

	public BlockScope(int line) {
		super("", line);
		locals = new ArrayList<Scope>();
		blocks = new ArrayList<Scope>();
	}

	@Override
	public Object accept(ScopeVisitor visitor) {
		return visitor.visit(this);
	}

//	public BlockType getType() {
//		return type;
//	}
//
//	public void setType(BlockType type) {
//		this.type = type;
//	}

//	public enum BlockType {
//
//		IF("if"), 
//		WHILE("while"), 
//		BLOCK("block"), 
//		ELSE("else");
//		
//		private String description;
//
//		private BlockType(String description) {
//			this.description = description;
//		}
//
//		@Override
//		public String toString()
//		{
//			return description;
//		}
//	}
	
	@Override
	public String getName(){
		if (parent instanceof BlockScope){
			return parent.getName();
		}
		return "@" + parent.getName();
	}
}
