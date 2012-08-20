package ylj.Dict;

import java.io.IOException;

public class testMemDict {
	
	public static void main(String[] args) throws IOException{
		
	MemDict aMemDict=new MemDict();
		
		FileDictLoader aFileDictLoader = new FileDictLoader();
		aFileDictLoader.setDict(aMemDict);
		
		StandardDictReader aStandardFileDictReader=new StandardDictReader();
		aStandardFileDictReader.addDictFile("Dic/XianDaiHanYu.dic");
		aFileDictLoader.load2MemDict(aStandardFileDictReader);
		
		boolean result=aMemDict.isExist("123");
		if(result)
		{
			System.out.println("Exist");
		}
		else
		{
			System.out.println("Not Exist");
		}
		
		System.out.println("***************  1  *****************");
		
		for(String aStr:aMemDict.getStartWithTermsValue("บร")){
			
			System.out.println(aStr);
		}
		System.out.println("***************  2  *****************");
		for(String aStr:aMemDict.getEndWithTermsValue("บร")){
			
			System.out.println(aStr);
		}
		/**********************/
		/*
		System.out.println("***************  3  *****************");
		for(String aStr:aMemDict.getContainsTermsValue("บร")){
			
			System.out.println(aStr);
		}
		*/
	}

}
