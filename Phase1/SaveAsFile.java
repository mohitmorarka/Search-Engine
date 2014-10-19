import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class SaveAsFile {
	String dir;
	SaveAsFile(String d)
	{
		dir = d;
	}
	public void saveAsFile (int name, String content) 
	{
		
		Exceptionlog el=new Exceptionlog(dir);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(dir + name +".txt",true));
		} catch (IOException e1) {
			
			el.appendToFile(e1);
		}
		try {
			out.write(content);
		} catch (IOException e) {
		
			el.appendToFile(e);
		}
		try {
			out.close();
		} catch (IOException e) {
			
			el.appendToFile(e);
		}

	}
}
