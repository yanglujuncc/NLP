package NGram;


import java.util.List;


public class testCosSimilarityCompute {

	
	public static void testCompute(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		
		GramVectorModel aGramVectorModel = new GramVectorModel();
		GramVectorModel aGramVectorModel2 = new GramVectorModel();
		
		String str="不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
		aGramVectorModel.addHanGramInstence("直接", str);
		aGramVectorModel2.addHanGramInstence("直接", str2);
	
		
		System.out.println("v_X :"+aGramVectorModel.getGramVectorStr("直接"));
		System.out.println("v_y :"+aGramVectorModel2.getGramVectorStr("直接"));
	
		
		double result=SimilarityComputer.computeInt(aGramVectorModel.getGramVectorDig("直接").vector, aGramVectorModel2.getGramVectorDig("直接").vector);
		System.out.println("result="+result);
		System.out.println("result="+15/(Math.sqrt(31)*Math.sqrt(10)));
	}
	public static void main(String[] args){
		
		testCompute();
	}
}
