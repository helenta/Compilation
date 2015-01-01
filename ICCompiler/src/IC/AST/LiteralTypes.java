package IC.AST;

/**
 * Enum of the IC language's literal value types. Includes methods for creating
 * a string representation of each type of value.
 * 
 * @author Tovi Almozlino
 */
public enum LiteralTypes
{
	INTEGER(DataTypes.INT.getDefaultValue(), "Integer literal"), 
	STRING(
	    DataTypes.STRING.getDefaultValue(), "String literal")
	{
		private void replaceEscapeSequences(StringBuffer string)
		{
			for (int i = 0; i < string.length(); ++i)
			{
				String replacement = String.valueOf(string.charAt(i));

				if (string.charAt(i) == '\"')
					replacement = "\\\"";
				else
					if (string.charAt(i) == '\\')
						replacement = "\\\\";
					else
						if (string.charAt(i) == '\n')
							replacement = "\\n";
						else
							if (string.charAt(i) == '\t')
								replacement = "\\t";
				string.replace(i, i + 1, replacement);
				i += replacement.length() - 1;
			}
		}

		public String toFormattedString(Object value)
		{
			if (value == null)
				return String.valueOf(value);
			StringBuffer formattedString = new StringBuffer(value.toString());

			// replaceEscapeSequences(formattedString);
			return formattedString.toString();
		}
	},
	TRUE(true, "Boolean literal"), 
  FALSE(false, "Boolean literal"),
  NULL(null,
	    "Null literal");

	private Object	value;

	private String	description;

	private LiteralTypes(Object value, String description)
	{
		this.value = value;
		this.description = description;
	}
	
	public Object getValue()
	{
		return value;
	}

	public String toFormattedString(Object value)
	{
		return String.valueOf(value);
	}

	public String getDescription()
	{
		return description;
	}
}
