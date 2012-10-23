package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
import ylj.Util.SymbolRecognizer;

public class GramVectorModel implements Serializable {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(GramVectorModel.class
			.getName());
	private static final long serialVersionUID = 1644517928490202752L;

	Index<String> GramIndexer = new HashIndex<String>(); // 0 1 2 3 .....
	Index<String> WordIndexer = new HashIndex<String>(); // 0 1 2 3 .....

	// Map<Integer,Map<Integer,Integer>> GramVectorMap=new
	// HashMap<Integer,Map<Integer,Integer>>();

	int ENGramMapIdex = 0;
	int NGramMAX = 10;

	ArrayList<Map<Integer, Map<Integer, Integer>>> NGramVectorMapList;
	// Map<Integer, Map<Integer, Integer>> EnVectorMap;
	long instanceCounter = 0;

	final static int InstanceCounterIndex = -1;
	final static String InstanceCounterStr = "INS";

	int[] oneSpliteFlag = { 1 };

	NGramSpliter nGramSpliter=new NGramSpliter(10);
	
	
	
	class VectorMapState {

		String name;
		long gramNum;

		long maxInstance;
		long minInstance;
		float averageInstance;

		public String toString() {
			return name + ":	GramNum=" + gramNum + "	max=" + maxInstance
					+ "	min=" + minInstance + "	avg=" + averageInstance;
		}

	}

	public GramVectorModel() {

		NGramVectorMapList = new ArrayList<Map<Integer, Map<Integer, Integer>>>(
				NGramMAX + 1);

		for (int i = 0; i <= NGramMAX; i++)
			NGramVectorMapList
					.add(new HashMap<Integer, Map<Integer, Integer>>());

	}

	public class WordVectorDig {

		int gramIndex;
		long instanceCounter;
		ArrayList<KVPair<Integer, Integer>> vector;

		public ArrayList<KVPair<Integer, Integer>> getVectorList() {
			return vector;
		}

		public String getGramValue() {
			return GramIndexer.get(gramIndex);
		}

		public int getGramIndex() {
			return gramIndex;
		}

		public String toString() {
			return getGramIndex() + " " + instanceCounter + " " + vector;
		}

	}

	public class WordVectorStr {

		String gramStr;
		long instanceCounter;
		ArrayList<KVPair<String, Integer>> vector;

		public ArrayList<KVPair<String, Integer>> getVectorList() {
			return vector;
		}

		public String getGramValue() {
			return gramStr;
		}

		public int getGramIndex() {
			return GramIndexer.indexOf(gramStr);
		}

		public String toString() {
			return "Index=" + getGramIndex() + " GramStr=" + getGramValue()
					+ " InsCout=" + instanceCounter + " \n" + vector;
		}

