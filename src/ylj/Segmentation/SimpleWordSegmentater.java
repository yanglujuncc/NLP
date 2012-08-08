package ylj.Segmentation;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleWordSegmentater  implements Segmentater{

	
	static String hanziReg="[\u4e00-\u9fa5]";
	static Pattern hanziPattern=Pattern.compile(hanziReg);
	static Matcher  hanziMatcher=hanziPattern.matcher("");
	
	public SimpleWordSegmentater(){
		
		
		
	}
	public static boolean isADigital(CharSequence  str){
		
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)>='0'&&str.charAt(i)<='9')
				continue;
			else
				return false;
		}
		return true;
		
	}
	public static boolean isEnWord(CharSequence  str){
		
		if(str.length()==0)
			return false;
		
		for(int i=0;i<str.length();i++)
		{
			if((str.charAt(i)>='a'&&str.charAt(i)<='z')||(str.charAt(i)>='A'&&str.charAt(i)<='Z'))
				continue;
			else
				return false;
		}
		return true;
		
	}
	
	public static boolean isCN_zhWord(String  str){
		
		if(str.length()!=1)
			return false;
		
		hanziMatcher.reset(str);
		
		if(hanziMatcher.matches())
			return true;
		else
			return false;
	}
	public static boolean isCN_zhWord(char  aChar){
		
		String aStr=""+aChar;
		
		return isCN_zhWord(aStr);
	}
	public static  boolean isATerm(CharSequence  str){
		
		if(str==null)
			return false;
		
		if(isADigital(str))
			return true;
		if(isEnWord(str))
			return true;
		if(str.length()==1)
			return true;
		
		return false;
		
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
