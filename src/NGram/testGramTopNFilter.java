package NGram;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

public class testGramTopNFilter {

	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("conf/log4j.properties");
		GramTopNFilter aGramTopNFilter=new GramTopNFilter();
		
		aGramTopNFilter.setAccept("unknow");
		
	
		aGramTopNFilter.loadKnowWordFromDir("KnowWords");
		aGramTopNFilter.loadStopGramDir("StopGrams");
		
		
		boolean result=aGramTopNFilter.isAccept("∞°”¥Œ“");
		
		System.out.println(result);
	}
}
