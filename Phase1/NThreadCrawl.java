import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentMap;


public class NThreadCrawl implements Runnable {
	FileWriter f;
	PrintWriter p;
	MainCrawl mc = new MainCrawl();
	Thread t;
	int tnum;
	String Dir;
	ArrayBlockingQueue<Wrapper> Frontier;
	CountClass count;
	ConcurrentMap<String,Integer> Discovered;
	
	NThreadCrawl(ArrayBlockingQueue<Wrapper> Fr, ConcurrentMap<String,Integer> Di, CountClass co, int t1){
		Frontier=Fr;
		Discovered=Di;
		count=co;
		tnum = t1;
		t=new Thread(this,"t1");
		t.start();
		
	}
	public void run()
	{
			mc.crawl(Frontier, Discovered, count,tnum);

	}
}
