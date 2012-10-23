package NGram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import NGram.GramVectorModel.WordVectorDig;


import ylj.Util.Pair;


public class GramSimilarRecognizerInteractive {
	
	GramVectorModel gramModel;
	int threadNum;
	public GramSimilarRecognizerInteractive(int threadNum){
		this.threadNum=threadNum;
	}

	public void loadModel(String modelPath) throws Exception{
		gramModel=GramVectorModel.loadModel(modelPath);
	}
	
public void doRequest(String[] args){
	
	if(args.length!=3)
	{
		System.out.println("Find Most N Similar Grams of one Gram");
		System.out.println("usage: GramKind N gram");
		System.out.println("                       	GramKind	(like:1 2 EN(en)  means use 1Gram And 2Gram or just englisgh)");
		System.out.println("                       	N	(number of Similar Grams)");
		System.out.println("                       	gram	(target gram)");
		System.out.println("Example: ");
		System.out.println("    1,2,3,en 200  ÍøÒ×");
	
		return ;
	}
	
	String GramKindS=args[0];
	int neighborCounter=Integer.parseInt(args[1]);
	String targetGram=args[2];

	List<Pair<WordVectorDig,Double>> resultPairs=null;
	
	String[] NGrams=GramKindS.split("[,£¬]");
	resultPairs=gramModel.getNNearestNegihborOfMutiThreadBased(NGrams,targetGram,neighborCounter,threadNum);
	
	if(resultPairs==null)
	{
		System.out.println("NULL");
		return ;
	}
	for(Pair<WordVectorDig,Double> pair:resultPairs){
		System.out.println("["+pair.first.getGramValue()+" Similarity:"+pair.second+" Occ:"+pair.first.instanceCounter+"]");
	}
	
}
public static void main(String[] args) throws Exception {
		
	DOMConfigurator.configureAndWatch("conf/log4j.xml"); 	

		if(args.length!=2)
		{
			System.out.println("Find Most N Similar Grams of one Gram");
			System.out.println("usage:GramSimilarRecognizer   model ThreadNum");
			
			
			System.out.println("                       	model	(path of the gramModel)");
			System.out.println("                       	ThreadNum	(Threader Num to compute Simility)");
			
			System.out.println("Example: ");
			System.out.println("   GramSimilarRecognizer  model ");
		
			return ;
		}
		
		String modelPath=args[0];
		int threadNum=Integer.parseInt(args[1]);
		
		GramSimilarRecognizerInteractive aInteractiveGramSimilarRecognizer=new GramSimilarRecognizerInteractive(threadNum);
		
		long timeBegin=System.currentTimeMillis();		
		
		aInteractiveGramSimilarRecognizer.loadModel(modelPath);
		long timeEnd=System.currentTimeMillis();
		long loadModelCost=timeEnd-timeBegin;
		System.out.println("LoadModelCost="+loadModelCost+"'ms");
		
		InputStreamReader aInputStramReader=new InputStreamReader(System.in,"gbk");
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
