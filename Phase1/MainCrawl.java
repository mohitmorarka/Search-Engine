import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainCrawl 
{
	
	public void crawl(ArrayBlockingQueue<Wrapper> Frontier, ConcurrentMap<String,Integer> Discovered, CountClass count, int t) 
	{
	Exceptionlog el=new Exceptionlog(count.getdir());
	Robots r = new Robots();
	DownloadFile dow = new DownloadFile(Discovered, t,count.getdir());
	BuildUrl bu = new BuildUrl();
	CleanUrl c = new CleanUrl();
	String builtUrl;
	int b=0;
	int hop;
	int i=0;
	while(count.get() <= count.getP())
	{
		if(b==1)
		{
			break;
		}
		if (Frontier.peek() == null)
		{
			System.out.println("Frontier empty!" + t);
			break;
		}
		Wrapper First =  Frontier.poll();
		count.set(count.get() + 1);   //Check once
		//hop = First.getHops();
		if (r.CheckRobots(First))
			{
				try {
						Elements Links = dow.downloadFile(First);
						for (Element link:Links)
						{
							String u = link.attr("href");
							builtUrl = bu.buildUrl(u, First);
							if (builtUrl != "")
							{
								if(c.cleanUrl(builtUrl))
								{
									if (Discovered.get(builtUrl) == null)
									{	
										hop=First.getHops() + 1;
										if (hop > count.getmd())
										{
											b=1;
											break;
										}		
										Wrapper builtwrpd = new Wrapper(builtUrl,hop);
										Frontier.add(builtwrpd);
										//System.out.println("URL = " +  builtwrpd.getUrl() + " hop = " + builtwrpd.getHops() + "Thread = " + t);
										Discovered.put(builtwrpd.getUrl(), new Integer(++i));
									}
								}
							}
			
						}
			
			
				}
	
				catch (NullPointerException e)
				{
					el.appendToFile(e);
				}
				catch(UnknownHostException e)
				{
					//System.out.println("Host not found!");
					el.appendToFile(e);
					
				}
				catch(HttpStatusException e)
				{
					//System.out.println("Page not found!!");
					el.appendToFile(e);
				}
				catch (IOException e) {
		
					el.appendToFile(e);
				} catch (Exception e) {
					
					el.appendToFile(e);
				}
	
			}
	}
	}
}

