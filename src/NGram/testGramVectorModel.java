package NGram;


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import NGram.GramVectorModel.WordVectorDig;
import NGram.GramVectorModel.WordVectorStr;


import ylj.Util.Pair;
import ylj.Util.StringRecognizer;

public class testGramVectorModel {

	public static int[] array_one={1};
	public static int[] array_two={2};
	public static  NGramSpliter nGramSpliter=new NGramSpliter(10);
	public static void testNNearestNegihbor() {

		
		
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "ȥwy�Է��� ��", "ȥ˯��hello�� ��" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			int [] one={1};
			List<String> grams = nGramSpliter.splite(str,one);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams = nGramSpliter.splite(str,array_two);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);

			}
			System.out.println();
		}
		System.out.println("****************************");
		System.out.println(aGramVectorModel);
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		List<WordVectorStr> vectorsStr = aGramVectorModel.getAllWordVectorStr();
		List<WordVectorDig> vectorsDig = aGramVectorModel.getAllWordVectorDig();
		System.out.println("****************************");
		for (int i = 0; i < vectorsStr.size(); i++) {
			System.out.println(vectorsStr.get(i));
			System.out.println(vectorsDig.get(i).vector);
		}

		String[] NGrams={"1","2"};
		ArrayList<Pair<WordVectorDig, Double>> vectors = aGramVectorModel.getNNearestNegihborOf(NGrams,"�Է�", 10);
		for (Pair<WordVectorDig, Double> vector : vectors) {
			System.out.println(vector);
		}

	}
	public static void  testSerializable(){
		
		NGramSpliter a1GramCounter = new NGramSpliter(1);
		NGramSpliter a2GramCounter = new NGramSpliter(2);
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "ȥ�Է��� ��", "ȥ˯���� ��" };

		System.out.println("****************************");
		String str=strs[0];
			System.out.println(str);
			List<String> grams = nGramSpliter.splite(str,array_one);
			for (String gram : grams) {
				System.out.print(" " + gram);
				aGramVectorModel.addHanGramInstence(gram, str);

			}
			grams = nGramSpliter.splite(str,array_two);
			for (String gram : grams) {
				System.out.print(" " + gram);
				aGramVectorModel.addHanGramInstence(gram, str);

			}
			System.out.println();
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		System.out.println(aGramVectorModel.getGramVectorStr("ȥ"));
		
	        
		try {
			GramVectorModel.writeModel("aModel", aGramVectorModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		try {
			GramVectorModel restoredModel=GramVectorModel.loadModel("aModel");
			str=strs[1]; 
			System.out.println(str);
			grams =nGramSpliter.splite(str,array_one);
			for (String gram : grams) {
				System.out.print(" " + gram);
				restoredModel.addHanGramInstence(gram, str);

			}
			grams = nGramSpliter.splite(str,array_two);
			for (String gram : grams) {
				System.out.print(" " + gram);
				restoredModel.addHanGramInstence(gram, str);

			}
			System.out.println();
		System.out.println(restoredModel.GramIndexer);
		System.out.println(restoredModel.WordIndexer);
		System.out.println(restoredModel.getGramVectorStr("ȥ"));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		ArrayList<Pair<GramVector<Double>,Double>> vectors=aNGramOccTable.getNNearestNegihbor2("����", 14);
		
		
		for(Pair<GramVector<Double>,Double> vector:vectors)
		{	
			System.out.println(vector);
		}
		*/
	}
	public static void  testGetAllGrams(){
		NGramSpliter a1GramCounter = new NGramSpliter(1);
		NGramSpliter a2GramCounter = new NGramSpliter(2);
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "ȥwy�Է��� ��", "ȥ˯��hello�� ��" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			List<String> grams =nGramSpliter.splite(str,array_one);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams =nGramSpliter.splite(str,array_two);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);

			}
			System.out.println();
		}
		System.out.println("****************************");
		System.out.println(aGramVectorModel);
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		List<WordVectorStr> vectorsStr = aGramVectorModel.getAllWordVectorStr();
		List<WordVectorDig> vectorsDig = aGramVectorModel.getAllWordVectorDig();
		
		
		System.out.println("****************************");
		for (int i = 0; i < vectorsStr.size(); i++) {
			System.out.println(vectorsStr.get(i));
			System.out.println(vectorsDig.get(i).vector);
		}

	
		System.out.println(aGramVectorModel.getAllGramsOfEn());
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(1));
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(2));
	}
	
	public static void  testGetWordsOfGramStr(){
		NGramSpliter a1GramCounter = new NGramSpliter(1);
		NGramSpliter a2GramCounter = new NGramSpliter(2);
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "ȥwy�Է��� ��", "ȥ˯��hello�� ��" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			List<String> grams =nGramSpliter.splite(str,array_one);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams = nGramSpliter.splite(str,array_two);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);

			}
			System.out.println();
		}
		System.out.println("****************************");
		System.out.println(aGramVectorModel);
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		
		List<WordVectorStr> vectorsStr = aGramVectorModel.getAllWordVectorStr();
		List<WordVectorDig> vectorsDig = aGramVectorModel.getAllWordVectorDig();
		
		
		System.out.println("****************************");
		

	
	
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(1));
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(2));
	
		
	}
	
	public static void  testSaveToFile() throws IOException{
		System.out.println("************** testSaveToFile *************");
		
		NGramSpliter a1GramCounter = new NGramSpliter(1);
		NGramSpliter a2GramCounter = new NGramSpliter(2);
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "ȥwy�Է��� ��", "ȥ˯��hello�� ��" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			List<String> grams = nGramSpliter.splite(str,array_one);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams = nGramSpliter.splite(str,array_two);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(gram, str);

			}
			System.out.println();
		}
		System.out.println("****************************");
		System.out.println(aGramVectorModel);
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		
		List<WordVectorStr> vectorsStr = aGramVectorModel.getAllWordVectorStr();
		List<WordVectorDig> vectorsDig = aGramVectorModel.getAllWordVectorDig();
		
		
		System.out.println("****************************");
		

	
	
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(1));
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(2));
	
		aGramVectorModel.saveToDir("testModel");
	}
	
	public static void  testLoadFromDir() throws IOException{
		System.out.println("************** testLoadFromDir *************");
		GramVectorModel aGramVectorModel=GramVectorModel.loadFromDir("testModel");
		
		System.out.println(aGramVectorModel);
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(1));
		System.out.println(aGramVectorModel.getAllGramsOfHanGramN(2));
		
	}
	public static void main(String[] args) throws IOException {
		
		PropertyConfigurator.configure("conf/log4j.properties");
		//testGetOtherGrams();
		testNNearestNegihbor();
		//testGetWordsOfGramStr();
		
		testSaveToFile();
		testLoadFromDir();
		
		//testSerializable();
		// testAddGrams();
		// testGetIntVector();
		// testGetAllIntVector();
		// testGetAllStrVector();
		// testGetDoubleVector();
		// testGetNNearestNegihbor();
		// testGetNNearestNegihbor2();
	}
}