		public List<String> getWords() {

			List<String> wordsList = new LinkedList<String>();
			for (KVPair<String, Integer> pair : vector) {
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

	public static GramVectorModel loadModel(String modelPath) throws Exception {

		if (modelPath.endsWith(".obj"))
			return GramVectorModel.loadModelObj(modelPath);
		else
			return GramVectorModel.loadFromDir(modelPath);
	}

	public static GramVectorModel loadModelObj(String path) throws Exception {

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

	public long getInstanceCounterOfGram(String gram) {

		int index = GramIndexer.indexOf(gram, false);
		if (index == -1)
			return 0;

		Map<Integer, Integer> mapVector = getGramMapVector(index);
		Integer InstanceCounter = mapVector.get(InstanceCounterIndex);

		return InstanceCounter;

	}

	public List<String> getOtherWordsOfGramStr(String gram, String str) {

		// 不可省略
		String filtedStr = str.replace(gram, " ");
		
		List<String> words = nGramSpliter.splite(filtedStr, oneSpliteFlag);
		
		//words.remove(gram);

		return words;
	}

	public void mergeVectorMap(Map<Integer, Map<Integer, Integer>> vectorMap,
			WordVectorStr otherWordVectorStr) {

		int index = GramIndexer.indexOf(otherWordVectorStr.gramStr, true);
		Map<Integer, Integer> vector = vectorMap.get(index);
		if (vector == null) {
			vector = new TreeMap<Integer, Integer>();
			vectorMap.put(index, vector);
		}

		for (KVPair<String, Integer> wordPair : otherWordVectorStr.vector) {
			int counter = 0;
			int wordIndex = WordIndexer.indexOf(wordPair.key(), true);
			Integer wordCounter = vector.get(wordIndex);
			if (wordCounter != null)
				counter = wordCounter;
			vector.put(wordIndex, counter + wordPair.value());
		}

	}

	public void mergeHanGramVector(WordVectorStr otherWordVectorStr, int NGram) {

		Map<Integer, Map<Integer, Integer>> gramVectorMap = NGramVectorMapList
				.get(NGram);
		mergeVectorMap(gramVectorMap, otherWordVectorStr);

	}

	public void mergeEnGramVector(WordVectorStr otherWordVectorStr) {

		mergeVectorMap(NGramVectorMapList.get(ENGramMapIdex),
				otherWordVectorStr);

	}

	public GramVectorModel mergeModel(GramVectorModel otherModel) {

		logger.info("Merging Model ...");

		long count = 0;
		long total = otherModel.getGramNum();

		logger.info("Merging Han Grams...");
		for (int ngram = 1; ngram <= NGramMAX; ngram++) {

			logger.info("Merging Han " + ngram + " Grams...");
			Map<Integer, Map<Integer, Integer>> vectorMap = otherModel.NGramVectorMapList
					.get(ngram);
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
				.getAllWordVectorStrOf(otherModel.NGramVectorMapList
						.get(ENGramMapIdex));
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
			Map<Integer, Map<Integer, Integer>> VectorMap, String gram,
			String str) {

		int index = GramIndexer.indexOf(gram, true);

		Map<Integer, Integer> vector = VectorMap.get(index);

		if (vector == null) {
			vector = new TreeMap<Integer, Integer>();
			VectorMap.put(index, vector);
			// init instance counter to 0
			vector.put(InstanceCounterIndex, 0);
		}

		List<String> words = getOtherWordsOfGramStr(gram, str);

		for (String word : words) {

			
			if(StringRecognizer.isRepeating(word))
				word=StringRecognizer.shortRepeatFormat(word);
			
			// 作为维度的字
			if (StringRecognizer.isCN_zhWord(word)  //中文汉字
					|| StringRecognizer.isEnWord(word)  //英文字母
					|| StringRecognizer.isRepeatShortFormat(word)  //重复符号
					|| SymbolRecognizer.isExpressionSymbol(word)) {		//表情符号

				
				int counter = 0;
				//big case
				word=StringRecognizer.upCase(word);
				
				int wordIndex = WordIndexer.indexOf(word, true);
				Integer wordCounter = vector.get(wordIndex);
				if (wordCounter != null)
					counter = wordCounter;
				vector.put(wordIndex, counter + 1);
			}
		}

		// instance counter ++
		Integer instanceCounter = vector.get(InstanceCounterIndex);
		vector.put(InstanceCounterIndex, ++instanceCounter);

	}

	public void addHanGramInstence(String gram, String str) {

		// addGramCount(index);
		int NGram = gram.length();
		Map<Integer, Map<Integer, Integer>> NGramVectorMap = NGramVectorMapList
				.get(NGram);

		addGramInstenceToVectorMap(NGramVectorMap, gram, str);

	}

	public void addEngGramInstence(String gram, String str) {

		addGramInstenceToVectorMap(NGramVectorMapList.get(ENGramMapIdex), gram,
				str);

	}

	private WordVectorDig mapVectorToWordVectorDig(int index,
			Map<Integer, Integer> gramVector) {

		if (gramVector == null)
			return null;

		Set<Entry<Integer, Integer>> entrys = gramVector.entrySet();
		if (entrys == null)
			return null;

		ArrayList<Entry<Integer, Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(
				entrys);

		Collections.sort(entryList,
				new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Map.Entry<Integer, Integer> o1,
							Map.Entry<Integer, Integer> o2) {
						return (o1.getKey() - o2.getKey());
					}
				});

		ArrayList<KVPair<Integer, Integer>> kvPairList = new ArrayList<KVPair<Integer, Integer>>(
				entrys.size());
		WordVectorDig aWordVectorDig = new WordVectorDig();

		for (Entry<Integer, Integer> entry : entryList) {
			KVPair<Integer, Integer> newPair = new KVPair<Integer, Integer>();

			// the special key
			if (entry.getKey() == InstanceCounterIndex) {
				aWordVectorDig.instanceCounter = entry.getValue();
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

		ArrayList<KVPair<String, Integer>> kvPairList = new ArrayList<KVPair<String, Integer>>(
				aWordVectorDig.vector.size());

		for (KVPair<Integer, Integer> entry : aWordVectorDig.vector) {
			KVPair<String, Integer> newPair = new KVPair<String, Integer>();

			newPair.setKey(WordIndexer.get(entry.key()));

			newPair.setValue(entry.value());
			kvPairList.add(newPair);

		}

		aWordVectorStr.vector = kvPairList;
		aWordVectorStr.instanceCounter = aWordVectorDig.instanceCounter;
		return aWordVectorStr;
	}

	private WordVectorStr mapVectorToWordVectorStr(int index,
			Map<Integer, Integer> gramVector) {

		if (gramVector == null)
			return null;
		WordVectorDig aWordVectorDig = mapVectorToWordVectorDig(index,
				gramVector);

		return wordVectorDigToWordVectorStr(aWordVectorDig);
	}

	public Map<Integer, Integer> getGramMapVector(int gramIndex) {

		if (gramIndex == -1)
			return null;
		Map<Integer, Integer> gramVector = null;

		for (Map<Integer, Map<Integer, Integer>> gramMap : NGramVectorMapList) {
			gramVector = gramMap.get(gramIndex);
			if (gramVector != null)
				break;
		}

		if (gramVector == null)
			return null;

		return gramVector;
	}

	public WordVectorDig getGramVectorDig(String gram) {

		int index = GramIndexer.indexOf(gram);

		Map<Integer, Integer> gramVectorMap = getGramMapVector(index);

		return mapVectorToWordVectorDig(index, gramVectorMap);

	}

	public WordVectorStr getGramVectorStr(String gram) {

		int index = GramIndexer.indexOf(gram);

		Map<Integer, Integer> gramVectorMap = getGramMapVector(index);

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
			Map<Integer, Map<Integer, Integer>> vectorMap) {

		if (vectorMap == null)
			return null;
		List<WordVectorStr> vectors = new LinkedList<WordVectorStr>();

		Set<Entry<Integer, Map<Integer, Integer>>> vectorSet = vectorMap
				.entrySet();
		for (Entry<Integer, Map<Integer, Integer>> vectorEntry : vectorSet) {

			WordVectorStr aWordVectorStr = mapVectorToWordVectorStr(
					vectorEntry.getKey(), vectorEntry.getValue());
			vectors.add(aWordVectorStr);
		}

		return vectors;
	}

	public List<WordVectorDig> getAllWordVectorDigOf(
			Map<Integer, Map<Integer, Integer>> vectorMap) {

		if (vectorMap == null)
			return null;
		List<WordVectorDig> vectors = new LinkedList<WordVectorDig>();

		Set<Entry<Integer, Map<Integer, Integer>>> vectorSet = vectorMap
				.entrySet();
		for (Entry<Integer, Map<Integer, Integer>> vectorEntry : vectorSet) {

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

	private List<Map<Integer, Map<Integer, Integer>>> getVectorMapsOfNGram(
			String[] NGrams) {

		List<Map<Integer, Map<Integer, Integer>>> vectorMaps = new LinkedList<Map<Integer, Map<Integer, Integer>>>();
		for (String NGram : NGrams) {

			if (StringRecognizer.isEnWord(NGram)) {
				if (NGram.equalsIgnoreCase("en")) {
					logger.info("En Gram VectorMap grams="
							+ NGramVectorMapList.get(ENGramMapIdex).size());
					vectorMaps.add(NGramVectorMapList.get(ENGramMapIdex));
				} else {
					logger.error("arg ERROR , NGram=" + NGram);
					System.exit(1);
				}

			} else if (StringRecognizer.isDigital(NGram)) {

				int NGram_int = Integer.parseInt(NGram);

				if (NGram_int > NGramMAX) {
					logger.error("No " + NGram_int + " NGram in this mode .");
					continue;
				}
				Map<Integer, Map<Integer, Integer>> vectorMap = NGramVectorMapList
						.get(NGram_int);

				logger.info(NGram + " Gram VectorMap grams=" + vectorMap.size());

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

		List<Map<Integer, Map<Integer, Integer>>> vectorMaps = getVectorMapsOfNGram(NGrams);

		return getNNearestNegihborInVectorMaps(vectorMaps, gram, n);
	}

	public List<Pair<WordVectorDig, Double>> getNNearestNegihborOfMutiThreadBased(
			String[] NGrams, String gram, int n, int threadNum) {

		List<Map<Integer, Map<Integer, Integer>>> vectorMaps = getVectorMapsOfNGram(NGrams);

		return getNNearestNegihborInVectorMapsMutiThreadBased(vectorMaps, gram,
				n, threadNum);
	}

	/*
	 * public ArrayList<Pair<WordVectorDig,Double>>
	 * getNNearestNegihborInVectorMap(Map<Integer,Map<Integer,Integer>>
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
	 * Set<Entry<Integer, Map<Integer, Integer>>> keySet=vectorMap.entrySet();
	 * 
	 * int i=0; int totalVecNum=keySet.size(); for(Entry<Integer, Map<Integer,
	 * Integer>> vectorEntry:keySet){
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
	 * double simlarity=SimilarityComputer.computeInteger(thisVector.vector,
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
			List<Map<Integer, Map<Integer, Integer>>> vectorMapList,
			String gram, int n) {

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

		Set<Entry<Integer, Map<Integer, Integer>>> entrySet = new HashSet<Entry<Integer, Map<Integer, Integer>>>();
		for (Map<Integer, Map<Integer, Integer>> vectorMap : vectorMapList) {
			entrySet.addAll(vectorMap.entrySet());
		}

		int i = 0;
		int totalVecNum = entrySet.size();
		for (Entry<Integer, Map<Integer, Integer>> vectorEntry : entrySet) {

			if (i % 10000 == 0)
				logger.info(i + " Vectors compared. total=" + totalVecNum + " "
						+ (int) ((i * 100) / totalVecNum) + "%");
			i++;

			WordVectorDig otherV = mapVectorToWordVectorDig(
					vectorEntry.getKey(), vectorEntry.getValue());

			if (thisVector.getGramIndex() == otherV.getGramIndex())
				continue;

			double simlarity = SimilarityComputer.computeInt(thisVector.vector,
					otherV.vector);

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

	public List<Pair<WordVectorDig, Double>> getNNearestNegihborInVectorMapsMutiThreadBased(
			List<Map<Integer, Map<Integer, Integer>>> vectorMapList,
			String gram, int topN, int threadNum) {

		long prepare_data_start_time = System.currentTimeMillis();

		WordVectorDig thisVector = getGramVectorDig(gram);
		if (thisVector == null)
			return null;

		PriorityQueue<Pair<WordVectorDig, Double>> finalTopNVectors = new PriorityQueue<Pair<WordVectorDig, Double>>(
				topN, new Comparator<Pair<WordVectorDig, Double>>() {
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

		ArrayList<ArrayList<Entry<Integer, Map<Integer, Integer>>>> threadJobList = new ArrayList<ArrayList<Entry<Integer, Map<Integer, Integer>>>>(
				threadNum);

		int totalEntrys = 0;
		for (Map<Integer, Map<Integer, Integer>> vectorMap : vectorMapList) {
			totalEntrys += vectorMap.size();
		}
		int entrysPerThread = totalEntrys / threadNum
				+ (totalEntrys % threadNum == 0 ? 0 : 1);

		logger.info("totalEntry= " + totalEntrys);
		logger.info("threads= " + threadNum);
		logger.info("entrysPerThread= " + entrysPerThread);

		int vectorMapIndex = 0;

		Map<Integer, Map<Integer, Integer>> thisMap = vectorMapList
				.get(vectorMapIndex++);
		Iterator<Entry<Integer, Map<Integer, Integer>>> it = thisMap.entrySet()
				.iterator();
		boolean hasRemain = true;

		// prepare data

		for (int i = 0; i < threadNum; i++) {
			ArrayList<Entry<Integer, Map<Integer, Integer>>> perThreadEntryList = new ArrayList<Entry<Integer, Map<Integer, Integer>>>(
					entrysPerThread);

			for (int j = 0; j < entrysPerThread && hasRemain; j++) {

				while (!it.hasNext()) {
					if (vectorMapIndex >= vectorMapList.size()) {
						hasRemain = false;
						break;
					}

					thisMap = vectorMapList.get(vectorMapIndex++);
					it = thisMap.entrySet().iterator();
				}

				if (it.hasNext())
					perThreadEntryList.add(it.next());

			}

			threadJobList.add(perThreadEntryList);

		}
		long prepare_data_end_time = System.currentTimeMillis();

		long run_compute_start_time = prepare_data_end_time;
		computeTopNThreader[] topNRunners = new computeTopNThreader[threadNum];
		for (int i = 0; i < threadNum; i++) {
			topNRunners[i] = new computeTopNThreader(i, topN, thisVector,
					threadJobList.get(i));
			topNRunners[i].start();
		}

		// do join
		for (int i = 0; i < threadNum; i++) {
			try {
				topNRunners[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long run_compute_end_time = System.currentTimeMillis();

		// do result merge
		for (int i = 0; i < threadNum; i++) {
			finalTopNVectors = mergerPriorityQueue(finalTopNVectors,
					topNRunners[i].topNVectors, topN);
		}

		LinkedList<Pair<WordVectorDig, Double>> returnList = new LinkedList<Pair<WordVectorDig, Double>>();

		
		
		while (!finalTopNVectors.isEmpty())
			returnList.add(finalTopNVectors.poll());
		
		returnList.add(new Pair<WordVectorDig, Double>(thisVector,1.0));
		
		
		logger.info("#prepare_data_time= "
				+ (prepare_data_end_time - prepare_data_start_time));
		logger.info("#run_compute_time= "
				+ (run_compute_end_time - run_compute_start_time));

		
		return returnList;

	}

	class computeTopNThreader extends Thread {

		WordVectorDig thisVector;
		PriorityQueue<Pair<WordVectorDig, Double>> topNVectors;
		ArrayList<Entry<Integer, Map<Integer, Integer>>> entryList;
		int topN;
		int blockNum;

		long lastJobCost;

		public computeTopNThreader(int blockNum, int topN,
				WordVectorDig thisVector,
				ArrayList<Entry<Integer, Map<Integer, Integer>>> entryList) {

			this.blockNum = blockNum;
			this.topN = topN;
			this.thisVector = thisVector;
			this.entryList = entryList;
			this.topNVectors = new PriorityQueue<Pair<WordVectorDig, Double>>(
					topN, new Comparator<Pair<WordVectorDig, Double>>() {
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

		}

		@Override
		public void run() {

			int i = 0;
			int totalVecNum = entryList.size();
			long startTime = System.currentTimeMillis();
			for (Entry<Integer, Map<Integer, Integer>> vectorEntry : entryList) {

				if (i % 10000 == 0)
					logger.info("Block[" + blockNum + "]  " + i
							+ " Vectors compared. total=" + totalVecNum + " "
							+ (int) ((i * 100) / totalVecNum) + "%");
				i++;

				WordVectorDig otherV = mapVectorToWordVectorDig(
						vectorEntry.getKey(), vectorEntry.getValue());

				if (thisVector.getGramIndex() == otherV.getGramIndex())
					continue;

				double simlarity = SimilarityComputer.computeInt(
						thisVector.vector, otherV.vector);

				Pair<WordVectorDig, Double> aNewPair = new Pair<WordVectorDig, Double>();
				aNewPair.first = otherV;
				aNewPair.second = simlarity;

				if (topNVectors.size() < topN)
					topNVectors.add(aNewPair);
				else {
					if (topNVectors.peek().second < simlarity) {
						topNVectors.poll();
						topNVectors.add(aNewPair);
					}
				}
			}
			long endTime = System.currentTimeMillis();
			long timeCost = endTime - startTime;
			logger.info("Block[" + blockNum + "] totalVecNum=" + totalVecNum
					+ " timeCost=" + timeCost + "'ms");
			lastJobCost = timeCost;
		}
	}

	public PriorityQueue<Pair<WordVectorDig, Double>> mergerPriorityQueue(
			PriorityQueue<Pair<WordVectorDig, Double>> pQueue1,
			PriorityQueue<Pair<WordVectorDig, Double>> pQueue2, int topN) {

		PriorityQueue<Pair<WordVectorDig, Double>> newPQueue = new PriorityQueue<Pair<WordVectorDig, Double>>(
				topN, new Comparator<Pair<WordVectorDig, Double>>() {
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
		while (!pQueue1.isEmpty()) {
			Pair<WordVectorDig, Double> pair = pQueue1.poll();
			if (newPQueue.size() < topN)
				newPQueue.add(pair);
			else {
				if (newPQueue.peek().second < pair.second) {
					newPQueue.poll();
					newPQueue.add(pair);
				}
			}
		}
		while (!pQueue2.isEmpty()) {
			Pair<WordVectorDig, Double> pair = pQueue2.poll();
			if (newPQueue.size() < topN)
				newPQueue.add(pair);
			else {
				if (newPQueue.peek().second < pair.second) {
					newPQueue.poll();
					newPQueue.add(pair);
				}
			}
		}
		return newPQueue;

	}

	public List<Pair<String, Integer>> getAllVectorMapGrams(
			Map<Integer, Map<Integer, Integer>> vectorMap) {

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
		return getAllVectorMapGrams(NGramVectorMapList.get(ENGramMapIdex));
	}

	public List<Pair<String, Integer>> getAllGramsOfHanGramN(int nGram) {
		return getAllVectorMapGrams(NGramVectorMapList.get(nGram));
	}

	public List<Pair<String, Integer>> getTopNGrams(String[] NGrams, int n,
			String accepted) throws IOException {

		List<Map<Integer, Map<Integer, Integer>>> vectorMaps = getVectorMapsOfNGram(NGrams);
		return getTopNGramsInVectorMaps(vectorMaps, n, accepted);
	}

	public ArrayList<Pair<String, Integer>> getTopNGramsInVectorMaps(
			List<Map<Integer, Map<Integer, Integer>>> vectorMapList, int n,
			String accepted) throws IOException {

		PriorityQueue<Pair<String, Integer>> TopNGrams = new PriorityQueue<Pair<String, Integer>>(
				n, new Comparator<Pair<String, Integer>>() {
					public int compare(Pair<String, Integer> o1,
							Pair<String, Integer> o2) {
						double comResult = o1.second() - o2.second();
						if (comResult > 0)
							return 1;
						else if (comResult < 0)
							return -1;
						return 0;
					}
				});

		Set<Entry<Integer, Map<Integer, Integer>>> keySet = new HashSet<Entry<Integer, Map<Integer, Integer>>>();
		for (Map<Integer, Map<Integer, Integer>> vectorMap : vectorMapList) {
			keySet.addAll(vectorMap.entrySet());
		}

		int i = 0;
		int totalVecNum = keySet.size();

		GramTopNFilter aGramTopNFilter = new GramTopNFilter();
		aGramTopNFilter.setAccept(accepted);

		aGramTopNFilter.loadKnowWordFromDir("./GramData/KnowWords");
		aGramTopNFilter.loadStopGramDir("./GramData/StopGrams");

		for (Entry<Integer, Map<Integer, Integer>> vectorEntry : keySet) {

			if (i % 10000 == 0) {
				long pecent = i * 100 / totalVecNum;
				logger.info("Processed (" + i + "/" + totalVecNum + ") grams "
						+ pecent + "%");
			}
			i++;
			String gram = GramIndexer.get(vectorEntry.getKey());
			int insCounter = vectorEntry.getValue().get(InstanceCounterIndex);

			if (!aGramTopNFilter.isAccept(gram))
				continue;

			// logger.info("gram ="+gram);
			// logger.info("insCounter ="+insCounter);

			if (TopNGrams.size() < n) {
				Pair<String, Integer> aNewPair = new Pair<String, Integer>();
				aNewPair.first = gram;
				aNewPair.second = insCounter;
				TopNGrams.add(aNewPair);
			} else {
				if (TopNGrams.peek().second < insCounter) {

					TopNGrams.poll();

					Pair<String, Integer> aNewPair = new Pair<String, Integer>();
					aNewPair.first = gram;
					aNewPair.second = insCounter;
					TopNGrams.add(aNewPair);
				}
			}
		}

		ArrayList<Pair<String, Integer>> returnList = new ArrayList<Pair<String, Integer>>(
				n);

		while (!TopNGrams.isEmpty())
			returnList.add(TopNGrams.poll());

		return returnList;

	}

	public List<String> getAllGrams() {

		List<String> list = new LinkedList<String>();
		Iterator<String> it = GramIndexer.iterator();
		while (it.hasNext()) {
			String gram = it.next();
			list.add(gram);
		}
		return list;
	}

	public List<Pair<String, Integer>> rmGramLessThan(int minInstanceCounter) {

		List<Pair<String, Integer>> removedList = new LinkedList<Pair<String, Integer>>();

		Index<String> newGramIndexer = new HashIndex<String>(); // 0 1 2 3 .....

		// 英文符号不作检查
		// 单字不作检查
		for (int i = 2; i < NGramVectorMapList.size(); i++) {

			Map<Integer, Map<Integer, Integer>> oldVectorMap = NGramVectorMapList
					.get(i);
			Map<Integer, Map<Integer, Integer>> newVectorMap = new HashMap<Integer, Map<Integer, Integer>>();

			for (Entry<Integer, Map<Integer, Integer>> entry : oldVectorMap
					.entrySet()) {
				int gramIndex = entry.getKey();
				String gram = GramIndexer.get(gramIndex);
				Map<Integer, Integer> vector = entry.getValue();
				Integer instanceCounter = vector.get(InstanceCounterIndex);

				if (instanceCounter < minInstanceCounter) {
					// do nothing
					removedList.add(new Pair<String, Integer>(gram,
							instanceCounter));
				} else {
					int newIdex = newGramIndexer.indexOf(gram, true);
					newVectorMap.put(newIdex, vector);
				}

			}

			NGramVectorMapList.set(i, newVectorMap);
		}

		GramIndexer = newGramIndexer;
		return removedList;
	}

	private float averageInstanceCounter(
			Map<Integer, Map<Integer, Integer>> vectorMap) {

		long totalInstance = 0;
		long totalGram = vectorMap.size();

		for (Entry<Integer, Map<Integer, Integer>> entry : vectorMap.entrySet()) {
			totalInstance += entry.getValue().get(InstanceCounterIndex);
		}
		if (totalGram == 0)
			return 0;
		return totalInstance / totalGram;
	}

	private long minInstanceCounter(
			Map<Integer, Map<Integer, Integer>> vectorMap) {

		long min = Integer.MAX_VALUE;

		for (Entry<Integer, Map<Integer, Integer>> entry : vectorMap.entrySet()) {
			long counter = entry.getValue().get(InstanceCounterIndex);
			if (counter < min)
				min = counter;
		}
		return min;
	}

	private long maxInstanceCounter(
			Map<Integer, Map<Integer, Integer>> vectorMap) {

		long max = Integer.MIN_VALUE;

		for (Entry<Integer, Map<Integer, Integer>> entry : vectorMap.entrySet()) {
			long counter = entry.getValue().get(InstanceCounterIndex);
			if (counter > max)
				max = counter;
		}
		return max;
	}

	private VectorMapState vectorMapState(
			Map<Integer, Map<Integer, Integer>> vectorMap) {
		VectorMapState state = new VectorMapState();
		state.averageInstance = averageInstanceCounter(vectorMap);
		state.gramNum = vectorMap.size();
		state.maxInstance = maxInstanceCounter(vectorMap);
		state.minInstance = minInstanceCounter(vectorMap);

		return state;
	}

	public String getState() {
		StringBuilder aStringBuilder = new StringBuilder();
		int i = 0;
		for (Map<Integer, Map<Integer, Integer>> map : NGramVectorMapList) {
			VectorMapState state = vectorMapState(map);
			if (i == 0)
				state.name = "E";
			else
				state.name = i + "";
			aStringBuilder.append(state.toString() + "\n");
			i++;
		}
		aStringBuilder.append(toString());
		return aStringBuilder.toString();
	}

	public void saveToDir(String modelDir) throws IOException {
		File modelDirFile = new File(modelDir);

		if (!modelDirFile.exists()) {
			System.err.println("file:" + modelDir + "do not exists");
			System.err.println("mkdir:" + modelDir + "");
			modelDirFile.mkdir();
		} else {
			System.err.println("file:" + modelDir + "is exist");
			System.err.println("override..");
		}

		GramIndexer.saveToFilename(modelDir + "/" + "GramIndexer");
		WordIndexer.saveToFilename(modelDir + "/" + "WordIndexer");

		for (int i = 0; i < NGramVectorMapList.size(); i++) {
			saveVectorMapToFile(NGramVectorMapList.get(i), modelDir + "/" + i
					+ "_GramVectors");
		}
	}

	private void saveVectorMapToFile(Map<Integer, Map<Integer, Integer>> map,
			String filePath) throws IOException {

		FileOutputStream fos = new FileOutputStream(filePath);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		for (Entry<Integer, Map<Integer, Integer>> entry : map.entrySet()) {

			int gramIndex = entry.getKey();
			Map<Integer, Integer> gramVector = entry.getValue();
			WordVectorDig aWordVectorDig = mapVectorToWordVectorDig(gramIndex,
					gramVector);
			bw.append(aWordVectorDig.toString() + "\n");
		}
		bw.close();
	}

	public  Map<Integer, Map<Integer, Integer>> loadVectorMapFromFile(
			String filePath) throws IOException {

		Map<Integer, Map<Integer, Integer>> restoredMap = new HashMap<Integer, Map<Integer, Integer>>();

		File file=new File(filePath);
		if(!file.exists())
			return restoredMap;
		
		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader br = new BufferedReader(isr);

		
		String aRecord = null;

		while ((aRecord = br.readLine()) != null) {

			String sharpedRecord = aRecord.replace(",", "");
			sharpedRecord = sharpedRecord.replace("[", "");
			sharpedRecord = sharpedRecord.replace("]", "");
			sharpedRecord = sharpedRecord.replace("(", "");
			sharpedRecord = sharpedRecord.replace(")", "");
			
			String[] terms = sharpedRecord.split(" ");
			int gramIndex = Integer.parseInt(terms[0]);
			int instCounter = Integer.parseInt(terms[1]);

			Map<Integer, Integer> vector = new TreeMap<Integer, Integer>();
			vector.put(InstanceCounterIndex, instCounter);

			for (int i = 2; i < terms.length; i++) {

				// System.out.println(terms[i]);

				String[] element = terms[i].split("=");
				int wordIndex = Integer.parseInt(element[0]);
				int wordCounter = Integer.parseInt(element[1]);
				vector.put(wordIndex, wordCounter);
			}

			restoredMap.put(gramIndex, vector);
		}

		return restoredMap;
	}

	public static GramVectorModel loadFromDir(String modelDirPath)
			throws IOException {

		File modelDirFile = new File(modelDirPath);

		if (!modelDirFile.exists()) {
			logger.error("model:" + modelDirPath
					+ " does not exist. create empty model.");

			GramVectorModel aGramVectorModel = new GramVectorModel();
			return aGramVectorModel;
		}

		GramVectorModel restoredModel = new GramVectorModel();
		restoredModel.GramIndexer = HashIndex.loadFromFilename(modelDirPath
				+ "/" + "GramIndexer");
		restoredModel.WordIndexer = HashIndex.loadFromFilename(modelDirPath
				+ "/" + "WordIndexer");

		restoredModel.NGramVectorMapList.clear();

		for (int i = 0; i <= restoredModel.NGramMAX; i++) {
			String path=modelDirPath+ "/" + i + "_GramVectors";
		
			
			Map<Integer, Map<Integer, Integer>> restoredMap = restoredModel.loadVectorMapFromFile(path);
			
			
			restoredModel.NGramVectorMapList.add(restoredMap);
		}

		logger.info("Load model complete." + restoredModel);
		return restoredModel;
	}

	public String toString() {

		StringBuilder aStringBuilder = new StringBuilder();
		aStringBuilder.append("[");
		aStringBuilder.append("Gram=" + GramIndexer.size());
		aStringBuilder.append(" Word=" + WordIndexer.size());
		aStringBuilder.append(" Instence=" + instanceCounter);
		aStringBuilder.append(",");
		int ngram = 0;
		for (Map<Integer, Map<Integer, Integer>> aHanNGramVectorMap : NGramVectorMapList) {
			if (ngram == 0)
				aStringBuilder.append(" Gram_EN=" + aHanNGramVectorMap.size());
			else
				aStringBuilder.append(" Gram_" + ngram + "="
						+ aHanNGramVectorMap.size());
			ngram++;
		}

		aStringBuilder.append("]");
		return aStringBuilder.toString();
	}
}
