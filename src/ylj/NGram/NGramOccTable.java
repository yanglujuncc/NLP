package ylj.NGram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;


import ylj.Util.HashIndex;
import ylj.Util.Index;
import ylj.Util.KVPair;
import ylj.Util.Pair;

public class NGramOccTable {
	
	
	
	
	Index<String>  NGramIndexer=new HashIndex<String>();
	Index<String>  NGramVectorIndexer=new HashIndex<String>();
	
	
	//Map<Integer,Integer>	IndexCount=new HashMap<Integer,Integer>();
	
	Map<Integer,Map<Integer,Integer>> NGramVectorMap=new HashMap<Integer,Map<Integer,Integer>>();
	
	
	
	int NGram;
	int NGramOfVector;
	
	NGramSpliter NGramSpliter;
	NGramSpliter NGramVectorSpliter;
	
	public NGramOccTable(int n,int nv){
		NGram=n;
		NGramOfVector=nv;
		NGramSpliter=new NGramSpliter(NGram);
		NGramVectorSpliter=new NGramSpliter(NGramOfVector);
		
	}
	List<Integer> toIndex(List<String> grams){
		
		List<Integer> indexList=new LinkedList<Integer>();
		for(String gram:grams)
		{
			int index=NGramIndexer.indexOf(gram, true);
			indexList.add(index);	
		}
		
		return indexList;
	}
	public List<String> getInstanceNGramStr(String gram,String str){
		
		String filtedStr=str.replace(gram, " ");
		
		return NGramVectorSpliter.splite(filtedStr);
	}
	
	/*
	public void addGramCount(int gramIndex){
		
		int counter=0;
	
		Integer gramCounter=IndexCount.get(gramIndex);
		if(gramCounter!=null)
			counter=gramCounter;	
		IndexCount.put(gramIndex, counter+1);	
	}
	
	public int getDF(int gramIndex){
		Map<Integer,Integer> vector=gramVectorMap.get(gramIndex);
		return vector.size();
	}
	*/
	
	public void addGrams(String str){
		
		List<String> grams=NGramSpliter.splite(str);
		
		for(String gram:grams){
			
			int index=NGramIndexer.indexOf(gram, true);
			
			//addGramCount(index);
			
			Map<Integer,Integer> vector=NGramVectorMap.get(index);
			
			if(vector==null)
			{
				vector=new HashMap<Integer,Integer>();
				NGramVectorMap.put(index, vector);
			}
			
			List<String> vectorGrams=getInstanceNGramStr(gram, str);
			for(String elementGram:vectorGrams)
			{
				int counter=0;
				int elementIndex=NGramVectorIndexer.indexOf(elementGram, true);
				Integer elementIndexCounter=vector.get(elementIndex);
				if(elementIndexCounter!=null)
					counter=elementIndexCounter;	
				vector.put(elementIndex, counter+1);				
			}
				
		}
	}
	
