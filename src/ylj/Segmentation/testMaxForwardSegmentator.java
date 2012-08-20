package ylj.Segmentation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import ylj.Dict.FileDictLoader;
import ylj.Dict.MemDict;
import ylj.Dict.StandardDictReader;


public class testMaxForwardSegmentator {

	
	
	public static void testStr() throws IOException{
		MemDict aMemDict=new MemDict();
		
		FileDictLoader aFileDictLoader = new FileDictLoader();
		aFileDictLoader.setDict(aMemDict);
		
		StandardDictReader aStandardFileDictReader=new StandardDictReader();
		aStandardFileDictReader.addDictFile("Dic/XianDaiHanYu.dic");
		aFileDictLoader.load2MemDict(aStandardFileDictReader);
		
		
		//SogouFileDictReader aSogouDictReader=new SogouFileDictReader();
		//aSogouDictReader.addDictFile("SogouLabDic.dic");
		//aFileDictLoader.load2MemDict(aSogouDictReader)
		MaxForwardSegmentater aSegmentator=new MaxForwardSegmentater();
		aSegmentator.setMemDict(aMemDict);
		
		//System.out.print(aMemDict.getContains("天安"));
		
		String sentence="老婆小心点别让狗咬了++++MMMM！！！！";
		
		long timeBegin=System.currentTimeMillis();
		List<String> fields=null;
		
		
		fields=aSegmentator.makeSegment(sentence);    
		long timeEnds=System.currentTimeMillis();   
		long timeCost=timeEnds-timeBegin;     //0.15‘ms
		System.out.println(timeCost+"'ms");
		for(String filed:fields){
			System.out.print(filed+"|");
		}
		System.out.println("");
	
		
	}
	public static void testFile() throws IOException{
		MemDict aMemDict=new MemDict();
		
		FileDictLoader aFileDictLoader = new FileDictLoader();
		aFileDictLoader.setDict(aMemDict);
		
		StandardDictReader aStandardFileDictReader=new StandardDictReader();
		aStandardFileDictReader.addDictFile("Dic/XianDaiHanYu.dic");
		aFileDictLoader.load2MemDict(aStandardFileDictReader);
		
		
		//SogouFileDictReader aSogouDictReader=new SogouFileDictReader();
		//aSogouDictReader.addDictFile("SogouLabDic.dic");
		//aFileDictLoader.load2MemDict(aSogouDictReader)
		MaxForwardSegmentater aSegmentator=new MaxForwardSegmentater();
		aSegmentator.setMemDict(aMemDict);
		
		/////////////////////////////////////////////////////
		
		File file=new File("data/DangQian");
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br=new BufferedReader(isr);
		
		String line=null;
		while((line=br.readLine())!=null)
		{
			System.out.println("["+line+"]");
			List<String>fields=aSegmentator.makeSegment(line);    
			for(String filed:fields){
				System.out.print(filed+"|");
			}
			System.out.println();
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		// testStr() ;
		testFile();
		
	}
}
