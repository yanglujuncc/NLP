package ylj.NGram;

import java.util.List;

public class testNGramSpliter {

	public static void main(String[] args)
	{
		
		String str="不捡钱了任dng务234直接出285去交任务 ？";
		
		System.out.println(str);
		System.out.println("****************************");
		NGramSpliter aNGramSpliter=new NGramSpliter(2);
		List<String> grams=aNGramSpliter.splite(str);
		
		for(String gram:grams)
		{
			System.out.print(" "+gram);
		}
	}
}
