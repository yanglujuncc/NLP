import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.PropertyConfigurator;

import ylj.Dict.FileDictLoader;
import ylj.Dict.MemDict;
import ylj.Dict.SogouFileDictReader;
import ylj.Dict.StandardFileDictReader;
import ylj.NGram.GramVector;
import ylj.NGram.NGramOccTable;
import ylj.NGram.NGramSpliter;
import ylj.NGram.SimilarityComputer;
import ylj.Segmentation.AllTermsSegmentater;
import ylj.Segmentation.Segmentater;
import ylj.Segmentation.StanfordSegmenterWraper;
import ylj.Util.Pair;


public class SimilarTermsRecognizer {

	public static void main(String[] args) throws Exception {
		
		
		Map<String, Integer> counterMap = new HashMap<String, Integer>();
		
		
		
		long maxLine=Long.parseLong(args[0]);
		if(maxLine==0)
			maxLine=Long.MAX_VALUE;
		int neighborCounter=Integer.parseInt(args[1]);
		String instanceFile=args[2];
		String targetGram=args[3];
		
		NGramSpliter aNGramCounter=new NGramSpliter(2);
		NGramOccTable aNGramOccTable=new NGramOccTable(2,1);
		
		/*
		
		StanfordSegmenterWraper aStanfordSegmenter=new StanfordSegmenterWraper();
		*/

		
		FileInputStream fis=new FileInputStream(instanceFile);
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br=new BufferedReader(isr);
		
		
		String aline=null;
		
		int i=0;
		long totalNGram=0;
		while((aline=br.readLine())!=null){
			
			
			if(i%1000==0)
			{
				System.out.println("processed "+i+" lines.");
			}
			i++;
			
			if(i>maxLine)
				break;
			
			
			
			//List<String> terms=segmentater.makeSegment(aline);
			
			aNGramOccTable.addGrams(aline);
			List<String> terms=aNGramCounter.splite(aline);
			
			for(String term:terms){
				totalNGram++;
				int counter=0;
				if(counterMap.containsKey(term)){
					counter=counterMap.get(term);
				}
				counterMap.put(term, counter+1);		
			}
		}
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
				counterMap.entrySet());
		
		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						return (o2.getValue() - o1.getValue());
					}
				});
		
		
		int j = 0;
		System.out.println("total gram="+totalNGram);
		for (Map.Entry<String, Integer> e : entryList) {
			j++;
			System.out.println(j + "@" + e.getKey() + ":" + e.getValue());
			if (j == 3000)
				break;
		}
		
		//List<Pair<String,Integer>> strEntryList=aNGramOccTable.getStrVector(gram);

		
		
		/*
		
		ArrayList<Pair<GramVector<Double>,Double>> vectors=aNGramOccTable.getNNearestNegihbor2(gram, 100);
		j = 0;
		for(Pair<GramVector<Double>,Double> v:vectors)
		{	
				
				System.out.println("[gram="+v.first.getGramValue()+" similarity="+v.second+"]");
			
			
		}
		*/
		
		ArrayList<Pair<GramVector<Integer>,Double>> vectors=aNGramOccTable.getNNearestNegihbor(targetGram, neighborCounter);
		j = 0;
		for(Pair<GramVector<Integer>,Double> v:vectors)
		{	
				
			System.out.println("[gram="+v.first.getGramValue()+" similarity="+v.second+"]");
		
		}
		
		/*
		GramVector<Integer> zhaoyao_v=aNGramOccTable.getIntVector(gram);
		GramVector<Integer> zhaoyao2_v=aNGramOccTable.getIntVector("ÕÕÒ«");
		System.out.println(SimilarityComputer.compute(zhaoyao_v.getVectorList(), zhaoyao2_v.getVectorList()));
		System.out.println(zhaoyao_v);
		System.out.println(zhaoyao2_v);
		GramVector<Integer> shuilan_v=aNGramOccTable.getIntVector("Ë®À¶");
		System.out.println(shuilan_v);
		System.out.println(SimilarityComputer.compute(zhaoyao_v.getVectorList(), shuilan_v.getVectorList()));
		*/
		/*
		System.out.println("**********************************************************************************");
		GramVector<Integer> vectorInt=aNGramOccTable.getIntVector(gram);
		GramVector<Double> vector=aNGramOccTable.getDoubleVector(gram);

		System.out.println(aNGramOccTable.getIndexGramStr());
		System.out.println(aNGramOccTable.getIndexCountStr());
		System.out.println(vectorInt);
		System.out.println(vector);
		*/
	}
}
