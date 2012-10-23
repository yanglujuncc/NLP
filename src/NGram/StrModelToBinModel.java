package NGram;

import java.io.IOException;

import org.apache.log4j.xml.DOMConfigurator;

public class StrModelToBinModel {

	public static void main(String[] args) throws IOException {

		DOMConfigurator.configureAndWatch("conf/log4j.xml");

		if (args.length != 3) {
			System.out.println("Transform StrModel To BinModel ");
			System.out.println("StrModelToBinModel: MaxGram  StrModelDirPath BinModelDirPath ");


			return;
		}

	//	String strVectorMap = "Model_2012-10-16/2_GramVectors";
	//	String binVectorMap = "Model_2012-10-16_bin/2_GramVectorsBin";
		int maxGram=Integer.parseInt(args[0]);
		
		String strVectorMap = args[1];
		String binVectorMap = args[2];
		
		
		GramVectorModelBinBased.strModel2BinModel(strVectorMap, binVectorMap,maxGram);

	}

}
