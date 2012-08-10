package ylj.NGram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ylj.Util.Pair;

public class testNGram {
	
	
	public static void testGetOtherGrams(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,1);
		
		String str="不捡钱了任务直接出去交任务 ？";
		aNGramOccTable.addGrams(str);
		System.out.println(str);
		System.out.println("****************************");
		System.out.println("NGram="+aNGramOccTable.getNGramIndexStr());
		System.out.println("vectorGram="+aNGramOccTable.getNGramVectorIndexeStr());
		System.out.println("****************************");
		List<String> grams=aNGramCounter.splite(str);
		
		for(String gram:grams)
		{
			System.out.print(" "+gram);
		}
		System.out.println();
		List<String> other_grams=aNGramOccTable.getInstanceNGramStr("直接", str);
		System.out.println("****************************");
		for(String gram:other_grams)
		{
			System.out.print(" "+gram);
		}
		System.out.println();
		System.out.println("****************************");
		GramVector<Integer> vector=aNGramOccTable.getIntVector("直接");
		System.out.println(vector);
		
	
	}
	public static void testAddGrams(){
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,2);
		
		String str="不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
		List<String> grams=aNGramCounter.splite(str);
	
		aNGramOccTable.addGrams(str);
		
		List<String> grams2=aNGramCounter.splite(str2);
		
		aNGramOccTable.addGrams(str);
		
		
		GramVector<Integer> Vector=aNGramOccTable.getIntVector("直接");
		
		
		System.out.println(Vector);
	}
	public static void testGetIntVector(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,2);
		
		String str="不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
		List<String> grams=aNGramCounter.splite(str);
	
		aNGramOccTable.addGrams(str);
		
		List<String> grams2=aNGramCounter.splite(str2);
		
		aNGramOccTable.addGrams(str);
		
		
		GramVector<Integer> vector=aNGramOccTable.getIntVector("直接");
		
		
			System.out.println(vector);
		
	}
	public static void  testGetAllIntVector(){
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,2);
		
		String str="不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
	
		aNGramOccTable.addGrams(str);
		
		aNGramOccTable.addGrams(str);
		
		
		List<GramVector<Integer>> vectors=aNGramOccTable.getAllIntVector();
		
		
		for(GramVector<Integer> vector:vectors)
		{	
			System.out.println(vector);
		}
	}
public static void testGetDoubleVector(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,2);
		
		String str="不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
	
		aNGramOccTable.addGrams(str);
		
		
		aNGramOccTable.addGrams(str);
		
		GramVector<Integer> vectorInt=aNGramOccTable.getIntVector("捡钱");
		//GramVector<Double> vector=aNGramOccTable.getDoubleVector("捡钱");
		
	//	System.out.println(aNGramOccTable.getIndexGramStr());
		//System.out.println(aNGramOccTable.getIndexCountStr());
		System.out.println(vectorInt);
		//System.out.println(vector);
			
		
	}
	public static void  testGetNNearestNegihbor(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,1);
		
		String str=" 不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
		aNGramOccTable.addGrams(str);
		aNGramOccTable.addGrams(str2);
		
		
		ArrayList<Pair<GramVector<Integer>,Double>> vectors=aNGramOccTable.getNNearestNegihbor("任务", 14);
		
		for(Pair<GramVector<Integer>,Double> vector:vectors)
		{	
			System.out.println(vector);
		}
	}
public static void  testGetNNearestNegihbor2(){
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,2);
		String str="不捡钱了直接出去交任务 ";
		String str2="还是不了直接出去交任务吧 ";
		
	
		aNGramOccTable.addGrams(str);
		
		
		
		aNGramOccTable.addGrams(str);
		
		/*
		ArrayList<Pair<GramVector<Double>,Double>> vectors=aNGramOccTable.getNNearestNegihbor2("任务", 14);
		
		
		for(Pair<GramVector<Double>,Double> vector:vectors)
		{	
			System.out.println(vector);
		}
		*/
	}


	public static void main(String[] args)
	{
		testGetOtherGrams();
		testGetNNearestNegihbor();
		//testAddGrams();
		//testGetIntVector();
		//testGetAllIntVector();
		//testGetAllStrVector();
		//testGetDoubleVector();
		//testGetNNearestNegihbor();
		//testGetNNearestNegihbor2();
	}
}
