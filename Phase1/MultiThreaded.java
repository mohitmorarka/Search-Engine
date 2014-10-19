import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MultiThreaded {

	public static void main(String[] args)
{
		CountClass count=new CountClass();
		Exceptionlog el=new Exceptionlog(count.getdir());
		
		String builtUrl = null;
		BufferedReader in =null;
		try {
			in = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e1) {
			el.appendToFile(e1);
		}
		String Ucr=null;
		try {
			Ucr = new String(in.readLine());
		} catch (IOException e1) {
			el.appendToFile(e1);
		}
//		String Ucr = "http://www.ucr.edu/";
		MainCrawl mc = new MainCrawl();
		int maxd = Integer.parseInt(args[2]);
		Wrapper Seed = new Wrapper(Ucr, 0);
		String dir = args[3];
		ArrayBlockingQueue<Wrapper> Frontier = new ArrayBlockingQueue<Wrapper>(30000);
		ConcurrentMap<String,Integer> Discovered = new ConcurrentHashMap<String,Integer>();
		
		
		count.set(1);
		count.setmd(maxd);
		count.setP(Integer.parseInt(args[1]));
		count.setdir(args[3]);
		
		Frontier.offer(Seed);
		Discovered.put((Seed.getUrl()), new Integer(1));

	
		new NThreadCrawl(Frontier, Discovered,count,1);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			el.appendToFile(e);
		}
		
		new NThreadCrawl(Frontier, Discovered,count,2);
		new NThreadCrawl(Frontier, Discovered,count,3);
		new NThreadCrawl(Frontier, Discovered,count,4);
		
		mc.crawl(Frontier, Discovered, count, 5);

	}

}
