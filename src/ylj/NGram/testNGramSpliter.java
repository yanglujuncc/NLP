package ylj.NGram;

import java.util.List;

public class testNGramSpliter {

	public static void main(String[] args)
	{
		
		String str="����Ǯ����dng��234ֱ�ӳ�285ȥ������ ��";
		
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
