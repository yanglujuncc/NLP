package ylj.NGram;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import ylj.NGram.GramVectorModel.WordVectorDig;
import ylj.NGram.GramVectorModel.WordVectorStr;
import ylj.Util.Pair;
import ylj.Util.StringRecognizer;

public class testGramVectorModel {

	public static void testNNearestNegihbor() {

		NGramSpliter a1GramCounter = new NGramSpliter(1);
		NGramSpliter a2GramCounter = new NGramSpliter(2);
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "È¥wy³Ô·¹°É £¿", "È¥Ë¯¾õhello°É £¿" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			List<String> grams = a1GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(1,gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams = a2GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(2,gram, str);

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
		ArrayList<Pair<WordVectorDig, Double>> vectors = aGramVectorModel.getNNearestNegihborOf(NGrams,"³Ô·¹", 10);
		for (Pair<WordVectorDig, Double> vector : vectors) {
			System.out.println(vector);
		}

	}
	public static void  testSerializable(){
		
		NGramSpliter a1GramCounter = new NGramSpliter(1);
		NGramSpliter a2GramCounter = new NGramSpliter(2);
		GramVectorModel aGramVectorModel = new GramVectorModel();

		String[] strs = { "È¥³Ô·¹°É £¿", "È¥Ë¯¾õ°É £¿" };

		System.out.println("****************************");
		String str=strs[0];
			System.out.println(str);
			List<String> grams = a1GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				aGramVectorModel.addHanGramInstence(1,gram, str);

			}
			grams = a2GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				aGramVectorModel.addHanGramInstence(2,gram, str);

			}
			System.out.println();
		System.out.println(aGramVectorModel.GramIndexer);
		System.out.println(aGramVectorModel.WordIndexer);
		System.out.println(aGramVectorModel.getGramVectorStr("È¥"));
		
	        
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
			grams = a1GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				restoredModel.addHanGramInstence(1,gram, str);

			}
			grams = a2GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				restoredModel.addHanGramInstence(2,gram, str);

			}
			System.out.println();
		System.out.println(restoredModel.GramIndexer);
		System.out.println(restoredModel.WordIndexer);
		System.out.println(restoredModel.getGramVectorStr("È¥"));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		ArrayList<Pair<GramVector<Double>,Double>> vectors=aNGramOccTable.getNNearestNegihbor2("ÈÎÎñ", 14);
		
		
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

		String[] strs = { "È¥wy³Ô·¹°É £¿", "È¥Ë¯¾õhello°É £¿" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			List<String> grams = a1GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(1,gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams = a2GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(2,gram, str);

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

		String[] strs = { "È¥wy³Ô·¹°É £¿", "È¥Ë¯¾õhello°É £¿" };

		System.out.println("****************************");
		for (String str : strs) {
			System.out.println(str);
			System.out.print("1Gram:");
			List<String> grams = a1GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(1,gram, str);
			}
			System.out.println();
			System.out.print("2Gram:");
			grams = a2GramCounter.splite(str);
			for (String gram : grams) {
				System.out.print(" " + gram);
				if(StringRecognizer.isEnWord(gram))
					aGramVectorModel.addEngGramInstence(gram, str);
				else
					aGramVectorModel.addHanGramInstence(2,gram, str);

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
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("conf/log4j.properties");
		//testGetOtherGrams();
		//testNNearestNegihbor();
		testGetWordsOfGramStr();
		
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
