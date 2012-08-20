package ylj.NGram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ylj.Util.HashIndex;
import ylj.Util.Index;
import ylj.Util.KVPair;
import ylj.Util.Pair;
import ylj.Util.StringRecognizer;

public class GramVectorModel implements Serializable {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(GramVectorModel.class
			.getName());
	private static final long serialVersionUID = 1644517928490202752L;

	Index<String> GramIndexer = new HashIndex<String>(); // 0 1 2 3 ..... 
	Index<String> WordIndexer = new HashIndex<String>(); // 0 1 2 3 ..... 

	// Map<Integer,Map<Integer,Long>> GramVectorMap=new
	// HashMap<Integer,Map<Integer,Long>>();

	int NGramMAX = 4;
	ArrayList<Map<Integer, Map<Integer, Long>>> HanNGramVectorMaps;
	Map<Integer, Map<Integer, Long>> EnVectorMap;
	long instanceCounter = 0;

	final static int InstanceCounterIndex=-1;
	final static String InstanceCounterStr="INS";
	NGramSpliter wordSpliter = new NGramSpliter(1);
	
	int[] HanNGramMapInitSize={
			
	};
	
	public GramVectorModel() {
		HanNGramVectorMaps = new ArrayList<Map<Integer, Map<Integer, Long>>>(
				NGramMAX);
		for (int i = 0; i < NGramMAX; i++)
			HanNGramVectorMaps.add(new HashMap<Integer, Map<Integer, Long>>(2000000));

		EnVectorMap = new HashMap<Integer, Map<Integer, Long>>(50000);

	}

	

	public class WordVectorDig {

		int gramIndex;
		long instanceCounter;
		ArrayList<KVPair<Integer, Long>> vector;

		public ArrayList<KVPair<Integer, Long>> getVectorList() {
			return vector;
		}

		public String getGramValue() {
			return GramIndexer.get(gramIndex);
		}

		public int getGramIndex() {
			return gramIndex;
		}

		public String toString() {
			return "Index=" + getGramIndex() + " GramStr=" + getGramValue()+" InsCout="+instanceCounter
					+ " " + vector;
		}

	}

	public class WordVectorStr {

		String gramStr;
		long instanceCounter;
		ArrayList<KVPair<String, Long>> vector;

		public ArrayList<KVPair<String, Long>> getVectorList() {
			return vector;
		}

		public String getGramValue() {
			return gramStr;
		}

		public int getGramIndex() {
			return GramIndexer.indexOf(gramStr);
		}

		public String toString() {
			return "Index=" + getGramIndex() + " GramStr=" + getGramValue()+" InsCout="+instanceCounter
					+ " \n" + vector;
		}

		public List<String> getWords() {

			List<String> wordsList = new LinkedList<String>();
			for (KVPair<String, Long> pair : vector) {
				wordsList.add(pair.key());
			}
			return wordsList;
		}

	}

	public long getGramNum() {
		return GramIndexer.size();
	}

	public long getWordNum() {
		return WordIndexer.size();
	}

	public long instanceCounterIncrease() {

		return ++instanceCounter;
	}

	public static GramVectorModel loadModel(String path) throws Exception {

		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		logger.info("Loading Model From " + path + "... ");

		GramVectorModel restoredModel = (GramVectorModel) ois.readObject();
		logger.info("Load Model complete. " + restoredModel);

		ois.close();

		return restoredModel;

	}

	public static void writeModel(String path, GramVectorModel model)
			throws Exception {

		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		logger.info("Writing Model To [" + path + "] ... ...");
		oos.writeObject(model);
		oos.flush();
		oos.close();
		logger.info("Write Model Complete." + model);

	}
	public long getInstanceCounterOfGram(String gram){
		
		int index = GramIndexer.indexOf(gram, false);
		if(index==-1)
			return 0;
		
		 Map<Integer, Long> mapVector= getGramMapVector(index);
		 Long InstanceCounter=mapVector.get(InstanceCounterIndex);
		 
		return InstanceCounter;
		
	}
	public List<String> getWordsOfGramStr(String gram, String str) {

		String filtedStr = str.replace(gram, " ");

		return wordSpliter.splite(filtedStr);
	}

