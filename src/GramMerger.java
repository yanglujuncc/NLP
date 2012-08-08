import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


public class GramMerger {

	private static Logger logger = Logger
	.getLogger(GramMerger.class.getName());
	
	Map<String, Long> counterMap = new HashMap<String, Long>();
	FilesInput aFilesInput=new FilesInput("gbk");
	long totalNGram=0;
	File outputFile;
	long maxLine=Long.MAX_VALUE;
	
	public void addInputFile(String filePath) throws IOException{
		
		aFilesInput.addFile(filePath);
		
	}
	public void setMaxLine(long max){
		maxLine=max;
	}
	
	public void doMerge() throws IOException{
		
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
		
			String[] record=aline.split("\t");
			String gram=record[0];
			long count=Long.parseLong(record[1]);
		
				
			totalNGram+=count;
			long counter=0;
			if(counterMap.containsKey(gram)){
					counter=counterMap.get(gram);
				}
			counterMap.put(gram, counter+count);
				
				
			
		}
		System.out.println("total gram="+counterMap.size());
		System.out.println("total gramNum="+totalNGram);
		
	}
	public void setOutPutPath(String path) throws IOException
	{
		System.out.println("output path="+path);
		outputFile=new File(path);
		//outputFile.deleteOnExit();
		outputFile.createNewFile();
		
	}
public void writeFile() throws IOException{
		
		logger.info("write record to File...");
		
		ArrayList<Map.Entry<String, Long>> entryList = new ArrayList<Map.Entry<String, Long>>(
				counterMap.entrySet());
		
		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Long>>() {
					public int compare(Map.Entry<String, Long> o1,
							Map.Entry<String, Long> o2) {
						if(o2.getValue() - o1.getValue()>0)
							return 1;
						if(o2.getValue() - o1.getValue()<0)
							return -1;
						return 0;
					}
				});
		
		
	
	
		FileOutputStream fos=new FileOutputStream(outputFile);
		OutputStreamWriter osw=new OutputStreamWriter(fos);
		BufferedWriter bw=new BufferedWriter(osw);
		
		for (Map.Entry<String, Long> e : entryList) {
			//logger.info(e.getKey() + "	" + e.getValue());
			bw.append(e.getKey() + "	" + e.getValue());
			bw.newLine();
		}
		bw.close();
	}

public static void main(String[] args) throws Exception {
		
		
		PropertyConfigurator.configure("conf/log4j.properties");

		
		
		long maxLine=Long.parseLong(args[0]);
		String[] inputFiles=args[1].split(",");
		String outputFile=args[2];
	
	
		GramMerger aGramMerger=new GramMerger();
		
		for(String file:inputFiles)
			aGramMerger.addInputFile(file);
		
		aGramMerger.setOutPutPath(outputFile);
		if(maxLine!=0)
			aGramMerger.setMaxLine(maxLine);
		aGramMerger.doMerge();
		aGramMerger.writeFile();
	
	}
}
