import java.net.URL;


public class CleanUrl {
public boolean cleanUrl( String u) throws Exception
	
	{
		if (!u.contains("http://") || !u.contains(".edu") || u.contains("#") || u.contains(".pdf") || u.contains(".jpg") || u.contains(".png") || u.contains("jif") || u.contains(".doc") || u.contains("docx") || u.contains("mailto:") || u.contains(".rtf") || u.contains(".ppt")|| u.contains("cgi-bin"))
		{
			return false;
		}
		
		return true;
	}
}
