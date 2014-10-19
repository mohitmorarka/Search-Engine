import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;


public class Exceptionlog {
	
	String dir;
	
	Exceptionlog(String d)
	{
		dir=d;
	}
	public void appendToFile(Exception e) {
	      try {
	         FileWriter fstream = new FileWriter(dir +"exception.txt", false);
	         BufferedWriter out = new BufferedWriter(fstream);
	         PrintWriter pWriter = new PrintWriter(out, true);
	         e.printStackTrace(pWriter);
	      }
	      catch (Exception ie) {
	         throw new RuntimeException("Could not write Exception to file", ie);
	      }
	   }
}
