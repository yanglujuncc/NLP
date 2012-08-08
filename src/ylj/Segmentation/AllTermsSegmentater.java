package ylj.Segmentation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ylj.Dict.FileDictLoader;
import ylj.Dict.MemDict;
import ylj.Dict.StandardFileDictReader;

public class AllTermsSegmentater implements Segmentater{
	

	MemDict aMemDict=new MemDict();
	

	
	public void setMemDict(MemDict aDict){
		aMemDict=aDict;
	}
	
	@Override
	public List<String> makeSegment(String sentence) {
		// TODO Auto-generated method stub
		List<String> termList=new LinkedList<String>();
		String prevTerm;
		
		
		int termBeginIndex=0;
		
		for(int i=0;i<sentence.length();i++){
			
	
			String tempStr=""+sentence.charAt(i);
			List<String> startWithTerms=aMemDict.getStartWithTermsValue(tempStr);
			
			//System.out.println(tempStr);
			
			//termList.add(tempStr);
			
			if(startWithTerms!=null)
			{
				for(String aTerm:startWithTerms)
				{
					int subTermEndInx=aTerm.length()+i;
					if(subTermEndInx>sentence.length())
						continue;
					
					String subTerm=sentence.substring(i, subTermEndInx);
					if(subTerm.equals(aTerm))
						termList.add(aTerm);
					
				}
			}
		}
		String newTerm=sentence.substring(termBeginIndex, sentence.length());
		termList.add(newTerm);
		
		
		return termList;
	}
	
	public static void main(String[] args) throws IOException
	{
		MemDict aMemDict=new MemDict();
		
		FileDictLoader aFileDictLoader = new FileDictLoader();
		aFileDictLoader.setDict(aMemDict);
		StandardFileDictReader aStandardFileDictReader=new StandardFileDictReader();
		aStandardFileDictReader.addDictFile("Dic/XianDaiHanYu.dic");
		aFileDictLoader.load2MemDict(aStandardFileDictReader);
		
		
		AllTermsSegmentater aAllTermsSegmentater=new AllTermsSegmentater();
		aAllTermsSegmentater.setMemDict(aMemDict);
		
		
		
		List<String> terms=aAllTermsSegmentater.makeSegment("你说说法多少124Esfdf");
		
		for(String aTerm:terms)
		{
			System.out.println(aTerm);
		}
	}

}
