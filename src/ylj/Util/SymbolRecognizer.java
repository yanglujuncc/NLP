package ylj.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolRecognizer {

	static final String expressionSymbol_reg="#[0-9]{1,3}";
	static final Pattern expressionSymbolPattern=Pattern.compile(expressionSymbol_reg);
	static final Matcher  expressionSymbolMatcher=expressionSymbolPattern.matcher("");
	
	static final String  decorateSymbol_reg="((#[ce][0-9abcdefABCDEF]{0,6})|(#[RGBKYW])|(#[unr#b]))";
	static final Pattern decorateSymbolPattern=Pattern.compile(decorateSymbol_reg);
	static final Matcher decorateSymbolMatcher=decorateSymbolPattern.matcher("");
		
	static final String  halfSymbol_reg="#";
	static final Pattern halfSymbolPattern=Pattern.compile(halfSymbol_reg);
	static final Matcher halfSymbolMatcher=halfSymbolPattern.matcher("");
	
	public static boolean isExpressionSymbol(CharSequence  Str){
		
		expressionSymbolMatcher.reset(Str);
		
		if(expressionSymbolMatcher.matches())
			return true;
		else
			return false;
	}
	
	public static boolean isDecorateSymbol(CharSequence  Str){
		
		decorateSymbolMatcher.reset(Str);
		
		if(decorateSymbolMatcher.matches())
		{
			
			return true;
		}
		else
			return false;
	}

	
	public static boolean isSpecialSymbol(CharSequence  Str){
		
		String str=Str.toString();
		if(str.equals("#"))
			return true;
		
		if(isExpressionSymbol(Str))
			return true;
		
		if(isDecorateSymbol(Str))
			return true;
		
		return false;
	}
}
