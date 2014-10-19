
public class Wrapper {
	int hops;
	String url;	
	Wrapper(String url1, int d1)
	{
		hops=d1;
		url= new String(url1);
	}
	public String getUrl()
	{
		return url;
	}
	public int getHops()
	{
		return hops;
	}
}
