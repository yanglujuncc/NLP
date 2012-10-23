package ylj.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRecognizer {

	static String hanziReg = "[\u4e00-\u9fa5]";
	static Pattern hanziPattern = Pattern.compile(hanziReg);
	static Matcher hanziMatcher = hanziPattern.matcher("");
	
	static String hanzisReg = "[\u4e00-\u9fa5]+";
	static Pattern hanzisPattern = Pattern.compile(hanzisReg);
	static Matcher hanzisMatcher = hanzisPattern.matcher("");

	
	
	static int MinRepeatNum=4;
	
	static String shortFormatSuffix=MinRepeatNum+"[+]$";
	static Pattern shortFormatSuffixPattern = Pattern.compile(shortFormatSuffix);
	static Matcher shortFormatSuffixMatcher= shortFormatSuffixPattern.matcher("");

	//static String symbolReg_1="[`~!@#$%^&*()-_=+\\|[{]};:'\",<.>/?]"; //半角
//	static String symbolReg_2="[·~！@#￥%……&*（）-_=+、|【{】}；：’”，《。》、？]"; //全角
	static String symbolReg_1="[`~!@#$%^&*()-_=+\\|[{]};:'\",<.>/?]"; //半角
	static String symbolReg_2="[·~！@#￥%……&*-——=+、|【{】}；：’”，《。》、？]"; //全角  有问题大括号 
	static String symbolReg="("+symbolReg_1+"|"+symbolReg_2+")";
	static Pattern symbolPattern = Pattern.compile(symbolReg);
	static Matcher symbolMatcher= symbolPattern.matcher("");

	
	public static boolean isADigital(CharSequence str) {

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9')
				continue;
			else
				return false;
		}
		return true;

	}

	public static boolean isEnWord(CharSequence str) {

		if (str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
					|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z'))
				continue;
			else
				return false;
		}
		return true;

	}

	public static boolean isCN_zhWord(String str) {

		if (str.length() != 1)
			return false;

		hanziMatcher.reset(str);

		if (hanziMatcher.matches())
			return true;
		else
			return false;
	}
	public static boolean isCN_zhStr(String str) {

		hanzisMatcher.reset(str);

		if (hanzisMatcher.matches())
			return true;
		else
			return false;
	}
	public static boolean isCN_zhWord(char aChar) {

		String aStr = "" + aChar;

		return isCN_zhWord(aStr);
	}

	public static boolean isDigital(CharSequence str) {

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9')
				continue;
			else
				return false;
		}
		return true;

	}

	/*********** rewrite **********/

	public static boolean isEnOrDigWord(CharSequence str) {

		if (str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
					|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
					|| (str.charAt(i) >= '0' && str.charAt(i) <= '9'))
				continue;
			else
				return false;
		}
		return true;

	}

	// 重复的符号
	public static boolean isRepeatShortFormat(CharSequence str) {

		shortFormatSuffixMatcher.reset(str);
		if(shortFormatSuffixMatcher.find())
			return true;
		else
			return false;
	}

	public static boolean isRepeating(CharSequence str) {

		if(str==null||str.length()==0)
			return false;
		

		int repeatCount=getRepeatCount(str);
	
		if(repeatCount>=MinRepeatNum)
			return true;
		else
			return false;
		
		
	}
	public static String getRepeatStr(CharSequence str) {
		
		StringBuffer repeatStr=new StringBuffer();
		
		for(int i=0;i<str.length();i++){
			
			repeatStr.append(str.charAt(i));
			
			if(isRepeatedBy(repeatStr.toString(),str))
				return repeatStr.toString();
		}
		
		return repeatStr.toString();
		
	}
	private static boolean isRepeatedBy(String repeatStr,CharSequence str) {
		
		String testStr=str.toString();
		int i=0;
		while(i<testStr.length())
		{
			if(!testStr.startsWith(repeatStr,i))
				return false;
				
			i+=repeatStr.length();
		}
		return true;
		
	}
	public static int getRepeatCount(CharSequence str) {
		
		String repeatStr=getRepeatStr(str);
		
		return str.toString().length()/repeatStr.length();
	}
	
	
	public static String shortRepeatFormat(CharSequence str) {
	
		String repeatStr=getRepeatStr(str);
		int repeatCount=getRepeatCount(str);
	
		
		if(repeatCount>=MinRepeatNum)
		{
			return repeatStr+""+MinRepeatNum+"+";
		}
		else
			return str.toString();
	}
	
	public static String upCase(CharSequence str) {
		
		StringBuffer aStringBuffer=new StringBuffer();
		
		for(int i=0;i<str.length();i++){
			char c=str.charAt(i);
			if(c>='a'&&c<='z')
				c=(char) (c-32);
			
			aStringBuffer.append(c);	
					
		}
		return aStringBuffer.toString();
	}
	
	
	public static boolean isSymbol(CharSequence str) {
		
		symbolMatcher.reset(str);
		if(symbolMatcher.matches())
			return true;
		else
			return false;

	}
	


}
