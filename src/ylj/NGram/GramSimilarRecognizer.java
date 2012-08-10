package ylj.NGram;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import ylj.NGram.GramVectorModel.WordVectorDig;
import ylj.Util.Pair;
import ylj.Util.StringRecognizer;

public class GramSimilarRecognizer {

	public static void main(String[] args) throws Exception {
		
		
		PropertyConfigurator.configure("conf/log4j.properties");
		

		if(args.length!=4)
		{
			System.out.println("Find Most N Similar Grams of one Gram");
			System.out.println("usage:GramSimilarRecognizer   model GramKind N gram");
			
			
			System.out.println("                       	model	(path of the gramModel)");
			System.out.println("                       	GramKind	(like:1 2 EN(en) ALL(all) means use 1Gram And 2Gram or just englisgh)");
			System.out.println("                       	N	(number of Similar Grams)");
			System.out.println("                       	gram	(target gram)");
			System.out.println("Example: ");
			System.out.println("   GramSimilarRecognizer  model 2 200  Эјвз");
		
			return ;
		}
		
		String modelPath=args[0];
		String GramKind=args[1];
		int neighborCounter=Integer.parseInt(args[2]);
		String targetGram=args[3];
		
		
		GramVectorModel gramModel=GramVectorModel.loadModel(modelPath);
		
		
		
		ArrayList<Pair<WordVectorDig,Double>> resultPairs=null;
		
		if(GramKind.equals("ALL")||GramKind.equals("all"))
		{
			resultPairs=gramModel.getNNearestNegihbor(targetGram, neighborCounter);
		}
		else if(GramKind.equals("EN")||GramKind.equals("en"))
		{
			resultPairs=gramModel.getNNearestNegihborOfEn(targetGram, neighborCounter);
		}
		else if(StringRecognizer.isADigital(GramKind))
		{
			int ngram=Integer.parseInt(GramKind);
			if(ngram>gramModel.NGramMAX)
			{
				System.out.println("input error of GramKind,GramModel support MaxGram="+gramModel.NGramMAX);
				return ;
			}
			resultPairs=gramModel.getNNearestNegihborOfHan(ngram, targetGram, neighborCounter);
			
		}
		else
		{
			System.out.println("input error of GramKind");
			System.out.println("GramKind = all ALL EN en  1 2 3 4 ..");
			return ;
		}
		
		if(resultPairs==null)
		{
			System.out.println("NULL");
			return ;
		}
		for(Pair<WordVectorDig,Double> pair:resultPairs){
			System.out.println("[gram="+pair.first.getGramValue()+" similarity="+pair.second+"]");
		}
	}
}
