
class Command 
{
    public CmdType m_Type = CmdType.None;
    
    private char m_VarName1 = 0;
    private char m_VarName2 = 0;
    private int m_Lable = -1;
    private Expression m_Expression = null;
    private BoolOp m_BoolOp = BoolOp.None;
    private Command m_Command = null;
    
    public Command(PolishProgram program, String strCommand) 
    {
        //Receive the PARTIAL string - without the label and the ':'. The first char should not be whitespace!
        //Should parse the string into the object
        //Should update the error list in the program in case there is an error
        //should handle error 1
        //should handle error 2
        
        int startIndex = StringUtils.TrimForward(strCommand, 0);
        if (startIndex >= strCommand.length() - 4)
        {
            return;
        }
        
        strCommand = strCommand.substring(startIndex);
        
        char firstChar = strCommand.charAt(0);
        if (!StringUtils.IsEnglishChar(firstChar))
        {
            program.AddError(PolishProgram.ErrorCodeGrammer, "Expected english char");
            return;
        }
        
        if (strCommand.startsWith("print"))
        {
            strCommand = strCommand.substring(5);
            strCommand = strCommand.replaceAll(";", "");
            strCommand = strCommand.trim();
            if (!strCommand.startsWith("(") || !strCommand.endsWith(")"))
            {
                program.AddError(PolishProgram.ErrorCodeGrammer, "Expected '(' and ') for print cmd");
                return;
            }
            
            strCommand = strCommand.substring(1, strCommand.length() - 1);
            strCommand = strCommand.trim();
            
            m_Expression = new Expression(program, strCommand);
            if (m_Expression.CheckSyntax(program))
            {
                m_Type = CmdType.Print;
            }
        }
        else if (strCommand.startsWith("if"))
        {
            strCommand = strCommand.substring(2);
            strCommand = strCommand.trim();
            
            int index = strCommand.indexOf(')');
            if (!strCommand.startsWith("(") || (index == -1))
            {
                program.AddError(PolishProgram.ErrorCodeGrammer, "Exprcted ') and ') for if cmd");
                return;
            }
            
            String ifExp = strCommand.substring(1, index);
            ifExp = StringUtils.TrimAll(ifExp);
            
            if (ifExp.length() < 3)
            {
                program.AddError(PolishProgram.ErrorCodeGrammer, "Exprcted too vars and boolean op");
                return;
            }
            
            m_VarName1 = ifExp.charAt(0); 
            char boolOp = ifExp.charAt(1);
            char next = ifExp.charAt(2);
            char third = ifExp.length() == 4 ? ifExp.charAt(3) : 0;
            if (boolOp == '<' || boolOp == '>')
            {
                if (next == '=' && StringUtils.IsEnglishChar(third))
                {
                    m_BoolOp = boolOp == '<' ? BoolOp.smallerEquals : BoolOp.biggerEquals;
                    m_VarName2 = third;
                }
                else if (StringUtils.IsEnglishChar(next) && third == 0)
                {
                    m_BoolOp = boolOp == '<' ? BoolOp.samller : BoolOp.bigger;
                    m_VarName2 = next;
                }
                else
                {
                    program.AddError(PolishProgram.ErrorCodeGrammer, "Unable to parse bool op 1");
                    return;
                }
            }
            else if ((boolOp == '=' || boolOp == '!') && (next == '=' && StringUtils.IsEnglishChar(third)))
            {
                m_BoolOp = boolOp == '=' ? BoolOp.equals : BoolOp.differ;
                m_VarName2 = third;
            }
            else
            {
                program.AddError(PolishProgram.ErrorCodeGrammer, "Unable to parse bool op 2");
                return;
            }
            
            
            strCommand = strCommand.substring(index + 1);
            strCommand = strCommand.trim();
            
            m_Command = new Command(program, strCommand);
            if (m_Command.m_Type != CmdType.None)
            {
                m_Type = CmdType.BoolOpertion;
            }
        }
        else if (strCommand.startsWith("goto"))
        {	
            strCommand = strCommand.substring(4);
            strCommand = strCommand.replaceFirst(";", "");
            strCommand = strCommand.trim();
            
            int lable = Integer.parseInt(strCommand);
            if (lable < 0)
            {
            	program.AddError(PolishProgram.ErrorCodeGrammer, "Unable parse int");
                return;
            }
            else if (!program.LabelExists(lable))
            {
                program.AddError(PolishProgram.ErrorCodeGoto, "Wrong lable in goto op.");
                return;
            }
            else {
            	program.checkLabelErrors(lable);
            }
          
            m_Lable = lable;
            m_Type = CmdType.Goto;
        }
        else // try to create assignment command
        {
            int index = strCommand.indexOf(":=");
            if (index == -1) // check if we have only one letter and := after that
            {
                program.AddError(PolishProgram.ErrorCodeGrammer, "Expected ':=' for assignement op.");
                return;
            }
            
            if (strCommand.charAt(index+2) != ' ')
        	{
        		program.AddError(PolishProgram.ErrorCodeGrammer, "No space at expression start");
        		return;
        	}
            
            String expCommand = strCommand.substring(index+3);
            m_VarName1 = firstChar;
            m_Expression = new Expression(program, expCommand);
            if (m_Expression.CheckSyntax(program))
            {
                m_Type = CmdType.Assignment;
            }
        }
    }

    public int Execute(PolishProgram program) throws Exception
    {
        //execute the command.
        //returns -10 - standard running
        //returns label in goto - in case the command was goto
        //returns -1 - in case there is a runtime error
        
        int expressionVal = -1;
        switch(m_Type)
        {
            case Assignment:
            {
                expressionVal = m_Expression.Execute(program);
                if (expressionVal != -1)
                {
                    program.AssignVar(m_VarName1, expressionVal);
                    return -10;
                }
                return -1;
            }
            case Goto:
                return m_Lable;
            case Print:
            {
                expressionVal = m_Expression.Execute(program);
                if (expressionVal != -1)
                {
                    program.Print(expressionVal);
                    return -10;
                }
                return -1;
            }
            case BoolOpertion:
	            {
	            	int val1 = program.GetVarValue(m_VarName1);
	            	if (val1 == -1)
	            	{
	            		program.AddError(PolishProgram.ErrorCodeInitalization, "Not initalized var " + m_VarName1);
	            		return -1;
	            	}
	            	
	            	int val2 = program.GetVarValue(m_VarName2);
	            	if (val2 == -1)
	            	{
	            		program.AddError(PolishProgram.ErrorCodeInitalization, "Not initalized var " + m_VarName2);
	            		return -1;
	            	}
	            	
	            	if (EveluateBoolOpearion(val1, val2, m_BoolOp))
	            	{
	            		return m_Command.Execute(program);
	            	}
	            	else
	            	{
	            		return -10;
	            	}
	            }
            default:
                throw new Exception("Why there is some execution?");
        }
    }
    
    private static boolean EveluateBoolOpearion(int val1, int val2, BoolOp boolOp) throws Exception
    {
    	switch(boolOp) 
    	{
			case bigger:
				return val1 > val2;
			case biggerEquals:
				return val1 >= val2;
			case differ:
				return val1 != val2;
			case equals:
				return val1 == val2;
			case samller:
				return val1 < val2;
			case smallerEquals:
				return val1 <= val2;

		default:
			throw new Exception("Why there is some execution?");
		}
    }
    
}