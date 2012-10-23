package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import ylj.Util.Pair;
import ylj.Util.StringRecognizer;
import ylj.Util.SymbolRecognizer;

public class GramOccCounterMergeBased {

	Map<String, Integer> counterMap = new TreeMap<String, Integer>();

	int MaxGram;
	int[] NArray;

	int cnCounter;
	int cnSingleCounter;
	int enCounter;
	int digCounter;
	int enAndDigCounter;
	int symbolCounter;
	int repeatCounter;
	int specialSymbolCounter;
	int otherCounter;

	NGramSpliter nGramSpliter;

	String occSliceDirName = "OccSliceDir";
	static String reversedGramSliceDirName = "RevsdGramSliceDir";

	

	// List<File> nextRoundFiles = new LinkedList<File>();

	public GramOccCounterMergeBased() {

	}

	public GramOccCounterMergeBased(int maxGramValue) {

		MaxGram = maxGramValue;
		nGramSpliter = new NGramSpliter(MaxGram);
		NArray = new int[MaxGram];

		for (int i = 0; i < NArray.length; i++) {
			NArray[i] = i + 1;
		}
		System.out.println(Arrays.toString(NArray));

	}

	public void addLineStr(String line) {

		List<String> grams = nGramSpliter.splite(line, NArray);
		if (grams == null)
			return;

		for (String gram : grams) {
			addAGram(gram);
		}

	}

	public void addAGram(String gram) {

		if (StringRecognizer.isRepeating(gram))
			gram = StringRecognizer.shortRepeatFormat(gram);

		// add new gram
		if (!counterMap.containsKey(gram)) {

			if (StringRecognizer.isCN_zhStr(gram)) {
				cnCounter++;
				if (gram.length() == 1)
					cnSingleCounter++;
			} else if (StringRecognizer.isEnWord(gram)) {
				enCounter++;
			}
			// jump
			else if (StringRecognizer.isDigital(gram)) {
				// digCounter++;
				// System.out.println(gram);
				return;
			} else if (StringRecognizer.isEnOrDigWord(gram)) {
				// System.out.println(gram);
				enAndDigCounter++;
				// System.out.println(gram);
			}
			// jump
			else if (StringRecognizer.isSymbol(gram)) {

				// symbolCounter++;
				return;

			} else if (StringRecognizer.isRepeatShortFormat(gram)) {

				repeatCounter++;
				// System.out.println(gram);
			} else if (SymbolRecognizer.isSpecialSymbol(gram)) {
				// 一些特殊符号，表情，颜色识别
				specialSymbolCounter++;

			} else {
				// otherCounter++;
				return;

			}
		}

		addGramToMap(counterMap, gram, 1);

	}

	public void addAGram(String gram, int add) {

		addGramToMap(counterMap, gram, add);

	}

	public static long getGramCounterFromMap(Map<String, Integer> map, String gram) {
		Integer counter = map.get(gram);
		if (counter == null)
			return 0;
		return counter;
	}

	public static long getTotalWordCounterFromMap(Map<String, Integer> occMap) {

		long total = 0;
		for (Entry<String, Integer> entry : occMap.entrySet()) {
			String gram = entry.getKey();
			if (StringRecognizer.isEnWord(gram))
				total += entry.getValue();
			if (StringRecognizer.isCN_zhStr(gram)) {
				if (gram.length() == 1)
					total += entry.getValue();
			}
		}
		return total;
	}

	private static void addGramToMap(Map<String, Integer> map, String gram, int add) {

		int oldCounter = 0;
		Integer longCounter = map.get(gram);
		if (longCounter != null)
			oldCounter = longCounter;
		else {
			// add new Gram
		}

		map.put(gram, oldCounter + add);

	}

	public long getGramCounter(String gram) {

		return getGramCounterFromMap(counterMap, gram);

	}

	// 单词 字 都是一个word
	public long getTotalWordCounter() {

		return getTotalWordCounterFromMap(counterMap);
	}

	public void flushToFile(String filePath) throws IOException {

		fulshMapToFile(counterMap, filePath);

	}

