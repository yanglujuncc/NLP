package ylj.NGram;


import java.util.List;


public class testCosSimilarityCompute {

	
	public static void testCompute(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,1);
		
		String str="����Ǯ��ֱ�ӳ�ȥ������ ";
		String str2="���ǲ���ֱ�ӳ�ȥ������� ";

		aNGramOccTable.addGrams(str);
		
		aNGramOccTable.addGrams(str2);
		
		
		GramVector<Integer> v_x=aNGramOccTable.getIntVector("����");
		GramVector<Integer> v_y=aNGramOccTable.getIntVector("����");
		System.out.println("v_X :"+v_x);
		System.out.println("v_y :"+v_y);
		
		
		double result=SimilarityComputer.compute(v_x.vector, v_y.vector);
		System.out.println("result="+result);
		System.out.println("result="+15/(Math.sqrt(31)*Math.sqrt(10)));
	}
	public static void main(String[] args){
		
		testCompute();
	}
}
