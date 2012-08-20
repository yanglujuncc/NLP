package ylj.NGram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import ylj.Util.FilesInput;

public class GramTopNFilter {
	protected static Logger logger = Logger.getLogger(GramTopNFilter.class.getName());
	
	
	Set<String> knowWordSet=new HashSet<String>();
	Set<String> stopGramSet=new HashSet<String>();
	enum ACCEPT{
		KNOW,
		UNKNOW,
		ALL
	};
	
	ACCEPT accpetKind=ACCEPT.ALL;
	
	
	public void addKnowWord(String word){
		knowWordSet.add(word);	
	}
	public void addStopGram(String word){
		stopGramSet.add(word);	
	}
	
	public void setAccept(String accept){
		
		
		if(accept.equalsIgnoreCase("know"))
			accpetKind=ACCEPT.KNOW;
			
		if(accept.equalsIgnoreCase("unknow"))
		{
			accpetKind=ACCEPT.UNKNOW;
			
		}
			
		if(accept.equalsIgnoreCase("all"))
			accpetKind=ACCEPT.ALL;
			
		logger.info("accpetKind="+accpetKind+" ");
			
	}
	public long loadKnowWordFromDir(String knowWordDir) throws IOException{
		logger.info("Load... KnowWords FromDir:"+knowWordDir+" ");
		long counter=loadGramsFromDir(knowWordSet,knowWordDir);
		logger.info("Load complete ("+counter+")");
		return counter;
	}
	public long loadStopGramDir(String stopGramDir) throws IOException{
		logger.info("Load... stopGram FromDir:"+stopGramDir+" ");
		long counter=loadGramsFromDir(stopGramSet,stopGramDir);
		logger.info("Load complete ("+counter+")");
		return counter ;
	}
	
	
	private long loadGramsFromDir(Set<String> gramSet,String gramDir) throws IOException{
		
		File[] dataFiles=FilesInput.getAllSubFiles(gramDir);
		if(dataFiles==null)
		{
			logger.error("GramDir:"+gramDir+" has no data file");
			return 0;
		}
		
		
		for(File dataFile:dataFiles){
			logger.info("Load... File:"+dataFile.getName()+" ");
			
			FileInputStream is=new FileInputStream(dataFile);
			InputStreamReader isr=new InputStreamReader(is,"gbk");
			BufferedReader buffReader=new BufferedReader(isr);
			
			String aline=null;
			
			while((aline=buffReader.readLine())!=null)
			{
				String[] terms=aline.split("\t");
				String gram=terms[0];
				//System.out.println("gram="+gram);
				gramSet.add(gram);
				
				
			}		
			buffReader.close();
		}
		
		
		return gramSet.size();
	}
	
	public boolean isAccept(String gram){
		
		//logger.info("gram="+gram);
		//logger.info("stopGramSet.contains(gram)="+stopGramSet.contains(gram));
		//logger.info("knowWordSet.contains(gram)="+knowWordSet.contains(gram));
		
		//logger.info("accpetKind="+accpetKind);
		
		
		if(stopGramSet.contains(gram))
			return false;
		
		if(accpetKind==ACCEPT.KNOW)
		{
			if(knowWordSet.contains(gram))
				return true;
			else
				return false;
		}
		
		if(accpetKind==ACCEPT.UNKNOW)
		{
			if(knowWordSet.contains(gram))
				return false;
			else
				return true;
		}
		
		return true;
	}
	
	
}
