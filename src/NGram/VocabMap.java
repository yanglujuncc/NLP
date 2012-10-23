package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import ylj.Util.HashIndex;
import ylj.Util.Index;

public class VocabMap {

	protected static Logger logger = Logger.getLogger(VocabMap.class.getName());
	
	Index<String> vocabIndex=new HashIndex<String>();
	
	Map<String,Integer> newVocabMap=new HashMap<String,Integer>();
	
	public int vocabIdOf(String str){
		
		return vocabIndex.indexOf(str);
		
	}
	public int vocabNum(){
		return vocabIndex.size();
	}

	public  int addNewVocabsFromFile(File file) throws IOException{
		
		logger.info("Add New Vocabs From File "+file.getPath());
		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		int oldSize=vocabIndex.size();
		String aline = null;
		int i = 0;
		while ((aline = br.readLine()) != null) {
			i++;
			String[] terms=aline.split("[\t :]");
			String gram=terms[0];
	
			if(vocabIndex.contains(gram))
				continue;
			else
			{
				int index=vocabIndex.indexOf(gram, true);
				newVocabMap.put(gram, index);
			}			
			
		}
		br.close();
		
		logger.info("Process New Vocabs File complete.");
		
		int newSize=vocabIndex.size();
		int addNum=newSize-oldSize;
		
		logger.info("Add "+addNum+" New Vocabs");
		
		return addNum;
		
		
	}
public int addNewVocabsFromDir(File dirFile) throws IOException{
		
	
		logger.info("Load Vocab From Dir:"+dirFile.getPath());
		int vocabs=0;
		
		File[] subFiles=dirFile.listFiles();
		for(File file:subFiles){
			if(file.isDirectory())
				continue;
			vocabs+=addNewVocabsFromFile(file);
		}
		logger.info("Load "+vocabs+" Vocabs From Dir:"+dirFile.getAbsolutePath());
		return vocabs;
		
		
	}
	public int loadFromFile(String path){
		
		logger.info("Load VocabMap From Filename:"+path);
		
		File VocabMapFile=new File(path);
		if(!VocabMapFile.exists())
		{
			logger.info("File do not exists , Filename:"+path);
			return 0;
		}
		
		vocabIndex= HashIndex.loadFromFilename(path);
		
		return vocabIndex.size();
	}
	
	public void writeToFile(String path){
		logger.info("Save VocabMap To Filename:"+path);
		vocabIndex.saveToFilename(path);
		logger.info("Save Vocabs :"+vocabIndex.size());
		return ;
	}
	public void writeNewVocabsToFile(String path) throws IOException{
		
		logger.info("Write New Vocabs to File:"+path);
		
		FileOutputStream fos_new = new FileOutputStream(path );
		OutputStreamWriter osw_new  = new OutputStreamWriter(fos_new,"gbk");
		BufferedWriter bw_new = new BufferedWriter(osw_new );
		
		for(Entry<String, Integer> entry:newVocabMap.entrySet()){
			bw_new.append(entry.getValue()+"="+entry.getKey()+"\n");
		}
		bw_new.close();
		logger.info("Write New Vocabs :"+newVocabMap.size());
		return ;
	}
	public int loadFromDB(String dburl){

		return 0;
	}
	public void writeNewVocabsToDB(String dburl){

		return ;
	}
	
public static void main(String[] args) throws Exception {
		
		DOMConfigurator.configureAndWatch("conf/log4j.xml"); 
		
		if(args.length!=4)
		{
			System.out.println("VocabMap ");
			System.out.println("usage:VocabMap VocabMapFile InputVocabsFile AddOutput");
			System.out.println("                       	VocabMapFile	(or NULL )");
			System.out.println("                       	InputVocabsPath 	(new gram file or dir)");
			System.out.println("                       	NewVocabMapFile		(new VocabMapFile)");
			System.out.println("                       	AddedVocabs	(AddOutputFile)");
			return ;
		}
		
		String oldVocabMapFilePath=args[0];
		String inputVocabsPath=args[1];
		String newVocabMapFilePath=args[2];
		String addOutputPath=args[3];
		
		VocabMap vocabMap=new VocabMap();
		if(!oldVocabMapFilePath.equalsIgnoreCase("null"))
			vocabMap.loadFromFile(oldVocabMapFilePath);
		
		File inputVocabsFile=new File(inputVocabsPath);
		if(inputVocabsFile.exists())
		{
			if(inputVocabsFile.isFile())
				vocabMap.addNewVocabsFromFile(inputVocabsFile);
			else if(inputVocabsFile.isDirectory())
				vocabMap.addNewVocabsFromDir(inputVocabsFile);
				
		}
		else
		{
			
			logger.error("Path file do not exists. :"+inputVocabsFile.getPath());
			
		}
		
		vocabMap.writeNewVocabsToFile(addOutputPath);
		
		vocabMap.writeToFile(newVocabMapFilePath);
		
		
		
}
	
	
}
