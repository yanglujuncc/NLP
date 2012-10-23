package NGram;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ylj.Util.StringRecognizer;

public class NGramSpliter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4365419499371171963L;
	private int N;
	private Map<String, Integer> counterMap = new HashMap<String, Integer>();

	// SimpleWordSegmentater wordSegmentater=new SimpleWordSegmentater();
	static String hanziReg = "[\u4e00-\u9fa5]";
	static Pattern[] hanziPatterns;
	static Matcher[] hanziMatchers;
	
	static String digtalReg_1="[0-9]"; //半角
	static String digtalReg="("+digtalReg_1+")+";
	static Pattern digtalPattern = Pattern.compile(digtalReg);
	static Matcher digtalMatcher= digtalPattern.matcher("");

	static String enReg_1="[a-zA-Z]"; //半角
	static String enReg="("+enReg_1+")+";
	static Pattern enPattern = Pattern.compile(enReg);
	static Matcher enMatcher= enPattern.matcher("");
	
	static String enOrDigReg_1="[a-zA-Z0-9]"; //半角
	static String enOrDigReg="("+enOrDigReg_1+")+";
	static Pattern enOrDigPattern = Pattern.compile(enOrDigReg);
	static Matcher enOrDigMatcher= enOrDigPattern.matcher("");
	
	static  String expressionSymbol_reg="#[0-9]{1,3}(?!=[0-9])";  
	static  Pattern expressionSymbolPattern=Pattern.compile(expressionSymbol_reg);
	static  Matcher  expressionSymbolMatcher=expressionSymbolPattern.matcher("");
	
	/*  
	static String symbolReg_1="[`~!@#$%^&*()-_=+\\|\\[{\\]};:'\",<.>/?]"; //半角
	static String symbolReg_2="[·~！@#￥%……&*-——=+、|【{】}；：’”，《。》、？]"; //全角  有问题大括号 
	static String symbolReg="("+symbolReg_1+"|"+symbolReg_2+")+";
	static Pattern symbolPattern = Pattern.compile(symbolReg);
	Matcher symbolMatcher= symbolPattern.matcher("");
	*/
	
	static String otherSymbolReg="[^ ]+"; //半角
	static Pattern otherSymbolPattern = Pattern.compile(otherSymbolReg);
	static Matcher otherSymbol= otherSymbolPattern.matcher("");
		 
	public NGramSpliter(int n) {
		
		N = n;
		hanziPatterns=new Pattern[n+1];
		hanziMatchers=new Matcher[n+1];
		
		hanziPatterns[1]=Pattern.compile(hanziReg+"{"+1+"}");
		hanziMatchers[1]=hanziPatterns[1].matcher("");
		
		for(int i=2;i<=N;i++){
			
			hanziPatterns[i]=Pattern.compile(hanziReg+"(?="+hanziReg+"{"+(i-1)+"})");
			hanziMatchers[i]=hanziPatterns[i].matcher("");
			
		}
	}

	public int getGramN() {
		return N;
	}

	public void addCounter(String term) {

		int counter = 0;
		Integer counterObj = counterMap.get(term);
		if (counterObj != null)
			counter = counterObj + counter;
		counterMap.put(term, counter);

	}
	public  List<String> splite(String str, int[] NArray) {
		
		if (str == null)
			return null;

		List<String> instanceList = new LinkedList<String>();
		
		
		/****
		 *  1 process hanzi
		 */
		for(int n:NArray){
			
			if(n>hanziMatchers.length-1)
			{
				System.err.println("NArray n:"+n+" bigger then max hanziMatcher:"+(hanziMatchers.length-1));
				System.exit(1);
			}
			
			Matcher hanMatcher=hanziMatchers[n];
			hanMatcher.reset(str);
			while(hanMatcher.find()){
				
				int startIndex=hanMatcher.start();
				String NGram=str.substring(startIndex, hanMatcher.start()+n);
				instanceList.add(NGram);
			}
			
		}
		
		//remove all hanzi
		hanziMatchers[1].reset(str);
		String subString=hanziMatchers[1].replaceAll(" ");
		// System.out.println("subSentences="+Arrays.toString(subSentences));
		
		
		/****
		 *  2 process  expressionSymbol
		 */
		expressionSymbolMatcher.reset(subString);
		while(expressionSymbolMatcher.find()){
			
			instanceList.add(expressionSymbolMatcher.group());
		}
		//remove all expressionSymbol
		expressionSymbolMatcher.reset(subString);
		subString=expressionSymbolMatcher.replaceAll(" ");
		
		
		/****
		 *  3 process en and digital
		 */
		enOrDigMatcher.reset(subString);
		while(enOrDigMatcher.find()){
			
			instanceList.add(enOrDigMatcher.group());
		}
		//remove all en and digital
		enOrDigMatcher.reset(subString);
		subString=enOrDigMatcher.replaceAll(" ");
		
		
		/****
		 *  4 process symbol
		 */
		otherSymbol.reset(subString);
		while(otherSymbol.find()){
			
			instanceList.add(otherSymbol.group());
		}
		

		return instanceList;

	}

	public static int gramNum(String str) {
		
		if(StringRecognizer.isCN_zhStr(str))
			return str.length();
		return 1;
	}

	public  String reverseCN_zh(String gram) {

		
		if(!StringRecognizer.isCN_zhStr(gram))
			return gram;
		
		
		StringBuilder aStringBuilder = new StringBuilder();
		
		for (int i=gram.length()-1;i>=0;i--) {

			aStringBuilder.append(gram.charAt(i));
		}

		return aStringBuilder.toString();

	}

	public static void main(String[] args) throws Exception {

		
		String str="你30w说iphone4的30说+++++++++法<+MMMMM多少#124xEsfdf";
		
		int[] NArray_1={1};
		int[] NArray_2={1,2};
		NGramSpliter aNGramSpliter=new NGramSpliter(5);
		List<String> strs=aNGramSpliter.splite(str, NArray_1);
		
		for(String aTerm:strs)
		{
			System.out.print(aTerm+",");
		}
		System.out.println();
		strs=aNGramSpliter.splite(str, NArray_2);
		
		for(String aTerm:strs)
		{
			System.out.print(aTerm+",");
		}
		System.out.println();
		
		/*
		strs=SimpleWordSegmentater.makeSegmentGlobal(str);
		
		for(String aTerm:strs)
		{
			System.out.print(aTerm+",");
		}
		*/
	}
}
