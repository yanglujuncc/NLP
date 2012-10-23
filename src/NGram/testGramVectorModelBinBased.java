package NGram;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import ylj.Util.Pair;
import NGram.GramVectorModel.WordVectorDig;
import NGram.GramVectorModelBinBased.IntVectorDig;

public class testGramVectorModelBinBased {

	public static void testTopN_origin() throws IOException {

		String modelDirPath = "Model_2012-10-16_bin";

		GramVectorModel aGramVectorModel = GramVectorModel.loadFromDir(modelDirPath);

		String[] NGrams={"0"};
		ArrayList<Pair<WordVectorDig, Double>>  list = aGramVectorModel.getNNearestNegihborOf(NGrams, "WY", 10);

		for (Pair<WordVectorDig, Double>pair : list) {
			System.out.println(pair.second + "," + pair.first.getGramValue());
		}

	}

	public static void testTopN() throws IOException {

		String modelDirPath = "Model_2012-10-16_bin";

		GramVectorModelBinBased aGramVectorModelBinBased = GramVectorModelBinBased.loadFromDir(modelDirPath);

		IntVectorDig intVectorDig = aGramVectorModelBinBased.getIntVectorDig("WY");
		System.out.println(intVectorDig);

		ArrayList<Pair<IntVectorDig, Double>> list = aGramVectorModelBinBased.getNNearestNegihborOf("WY", 10, 1);

		for (Pair<IntVectorDig, Double> pair : list) {
			System.out.println(pair.second + "," + pair.first.getGram());
		}

	}

	public static void testComputerSim() throws IOException {

		String modelDirPath = "Model_2012-10-16_bin";

		GramVectorModelBinBased aGramVectorModelBinBased = GramVectorModelBinBased.loadFromDir(modelDirPath);

		IntVectorDig intVectorDig = aGramVectorModelBinBased.getIntVectorDig("WY");
		System.out.println(intVectorDig);
		System.out.println(aGramVectorModelBinBased.computeSimlarity(intVectorDig, intVectorDig));
	}

	public static void testTranfor() throws IOException {

		String modelDirPath = "Model_2012-10-16_bin";

		String strVectorMap = "Model_2012-10-16/2_GramVectors";
		String binVectorMap = "Model_2012-10-16_bin/2_GramVectorsBin";

		GramVectorModelBinBased.strVectorMap2bin(strVectorMap, binVectorMap);

	}
	public static void testTranforDir() throws IOException {

		String binModelDirPath = "Model_2012-10-16_bin";

		String strmodelDirPath = "Model_2012-10-16";
	
		GramVectorModelBinBased.strModel2BinModel(strmodelDirPath, binModelDirPath, 20);

	}

	public static void main(String[] args) throws IOException {

		DOMConfigurator.configureAndWatch("conf/log4j.xml");
		// testGetOtherGrams();
		// testNNearestNegihbor();
		// testGetWordsOfGramStr();
		//testTranforDir();
		testTopN();
		//testTopN_origin();
		// testSerializable();
		// testAddGrams();
		// testGetIntVector();
		// testGetAllIntVector();
		// testGetAllStrVector();
		// testGetDoubleVector();
		// testGetNNearestNegihbor();
		// testGetNNearestNegihbor2();
	}
}