	public void mergeVectorMap(Map<Integer, Map<Integer, Long>> vectorMap,
			WordVectorStr otherWordVectorStr) {

		int index = GramIndexer.indexOf(otherWordVectorStr.gramStr, true);
		Map<Integer, Long> vector = vectorMap.get(index);
		if (vector == null) {
			vector = new TreeMap<Integer, Long>();
			vectorMap.put(index, vector);
		}

		for (KVPair<String, Long> wordPair : otherWordVectorStr.vector) {
			long counter = 0;
			int wordIndex = WordIndexer.indexOf(wordPair.key(), true);
			Long wordCounter = vector.get(wordIndex);
			if (wordCounter != null)
				counter = wordCounter;
			vector.put(wordIndex, counter + wordPair.value());
		}

	}

	public void mergeHanGramVector(WordVectorStr otherWordVectorStr, int NGram) {

		Map<Integer, Map<Integer, Long>> gramVectorMap = HanNGramVectorMaps
				.get(NGram - 1);
		mergeVectorMap(gramVectorMap, otherWordVectorStr);

	}

	public void mergeEnGramVector(WordVectorStr otherWordVectorStr) {

		mergeVectorMap(EnVectorMap, otherWordVectorStr);

	}

	public GramVectorModel mergeModel(GramVectorModel otherModel) {

		logger.info("Merging Model ...");

		long count = 0;
		long total = otherModel.getGramNum();

		logger.info("Merging Han Grams...");
		for (int ngram = 1; ngram <= NGramMAX; ngram++) {

			logger.info("Merging Han " + ngram + " Grams...");
			Map<Integer, Map<Integer, Long>> vectorMap = otherModel.HanNGramVectorMaps
					.get(ngram - 1);
			List<WordVectorStr> wordVectors = otherModel
					.getAllWordVectorStrOf(vectorMap);
			long ngramCount = 0;
			for (WordVectorStr wordVectorStr : wordVectors) {
				if (count % 50000 == 0)
					logger.info("Merged " + count + "/" + total + " Grams...");
				mergeHanGramVector(wordVectorStr, ngram);
				count++;
				ngramCount++;
			}

			logger.info("Merging Han " + ngram + " Grams complete, merged "
					+ ngramCount + " grams.");
		}

		logger.info("Merging En Grams...");
		List<WordVectorStr> wordVectors = otherModel
				.getAllWordVectorStrOf(otherModel.EnVectorMap);
		long enCount = 0;
		for (WordVectorStr wordVectorStr : wordVectors) {
			if (count % 50000 == 0)
				logger.info("Merged " + count + "/" + total + " Grams...");
			mergeEnGramVector(wordVectorStr);
			count++;
			enCount++;
		}
		this.instanceCounter += otherModel.instanceCounter;
		logger.info("Merging En Grams complete, merged " + enCount + " grams.");

		return this;
	}

	private void addGramInstenceToVectorMap(
			Map<Integer, Map<Integer, Long>> VectorMap, String gram, String str) {

		int index = GramIndexer.indexOf(gram, true);

		Map<Integer, Long> vector = VectorMap.get(index);

		if (vector == null) {
			vector = new TreeMap<Integer, Long>();
			VectorMap.put(index, vector);
			//init instance counter to 0
			vector.put(InstanceCounterIndex, (long) 0);
		}

		List<String> words = getWordsOfGramStr(gram, str);
		for (String word : words) {
			long counter = 0;
			int wordIndex = WordIndexer.indexOf(word, true);
			Long wordCounter = vector.get(wordIndex);
			if (wordCounter != null)
				counter = wordCounter;
			vector.put(wordIndex, counter + 1);
		}
		
		//instance counter ++ 
		Long instanceCounter=vector.get(InstanceCounterIndex);
		vector.put(InstanceCounterIndex, ++instanceCounter);
			

	}

