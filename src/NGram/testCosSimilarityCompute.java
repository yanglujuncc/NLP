package NGram;


import java.util.List;


public class testCosSimilarityCompute {

	
	public static void testCompute(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		
		GramVectorModel aGramVectorModel = new GramVectorModel();
		GramVectorModel aGramVectorModel2 = new GramVectorModel();
		
		String str="����Ǯ��ֱ�ӳ�ȥ������ ";
		String str2="���ǲ���ֱ�ӳ�ȥ������� ";
		
		aGramVectorModel.addHanGramInstence("ֱ��", str);
		aGramVectorModel2.addHanGramInstence("ֱ��", str2);
	
		
		System.out.println("v_X :"+aGramVectorModel.getGramVectorStr("ֱ��"));
		System.out.println("v_y :"+aGramVectorModel2.getGramVectorStr("ֱ��"));
	
		
		double result=SimilarityComputer.computeInt(aGramVectorModel.getGramVectorDig("ֱ��").vector, aGramVectorModel2.getGramVectorDig("ֱ��").vector);
		System.out.println("result="+result);
		System.out.println("result="+15/(Math.sqrt(31)*Math.sqrt(10)));
	}
	public static void main(String[] args){
		
		testCompute();
	}
}
