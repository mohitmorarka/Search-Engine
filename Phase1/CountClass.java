
public class CountClass {

	int count, maxPages, maxd;
	String dir;
	void setdir(String d)
	{
		dir = d;
	}
	String getdir(){
		return dir;
	}
	
	int get(){
		return count;
	}
	void set(int x){
		count = x;
	}
	
	int getP(){
		return maxPages;
	}
	void setP( int p)
	{
		maxPages = p;
	}
	
	void setmd(int d){
		maxd = d;
	}
	int getmd(){
		return maxd;
	}	
}
