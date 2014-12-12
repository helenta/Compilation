import java.util.ArrayList;
import java.util.List;


public class Main
{

	private static String[] cleanEmptyRows(String[] lines) 
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].length() > 2) { 
				list.add(lines[i]);
			}
		}
		String[] newLines = new String[list.size()];
		newLines = list.toArray(newLines);
		return newLines;
	}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	String[] lines;
    	PolishProgram program;
    	
        if (args.length < 1)
        {
            System.out.println("Wrong parameter number");
            return;
        }
        
        String filePath = args[0];
           
        //fetching file
        try
        {
            char[] fileChars = StringUtils.LoadFile(filePath);
            String str = new String(fileChars);
            lines = str.split(";");
            lines = cleanEmptyRows(lines);
            
        }
        catch (Exception e)
        {
            //System.out.println("Exception: " + e.getMessage());
            return;
        }
        
        //fetching lines into a program class, and performing syntax check
        try 
        {
           program = new PolishProgram(lines);
           program.Execute();
           program.PrintErrors();
        }
        catch (Exception e)
        {
            //System.out.println("Exception: " + e.getMessage());
            return;
        }
                
        return;
    }
    
}
