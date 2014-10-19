import java.net.MalformedURLException;
import java.net.URL;


public class BuildUrl {
	public String buildUrl (String raw, Wrapper wrpobj) 
	{
		CountClass count=new CountClass();
		Exceptionlog el=new Exceptionlog(count.getdir());
		String Seed = wrpobj.getUrl();
		URL url = null;
		
			 try {
				url = new URL(Seed);
			} catch (MalformedURLException e) {
				el.appendToFile(e);
			}
		
		if (raw.length() < 1) {
			return "";
			}
			// Skip links that are just page anchors.
			if (raw.charAt(0) == '#') {
				return "";
			}
			// Skip mailto links.
			if (raw.indexOf("mailto:") != -1) {
				return "";
			}
			// Skip JavaScript links.
			if (raw.toLowerCase().indexOf("javascript") != -1) {
				return "";
			}
			
			if (raw.indexOf("://")== -1)
			{
				
				if (raw.charAt(0) == '/')
				{
					raw = url.getProtocol() + "://" + url.getHost() + (url.getPort() >= 0 ? "" + url.getPort() : "") + raw;
				}
				
				else
				{
					String file = url.getFile();
					if (file.indexOf("/") == -1)
					{
						raw = url.getProtocol() + "://" + url.getHost() + "/" + raw;
					}
					else
					{
						raw = url.getProtocol() + "://"  + url.getHost() + "/" + raw;
					}
			}
			}
			if (raw.indexOf("www") == -1)
			{
				StringBuffer str1 = new StringBuffer(raw);
				str1.insert(7,"www.");
				raw = new String(str1);
				
			}
			return raw;
	}
}
