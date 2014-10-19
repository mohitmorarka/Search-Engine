import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class Hadoop_Searcher {
	public static int n = 1000; //number of documents
	public static double k1 = 1.2;
	public static double k2 = 100;
	public static double b = 0.75; 
	public static void main(String[] args) {
 
	JSONParser parser = new JSONParser();
 
	try {

		String query = args[0];
		//String query = "college";
		File folder = new File("C:\\Crawler\\Out");//index location
		File[] listOfFiles = folder.listFiles();
		ConcurrentMap<Integer,String> Discovered = new ConcurrentHashMap<Integer,String>();
		BufferedReader in = new BufferedReader(new FileReader("C:\\workspace\\Json\\src\\Hash.txt"));
		String line = "";
		ConcurrentMap<String,Double> exist = new ConcurrentHashMap<String,Double>();
		try {
			while ((line = in.readLine()) != null) {
			    String parts[] = line.split("\t");
			    Discovered.put(Integer.parseInt(parts[0]), parts[1]);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
				BufferedReader in1 = new BufferedReader(new FileReader(file));
		
				while (in1.ready()) {
					String s = in1.readLine();
					Object obj = parser.parse(s);
					JSONObject jsonObject = (JSONObject) obj;
					String name = (String) jsonObject.get("word");
					
					if (name.equals(query))
					  {
						String posting = (String)jsonObject.get("posting");
						//System.out.println("posting"+posting);
						String [] pages = posting.split(",");
						System.out.println("Query:"+name);
				        for(String page: pages)
						{   int ni = pages.length;
						    double temprr = (0.8);
						    double k = k1 * ((1-b) + b * temprr);; //ignore document size
							int f1 = Integer.parseInt(page.substring(page.indexOf("(")+1, page.indexOf(")")));
							int qf1 = 1;
				        	double partialRank = Math.log10(n/ni - 1 ) * (k1 + 1) * f1 / (k +f1) * (k2+1) * qf1 / (k2 + qf1);
							if(!exist.containsKey(page.substring(0, page.indexOf("("))))
							{
								//System.out.print(page.substring(0, page.indexOf("(")));
								//System.out.print("\t");
								String Url = Discovered.get(Integer.parseInt(page.substring(0, page.indexOf("."))));
								exist.put(page.substring(0, page.indexOf("(")), partialRank);
								//System.out.print(Url);
								//System.out.println("");*/
							}
							else
							{
								double temprank = exist.get(page.substring(0, page.indexOf("(")));
								temprank = temprank + partialRank;
								exist.put(page.substring(0, page.indexOf("(")), temprank);
								
							}
						}
					  }
				}		
		  }
		}
		PriorityQueue<Page> pq1 = new PriorityQueue<Page>();
		for (Map.Entry<String, Double> result : exist.entrySet())
		{
			Page tempDoc = new Page();
	        tempDoc.docId = result.getKey();
	        tempDoc.rank = (result.getValue()/100);
	        String Url = Discovered.get(Integer.parseInt(result.getKey().substring(0, result.getKey().indexOf("."))));
	        tempDoc.URL = Url;
	        pq1.offer(tempDoc);
	        
		}
		Page page;
		int i=1;
		while((page = pq1.poll()) != null) {
			System.out.println(i+")_"+"URL  "+"<a href="+"'"+page.URL+"'"+">"+page.URL+"</a>");
			System.out.println("DOC ID:" + page.docId +"\t\t and \t\t"+"\t\t Score:" +page.rank);
			System.out.println("<br/>");
			System.out.println("<br/>");
			i++;
		}	
	}
		    catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
 
     }
     /*public static Comparator<Page> idComparator = new Comparator<Page>(){
 	    
 	    @Override
 	    public int compare(Page c1, Page c2) {
 	        return (int) (c1.rank - c2.rank);
 	    }
 	};	*/
 }
 
