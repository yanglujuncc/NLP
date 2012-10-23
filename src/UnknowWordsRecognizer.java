import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ylj.Util.FilesInput;


public class UnknowWordsRecognizer {
	private static Logger logger = Logger
	.getLogger(UnknowWordsRecognizer.class.getName());
	
	Map<String, Long> UnknowWordsMap = new HashMap<String, Long>();
	Map<String, Long> DicMap = new HashMap<String, Long>();
	long Threshold;
	
	FilesInput statisticsInput=new FilesInput("gbk");
	
	
	public long setStatisticsDir(String path) throws IOException{
		statisticsInput.preLoadFromDirName(path);
		return statisticsInput.total_num();
	}
	public long setStatisticsFile(String path) throws IOException{
		statisticsInput.addFile(path);
		return statisticsInput.total_num();
	}
	
	public void process() throws IOException{
		String aRecord=null;
		long processCount=0;
		long UnknowWordsCount=0;
		while((aRecord=statisticsInput.getLine())!=null)
		{
			//System.out.println(aRecord);
			processCount++;
			String[] terms=aRecord.split("\t");
			String gram=terms[0];
			//System.out.println(gram);
			long counter=Long.parseLong(terms[1]);
			//System.out.println(counter);
			UnknowWordsCount+=processRecord(gram,counter);
		
			if(processCount%10000==0)
			{
				logger.info("processed="+processCount+" .");
				logger.info("UnknowWords="+UnknowWordsCount+" .");
				logger.info("DicWords="+(processCount-UnknowWordsCount)+".");
			}
		}
	}
	
	public int processRecord(String NGram,long counter){
		
		if(DicMap.containsKey(NGram))
		{
			DicMap.put(NGram, counter);
			return 0;
		}
		else
		{
			UnknowWordsMap.put(NGram, counter);
			return 1;
		}
			
	}
	
	public long loadDicDir(String path) throws IOException{
		FilesInput aFilesInput=new FilesInput("gbk");
		aFilesInput.preLoadFromDirName("./Dic");
		
		String aline=null;
		
		while((aline=aFilesInput.getLine())!=null)
		{
			String[] terms=aline.split("\t");
			DicMap.put(terms[0],(long) 0);
		}
		
		logger.info("load Dic "+DicMap.size()+" terms.");
		return DicMap.size();
	}
	
	
	public long loadDicFile(String path) throws IOException{
		FilesInput aFilesInput=new FilesInput("gbk");
		aFilesInput.addFile(path);
		String aline=null;
		
		while((aline=aFilesInput.getLine())!=null)
		{
			String[] terms=aline.split("\t");
			DicMap.put(terms[0],(long) 0);
		}
		
		return DicMap.size();
	}
	
	private void writeCounterMapToFile(Map<String, Long> counterMap,String path)throws IOException{
		
		ArrayList<Map.Entry<String, Long>> entryList = new ArrayList<Map.Entry<String, Long>>(
				counterMap.entrySet());
		
		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Long>>() {
					public int compare(Map.Entry<String, Long> o1,
							Map.Entry<String, Long> o2) {
						if((o2.getValue() - o1.getValue())>0)
							return 1;
						if((o2.getValue() - o1.getValue())<0)
							return -1;
						return 0;
					}
				});
		
		FileOutputStream fos=new FileOutputStream(path);
		OutputStreamWriter osw=new OutputStreamWriter(fos);
		BufferedWriter bw=new BufferedWriter(osw);
		
		for (Map.Entry<String, Long> e : entryList) {
			//logger.info(e.getKey() + "	" + e.getValue());
			bw.append(e.getKey() + "	" + e.getValue());
			bw.newLine();
		}
		bw.close();
		
		
	}
	public void writeResultToFile() throws IOException
	{
		logger.info("write CounterMap of DicCount To File.");
		writeCounterMapToFile(DicMap,"DicCount");
		logger.info("write CounterMap of UnknowWordsCount To File .");
		writeCounterMapToFile(UnknowWordsMap,"UnknowWordsCount");
		logger.info("write complete .");
	}
	public static void main(String[] args) throws IOException
	{
		
		PropertyConfigurator.configure("conf/log4j.properties");

		UnknowWordsRecognizer aUnknowWordsRecognizer=new UnknowWordsRecognizer();
		
		aUnknowWordsRecognizer.loadDicDir("./Dic");
		
		aUnknowWordsRecognizer.setStatisticsFile(args[0]);
		aUnknowWordsRecognizer.process();
		aUnknowWordsRecognizer.writeResultToFile();
		
	}
}
