import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Task implements Runnable{
	
	private int chunkNumber;
	private String output;
	private String string;
	private Map<String, Integer> chunkHM;
	
	
	
	

	public Task(String finalString, int chunkNum, String outputDir) {
		// TODO Auto-generated constructor stub
		string = finalString;
		chunkNumber = chunkNum;
		output = outputDir;
		chunkHM = new HashMap<String, Integer>();
	}

	public TreeMap<String, Integer> SortByValue (Map<String, Integer> originalMap) {
        DescendingValueComparator dvc =  new DescendingValueComparator(originalMap);
        TreeMap<String,Integer> sortedMap = new TreeMap<>(dvc);
        sortedMap.putAll(originalMap);
        return sortedMap;
    }


	//run method
	public void run(){

		Thread.yield();
		
		string = string.replaceAll("[^\\w\\s\\-_]", "");
		string = string.toLowerCase();
		
		
			for (String words : string.split(" ")){
				
				
				Integer freq = chunkHM.get(words);
				//Integer resultsFreq = results.get(words[j]);
				if (freq == null)//word doesn't exist yet
				{
					chunkHM.put(words, 1);
				}
				else //word does exist, increment count
				{
					chunkHM.put(words, freq + 1);
				}
				
			}//end for loop
			
	
		
		TreeMap<String, Integer> sortedDescendingMap = SortByValue(chunkHM);
		
		
		File f = new File(output + "output_" + chunkNumber + ".chunk");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            for (Map.Entry<String, Integer> entry : sortedDescendingMap.entrySet()) {
            	String key = entry.getKey();
     		    Object value = entry.getValue();
                String output = String.format("%-20s %s", key, value);
                bw.write(output);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("Unable to write " + output + "output_" + chunkNumber + ".chunk");
        }
	}

	
	
	
	/*public void writeToChunkFile(Map<String, Integer> sortedMap, int chunkNum, String outputDir) throws IOException{
		File file = new File(outputDir + "output_" + chunkNum + ".chunk");
		file.createNewFile();
		
			try{FileWriter writer = new FileWriter(file);
			PrintWriter pw = new PrintWriter(writer);
			for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			    String output = String.format("%-20s %s", key, value);
			    pw.println(output);
			   
			}
			pw.flush();
			pw.close();
			}catch (IOException f)
			{
				System.out.println("error writing output_" + chunkNum + ".chunk");
			}
			
			
		
		
	}*/





}//end Task class