	public void addHanGramInstence(int NGram, String gram, String str) {

		// addGramCount(index);
		Map<Integer, Map<Integer, Long>> NGramVectorMap = HanNGramVectorMaps
				.get(NGram - 1);

		addGramInstenceToVectorMap(NGramVectorMap, gram, str);

	}

	public void addEngGramInstence(String gram, String str) {
		
		addGramInstenceToVectorMap(EnVectorMap, gram, str);

	}

	private WordVectorDig mapVectorToWordVectorDig(int index,
			Map<Integer, Long> gramVector) {

		if (gramVector == null)
			return null;

		Set<Entry<Integer, Long>> entrys = gramVector.entrySet();
		if (entrys == null)
			return null;
		
		ArrayList<Entry<Integer, Long>> entryList = new ArrayList<Map.Entry<Integer, Long>>(
				entrys);

		Collections.sort(entryList, new Comparator<Map.Entry<Integer, Long>>() {
			public int compare(Map.Entry<Integer, Long> o1,
					Map.Entry<Integer, Long> o2) {
				return (o1.getKey() - o2.getKey());
			}
		});

		ArrayList<KVPair<Integer, Long>> kvPairList = new ArrayList<KVPair<Integer, Long>>(
				entrys.size());
		WordVectorDig aWordVectorDig = new WordVectorDig();
		
		for (Entry<Integer, Long> entry : entryList) {
			KVPair<Integer, Long> newPair = new KVPair<Integer, Long>();
			
			// the special key  
			if(entry.getKey()==InstanceCounterIndex)
			{
				aWordVectorDig.instanceCounter=entry.getValue();
				continue;
			}
			
			newPair.setKey(entry.getKey());
			newPair.setValue(entry.getValue());
			kvPairList.add(newPair);

		}

		
		aWordVectorDig.gramIndex = index;
		aWordVectorDig.vector = kvPairList;
		return aWordVectorDig;
	}

	private WordVectorStr wordVectorDigToWordVectorStr(
			WordVectorDig aWordVectorDig) {
		if (aWordVectorDig == null)
			return null;
		WordVectorStr aWordVectorStr = new WordVectorStr();
		aWordVectorStr.gramStr = GramIndexer.get(aWordVectorDig.gramIndex);

		ArrayList<KVPair<String, Long>> kvPairList = new ArrayList<KVPair<String, Long>>(
				aWordVectorDig.vector.size());

		for (KVPair<Integer, Long> entry : aWordVectorDig.vector) {
			KVPair<String, Long> newPair = new KVPair<String, Long>();
			
			
			newPair.setKey(WordIndexer.get(entry.key()));
			
			newPair.setValue(entry.value());
			kvPairList.add(newPair);

		}

		aWordVectorStr.vector = kvPairList;
		aWordVectorStr.instanceCounter=aWordVectorDig.instanceCounter;
		return aWordVectorStr;
	}

	private WordVectorStr mapVectorToWordVectorStr(int index,
			Map<Integer, Long> gramVector) {

		if (gramVector == null)
			return null;
		WordVectorDig aWordVectorDig = mapVectorToWordVectorDig(index,
				gramVector);

		return wordVectorDigToWordVectorStr(aWordVectorDig);
	}

	public Map<Integer, Long> getGramMapVector(int gramIndex) {
		
		if(gramIndex==-1)
			return null;
		Map<Integer, Long> gramVector = null;

		for (Map<Integer, Map<Integer, Long>> gramMap : HanNGramVectorMaps) {
			gramVector = gramMap.get(gramIndex);
			if (gramVector != null)
				break;
		}
		if (gramVector == null)
			gramVector = EnVectorMap.get(gramIndex);

		if (gramVector == null)
			return null;

		return gramVector;
	}

	public WordVectorDig getGramVectorDig(String gram) {

		int index = GramIndexer.indexOf(gram);

		Map<Integer, Long> gramVectorMap = getGramMapVector(index);

		return mapVectorToWordVectorDig(index, gramVectorMap);

	}

	public WordVectorStr getGramVectorStr(String gram) {

		int index = GramIndexer.indexOf(gram);

		Map<Integer, Long> gramVectorMap = getGramMapVector(index);

		return mapVectorToWordVectorStr(index, gramVectorMap);

	}

