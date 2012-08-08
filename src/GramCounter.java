import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import ylj.NGram.NGramSpliter;
import ylj.Util.FilesInput;



public class GramCounter {
	

	private static Logger logger = Logger
	.getLogger(GramCounter.class.getName());
	
	Map<String, Integer> counterMap = new HashMap<String, Integer>();
	
	FilesInput aFilesInput=new FilesInput("gbk");
	int NGram=2;
	NGramSpliter aNGramSpliter;
	long totalNGram=0;
	File outputFile;
	long maxLine=Long.MAX_VALUE;
	
	public GramCounter(int n)
	{
		 NGram=n;
		 aNGramSpliter =new NGramSpliter(NGram);
	}
	
	public void addInputFile(String filePath) throws IOException{
		
		aFilesInput.addFile(filePath);
		
	}
	
	public void setOutPutPath(String path) throws IOException
	{
		outputFile=new File(path);
		//outputFile.deleteOnExit();
		outputFile.createNewFile();
		
	}
	
	public void setMaxLine(long max){
		maxLine=max;
	}
	
	
	public void doCount() throws IOException{
		
		String aline=null;
		int i=0;
		while((aline=aFilesInput.getLine())!=null){
			
			
			if(i%1000==0)
			{
				System.out.println("processed "+i+" lines.");
			}
			i++;
			
			//List<String> terms=segmentater.makeSegment(aline);
			if(i>maxLine)
				break;
		
			List<String> terms=aNGramSpliter.splite(aline);
			
			for(String term:terms){
				
				totalNGram++;
				int counter=0;
				if(counterMap.containsKey(term)){
					counter=counterMap.get(term);
				}
				counterMap.put(term, counter+1);
				
				
			}
		}
		System.out.println("total gram="+counterMap.size());
		System.out.println("total gramNum="+totalNGram);
		
	}
	
	public void writeFile() throws IOException{
		
		logger.info("write record to File...");
		
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
				counterMap.entrySet());
		
		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						return (o2.getValue() - o1.getValue());
					}
				});
		
		
	
	
		FileOutputStream fos=new FileOutputStream(outputFile);
		OutputStreamWriter osw=new OutputStreamWriter(fos);
		BufferedWriter bw=new BufferedWriter(osw);
		
		for (Map.Entry<String, Integer> e : entryList) {
			//logger.info(e.getKey() + "	" + e.getValue());
			bw.append(e.getKey() + "	" + e.getValue());
			bw.newLine();
		}
		bw.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		
		PropertyConfigurator.configure("conf/log4j.properties");

		
		int NGram=Integer.parseInt(args[0]);
		long maxLine=Long.parseLong(args[1]);
		String inputFile=args[2];
		String outputFile=args[3];
	
	
		GramCounter aGramCounter=new GramCounter(NGram);
	
		aGramCounter.addInputFile(inputFile);
		aGramCounter.setOutPutPath(outputFile);
		if(maxLine!=0)	
			aGramCounter.setMaxLine(maxLine);
		aGramCounter.doCount();
		aGramCounter.writeFile();
	
	}
}
