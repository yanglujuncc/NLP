package ylj.NGram;

import java.util.List;

public class testNGramSpliter {

	public static void main(String[] args)
	{
		
		String str="89һ�������� ������xx +++++++++++++++++++++++++++++++++++++++++++++++++++++ ��";
		
		System.out.println(str);
		System.out.println("****************************");
		NGramSpliter aNGramSpliter=new NGramSpliter(1);
		List<String> grams=aNGramSpliter.splite(str);
		
		for(String gram:grams)
		{
			System.out.print(" "+gram);
		}
	}
}
