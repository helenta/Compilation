package IC.AST;

/**
 * Enum of the IC language's primitive data types.
 * 
 * @author Tovi Almozlino
 */
public enum DataTypes
{
	INT(0, "int"), BOOLEAN(false, "boolean"), STRING(null, "string"), VOID(null,
	    "void");

	private Object	value;

	private String	description;

	private DataTypes(Object value, String description)
	{
		this.value = value;
		this.description = description;
	}

	public Object getDefaultValue()
	{
		return value;
	}

	public String getDescription()
	{
		return description;
	}

}