	/*
	public GramVector<Double> getDoubleVector(String gram){
		 
		
		int index=NGramIndexer.indexOf(gram);
		Map<Integer,Integer>  gramVector=NGramVectorMap.get(index);
		if(gramVector==null)
			return null;
		
		Set<Entry<Integer,Integer>> entrys=gramVector.entrySet();
		if(entrys==null)
			return null;
		
		GramVector<Double> aDoubleGramVector=new GramVector<Double>();
		aDoubleGramVector.gramValue=gram;
		aDoubleGramVector.index=index;
		aDoubleGramVector.vector=new ArrayList<KVPair<Integer, Double>>();
		
		for(Entry<Integer,Integer> entry:entrys){
			
			
			int indexOfEntry=entry.getKey();
			int counterOfIdex=IndexCount.get(indexOfEntry);
			
			KVPair<Integer, Double> kvPair=new KVPair<Integer, Double>();
			kvPair.setKey(indexOfEntry);
			kvPair.setValue((entry.getValue()*1.0)/counterOfIdex);
			aDoubleGramVector.vector.add(kvPair);
			
		}
		
		
		Collections.sort(aDoubleGramVector.vector,
				new Comparator<KVPair<Integer, Double>>() {
					public int compare(KVPair<Integer, Double> o1,
							KVPair<Integer, Double> o2) {
						return (o1.key()-o2.key());
					}
				});
		
		return aDoubleGramVector;
	}
	
	
	*/
	public GramVector<Integer> getIntVector(String gram){
		
		int index=NGramIndexer.indexOf(gram);
		Map<Integer,Integer>  gramVector=NGramVectorMap.get(index);
		if(gramVector==null)
			return null;

		
		Set<Entry<Integer,Integer>> entrys=gramVector.entrySet();
		if(entrys==null)
			return null;
		ArrayList<Entry<Integer, Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(
				entrys);
		
		Collections.sort(entryList,
				new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Map.Entry<Integer, Integer> o1,
							Map.Entry<Integer, Integer> o2) {
						return (o1.getKey()-o2.getKey());
					}
				});
		
		ArrayList<KVPair<Integer, Integer>> kvPairList = new ArrayList<KVPair<Integer, Integer>>(
				entrys.size());
		
		for(Entry<Integer, Integer> entry:entryList)
		{
			KVPair<Integer, Integer> newPair=new KVPair<Integer, Integer>();
			newPair.setKey(entry.getKey());
			newPair.setValue(entry.getValue());
			kvPairList.add(newPair);
			
		}
		
		
		GramVector<Integer> aIntGramVector=new GramVector<Integer>();
		aIntGramVector.gramValue=gram;
		aIntGramVector.index=index;
		aIntGramVector.vector=kvPairList;
		return aIntGramVector;
		
	}
	/*
	public GramVector<Double> getTFIDFVector(String gram){

		int index=nGramIndexer.indexOf(gram);
		Map<Integer,Integer>  gramVector=gramVectorMap.get(index);
		if(gramVector==null)
			return null;
		
		Set<Entry<Integer,Integer>> entrys=gramVector.entrySet();
		if(entrys==null)
			return null;
		
		GramVector<Double> aDoubleGramVector=new GramVector<Double>();
		aDoubleGramVector.gramValue=gram;
		aDoubleGramVector.index=index;
		aDoubleGramVector.vector=new ArrayList<KVPair<Integer, Double>>();
		
		for(Entry<Integer,Integer> entry:entrys){
			
			int indexOfEntry=entry.getKey();
			int DFOfIdex=getDF(indexOfEntry);
			
			KVPair<Integer, Double> kvPair=new KVPair<Integer, Double>();
			kvPair.setKey(indexOfEntry);
			kvPair.setValue((entry.getValue()*1.0)/DFOfIdex);
			aDoubleGramVector.vector.add(kvPair);
			
		}
		
		
		Collections.sort(aDoubleGramVector.vector,
				new Comparator<KVPair<Integer, Double>>() {
					public int compare(KVPair<Integer, Double> o1,
							KVPair<Integer, Double> o2) {
						return (o1.key()-o2.key());
					}
				});
		
		return aDoubleGramVector;
	}
	*/
	public List<GramVector<Integer>> getAllIntVector(){
		List<GramVector<Integer>> returnList =new LinkedList<GramVector<Integer>> ();
		
		
		Iterator<String> gramIter= NGramIndexer.iterator();
		while(gramIter.hasNext()){
			String gram=gramIter.next();
			GramVector<Integer> vector= getIntVector( gram);
			if(vector!=null)
				returnList.add(vector);
			else
			{
				System.err.println(" something wrong..");
				
			}
		}
		
		return returnList;
	}
	
	public String getNGramIndexStr(){
		return NGramIndexer.toString();
	}
	public String getNGramVectorIndexeStr(){
		return NGramVectorIndexer.toString();
	}
	/*
	public String getIndexCountStr(){
		//StringBuilder aStringBuilder=new StringBuilder();
		
		
		ArrayList<Entry<Integer, Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(
					IndexCount.entrySet());
		
		Collections.sort(entryList,
				new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Map.Entry<Integer, Integer> o1,
							Map.Entry<Integer, Integer> o2) {
						return (o1.getKey()-o2.getKey());
					}
				});
		
		return entryList.toString();
	}
	*/
	/*
	public ArrayList<Pair<GramVector<Double>,Double>> getNNearestNegihbor2(String gram,int n){
		
		GramVector<Double> thisVector= getTFIDFVector( gram);
		if(thisVector==null)
			return null;
		
		PriorityQueue<Pair<GramVector<Double>,Double>> NNearestVectors=new PriorityQueue<Pair<GramVector<Double>,Double>>(n,
				  new Comparator<Pair<GramVector<Double>,Double>>(){
						public int compare(Pair<GramVector<Double>,Double> o1,
								Pair<GramVector<Double>,Double> o2) {
								double comResult=o1.second() - o2.second();
								if(comResult>0)
									return 1;
								else if(comResult<0)
									return -1;
								return 0;
						}
		});
		Iterator<String> it=nGramIndexer.iterator();
		int i=0;
		int totalVecNum=nGramIndexer.size();
		while(it.hasNext())
		{
			if(i%10000==0)
				System.out.println(i+" Vectors compared. total="+totalVecNum+" "+(int)((i*100)/totalVecNum)+"%");
			i++;
			
			String gramString=it.next();
			if(gramString.equals(gram))
				continue;
			
			GramVector<Double> otherV=getTFIDFVector( gramString);
			double simlarity=SimilarityComputer.computeDouble(thisVector.vector, otherV.vector);
			Pair<GramVector<Double>,Double> aNewPair=new Pair<GramVector<Double>,Double>();
			aNewPair.first=otherV;
			aNewPair.second=simlarity;
			
			if(NNearestVectors.size()<n)
				NNearestVectors.add(aNewPair);
			else
			{
				if(NNearestVectors.peek().second<simlarity)
				{
					NNearestVectors.poll();
					NNearestVectors.add(aNewPair);
				}
			}
			
		//	System.out.println(aNewPair);
		//	System.out.println("#############################################3");
		}
		ArrayList<Pair<GramVector<Double>,Double>> returnList=new ArrayList<Pair<GramVector<Double>,Double>>(n);
		
		while(!NNearestVectors.isEmpty())
			returnList.add(NNearestVectors.poll());
		
		return returnList;
	}
	*/
	public ArrayList<Pair<GramVector<Integer>,Double>> getNNearestNegihbor(String gram,int n){
		
		GramVector<Integer> thisVector= getIntVector( gram);
		if(thisVector==null)
			return null;
		
		PriorityQueue<Pair<GramVector<Integer>,Double>> NNearestVectors=new PriorityQueue<Pair<GramVector<Integer>,Double>>(n,
				  new Comparator<Pair<GramVector<Integer>,Double>>(){
						public int compare(Pair<GramVector<Integer>,Double> o1,
								Pair<GramVector<Integer>,Double> o2) {
								double comResult=o1.second() - o2.second();
								if(comResult>0)
									return 1;
								else if(comResult<0)
									return -1;
								return 0;
						}
		});
		Iterator<String> it=NGramIndexer.iterator();
		int i=0;
		int totalVecNum=NGramIndexer.size();
		while(it.hasNext())
		{
			if(i%10000==0)
				System.out.println(i+" Vectors compared. total="+totalVecNum+" "+(int)((i*100)/totalVecNum)+"%");
			i++;
			
			String gramString=it.next();
			if(gramString.equals(gram))
				continue;
			
			GramVector<Integer> otherV=getIntVector(gramString);
			double simlarity=SimilarityComputer.compute(thisVector.vector, otherV.vector);
			Pair<GramVector<Integer>,Double> aNewPair=new Pair<GramVector<Integer>,Double>();
			aNewPair.first=otherV;
			aNewPair.second=simlarity;
			
			if(NNearestVectors.size()<n)
				NNearestVectors.add(aNewPair);
			else
			{
				if(NNearestVectors.peek().second<simlarity)
				{
					NNearestVectors.poll();
					NNearestVectors.add(aNewPair);
				}
			}
			
		//	System.out.println(aNewPair);
		//	System.out.println("#############################################3");
		}
		ArrayList<Pair<GramVector<Integer>,Double>> returnList=new ArrayList<Pair<GramVector<Integer>,Double>>(n);
		
		while(!NNearestVectors.isEmpty())
			returnList.add(NNearestVectors.poll());
		
		return returnList;
		
	}
	
	
}
