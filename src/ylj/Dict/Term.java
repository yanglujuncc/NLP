package ylj.Dict;



public class Term implements Comparable<Term>{
	Word[] words;

	
	@Override
	public int compareTo(Term o) {
		// TODO Auto-generated method stub
		if(o==null)
			return 1;
 		return this.toString().compareTo(o.toString());
	}


	
	
	public boolean isContains(String wordsValues){
		if(words==null)
			return false;
		
		String strValue=toString();
		
		return strValue.contains(wordsValues);
	}
	
	

	public boolean isStartWith(String wordsValues){
		if(words==null)
			return false;
		
		String strValue=toString();	
		return strValue.startsWith(wordsValues);
	}
	public boolean isEndWith(String wordsValues){
		if(words==null)
			return false;
		
		String strValue=toString();
		return strValue.endsWith(wordsValues);
	}
	
	
	public String getPrefixOf(String wordsValues){
		if(wordsValues==null)
			return null;
		
		String strValue=toString();
		int begin=strValue.indexOf(wordsValues);
		if(begin==-1)
			return null;
		
		
		return strValue.substring(0, begin);
	}
	public String getSuffixOf(String wordsValues){
		if(wordsValues==null)
			return null;
		
		String strValue=toString();
		int begin=strValue.indexOf(wordsValues);
		if(begin==-1)
			return null;
		
		int end=begin+wordsValues.length();
		
		return strValue.substring(end, strValue.length());
	}
	
	
	public String toString(){
		if(words==null&&words.length==0)
			return null;
		
		StringBuffer aStringBuffer=new StringBuffer();
		for(Word aWord:words){
			aStringBuffer.append(aWord.value);
		}
		
		return aStringBuffer.toString();
	}
	
	
	
}


