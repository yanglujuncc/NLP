package NGram;

import java.util.List;

public class testNGramSpliter {

	public static void main(String[] args)
	{
		
		String str="»éµÄ·è¿ñÁË";
		
		NGramSpliter aNGramSpliter=new NGramSpliter(5);
		System.out.println(str);
		System.out.println("****************************");
		int [] one={1};
		
		List<String> grams1=aNGramSpliter.splite(str,one);
		
		
		for(String gram:grams1)
		{
			System.out.print(gram+",");
		}
		System.out.println();
		
		int [] oneAndtwo={1,2,3,4};
		int [] two={2};
		List<String> grams2=aNGramSpliter.splite(str,oneAndtwo);
		for(String gram:grams2)
		{
			System.out.print(gram+",");
		}
	}
}