	public List<WordVectorStr> getAllWordVectorStr() {
		List<WordVectorStr> vectors = new LinkedList<WordVectorStr>();
		Iterator<String> it = GramIndexer.iterator();
		while (it.hasNext()) {
			WordVectorStr gramVector = getGramVectorStr(it.next());
			vectors.add(gramVector);
		}
		return vectors;
	}

	public List<WordVectorStr> getAllWordVectorStrOf(
			Map<Integer, Map<Integer, Long>> vectorMap) {

		if (vectorMap == null)
			return null;
		List<WordVectorStr> vectors = new LinkedList<WordVectorStr>();

		Set<Entry<Integer, Map<Integer, Long>>> vectorSet = vectorMap
				.entrySet();
		for (Entry<Integer, Map<Integer, Long>> vectorEntry : vectorSet) {

			WordVectorStr aWordVectorStr = mapVectorToWordVectorStr(
					vectorEntry.getKey(), vectorEntry.getValue());
			vectors.add(aWordVectorStr);
		}

		return vectors;
	}

	public List<WordVectorDig> getAllWordVectorDigOf(
			Map<Integer, Map<Integer, Long>> vectorMap) {

		if (vectorMap == null)
			return null;
		List<WordVectorDig> vectors = new LinkedList<WordVectorDig>();

		Set<Entry<Integer, Map<Integer, Long>>> vectorSet = vectorMap
				.entrySet();
		for (Entry<Integer, Map<Integer, Long>> vectorEntry : vectorSet) {

			WordVectorDig aWordVectorDig = mapVectorToWordVectorDig(
					vectorEntry.getKey(), vectorEntry.getValue());
			vectors.add(aWordVectorDig);
		}

		return vectors;
	}

	public List<WordVectorDig> getAllWordVectorDig() {
		List<WordVectorDig> vectors = new LinkedList<WordVectorDig>();
		Iterator<String> it = GramIndexer.iterator();
		while (it.hasNext()) {
			WordVectorDig gramVector = getGramVectorDig(it.next());
			vectors.add(gramVector);
		}
		return vectors;
	}

	private List<Map<Integer, Map<Integer, Long>>> getVectorMapsOfNGram(String[] NGrams){
		
		List<Map<Integer, Map<Integer, Long>>> vectorMaps = new LinkedList<Map<Integer, Map<Integer, Long>>>();
		for (String NGram : NGrams) {

			if (StringRecognizer.isEnWord(NGram)) {
				if (NGram.equalsIgnoreCase("en")) 
				{
					logger.info("En Gram VectorMap grams=" + EnVectorMap.size());
					vectorMaps.add(EnVectorMap);
				} else {
					logger.error("arg ERROR , NGram=" + NGram);
					System.exit(1);
				}

				
			} else if (StringRecognizer.isADigital(NGram)) {
				
				int NGram_int = Integer.parseInt(NGram);
				
				if(NGram_int>NGramMAX)
				{
					logger.error("No "+NGram_int+" NGram in this mode .");
					continue;
				}
				Map<Integer, Map<Integer, Long>> vectorMap = HanNGramVectorMaps
						.get(NGram_int - 1);
				
					logger.info(NGram + " Gram VectorMap grams="
							+ vectorMap.size());

					vectorMaps.add(vectorMap);
				
			} else {

				logger.error("arg ERROR , NGram=" + NGram);
				System.exit(1);
			}

		}
		return vectorMaps;
	}
	public ArrayList<Pair<WordVectorDig, Double>> getNNearestNegihborOf(
			String[] NGrams, String gram, int n) {

		List<Map<Integer, Map<Integer, Long>>> vectorMaps =getVectorMapsOfNGram(NGrams);
		
		return getNNearestNegihborInVectorMaps(vectorMaps, gram, n);
	}

