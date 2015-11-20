import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.*;

public class WordCount {

public static void main (String []args) throws IOException, InterruptedException{
	//first, check if any arguments have been given
	if (args.length > 0)
	{
		File path = new File(args[0]);//path = first argument (should be directory)
		String outputDir = args[0] + "output\\";
		int chunkSize = Integer.parseInt(args[1]);//chunkSize = the size of each task in number of lines per task. each thread can handle a maximum of chunkSize lines at a time
		int threads = Integer.parseInt(args[2]); //threads = number of threads to use.
		int lineCount = 0;
		int chunkNum = 0;
	
		ArrayList<String[]> al = new ArrayList<String[]>();		
		File output = new File(outputDir);
		if (output.exists())//delete all chunk files and results file to start over
		{
			File[] outputFiles = output.listFiles();
			for (int i = 0; i < outputFiles.length; i++)
			{
				outputFiles[i].delete();
			}
			output.delete();
			output.mkdir();
		}
		else{
			output.mkdir();
		}
			
		
	
		
		if (chunkSize >= 10 && chunkSize <= 5000 && threads >= 1 && threads <= 100) //chunkSize must be from 10-5000, threads must be 1-100
		{
			if (path.isDirectory()){ //if argument provided is a directory
				File[] allFiles = path.listFiles();//put all files in the directory into a File array called allFiles
				ExecutorService tpes = Executors.newFixedThreadPool(threads);//create thread pool
				for (int i = 0; i < allFiles.length; i++) //iterate through all files, sending them to be processed
				{
					
						
					
					if (allFiles[i].isFile())//if it is a file
					{
						lineCount = 0;
						
						
						
						BufferedReader br = new BufferedReader(new FileReader(allFiles[i])); //create BufferedReader to read in the file line by line
						String line;
						String endString = "";
						
						while ((line = br.readLine()) != null){//while not the end of the file 
						//this while loop reads in the file line by line. If it reads to as many lines as the 2nd parameter of the program, it will send all gathered lines to be 
						//executed by the thread pool and form a chunk. It will then start gathering the next X amount of lines to be sent to a new chunk.
							
							if (lineCount < chunkSize){
							
							endString += line + " ";
							lineCount++; //increment 
							}//end chunk if
							else //lineCount = chunkSize, need to reset and send the ArrayList to the thread pool
							{
							chunkNum++;	//this is just used to make sure the chunk is named correctly
							tpes.execute(new Task(endString, chunkNum, outputDir));
							lineCount = 0; //reset lineCount for a new
							//al.clear(); //clear contents of the ArrayList so it can hold the next chunk's contents.
							}
						}
						
						
						
						if ((line = br.readLine()) == null) //reaches null before chunkSize amount of lines are read
						{
							chunkNum++;
							
							tpes.execute(new Task(endString, chunkNum, outputDir));
							//al.clear();
						}
				
					}//end if (allFiles[i].isFile())
					else {
							//ignore for now
					}
					
				}
				
				
				
				tpes.shutdown();
				
				//tpes.awaitTermination(5, TimeUnit.SECONDS);
				
				while(!tpes.isTerminated()){
					//locks access until threads complete tasks	
				}
				
				writeToResultsFile(outputDir, output);
				
				System.exit(0);
				
			}
		}
		else //2nd or 3rd arguments aren't correct
		{
			System.out.println("Chunk size must be a number from 10-5000, threads must be a number from 1-100");
			
			System.exit(0);
		}
	}
	else //no arguments provided, exit program
	{
		System.out.println(" Usage: java WordCount <file|directory> <chunk size> <num of threads> ");
		
		System.exit(0);
	}
}



static TreeMap<String, Integer> SortByValue (HashMap<String, Integer> map) {
    DescendingValueComparator dvc =  new DescendingValueComparator(map);
    TreeMap<String,Integer> sortedMap = new TreeMap<>(dvc);
    sortedMap.putAll(map);
    return sortedMap;
}


static void writeToResultsFile(String outputDir, File path) throws IOException{
	
	File file = new File(outputDir + "results.txt");
	file.createNewFile();
	String[] words;
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	File[] allFiles = path.listFiles();//put all files in the directory into a File array called allFiles
	
	for (int i = 0; i < allFiles.length - 1; i++) //iterate through all files, sending them to be processed
	{	
		
		if (allFiles[i].isFile())//if it is a file
		{
				
			
			BufferedReader br = new BufferedReader(new FileReader(allFiles[i])); //create BufferedReader to read in the file line by line
			String line;
			
			while ((line = br.readLine()) != null)
			{
				
				words = line.split("\\s+");
				int count = Integer.parseInt(words[1]);
				Integer resultsFreq = hm.get(words[0]);
			if (words[0] != " ")
			{
				if (resultsFreq == null)//word doesn't exist in the results file, put it in there
				{
					hm.put(words[0], count);
				}
				else //word DOES exist in the results file, increment it's count by 1
				{
					hm.put(words[0], resultsFreq + count);
				}
			}
			}
	
		}
	}
	
	TreeMap<String, Integer> sortedDescendingMap = SortByValue(hm);
	
	try{
		FileWriter writer = new FileWriter(file);
	
	PrintWriter pw = new PrintWriter(writer);
	{
		for (Map.Entry<String, Integer> entry : sortedDescendingMap.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    String output = String.format("%-20s %s", key, value);
		    pw.println(output);
		}
		pw.flush();
		pw.close();
	}
	}catch (IOException e){
		System.out.println("Unable to write to results file");
	}
	
	System.out.println("Completed!");
	
	
}



}//end WordCount class