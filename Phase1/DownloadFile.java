import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class DownloadFile {
	
	ConcurrentMap<String, Integer> Discovered;
	int tnum;
	String Dir;
	DownloadFile(ConcurrentMap<String, Integer> d, int t1, String d1)
	{
		Discovered=d;
		tnum = t1;
		Dir=d1;
	}
	
	
		public Elements downloadFile(Wrapper wrpobj) throws Exception {
			
		SaveAsFile s = new SaveAsFile(Dir);	
		String url = wrpobj.getUrl();
		//System.out.println("Parsing and fetching links in :");
		System.out.println("Thread: " + tnum + " " + url);
		
		Connection connection = Jsoup.connect(url);
		connection.timeout(5000);
		
		Document doc = connection.get();
		String htmlcontent=doc.html();
		int fileno;
		fileno = Discovered.get(wrpobj.getUrl());
		s.saveAsFile(fileno,htmlcontent);
		Elements links = doc.select("a[href]");
		return links;

		}
}