	/*
	 * public ArrayList<Pair<WordVectorDig,Double>>
	 * getNNearestNegihborInVectorMap(Map<Integer,Map<Integer,Long>>
	 * vectorMap,String gram,int n){
	 * 
	 * WordVectorDig thisVector= getGramVectorDig(gram); if(thisVector==null)
	 * return null;
	 * 
	 * PriorityQueue<Pair<WordVectorDig,Double>> NNearestVectors=new
	 * PriorityQueue<Pair<WordVectorDig,Double>>(n, new
	 * Comparator<Pair<WordVectorDig,Double>>(){ public int
	 * compare(Pair<WordVectorDig,Double> o1, Pair<WordVectorDig,Double> o2) {
	 * double comResult=o1.second() - o2.second(); if(comResult>0) return 1;
	 * else if(comResult<0) return -1; return 0; } });
	 * 
	 * 
	 * Set<Entry<Integer, Map<Integer, Long>>> keySet=vectorMap.entrySet();
	 * 
	 * int i=0; int totalVecNum=keySet.size(); for(Entry<Integer, Map<Integer,
	 * Long>> vectorEntry:keySet){
	 * 
	 * if(i%10000==0)
	 * logger.info(i+" Vectors compared. total="+totalVecNum+" "+(
	 * int)((i*100)/totalVecNum)+"%"); i++;
	 * 
	 * WordVectorDig
	 * otherV=mapVectorToWordVectorDig(vectorEntry.getKey(),vectorEntry
	 * .getValue());
	 * 
	 * if(thisVector.getGramIndex()==otherV.getGramIndex()) continue;
	 * 
	 * 
	 * double simlarity=SimilarityComputer.computeLong(thisVector.vector,
	 * otherV.vector); Pair<WordVectorDig,Double> aNewPair=new
	 * Pair<WordVectorDig,Double>(); aNewPair.first=otherV;
	 * aNewPair.second=simlarity;
	 * 
	 * if(NNearestVectors.size()<n) NNearestVectors.add(aNewPair); else {
	 * if(NNearestVectors.peek().second<simlarity) { NNearestVectors.poll();
	 * NNearestVectors.add(aNewPair); } } }
	 * 
	 * ArrayList<Pair<WordVectorDig,Double>> returnList=new
	 * ArrayList<Pair<WordVectorDig,Double>>(n);
	 * 
	 * while(!NNearestVectors.isEmpty()) returnList.add(NNearestVectors.poll());
	 * 
	 * return returnList;
	 * 
	 * }
	 */
	public ArrayList<Pair<WordVectorDig, Double>> getNNearestNegihborInVectorMaps(
			List<Map<Integer, Map<Integer, Long>>> vectorMapList, String gram,
			int n) {

		WordVectorDig thisVector = getGramVectorDig(gram);
		if (thisVector == null)
			return null;

		PriorityQueue<Pair<WordVectorDig, Double>> NNearestVectors = new PriorityQueue<Pair<WordVectorDig, Double>>(
				n, new Comparator<Pair<WordVectorDig, Double>>() {
					public int compare(Pair<WordVectorDig, Double> o1,
							Pair<WordVectorDig, Double> o2) {
						double comResult = o1.second() - o2.second();
						if (comResult > 0)
							return 1;
						else if (comResult < 0)
							return -1;
						return 0;
					}
				});

		Set<Entry<Integer, Map<Integer, Long>>> keySet = new HashSet<Entry<Integer, Map<Integer, Long>>>();
		for (Map<Integer, Map<Integer, Long>> vectorMap : vectorMapList) {
			keySet.addAll(vectorMap.entrySet());
		}

		int i = 0;
		int totalVecNum = keySet.size();
		for (Entry<Integer, Map<Integer, Long>> vectorEntry : keySet) {

			if (i % 10000 == 0)
				logger.info(i + " Vectors compared. total=" + totalVecNum + " "
						+ (int) ((i * 100) / totalVecNum) + "%");
			i++;

			WordVectorDig otherV = mapVectorToWordVectorDig(
					vectorEntry.getKey(), vectorEntry.getValue());

			if (thisVector.getGramIndex() == otherV.getGramIndex())
				continue;

			double simlarity = SimilarityComputer.computeLong(
					thisVector.vector, otherV.vector);
			
			Pair<WordVectorDig, Double> aNewPair = new Pair<WordVectorDig, Double>();
			aNewPair.first = otherV;
			aNewPair.second = simlarity;

			if (NNearestVectors.size() < n)
				NNearestVectors.add(aNewPair);
			else {
				if (NNearestVectors.peek().second < simlarity) {
					NNearestVectors.poll();
					NNearestVectors.add(aNewPair);
				}
			}
		}

		ArrayList<Pair<WordVectorDig, Double>> returnList = new ArrayList<Pair<WordVectorDig, Double>>(
				n);

		while (!NNearestVectors.isEmpty())
			returnList.add(NNearestVectors.poll());

		return returnList;

	}

