package ylj.Segmentation;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ylj.Dict.FileDictLoader;
import ylj.Dict.MemDict;
import ylj.Dict.SimpleDictReader;
import ylj.Dict.StandardDictReader;
import ylj.Dict.Term;
import ylj.Dict.Word;
import ylj.Util.StringRecognizer;

public class MaxForwardSegmentater implements Segmentater {

	MemDict aMemDict=new MemDict();

	public MaxForwardSegmentater(){
		
	}
	
	public MaxForwardSegmentater(String dicDirPath) throws IOException{
		
		File dicDir=new File(dicDirPath);
		File[] subFiles=dicDir.listFiles();
		
			
		FileDictLoader aFileDictLoader = new FileDictLoader();
		aFileDictLoader.setDict(aMemDict);
		
		SimpleDictReader aSimpleDictReader=new SimpleDictReader();
		
		for(File dicFile:subFiles)
		{
			System.out.println("add dic file:"+dicFile.getAbsolutePath());
			aSimpleDictReader.addDictFile(dicFile.getAbsolutePath());
		}
	    long vocabs=aFileDictLoader.load2MemDict(aSimpleDictReader);
	    System.out.println("load :"+vocabs+" vocabs");
		
		
	}
	
	public void setMemDict(MemDict aDict){
		aMemDict=aDict;
	}
	
	@Override
	public List<String> makeSegment(String sentence) {
		// TODO Auto-generated method stub
		List<String> termList=new LinkedList<String>();
		String prevTerm;
		
		
		int termBeginIndex=0;
		
		for(int i=1;i<=sentence.length();i++){
			
			//init			
			String tempStr=sentence.substring(termBeginIndex, i);
			
			if(StringRecognizer.isDigital(tempStr)||StringRecognizer.isEnWord(tempStr)||StringRecognizer.isSymbol(tempStr))
				continue;
			
			
			List<String>  terms=aMemDict.getStartWithTermsValue(tempStr);
			
			//System.out.println(tempStr);
			
			if(terms==null)
			{
				String newTerm=sentence.substring(termBeginIndex, i-1);
				termList.add(newTerm);
				termBeginIndex= i-1;		
			}
			
		}
		String newTerm=sentence.substring(termBeginIndex, sentence.length());
		termList.add(newTerm);
		
		
		return termList;
	}
	
	
	
	

	
}
