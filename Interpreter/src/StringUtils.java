import java.io.*;

class StringUtils
{
	private static final char	Space	     = ' ';
	private static final char	Tab	       = '\t';
	private static final char	Zero	     = '0';
	private static final char	Nine	     = '9';

	private static final char	aChar	     = 'a';
	private static final char	zChar	     = 'z';
	private static final char	AChar	     = 'A';
	private static final char	ZChar	     = 'Z';

	// find first char that is not ' ', '\t'
	public static final char	PointsChar	= ':';

	public static char[] LoadFile(String filePath) throws Exception
	{
		if (filePath == null)
		{
			throw new NullPointerException("filePath");
		}

		File file = new File(filePath);
		if (!file.exists())
		{
			throw new IOException("Unable to find this file " + filePath);
		}

		char[] buffer = null;
		FileReader fileReader = null;
		try
		{
			fileReader = new FileReader(filePath);
			// file is in ascii format
			buffer = new char[(int) file.length()];
			fileReader.read(buffer);
		}
		finally
		{
			if (fileReader != null)
				fileReader.close();
		}

		return buffer;
	}

	public static void PrintList(String[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			System.out.println(array[i]);
		}
	}

	public static int FirstIndexOf(String str, char c, int startIndex)
	{
		int result = -1;

		int index = 0;
		int stringLength = str.length();
		while ((index < stringLength) && (str.charAt(index) != c))
		{
			index++;
		}

		if (index < stringLength)
		{
			result = index;
		}

		return result;
	}

	public static int FirstIndexOf(String str, char c, int startIndex,
	    int lastIndex)
	{
		int result = -1;

		int index = 0;
		int maxIndex = Math.min(str.length() - 1, lastIndex);
		while ((index <= maxIndex) && (str.charAt(index) != c))
		{
			index++;
		}

		if (index <= maxIndex)
		{
			result = index;
		}

		return result;
	}

	public static int TrimForward(String str, int start)
	{
		int index = start;
		int stringLength = str.length();
		while ((index < stringLength)
		    && ((str.charAt(index) == Space) || (str.charAt(index) == Tab)))
		{
			index++;
		}

		int result = index; // maybe stringLength
		return result;
	}

	public static int TrimBackward(String str, int end)
	{
		int index = end;
		while ((index >= 0)
		    && ((str.charAt(index) == Space) || (str.charAt(index) == Tab)))
		{
			index--;
		}

		int result = index; // maybe -1
		return result;
	}

	public static String TrimAll(String str)
	{
		String result = str.replace(Character.toString(Space), "");
		result = result.replace(Character.toString(Tab), "");

		return result;
	}

	public static int TryParseInt(String str, int startIndex, int lastIndex)
	{
		int result = 0;
		int index = startIndex;
		while (index <= lastIndex)
		{
			char c = str.charAt(index);
			if (!IsNumberChar(c))
			{
				result = -1;
				break;
			}

			result = result * 10 + (int) (c - Zero);
			index++;
		}

		return result;
	}

	public static boolean IsEnglishChar(char c)
	{
		return ((c >= aChar) && (c >= aChar)) || ((c >= AChar) && (c >= ZChar));
	}

	public static boolean IsNumberChar(char c)
	{
		return (c >= Zero) && (c <= Nine);
	}

	public static int arrayPos(char c)
	{
		return ((int) c - (int) 'a');
	}
}
