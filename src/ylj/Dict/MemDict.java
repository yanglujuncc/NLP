package ylj.Dict;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MemDict implements Dict{

	private Map<Character ,List<Term> > invertedIndexOfWord =new HashMap<Character ,List<Term> >() ;
	private Map<String,List<Term> > invertedIndexOfPinYin=new HashMap<String,List<Term> >() ;
	
	long termCount=0;
	
	
	
	/*****************    对外方法    ***********************/
	
	public long getTermsNum(){
		return termCount;
	}
	
	public long addTerm(Term aTerm){
		
		//去重这一步很耗时间，相当于每次插入都做了次查询
		if(isExist(aTerm))
			return termCount;
		
		addIndexOfWord(aTerm);
		return ++termCount;
	}
	
	public boolean isExist(Term aTerm){
		if(aTerm==null)
			return false;
		
		 List<Term> containList=getContains(aTerm.toString());
		 if(containList==null)
			 return false;
		 
		 for(Term tempTerm:containList){
			 if(tempTerm.compareTo(aTerm)==0)
			 {
				 return true;
			 }
		 }
		return false;
	}
	/**
	 * 获取包含关键字的集合
	 * 字倒排表就交集，得到包含所有字的词,不考虑先后顺序（目前没有彻底实现.现在直接给出最短的倒排表）
	
	 * @param wordsValues
	 * @return
	 */

	private List<Term> getIntersection(String wordsValues){
		
		if(wordsValues==null)
			return null;
		
		
		//System.out.println(" here 5");
		//伪实现
		
		
		List<List<Term>> results=new LinkedList<List<Term>>();
		List<Term> minResult = null;
			
		for(int i=0;i<wordsValues.length();i++)
		{
			List<Term> result=invertedIndexOfWord.get(wordsValues.charAt(i));
			if(result!=null)
			{
				results.add(result);
				
				if(minResult==null){
					minResult=result;
				}
				else if (minResult.size()>result.size())
				{
					minResult=result;
				}					
			}
		}
		
		return minResult;
	}
	public  List<Term> getContains(String wordsValues)
	{
		
		
		List<Term> intersectionSet=getIntersection(wordsValues);
		if(intersectionSet==null)
			return null;
		
		List<Term> returnList = new LinkedList<Term>();
		
		for(Term aTerm:intersectionSet){
			
			if(aTerm.isContains(wordsValues)){
				returnList.add(aTerm);
			}
		}
		
		return returnList;
	}
	
	public  List<Term> getStartWith(String wordsValues)
	{
		List<Term> intersectionSet=getIntersection(wordsValues);
		if(intersectionSet==null)
			return null;
		
		List<Term> returnList = new LinkedList<Term>();
		for(Term aTerm:intersectionSet){
			if(aTerm.isStartWith(wordsValues)){
				returnList.add(aTerm);
			}
		}
		return returnList;
	}
	public  List<Term> getEndWith(String wordsValues)
	{
		List<Term> intersectionSet=getIntersection(wordsValues);
		if(intersectionSet==null)
			return null;
		
		List<Term> returnList = new LinkedList<Term>();
		for(Term aTerm:intersectionSet){
			if(aTerm.isEndWith(wordsValues)){
				returnList.add(aTerm);
			}
		}
		return returnList;
	}
	/*****************    字处理    ***********************/
	
	private void addIndexOfWord(Term aTerm){
		
		if(aTerm==null)
			return ;
		for(Word word:aTerm.words)
		{
			List<Term> lst=invertedIndexOfWord.get(word.value);
			
			if(lst!=null)
			{
				lst.add(aTerm);
			}
			else
			{
				lst=new LinkedList<Term>();
				lst.add(aTerm);
				invertedIndexOfWord.put(word.value, lst);
			}					
		}				
	}
	
	
	

	
	/*****************      pinyin    ***********************/
	 
	private void addIndexOfPinYin(Term aTerm){
		if(aTerm==null)
			return ;
		
		StringBuffer pinyin_Ci=new StringBuffer();
		for(Word word:aTerm.words)
		{
			pinyin_Ci.append(word.PinYin);
		}
		List<Term> lst=invertedIndexOfPinYin.get(pinyin_Ci.toString());
			
		if(lst!=null)
		{
			lst.add(aTerm);
		}
		else
		{
			lst=new LinkedList<Term>();
			lst.add(aTerm);
			invertedIndexOfPinYin.put(pinyin_Ci.toString(), lst);
		}					
	
	}




	@Override
	public boolean isExist(String aTermStr) {
		
		if(aTermStr==null)
			return false;
		
		List<Term> intersectionSet=getIntersection(aTermStr);
		if(intersectionSet==null)
			return false;
		
		for(Term aTerm:intersectionSet){
			
			if(aTerm.toString().equals(aTermStr)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<String> getContainsTermsValue(String wordsValues) {
		 List<Term> termList=getContains( wordsValues);
		 if(termList==null||termList.size()==0)
			 return null;
		 
		 List<String> strList=new LinkedList<String>();
		 for(Term aTerm:termList)
		 {
			 strList.add(aTerm.toString());
		 }
		 
		return strList;
	}

	@Override
	public List<String> getStartWithTermsValue(String wordsValues) {
		 List<Term> termList=getStartWith( wordsValues);
		 if(termList==null||termList.size()==0)
			 return null;
		 
		 List<String> strList=new LinkedList<String>();
		 for(Term aTerm:termList)
		 {
			 strList.add(aTerm.toString());
		 }
		 
		return strList;
	}

	@Override
	public List<String> getEndWithTermsValue(String wordsValues) {
		 List<Term> termList=getEndWith( wordsValues);
		 if(termList==null||termList.size()==0)
			 return null;
		 
		 List<String> strList=new LinkedList<String>();
		 for(Term aTerm:termList)
		 {
			 strList.add(aTerm.toString());
		 }
		 
		return strList;
	}


	
	public long saveToFile(String path) throws IOException{
		
		Set<String> termSet=new HashSet<String>();
		for(Entry<Character ,List<Term>> entry:invertedIndexOfWord.entrySet())
		{
			List<Term> termList=entry.getValue();
			for(Term term:termList)
			{
				String value=term.toString();
				termSet.add(value);
			}
		}
		
		String[] termArray=termSet.toArray(new String[termSet.size()]);
		Arrays.sort(termArray, new Comparator<String>(){
			@Override
			public int compare(String arg0, String arg1) {
				
				return arg0.length()-arg1.length();
			}
			
		});
		
		FileOutputStream fos=new FileOutputStream(path);
		OutputStreamWriter ow=new OutputStreamWriter(fos);
		BufferedWriter bw=new BufferedWriter(ow);
			
		
		for(String term:termArray){
			bw.append(term+"\n");
			
		}
		bw.close();
		return termArray.length;
	}
	
}
