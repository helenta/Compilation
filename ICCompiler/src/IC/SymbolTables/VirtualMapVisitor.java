package IC.SymbolTables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import IC.AST.*;

public class VirtualMapVisitor implements Visitor
{
	HashMap<String, VirtualMap>	classMap;
	List<VirtualMap>	          extended;
	String	                    currentClass;
	SymbolTable	                table;

	public VirtualMapVisitor(HashMap<String, VirtualMap> map, SymbolTable table)
	{
		this.classMap = map;
		extended = new ArrayList<VirtualMap>();
		this.table = table;
	}

	public Object visit(Program program)
	{
		for (ICClass cls : program.getClasses())
		{
			cls.accept(this);
		}
		for (VirtualMap ext : extended)
		{
			ext.addSuperMethodsAndFields(classMap.get(ext.getScope().getParent()
			    .getName()));
		}
		return null;
	}

	public Object visit(ICClass icClass)
	{
		currentClass = icClass.getName();

		if (icClass.hasSuperClass())
		{
			VirtualMap superClass = classMap.get(icClass.getSuperClassName());
			if (superClass == null)
			{
				VirtualMap extCls = new VirtualMap(icClass.getName(), icClass.scope);
				classMap.put(icClass.getName(), extCls);
				extended.add(extCls);
			}
			else
			{
				VirtualMap extCls = VirtualMap.createExtendsClass(icClass.getName(),
				    superClass, icClass.scope);
				classMap.put(icClass.getName(), extCls);
			}
		}
		else
		{
			classMap.put(icClass.getName(), new VirtualMap(icClass.getName(),
			    icClass.scope));
		}
		for (Field field : icClass.getFields())
		{
			field.accept(this);
		}
		for (Method method : icClass.getMethods())
		{
			if (method instanceof StaticMethod)
			{
				method.accept(this);
			}
			if (method instanceof VirtualMethod)
			{
				method.accept(this);

			}
		}

		return null;
	}

	public Object visit(Field field)
	{
		String className = currentClass;
		classMap.get(className).addField(field.getName());
		return null;
	}

	public Object visit(VirtualMethod method)
	{
		String className = currentClass;
		classMap.get(className).addMethod(method.getName(), className,
		    (MethodScope) table.getSymbol(method.scope, method.getName()));
		return null;
	}

	public Object visit(StaticMethod method)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(LibraryMethod method)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Formal formal)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(PrimitiveType type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Type type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Assignment assignment)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(CallStatement callStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Return returnStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(If ifStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(While whileStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Break breakStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Continue continueStatement)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(StatementsBlock statementsBlock)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(LocalVariable localVariable)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(VariableLocation location)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(ArrayLocation location)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(StaticCall call)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(VirtualCall call)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(This thisExpression)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(NewClass newClass)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(NewArray newArray)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Length length)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(MathBinaryOp binaryOp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(LogicalBinaryOp binaryOp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(MathUnaryOp unaryOp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(LogicalUnaryOp unaryOp)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(Literal literal)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(ExpressionBlock expressionBlock)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
