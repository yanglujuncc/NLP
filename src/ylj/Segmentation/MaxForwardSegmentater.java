package ylj.Segmentation;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ylj.Dict.MemDict;
import ylj.Dict.Term;
import ylj.Dict.Word;

public class MaxForwardSegmentater implements Segmentater {

	MemDict aMemDict=new MemDict();
	
	static final String symbol_reg="[+]+|[£¡!]+|[£¿?]+";
	Pattern symbolPattern=Pattern.compile(symbol_reg);
	
	
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
			
			if(isDigitalTerm(tempStr)||isEnglish(tempStr)||isSymbol(tempStr))
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
	
	
	private boolean isDigitalTerm(String wordsValues)
	{
	
		
		if(wordsValues==null)
			return false;
	
		for(int i=0;i<wordsValues.length();i++){
			
			char tempChar=wordsValues.charAt(i);
			if(tempChar>'0'&&tempChar<'9')
				continue;
			else
				return false;
		
		}
		return true;
	}
	
	private boolean isEnglish(String wordsValues){
		if(wordsValues==null)
			return false;
		
	
		for(int i=0;i<wordsValues.length();i++){
			
			char tempChar=wordsValues.charAt(i);
			if((tempChar>'A'&&tempChar<'Z')||(tempChar>'a'&&tempChar<'z'))
				continue;
			
			else
				return false;
			
			
		}
		return true;
	}
	private boolean isSymbol(String wordsValues){
		if(wordsValues==null)
			return false;
		
		Matcher  aSymbolMatcher=symbolPattern.matcher(wordsValues);
		if(aSymbolMatcher.matches())
			return true;
		else
			return false;
		
	}
}
