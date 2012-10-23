package ylj.Dict;


import java.io.IOException;

import java.util.List;

public class FileDictLoader {

	MemDict memDict = null;

	public void setDict(MemDict aMemDict) {
		memDict = aMemDict;
	}

	public long load2MemDict(FileDictReader reader) throws IOException {

		Term aTerm = null;
		long l=0;
		while ((aTerm = reader.readTerm()) != null) {
			memDict.addTerm(aTerm);
			if(l%10000==0)
				System.out.println("load "+l+" vocabs.. ");
			l++;
		}

		return l;
	}

	
	public MemDict getMemDict() {
		return memDict;
	}

	public static void main(String[] args) throws IOException {
		MemDict aMemDict=new MemDict();
		FileDictLoader aFileDictLoader = new FileDictLoader();
		aFileDictLoader.setDict(aMemDict);
		
		StandardDictReader aStandardFileDictReader=new StandardDictReader();
		aStandardFileDictReader.addDictFile("XianDaiHanYu.txt");
		
		SogouDictReader aSogouDictReader=new SogouDictReader();
		aSogouDictReader.addDictFile("SogouLabDic.dic");
		
		
		long timeBegin=System.currentTimeMillis();
		aFileDictLoader.load2MemDict(aStandardFileDictReader);
		aFileDictLoader.load2MemDict(aSogouDictReader);
		
		
		long timeEnds=System.currentTimeMillis();   
		long timeCost=timeEnds-timeBegin;     //0.15¡®ms
		System.out.println("Load cost "+timeCost+"'ms");
		
		timeBegin=System.currentTimeMillis();
		
		List<Term> alist=null;
		int loop=1000;
		for(int i=0;i<loop;i++)
		{
			alist= aMemDict.getContains("ÊÖ½Å");
		}
		timeEnds=System.currentTimeMillis();   
		timeCost=timeEnds-timeBegin;     //0.15¡®ms
		System.out.println("getContains cost "+timeCost+"'ms"+"  LOOP="+loop);
		
		
		if (alist == null) {
			System.out.println("alist==null");

		} else {
			for (Term aTerm : alist) {
				System.out.println(aTerm.toString());

			}
		}
	}
}
