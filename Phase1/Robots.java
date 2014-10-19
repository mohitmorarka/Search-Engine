import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;


public class Robots {

public boolean CheckRobots(Wrapper wrpobj){
		
		String u = wrpobj.getUrl();
		URL url=null;
		try {
			url = new URL(u);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		String urlHost;
		urlHost = url.getHost();
		
		if (!url.getProtocol().startsWith("http"))
			return false;
		
		String RobotStr = "http://"  + urlHost + "/robots.txt";
		URL RobotUrl=null;
		try{
			RobotUrl = new URL (RobotStr);
		} catch( MalformedURLException e){
			return false;
		}
		
		String strCommands;
		
		try{
			InputStream RobotUrlStream = RobotUrl.openStream();
			int numRead;
			byte b[] = new byte[1000]; 
			numRead = RobotUrlStream.read(b);
			strCommands = new String(b, 0, numRead);	
			while(numRead!=-1)
			{
				numRead = RobotUrlStream.read(b);
				if(numRead!=-1)
				{
					String newCommands = new String(b, 0, numRead);
					strCommands = strCommands + newCommands;
				}
			}
			
			//System.out.println("Get File: " + x); 
			RobotUrlStream.close();
		
		//if no robots.txt return true	
		} catch (RuntimeException e) {
			
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return true;
		}
		String UrlStr = url.getFile();
		//System.out.println("URL String: " + UrlStr);
		int index=0;
		while ((index = strCommands.indexOf("Disallow:", index)) != -1){
			index = index + 9;
			String strPath = strCommands.substring(index);
			StringTokenizer st = new StringTokenizer(strPath);
			
			if (!st.hasMoreTokens())
				break;
			String disallowedPath = st.nextToken();
			
			if(UrlStr.indexOf(disallowedPath) == 0)
				return false;
		}
		return true;	
	}
}
