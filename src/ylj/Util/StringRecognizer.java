package ylj.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRecognizer {

	static String hanziReg="[\u4e00-\u9fa5]+";
	static Pattern hanziPattern=Pattern.compile(hanziReg);
	static Matcher  hanziMatcher=hanziPattern.matcher("");
	
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
}