	public List<Pair<String, Integer>> getAllVectorMapGrams(
			Map<Integer, Map<Integer, Long>> vectorMap) {

		if (vectorMap == null)
			return null;

		List<Pair<String, Integer>> grams = new LinkedList<Pair<String, Integer>>();

		Set<Integer> gramSets = vectorMap.keySet();
		for (Integer gramIndex : gramSets) {
			Pair<String, Integer> aPair = new Pair<String, Integer>();
			aPair.setFirst(GramIndexer.get(gramIndex));
			aPair.setSecond(gramIndex);
			grams.add(aPair);
		}

		return grams;
	}

	public List<Pair<String, Integer>> getAllGramsOfEn() {
		return getAllVectorMapGrams(EnVectorMap);
	}

	public List<Pair<String, Integer>> getAllGramsOfHanGramN(int nGram) {
		return getAllVectorMapGrams(HanNGramVectorMaps.get(nGram - 1));
	}
	public List<Pair<String, Long>> getTopNGrams(String[] NGrams,int n,String accepted) throws IOException{
		
		List<Map<Integer, Map<Integer, Long>>> vectorMaps =getVectorMapsOfNGram(NGrams);
		return getTopNGramsInVectorMaps(vectorMaps,n,accepted);
	}
	
	public ArrayList<Pair<String, Long>> getTopNGramsInVectorMaps(List<Map<Integer, Map<Integer, Long>>> vectorMapList,
			int n,String accepted) throws IOException{
		
		PriorityQueue<Pair<String, Long>> TopNGrams = new PriorityQueue<Pair<String, Long>>(
				n, new Comparator<Pair<String, Long>>() {
					public int compare(Pair<String, Long> o1,
							Pair<String, Long> o2) {
						double comResult = o1.second() - o2.second();
						if (comResult > 0)
							return 1;
						else if (comResult < 0)
							return -1;
						return 0;
					}
				});
		
		Set<Entry<Integer, Map<Integer, Long>>> keySet = new HashSet<Entry<Integer, Map<Integer, Long>>>();
		for (Map<Integer, Map<Integer, Long>> vectorMap : vectorMapList) {
			keySet.addAll(vectorMap.entrySet());
		}
		
		int i = 0;
		int totalVecNum = keySet.size();
		
		GramTopNFilter aGramTopNFilter=new GramTopNFilter();
		aGramTopNFilter.setAccept(accepted);
		
		aGramTopNFilter.loadKnowWordFromDir("./GramData/KnowWords");
		aGramTopNFilter.loadStopGramDir("./GramData/StopGrams");
		
		
		
		for (Entry<Integer, Map<Integer, Long>> vectorEntry : keySet) {
			
			if(i%10000==0)
			{
				long pecent=i*100/totalVecNum;
				logger.info("Processed ("+i+"/"+totalVecNum+") grams "+pecent+"%");
			}
			i++;
			String gram=GramIndexer.get(vectorEntry.getKey());
			long insCounter= vectorEntry.getValue().get(InstanceCounterIndex);
		
			if(!aGramTopNFilter.isAccept(gram))
				continue;
			
			//logger.info("gram ="+gram);
			//logger.info("insCounter ="+insCounter);
			
			if (TopNGrams.size() < n)
			{
				Pair<String, Long> aNewPair = new Pair<String, Long>();
				aNewPair.first = gram;
				aNewPair.second =insCounter;
				TopNGrams.add(aNewPair);
			}
			else {
				if (TopNGrams.peek().second < insCounter) {
					
					TopNGrams.poll();
					
					Pair<String, Long> aNewPair = new Pair<String, Long>();
					aNewPair.first = gram;
					aNewPair.second =insCounter;
					TopNGrams.add(aNewPair);
				}
			}
		}
		
		ArrayList<Pair<String, Long>> returnList = new ArrayList<Pair<String, Long>>(n);

		while (!TopNGrams.isEmpty())
			returnList.add(TopNGrams.poll());

		return returnList;
		
	}
	/*
	public ArrayList<Pair<String, Long>>  getThresholdInVectorMaps(List<Map<Integer, Map<Integer, Long>>> vectorMapList,
				long threshold,boolean bigger ,String accepted){

		PriorityQueue<Pair<String, Long>> TopNGrams = new PriorityQueue<Pair<String, Long>>(
				n, new Comparator<Pair<String, Long>>() {
					public int compare(Pair<String, Long> o1,
							Pair<String, Long> o2) {
						double comResult = o1.second() - o2.second();
						if (comResult > 0)
							return 1;
						else if (comResult < 0)
							return -1;
						return 0;
					}
				});
		
		Set<Entry<Integer, Map<Integer, Long>>> keySet = new HashSet<Entry<Integer, Map<Integer, Long>>>();
		for (Map<Integer, Map<Integer, Long>> vectorMap : vectorMapList) {
			keySet.addAll(vectorMap.entrySet());
		}
		
		int i = 0;
		int totalVecNum = keySet.size();
		
		GramTopNFilter aGramTopNFilter=new GramTopNFilter();
		aGramTopNFilter.setAccept(accepted);
		
		aGramTopNFilter.loadKnowWordFromDir("./GramData/KnowWords");
		aGramTopNFilter.loadStopGramDir("./GramData/StopGrams");
		
		
		
		for (Entry<Integer, Map<Integer, Long>> vectorEntry : keySet) {
			
			if(i%10000==0)
			{
				long pecent=i*100/totalVecNum;
				logger.info("Processed ("+i+"/"+totalVecNum+") grams "+pecent+"%");
			}
			i++;
			String gram=GramIndexer.get(vectorEntry.getKey());
			long insCounter= vectorEntry.getValue().get(InstanceCounterIndex);
		
			if(!aGramTopNFilter.isAccept(gram))
				continue;
			
			//logger.info("gram ="+gram);
			//logger.info("insCounter ="+insCounter);
			
			if (TopNGrams.size() < n)
			{
				Pair<String, Long> aNewPair = new Pair<String, Long>();
				aNewPair.first = gram;
				aNewPair.second =insCounter;
				TopNGrams.add(aNewPair);
			}
			else {
				if (TopNGrams.peek().second < insCounter) {
					
					TopNGrams.poll();
					
					Pair<String, Long> aNewPair = new Pair<String, Long>();
					aNewPair.first = gram;
					aNewPair.second =insCounter;
					TopNGrams.add(aNewPair);
				}
			}
		}
		
		ArrayList<Pair<String, Long>> returnList = new ArrayList<Pair<String, Long>>(n);

		while (!TopNGrams.isEmpty())
			returnList.add(TopNGrams.poll());

		return returnList;
	 }
	 */
	public String toString() {

		StringBuilder aStringBuilder = new StringBuilder();
		aStringBuilder.append("[");
		aStringBuilder.append("Gram=" + GramIndexer.size());
		aStringBuilder.append(" Word=" + WordIndexer.size());
		aStringBuilder.append(" Instence=" + instanceCounter);
		aStringBuilder.append(",");
		int ngram = 1;
		for (Map<Integer, Map<Integer, Long>> aHanNGramVectorMap : HanNGramVectorMaps) {
			aStringBuilder.append(" Gram_" + ngram + "="
					+ aHanNGramVectorMap.size());
			ngram++;
		}
		aStringBuilder.append(" Gram_En=" + EnVectorMap.size());
		aStringBuilder.append("]");
		return aStringBuilder.toString();
	}
}
