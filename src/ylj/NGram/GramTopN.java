package ylj.NGram;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import ylj.NGram.GramVectorModel.WordVectorDig;
import ylj.Util.Pair;

public class GramTopN {

public static void main(String[] args) throws Exception {
		
		
		PropertyConfigurator.configure("conf/log4j.properties");
		

		if(args.length!=4)
		{
			System.out.println("Get Top N Grams of one Gram");
			System.out.println("usage:GramTopN  model GramKind N filter gram");		
			System.out.println("                       	model	(path of the gramModel)");
			System.out.println("                       	GramKind	(like:1 2 EN(en)  means use 1Gram And 2Gram or just englisgh)");
			System.out.println("                       	accepted	(unknow or know or all)");
			System.out.println("                       	N	(topN Grams)");
			System.out.println("Example: ");
			System.out.println("   GramTopN  model 1,2,en unknow 200");
		
			return ;
		}
		
		String modelPath=args[0];
		String GramKindS=args[1];
		String accept=args[2];
		int N=Integer.parseInt(args[3]);
	
		
		
		GramVectorModel gramModel=GramVectorModel.loadModel(modelPath);
		
	
		
		String[] NGrams=GramKindS.split(",");
		List<Pair<String, Long>> resultPairs=gramModel.getTopNGrams(NGrams,N,accept);
		
		
		if(resultPairs==null)
		{
			System.out.println("NULL");
			return ;
		}
		for(Pair<String, Long> pair:resultPairs){
			
			System.out.println("[gram="+pair.first+" counter="+pair.second+"]");
			
		}
	}
}
