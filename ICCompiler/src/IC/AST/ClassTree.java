package IC.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public final class ClassTree
{
	public ICClass iCClass;
	public ICClass supperClass;
	
	public ClassTree supperClassTree;
	
	public List<ClassTree> derivedClasses = new ArrayList<ClassTree>();
	
	public static HashMap<String, ClassTree> ConstructRootClasses(HashMap<String, ICClass> allClasses)
	{
		HashMap<String, ClassTree> allClasseTrees = new HashMap<String, ClassTree>();
		
		for (Entry<String, ICClass> entry : allClasses.entrySet())
		{
			String className = entry.getKey();
			ICClass icClass = entry.getValue();
			
			ClassTree classtree = null;
			if (allClasseTrees.containsKey(className))
			{
				classtree = allClasseTrees.get(className);
			}
			else
			{
				classtree = new ClassTree();
				classtree.iCClass = icClass;
				if (icClass.getSuperClassName() != null)
				{
					classtree.supperClass = allClasses.get(icClass.getSuperClassName());
				}
				
				allClasseTrees.put(className, classtree);
			}
		}
		
		for (Entry<String, ClassTree> entry : allClasseTrees.entrySet())
		{
			ClassTree current = entry.getValue();
			if (current.supperClass != null)
			{
				ClassTree supperClassTree = allClasseTrees.get(current.supperClass.getName());
				current.supperClassTree = supperClassTree;
				current.supperClassTree.derivedClasses.add(current);
			}
		}
		
		return allClasseTrees;
	}
	
	public List<VirtualMethod> GetAllClassMethod()
	{
		ArrayList<ICClass> fromParentToCurrent = new ArrayList<ICClass>();
		
		ClassTree current = this;
		fromParentToCurrent.add(current.iCClass);
		
		while(current.supperClassTree != null)
		{
			fromParentToCurrent.add(0, current.supperClassTree.iCClass);
			current = current.supperClassTree;
		}
		
		List<VirtualMethod> result = new ArrayList<VirtualMethod>();
		for (ICClass icClass : fromParentToCurrent)
		{
			for (Method method : icClass.getMethods())
			{
				if (!(method instanceof VirtualMethod))
				{
					continue;
				}
				
				String methodName = method.getName();
				int index = IndexOf(result, methodName);
				if (index == -1)
				{
					result.add((VirtualMethod)method);
				}
				else
				{
					result.set(index, (VirtualMethod)method);
				}
			}
		}
		
		return result;
	}
	
	public List<Field> GetAllClassFields()
	{
		ArrayList<ICClass> fromParentToCurrent = new ArrayList<ICClass>();
		
		ClassTree current = this;
		fromParentToCurrent.add(current.iCClass);
		
		while(current.supperClassTree != null)
		{
			fromParentToCurrent.add(0, current.supperClassTree.iCClass);
			current = current.supperClassTree;
		}
		
		List<Field> result = new ArrayList<Field>();
		for (ICClass icClass : fromParentToCurrent)
		{
			for (Field field : icClass.getFields())
			{
				String fieldName = field.getName();
				result.add(field);
			}
		}
		
		return result;
	}
	
	private static int IndexOf(List<VirtualMethod> methods, String methodName)
	{
		int result = -1;
		for (int i = 0; i < methods.size(); i++)
		{
			String name = methods.get(i).getName();
			if (name.equals(methodName))
			{
				result = i;
				break;
			}
		}
		
		return result;
	}
	
}

