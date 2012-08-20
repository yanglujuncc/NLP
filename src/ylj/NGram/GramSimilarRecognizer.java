package ylj.NGram;


import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;

import ylj.NGram.GramVectorModel.WordVectorDig;
import ylj.Util.Pair;


public class GramSimilarRecognizer {

	public static void main(String[] args) throws Exception {
		
		
		PropertyConfigurator.configure("conf/log4j.properties");
		

		if(args.length!=4)
		{
			System.out.println("Find Most N Similar Grams of one Gram");
			System.out.println("usage:GramSimilarRecognizer   model GramKind N gram");
			
			
			System.out.println("                       	model	(path of the gramModel)");
			System.out.println("                       	GramKind	(like:1 2 EN(en)  means use 1Gram And 2Gram or just englisgh)");
			System.out.println("                       	N	(number of Similar Grams)");
			System.out.println("                       	gram	(target gram)");
			System.out.println("Example: ");
			System.out.println("   GramSimilarRecognizer  model 1,2,en 200  Эјвз");
		
			return ;
		}
		
		String modelPath=args[0];
		String GramKindS=args[1];
		int neighborCounter=Integer.parseInt(args[2]);
		String targetGram=args[3];
		
		
		GramVectorModel gramModel=GramVectorModel.loadModel(modelPath);
		
		
		
		ArrayList<Pair<WordVectorDig,Double>> resultPairs=null;
		
		String[] NGrams=GramKindS.split(",");
		resultPairs=gramModel.getNNearestNegihborOf(NGrams,targetGram,neighborCounter);
		
		
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