	public static void fulshMapToFile(Map<String, Integer> occMap, String filePath) throws IOException {

		System.out.println("Flush to file:" + filePath);

		File file = new File(filePath);
		if (!file.exists())
			file.createNewFile();

		FileOutputStream fos = new FileOutputStream(file, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		for (Entry<String, Integer> entry : occMap.entrySet()) {
			bw.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		bw.flush();
		bw.close();

	}
	public static void fulshMapToFile(String filePath,Map<String, String> gramMap) throws IOException {

		System.out.println("Flush to file:" + filePath);

		File file = new File(filePath);
		if (!file.exists())
			file.createNewFile();

		FileOutputStream fos = new FileOutputStream(file, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		for (Entry<String, String> entry : gramMap.entrySet()) {
			bw.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		bw.flush();
		bw.close();

	}
	public static GramOccCounter loadFromFile(String occFilePath) throws IOException {

		File occFile = new File(occFilePath);
		if (!occFile.exists()) {
			System.out.println("occFile:" + occFile + " not exist.");
			return null;
		} else
			System.out.println("occFile:" + occFile + "  exist.");
		GramOccCounter aGramOccCounter = new GramOccCounter();

		aGramOccCounter.counterMap = loadOccMapFromFile(occFilePath);

		return aGramOccCounter;

	}

	public static Map<String, Integer> loadOccMapFromFile(String filePath) throws IOException {

		Map<String, Integer> occMap = new TreeMap<String, Integer>();

		File file = new File(filePath);
		if (!file.exists())
			return null;

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isw = new InputStreamReader(fis, "gbk");
		BufferedReader br = new BufferedReader(isw);

		String line = null;
		long l = 0;
		while ((line = br.readLine()) != null) {

			if (l % 1000000 == 0)
				System.out.println("LoadOccMap: load " + l + " lines");
			String[] terms = line.split("\t");
			String gram = terms[0];
			int counter = Integer.parseInt(terms[1]);
			addGramToMap(occMap, gram, counter);
			l++;
		}
		br.close();
		return occMap;
	}

	public static Map<String, Integer> reversedOccMapFromFile(String filePath) throws IOException {
		NGramSpliter tempGramSpliter = new NGramSpliter(5);
		Map<String, Integer> occMap = new TreeMap<String, Integer>();

		File file = new File(filePath);
		if (!file.exists())
			return null;

		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isw = new InputStreamReader(fis, "gbk");
		BufferedReader br = new BufferedReader(isw);

		String line = null;
		long l = 0;
		while ((line = br.readLine()) != null) {

			if (l % 1000000 == 0)
				System.out.println("LoadReversedOccMap: load " + l + " lines");

			String[] terms = line.split("\t");
			String gram = terms[0];
			String reversedGram = tempGramSpliter.reverseCN_zh(gram);

			int counter = Integer.parseInt(terms[1]);

			addGramToMap(occMap, reversedGram, counter);
			l++;
		}
		br.close();
		return occMap;
	}

	
	public static void reverseGramFile(int maxCounterMapSize,String GramFilePath, String reversedGramFilePath) throws IOException {
		
		
		
		NGramSpliter tempGramSpliter = new NGramSpliter(5);
		
		List<File> sliceGramFiles=new LinkedList<File>();
		
		TreeMap<String,String> reversedGramMap=new TreeMap<String,String>();
		FileInputStream fis = new FileInputStream(GramFilePath);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		File reversedGramFile=new File(reversedGramFilePath);
		
		String reversedGramSliceDirPath=reversedGramFile.getParent()+"/"+reversedGramSliceDirName;
		File reversedGramSliceDir=new File(reversedGramSliceDirPath);
		if(!reversedGramSliceDir.exists())
			reversedGramSliceDir.mkdir();
		
		int i = 0;
		String aline = null;
		int totalRecordNum=0;
		while ((aline = br.readLine()) != null) {

			if (i % 100000 == 0)
				System.out.println("Gram Reversed " + i + " Records.");

			int tabIndex=aline.indexOf("\t");
			
			String gram =aline.substring(0,tabIndex);
			String reversedGram=tempGramSpliter.reverseCN_zh(gram);
			
			String remain=aline.substring(tabIndex+1);
			
		
			reversedGramMap.put(reversedGram, remain);
			
			if(reversedGramMap.size()>maxCounterMapSize)
			{
				totalRecordNum+=reversedGramMap.size();
				String sliceFilePath = reversedGramSliceDirPath + "/GramSlice_" + sliceGramFiles.size();
				System.out.println("Flush GramMap(" + reversedGramMap.size() + ") to file:" + sliceFilePath);
				fulshMapToFile(sliceFilePath,reversedGramMap);
				reversedGramMap.clear();
				
				File file = new File(sliceFilePath);
				sliceGramFiles.add(file);
				System.gc();
			}
			
			i++;
		}
		
		{
			totalRecordNum+=reversedGramMap.size();
			String sliceFilePath = reversedGramSliceDirPath + "/GramSlice_" + sliceGramFiles.size();
			System.out.println("Flush GramMap(" + reversedGramMap.size() + ") to file:" + sliceFilePath);
			fulshMapToFile(sliceFilePath,reversedGramMap);
			reversedGramMap.clear();
			
			File file = new File(sliceFilePath);
			sliceGramFiles.add(file);
		}
		
		System.out.println("Do GramMerge... Files:" + sliceGramFiles + " Total:" + totalRecordNum);

		mergeGramFile(sliceGramFiles, reversedGramFile, totalRecordNum);
		
		deleteDir(reversedGramSliceDir);
		
	}

	private static void deleteDir(File dirFile){
		
		for(File subFile:dirFile.listFiles()){
			
			if(subFile.isDirectory())
				deleteDir(subFile);
			else
				subFile.delete();
		}
		dirFile.delete();
	}
	public static GramOccCounter loadReversedGramFromFile(String occFilePath) throws IOException {

		File occFile = new File(occFilePath);
		if (!occFile.exists()) {
			System.out.println("occFile:" + occFile + " not exist.");
			return null;
		}
		GramOccCounter aGramOccCounter = new GramOccCounter();

		aGramOccCounter.counterMap = reversedOccMapFromFile(occFilePath);

		return aGramOccCounter;

	}

	public void process(int maxCounterMapSize, String inputFile, String occFilePath) throws IOException {

		File occFile=new File(occFilePath);
		
		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		String occSliceDirPath=occFile.getParent()+"/"+occSliceDirName;
		List<File> occSliceFiles = new LinkedList<File>();
		
		String aline = null;

		int i = 0;

		int totalGramRecord = 0;
		File occSliceDir = new File(occSliceDirPath);
		if (!occSliceDir.exists()) {
			occSliceDir.mkdir();
		}

		while ((aline = br.readLine()) != null) {

			if (i % 100000 == 0)
				System.out.println("OccCounter: processed " + i + " line. GramNum:" + counterMap.size() + " CN:" + cnCounter + " CNS:" + cnSingleCounter
						+ " EN:" + enCounter + " Dig:" + digCounter + " EN&Dig:" + enAndDigCounter + " Symb:" + symbolCounter + " SpSymb:"
						+ specialSymbolCounter + " REPT:" + repeatCounter + " Other:" + otherCounter);

			this.addLineStr(aline);

			if (counterMap.size() > maxCounterMapSize) {
				
				totalGramRecord += counterMap.size();

				String sliceFile = occSliceDirPath + "/OccSlice_" + occSliceFiles.size();
				System.out.println("Flush CounterMap(" + counterMap.size() + ") to file:" + sliceFile);
				fulshMapToFile(counterMap, sliceFile);
				
				counterMap.clear();

				File file = new File(sliceFile);
				occSliceFiles.add(file);
				
				
				 cnCounter=0;
				 cnSingleCounter=0;
				 enCounter=0;
				 digCounter=0;
				 enAndDigCounter=0;
				 symbolCounter=0;
				 repeatCounter=0;
				 specialSymbolCounter=0;
				 otherCounter=0;
				
				
				System.gc();
			}

			i++;
		}
		// last file
		{
			totalGramRecord += counterMap.size();
			String sliceFile = occSliceDirPath + "/OccSlice_" + occSliceFiles.size();
			System.out.println("Flush CounterMap(" + counterMap.size() + ") to file:" + sliceFile);
			fulshMapToFile(counterMap, sliceFile);
			counterMap.clear();

			File file = new File(sliceFile);
			occSliceFiles.add(file);
		}

		System.out.println("Do CounterMerge... Files:" + occSliceFiles + " Total:" + totalGramRecord);

		mergeOccFile(occSliceFiles, occFile, totalGramRecord);
		
		deleteDir(occSliceDir);

	}

	private void copyFileToFile(File input, File output) throws IOException {

		FileInputStream fis = new FileInputStream(input);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		FileOutputStream fos = new FileOutputStream(output, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		String line = null;
		while ((line = br.readLine()) != null) {

			bw.append(line + "\n");
		}

		br.close();
		bw.close();
	}

	private static void mergeOccFile(List<File> files, File mergedFile, int totalRecord) throws IOException {

		int processedRecordCounter = 0;

		BufferedReader[] readers = new BufferedReader[files.size()];
		String[] TopGrams = new String[files.size()];
		int[] TopGramCounter = new int[files.size()];

		for (int i = 0; i < files.size(); i++) {

			FileInputStream fis = new FileInputStream(files.get(i));
			InputStreamReader isr = new InputStreamReader(fis);
			readers[i] = new BufferedReader(isr);

			String firstRecord = readers[i].readLine();
			if(firstRecord==null){
				
				TopGrams[i] = "";
				TopGramCounter[i] = -1;
				continue;
			}
			
			processedRecordCounter++;
			String[] terms = firstRecord.split("\t");

			TopGrams[i] = terms[0];
			TopGramCounter[i] = Integer.parseInt(terms[1]);
		}

		FileOutputStream fos = new FileOutputStream(mergedFile, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		while (true) {

			if (processedRecordCounter % 1000000 == 0) {
				int percent=0;
				if(totalRecord!=0)
					percent = (int) (((long)processedRecordCounter*100) / totalRecord);
				System.out.println("Occ Merge processed (" + processedRecordCounter + "/" + totalRecord + ") " + percent + "%");
			}

			int minGramIndex = minGramIndex(TopGrams);
			if (minGramIndex == -1)
				break;

			String minGram = TopGrams[minGramIndex];
			int counter = 0;
			for (int i = 0; i < files.size(); i++) {
				if (TopGrams[i].equals(minGram)) {
					counter += TopGramCounter[i];

					String firstRecord = readers[i].readLine();
					processedRecordCounter++;
					if (firstRecord != null) {
						String[] terms = firstRecord.split("\t");
						TopGrams[i] = terms[0];
						TopGramCounter[i] = Integer.parseInt(terms[1]);
					} else {
						TopGrams[i] = "";
						TopGramCounter[i] = -1;
					}

				}
			}

			bw.append(minGram + "\t" + counter + "\n");

		}

		if (processedRecordCounter % 1000000 == 0) {
			int percent=0;
			if(totalRecord!=0)
				percent = (int) (((long)processedRecordCounter*100) / totalRecord);
			System.out.println("Occ Merge processed (" + processedRecordCounter + "/" + totalRecord + ") " + percent + "%");
		}

		for (int i = 0; i < files.size(); i++) {
			readers[i].close();
		}
		bw.close();

	}
	private static void mergeGramFile(List<File> files, File mergedFile, int totalRecord) throws IOException {

		int processedRecordCounter = 0;

		BufferedReader[] readers = new BufferedReader[files.size()];
		String[] TopGrams = new String[files.size()];
		String[] TopGramRemain = new String[files.size()];

		for (int i = 0; i < files.size(); i++) {

			FileInputStream fis = new FileInputStream(files.get(i));
			InputStreamReader isr = new InputStreamReader(fis);
			readers[i] = new BufferedReader(isr);

			String firstRecord = readers[i].readLine();
			
			if(firstRecord==null)
			{
				TopGrams[i] = "";
				TopGramRemain[i] = null;
				continue;
			}
			
			processedRecordCounter++;
			
			int tabIndex=firstRecord.indexOf("\t");
			
			TopGrams[i] = firstRecord.substring(0,tabIndex);
			TopGramRemain[i] = firstRecord.substring(tabIndex+1);
			
		}

		FileOutputStream fos = new FileOutputStream(mergedFile, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		while (true) {

			if (processedRecordCounter % 1000000 == 0) {
				int percent =0;
				if(totalRecord!=0)
					percent= (int) (((long)processedRecordCounter*100) / totalRecord);
				System.out.println("Gram Merge processed (" + processedRecordCounter + "/" + totalRecord + ") " + percent + "%");
			}

			int minGramIndex = minGramIndex(TopGrams);
			if (minGramIndex == -1)
				break;

			String minGram = TopGrams[minGramIndex];
			String gramValue =null;
			for (int i = 0; i < files.size(); i++) {
				
				if (TopGrams[i].equals(minGram)) {
					
					gramValue= TopGramRemain[i];

					String record = readers[i].readLine();
					processedRecordCounter++;
					if (record != null) {
						
						int tabIndex=record.indexOf("\t");
						
						TopGrams[i] = record.substring(0,tabIndex);
						TopGramRemain[i] = record.substring(tabIndex+1);
						
					} else {
						TopGrams[i] = "";
						TopGramRemain[i] = null;
					}

				}
			}

			bw.append(minGram + "\t" + gramValue + "\n");

		}

		if (processedRecordCounter % 1000000 == 0) {
			int percent =0;
			if(totalRecord!=0)
				percent= (int) (((long)processedRecordCounter*100) / totalRecord);
			System.out.println("Gram Merge processed (" + processedRecordCounter + "/" + totalRecord + ") " + percent + "%");
		}

		for (int i = 0; i < files.size(); i++) {
			readers[i].close();
		}
		bw.close();

	}
	private static int minGramIndex(String[] grams) {
		int minIndex = 0;

		for (int i = 0; i < grams.length; i++) {
			if (grams[i].equals(""))
				continue;
			if (grams[minIndex].equals("")) {
				minIndex = i;
				continue;
			}

			if (grams[i].compareTo(grams[minIndex]) < 0)
				minIndex = i;
		}

		if (grams[minIndex].equals(""))
			return -1;

		return minIndex;
	}
	public static void sharpOccFile(int threshold,String occFilePath, String sharpedOccFilePath) throws IOException {

		FileInputStream fis = new FileInputStream(occFilePath);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		FileOutputStream fos = new FileOutputStream(sharpedOccFilePath, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		int oldCounter=0;
		int newCounter=0;
		
		String line = null;
		while ((line = br.readLine()) != null) {
			oldCounter++;
			String[] terms = line.split("\t");
			//String gram= terms[0];
			int  counter = Integer.parseInt(terms[1]);
			
			if(counter<threshold)
				continue;
			newCounter++;
			bw.append(line + "\n");
		}

		long percent=0;
		if(oldCounter!=0)
			percent=((long)newCounter)*100/oldCounter;
		System.out.println("OccRecord:"+oldCounter+"  Sharped:"+newCounter+" "+percent+"%  threshold:"+threshold);
		br.close();
		bw.close();
		
	}

	public static void main(String[] args) throws IOException {

		
		if (args.length != 4) {

			System.out.println("Compute GramOccCounter  ");
			System.out.println("usage:GramOccCounter	MaxGram MaxOccMapSize InstancePath  outputFile");
			System.out.println("                       	MaxGram	(like:4  means use 1,2,3,4Gram )");
			System.out.println("                       	MaxOccMapSize	(  )");
			System.out.println("                       	InstancePath	(Increment Instance File Path)");
			System.out.println("                       	outputFile	(file to store counter)");
			System.out.println("Example: ");
			System.out.println("     GramOccCounter 4 1000000 IMESSAGE GramOcc");
			System.out.println("	");

			return;
		}

		int MaxGram = Integer.parseInt(args[0]);
		int MaxOccMapSize = Integer.parseInt(args[1]);
		String path = args[2];
		String occFilePath = args[3];
		
		GramOccCounterMergeBased aGramOccCounter = new GramOccCounterMergeBased(MaxGram);
		aGramOccCounter.process(MaxOccMapSize, path, occFilePath);

		//String reversedOccFilePath=occFilePath+"_reversed";
		//String reversedGramFilePath=occFilePath+"_gram_reversed";
		//GramOccCounterMergeBased.reverseOccFile(MaxOccMapSize, occFilePath, reversedOccFilePath);
		
		//GramOccCounterMergeBased.reverseGramFile(MaxOccMapSize, occFilePath, reversedGramFilePath);
	}

}
