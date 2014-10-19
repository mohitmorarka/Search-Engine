import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Hadoop_Indexer {

  public static class LineIndexMapper extends MapReduceBase
      implements Mapper<LongWritable, Text, Text, Text> {

    
	  private static Set<String> googleStopwords;
	  
	    static {
	        googleStopwords = new HashSet<String>();
	        googleStopwords.add("I"); googleStopwords.add("a"); 
	        googleStopwords.add("about"); googleStopwords.add("an"); 
	        googleStopwords.add("are"); googleStopwords.add("as");
	        googleStopwords.add("at"); googleStopwords.add("be"); 
	        googleStopwords.add("by"); googleStopwords.add("com"); 
	        googleStopwords.add("de"); googleStopwords.add("en");
	        googleStopwords.add("for"); googleStopwords.add("from"); 
	        googleStopwords.add("how"); googleStopwords.add("in"); 
	        googleStopwords.add("is"); googleStopwords.add("it");
	        googleStopwords.add("la"); googleStopwords.add("of"); 
	        googleStopwords.add("on"); googleStopwords.add("or"); 
	        googleStopwords.add("that"); googleStopwords.add("the");
	        googleStopwords.add("this"); googleStopwords.add("to"); 
	        googleStopwords.add("was"); googleStopwords.add("what"); 
	        googleStopwords.add("when"); googleStopwords.add("where");
	        googleStopwords.add("who"); googleStopwords.add("will"); 
	        googleStopwords.add("with"); googleStopwords.add("and"); 
	        googleStopwords.add("the"); googleStopwords.add("www");
	        googleStopwords.add("("); googleStopwords.add(")");
	        googleStopwords.add("{"); googleStopwords.add("}");
	        googleStopwords.add("})"); googleStopwords.add("width");
	        googleStopwords.add("margin"); googleStopwords.add("color");
	        googleStopwords.add("background"); googleStopwords.add("border");
	        googleStopwords.add("id="); googleStopwords.add("http");
	    }
	  
	private final static Text word = new Text();
    //private final static Text key1 = new Text();
    //private final static Text count = new Text();
    private static Text count = new Text();
    private static Text location = new Text();
    public void map(LongWritable key, Text val,
        OutputCollector<Text, Text> output, Reporter reporter)
        throws IOException {

      FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
      String fileName = fileSplit.getPath().getName();
      String line = val.toString();
      HashMap hm = new HashMap();
      StringTokenizer itr = new StringTokenizer(line.toLowerCase());
      while (itr.hasMoreTokens()) {
    	Text key1 = new Text();
    	String value = itr.nextToken();
    	Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(value);
        StringBuilder add_key = new StringBuilder();
    	while (m.find())
    	{
    		String matchedKey = m.group().toLowerCase();
        //  remove names starting with non letters, digits, considered stopwords or containing other chars
        	if (!Character.isLetter(matchedKey.charAt(0)) || Character.isDigit(matchedKey.charAt(0))
                || googleStopwords.contains(matchedKey) || matchedKey.contains("_") || matchedKey.contains("}")
                || matchedKey.contains(")") || matchedKey.contains("{") || matchedKey.contains("width")) {
        		continue;
        	}
	    	if (hm.containsKey(value))
	    	{
	    		
	    		//java.util.Map.Entry tempMapVal = (java.util.Map.Entry) hm.get(tempString);
	    		hm.put(value, (Integer)hm.get(value) + 1);
	    	}
	    	else
	    	{
	    		hm.put(value, 1);
	    	}
    	}
        
     }
      
      Set set = hm.entrySet();
      Iterator i = set.iterator();
      while(i.hasNext()) {
          java.util.Map.Entry me = (java.util.Map.Entry)i.next();
          word.set((String)me.getKey());
          output.collect(word, new Text (fileName + "@" + String.valueOf( me.getValue()) ));
       }
	 
    } 
    
}
  



  public static class LineIndexReducer extends MapReduceBase
      implements Reducer<Text, Text, Text, Text> {

    @SuppressWarnings("unchecked")
	public void reduce(Text key, Iterator<Text> values,
        OutputCollector<Text, Text> output, Reporter reporter)
        throws IOException {

    	Boolean flag = false;
        String myPadding = null;
        JSONObject obj = new JSONObject();
    	try {
			obj.put("word", key.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	JSONArray list = new JSONArray();
        String DocId;
        String tf;
        while (values.hasNext()){
    		Text val = values.next();
        	StringTokenizer tokenizer = new StringTokenizer(val.toString());
    		DocId = tokenizer.nextToken("@");
    		tf = tokenizer.nextToken();
        	JSONObject tempObj = new JSONObject();
        	try {
				tempObj.put("ID", DocId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
				tempObj.put("tf", tf);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		list.put(tempObj);
        	
        }
    	try {
			obj.put("docs", list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Text newText = new Text(obj.toString());
        output.collect(newText, null);
     } 	
  }

  static class MultiFileOutput extends MultipleTextOutputFormat<Text, Text> {
      protected String generateFileNameForKeyValue(Text key,Text value,String name) {
              return "./"+"out"+key.toString().charAt(9);
      }
}
  /**
   * The actual main() method for our program; this is the
   * "driver" for the MapReduce job.
 * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    JobClient client = new JobClient();
    JobConf conf = new JobConf(Hadoop_Indexer.class);

    Path inputPath = new Path(args[0]);
	Path outputDir = new Path(args[1]);
	
    conf.setJobName("Hadoop_Indexer");
    
    conf.setInputFormat(TextInputFormat.class);
    //conf.setOutputFormat(MultiFileOutput.class);
    
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(conf, inputPath);
    FileOutputFormat.setOutputPath(conf, outputDir);
    
    
    // Delete output if exists
    FileSystem hdfs = FileSystem.get(conf);
    if (hdfs.exists(outputDir))
        hdfs.delete(outputDir, true);

    conf.setMapperClass(LineIndexMapper.class);
    conf.setReducerClass(LineIndexReducer.class);

    client.setConf(conf);

    try {
      JobClient.runJob(conf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  }
