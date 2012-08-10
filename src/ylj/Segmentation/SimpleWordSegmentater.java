package ylj.Segmentation;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import  ylj.Util.StringRecognizer;

public class SimpleWordSegmentater  implements Segmentater{

	
	
	
	public SimpleWordSegmentater(){
		
		
		
	}

	public static  boolean isATerm(CharSequence  str){
		
		if(str==null)
			return false;
		
		if(StringRecognizer.isADigital(str))
			return true;
		if(StringRecognizer.isEnWord(str))
			return true;
		if(str.length()==1)
			return true;
		
		return false;
		
	}
	public static List<String> makeSegmentGlobal(String sentence) {
		// TODO Auto-generated method stub
		if(sentence==null)
			return null;
		
		List<String> termList=new LinkedList<String>();
		StringBuffer tempTerm=new StringBuffer();
		
		char[] chars=sentence.toCharArray();
		for(int i=0;i<chars.length;i++)
		{
			char thisChar=chars[i];
			tempTerm.append(thisChar);
			
			if(isATerm(tempTerm))	
				continue;
			else
			{
				tempTerm.deleteCharAt(tempTerm.length()-1);
				termList.add(tempTerm.toString());
				tempTerm.delete(0, tempTerm.length());
				tempTerm.append(thisChar);
			}
			
			
		}
		termList.add(tempTerm.toString());
		
		return termList;
	}
	@Override
	public List<String> makeSegment(String sentence) {
		// TODO Auto-generated method stub
		if(sentence==null)
			return null;
		
		List<String> termList=new LinkedList<String>();
		StringBuffer tempTerm=new StringBuffer();
		
		char[] chars=sentence.toCharArray();
		for(int i=0;i<chars.length;i++)
		{
			char thisChar=chars[i];
			tempTerm.append(thisChar);
			
			if(isATerm(tempTerm))	
				continue;
			else
			{
				tempTerm.deleteCharAt(tempTerm.length()-1);
				termList.add(tempTerm.toString());
				tempTerm.delete(0, tempTerm.length());
				tempTerm.append(thisChar);
			}
			
			
		}
		termList.add(tempTerm.toString());
		
		return termList;
	}
	
	public static void main(String[] args)
	{
		SimpleWordSegmentater aSimpleWordSegmentator=new SimpleWordSegmentater();
		
		List<String> strs=aSimpleWordSegmentator.makeSegment("你说 的说法多少124Esfdf");
		
		for(String aTerm:strs)
		{
			System.out.println(aTerm);
		}
	}
}
