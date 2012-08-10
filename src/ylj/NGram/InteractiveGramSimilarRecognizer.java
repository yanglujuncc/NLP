package ylj.NGram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.PropertyConfigurator;

import ylj.NGram.GramVectorModel.WordVectorDig;
import ylj.Util.Pair;
import ylj.Util.StringRecognizer;

public class InteractiveGramSimilarRecognizer {
	GramVectorModel gramModel;

public void loadModel(String modelPath) throws Exception{
	 gramModel=GramVectorModel.loadModel(modelPath);
	
}
public void doRequest(String[] args){
	
	if(args.length!=3)
	{
		System.out.println("Find Most N Similar Grams of one Gram");
		System.out.println("usage: GramKind N gram");
		System.out.println("                       	GramKind	(like:1 2 EN(en) ALL(all) means use 1Gram And 2Gram or just englisgh)");
		System.out.println("                       	N	(number of Similar Grams)");
		System.out.println("                       	gram	(target gram)");
		System.out.println("Example: ");
		System.out.println("    2 200  Эјвз");
	
		return ;
	}
	
	String GramKind=args[0];
	int neighborCounter=Integer.parseInt(args[1]);
	String targetGram=args[2];
	
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
public static void main(String[] args) throws Exception {
		
		
		PropertyConfigurator.configure("conf/log4j.properties");
		

		if(args.length!=1)
		{
			System.out.println("Find Most N Similar Grams of one Gram");
			System.out.println("usage:GramSimilarRecognizer   model ");
			
			
			System.out.println("                       	model	(path of the gramModel)");
			System.out.println("Example: ");
			System.out.println("   GramSimilarRecognizer  model ");
		
			return ;
		}
		
		String modelPath=args[0];
		InteractiveGramSimilarRecognizer aInteractiveGramSimilarRecognizer=new InteractiveGramSimilarRecognizer();
		
		aInteractiveGramSimilarRecognizer.loadModel(modelPath);
		
		InputStreamReader aInputStramReader=new InputStreamReader(System.in);
		BufferedReader aBufferedReader=new BufferedReader(aInputStramReader);
		String line=null;
		while((line=aBufferedReader.readLine())!=null){
			
			if(line.equals(""))
				return ;
			
			String[] reqArgs=line.split(" ");
			System.out.println(Arrays.toString(reqArgs));
			aInteractiveGramSimilarRecognizer.doRequest(reqArgs);
			
		}
		
	}
}
