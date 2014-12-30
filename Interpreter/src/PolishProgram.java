import java.util.*;

/**
 *
 * @author Tammy
 */
class PolishProgram
{
	// cmd error codes
	public static final int	          ErrorCodeGrammer	     = 1;
	public static final int	          ErrorCodeGoto	         = 2;
	public static final int	          ErrorCodeLable	       = 3;
	public static final int	          ErrorCodeInitalization	= 4;

	public final String[]	            Lines;

	// private StatementPrefix[] m_Prefixes;
	private int	                      m_CurrentLine;
	private Command[]	                m_CommandList;
	private int[]	                    m_Vars;
	// private HashMap<Integer, StatementPrefix> m_StatementNumberToIndex;
	private List<ErrorTupple>	        m_Errors;
	private HashMap<Integer, Integer>	m_MapLines;	               // Holds
	                                                               // mapping from
	                                                               // label to
	                                                               // line number

	PolishProgram(String[] lines)
	{
		Lines = lines;

		// vars array init
		m_Vars = new int[26];
		for (int i = 0; i < 26; i++)
		{
			m_Vars[i] = -1;
		}
		m_CommandList = new Command[Lines.length];
		m_MapLines = new HashMap<>();
		m_Errors = new ArrayList<>();
		FetchLabels();
		// Fetching lines to Statements
		fetchingLinesToCommandList();

	}

	/*** Methods for lines fetching ***/

	private void FetchLabels()
	{
		// fetching the labels. Should assign the right values in m_MapLines
		// should update the lines[] to include only the substring after the char ':
		// '
		String[] splittedLine;
		int label;
		int previousLabel = 0;
		for (int lineNumber = 0; lineNumber < Lines.length; lineNumber++)
		{
			m_CurrentLine = lineNumber;
			try
			{
				splittedLine = Lines[lineNumber].split(":", 2);
				label = Integer.parseInt(splittedLine[0].trim());
				if (label > previousLabel)
				{
					previousLabel = label;
				}
				else
				{
					AddError(ErrorCodeLable, "");
				}
				Lines[lineNumber] = splittedLine[1].trim();
				m_MapLines.put(label, lineNumber);
			}
			catch (Exception ex)
			{
				// System.out.println(ex);
				// System.out.println(lineNumber);
			}
		}

	}

	public boolean LabelExists(int label)
	{
		return m_MapLines.containsKey(label);
	}

	// 1. create new command
	// 2. add to m_CommandList
	private void fetchingLinesToCommandList()
	{
		for (int i = 0; i < Lines.length; i++)
		{
			m_CurrentLine = i;
			Command command = new Command(this, Lines[i]);
			m_CommandList[i] = command;
		}
	}

	/*** Methods for execute stage ***/

	public void AssignVar(char c, int value)
	{
		m_Vars[StringUtils.arrayPos(c)] = value;
	}

	public int GetVarValue(char c)
	{
		return m_Vars[StringUtils.arrayPos(c)];
	}

	/*** Error handling methods ***/

	public void Print(int val)
	{
		if (m_Errors.isEmpty())
		{
			System.out.println("" + val);
		}
	}

	public void AddError(int errorType, String message)
	{
		// adds an errorTuple to the errors list
		// The parameter 'message' is only for debugging purposes
		ErrorTupple currentError = new ErrorTupple(m_CurrentLine + 1, errorType,
		    message);
		m_Errors.add(currentError);
	}

	public void PrintErrors()
	{
		Collections.sort(m_Errors, new Comparator<ErrorTupple>()
		{

			@Override
			public int compare(ErrorTupple o1, ErrorTupple o2)
			{
				return (o1.LineNumber > o2.LineNumber) ? 1 : -1;
			}
		});

		int lastLine = 0;
		for (int i = 0; i < m_Errors.size(); i++)
		{
			ErrorTupple tupple = m_Errors.get(i);
			if (tupple.LineNumber != lastLine)
			{
				System.out.println("Error! Line:" + tupple.LineNumber + " Code:"
				    + tupple.Code);
			}
			lastLine = tupple.LineNumber;

		}
	}

	public void Execute() throws Exception
	{
		if (m_Errors.isEmpty())
		{
			int nextCommand = 0;
			while (nextCommand < Lines.length)
			{
				m_CurrentLine = nextCommand;
				int label = m_CommandList[nextCommand].Execute(this);
				// Label is not valid
				if (label == -1)
				{
					break;
				}
				// Label is valid
				else
					if (label == -10)
					{
						nextCommand++;
					}
					// Command is 'goto' type, Label is of previous line
					else
					{
						nextCommand = m_MapLines.get(label);
					}
			}
		}
	}

	public void checkLabelErrors(int label)
	{
		int lineNumber = m_MapLines.get(label) + 1;
		Iterator<ErrorTupple> itr = m_Errors.iterator();
		while (itr.hasNext())
		{
			if (lineNumber == itr.next().LineNumber)
			{
				AddError(ErrorCodeGoto, "");
				break;
			}
		}
	}

}
