import java.util.Comparator;
import java.util.Map;
public class DescendingValueComparator implements Comparator<String> {

	  Map<String, Integer> map;

	    public DescendingValueComparator(Map<String, Integer> unsortedMap) {
	        this.map = unsortedMap;
	    }

	    public int compare(String a, String b) {
	        if (map.get(a) >= map.get(b)) 
	        {
	        	return -1;
	        }
	        else 
	        	{
	        	return 1;
	        	}
	        	
	    }

}
