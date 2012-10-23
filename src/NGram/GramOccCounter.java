package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ylj.Util.StringRecognizer;
import ylj.Util.SymbolRecognizer;

public class GramOccCounter {


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

	public GramOccCounter() {

	}

	public GramOccCounter(int maxGramValue) {

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

	public static void reverseOccFile(String occFilePath, String reversedOccFile) throws IOException {

		GramOccCounter Reversed_GramOccCounter = loadReversedGramFromFile(occFilePath);
		Reversed_GramOccCounter.flushToFile(reversedOccFile);

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

	public void process(String inputFile, String outputFile) throws IOException {

		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		String aline = null;

		int i = 0;
		while ((aline = br.readLine()) != null) {

			if (i % 100000 == 0)
				System.out.println("OccCounter: processed " + i + " line. GramNum:" + counterMap.size() + " CN:" + cnCounter + " CNS:" + cnSingleCounter
						+ " EN:" + enCounter + " Dig:" + digCounter + " EN&Dig:" + enAndDigCounter + " Symb:" + symbolCounter + " SpSymb:"
						+ specialSymbolCounter + " REPT:" + repeatCounter + " Other:" + otherCounter);

			this.addLineStr(aline);
			i++;

			
		}
		this.flushToFile(outputFile);
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 3) {

			System.out.println("Compute GramOccCounter  ");
			System.out.println("usage:GramOccCounter	MaxGram InstancePath  outputFile");
			System.out.println("                       	MaxGram	(like:4  means use 1,2,3,4Gram )");
			System.out.println("                       	InstancePath	(Increment Instance File Path)");
			System.out.println("                       	outputFile	(file to store counter)");
			System.out.println("Example: ");
			System.out.println("     GramOccCounter 4 IMESSAGE GramOcc");
			System.out.println("	");

			return;
		}

		int MaxGram = Integer.parseInt(args[0]);
		String path = args[1];
		String outputFile = args[2];

		GramOccCounter aGramOccCounter = new GramOccCounter(MaxGram);
		
		aGramOccCounter.process(path, outputFile);

		GramOccCounter.reverseOccFile(outputFile, outputFile+"_Reversed");
	}

}
