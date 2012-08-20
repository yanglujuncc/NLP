package ylj.Dict;

import java.io.IOException;

public class MergeDict {

	public static void main(String[] args) throws IOException{
		
		MemDict aMemDict=new MemDict();
			
			FileDictLoader aFileDictLoader = new FileDictLoader();
			aFileDictLoader.setDict(aMemDict);
			
			StandardDictReader aStandardFileDictReader=new StandardDictReader();
			aStandardFileDictReader.addDictFile("Dic/XianDaiHanYu.dic");
			
			SogouDictReader aSogouFileDictReader=new SogouDictReader();
			aSogouFileDictReader.addDictFile("Dic/SogouLabDic.dic");
			
			long countXianDaiHanYu=aFileDictLoader.load2MemDict(aStandardFileDictReader);
			System.out.println("countXianDaiHanYu="+countXianDaiHanYu);
			long countSogou=aFileDictLoader.load2MemDict(aSogouFileDictReader);
			System.out.println("countSogou="+countSogou);
			
		
			long saceCount=aMemDict.saveToFile("Dic/combinedDic.dic");
			System.out.println("saceCount="+saceCount);
			
			/**********************/
			/*
			System.out.println("***************  3  *****************");
			for(String aStr:aMemDict.getContainsTermsValue("บร")){
				
				System.out.println(aStr);
			}
			*/
		}
}
