package NGram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ylj.Util.HashIndex;
import ylj.Util.Index;
import ylj.Util.KVPair;
import ylj.Util.Pair;

public class GramVectorModelBinBased {

	private static Logger logger = Logger.getLogger(GramVectorModelBinBased.class.getName());

	private static final long serialVersionUID = -8973206760052046123L;

	Index<String> GramIndexer = new HashIndex<String>(); // 0 1 2 3 .....
	Index<String> WordIndexer = new HashIndex<String>(); // 0 1 2 3 .....

	int ENGramMapIdex = 0;
	int NGramMAX = 10;

	// GramBinVectorMap gramBinVectorMap;

	static int gramIDOffset = 0;
	static int gramInsNumOffset = 1;
	static int gramElementNumOffset = 2;
	static int gramElementOffset = 3;
	static int gramElementSize = 2;

	public int[] recordBuffer;
	public int recordNum;

	public Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

	public double computeSimlarity(IntVectorDig v_x, IntVectorDig v_y) {

		long xy = 0;
		long x2 = 0;
		long y2 = 0;

		double sum = 0.0;

		int start_x = v_x.getElementIndex();
		int start_y = v_y.getElementIndex();

		int lengh_x = v_x.getGramElementNum();
		int lengh_y = v_y.getGramElementNum();

		if (lengh_x <= 0 || lengh_y <= 0) {

			return 0.0;
			// logger.error("Error for  lengh_x:"+lengh_x+"	lengh_y:"+lengh_y);
			// logger.error("Error for  v_x:"+v_x);
			// logger.error("Error for  v_y:"+v_y);
			// System.exit(1);
		}

		int index_x = 0;
		int index_y = 0;

		Pair<Integer, Integer> top_pair_x = new Pair<Integer, Integer>();
		Pair<Integer, Integer> top_pair_y = new Pair<Integer, Integer>();

		top_pair_x.first = recordBuffer[start_x + index_x * 2];
		top_pair_x.second = recordBuffer[start_x + index_x * 2 + 1];

		top_pair_y.first = recordBuffer[start_y + index_y * 2];
		top_pair_y.second = recordBuffer[start_y + index_y * 2 + 1];

		while (true) {
			try {
				// end

				if (top_pair_x.first.equals(top_pair_y.first) && top_pair_x.first.equals(Integer.MAX_VALUE)) {

					break;

				}

				if (top_pair_x.first < top_pair_y.first) {

					x2 += ((long) top_pair_x.second) * ((long) top_pair_x.second);

					index_x++;
					if (index_x == lengh_x) {

						top_pair_x.first = Integer.MAX_VALUE;
						top_pair_x.second = -1;
					} else {
						top_pair_x.first = recordBuffer[start_x + index_x * 2];
						top_pair_x.second = recordBuffer[start_x + index_x * 2 + 1];
					}

				} else if (top_pair_x.first > top_pair_y.first) {

					y2 += ((long) top_pair_y.second) * ((long) top_pair_y.second);

					index_y++;
					if (index_y == lengh_y) {

						top_pair_y.first = Integer.MAX_VALUE;
						top_pair_y.second = -1;
					} else {
						top_pair_y.first = recordBuffer[start_y + index_y * 2];
						top_pair_y.second = recordBuffer[start_y + index_y * 2 + 1];
					}

				} else {

					xy += ((long) top_pair_x.second) * ((long) top_pair_y.second);
					x2 += ((long) top_pair_x.second) * ((long) top_pair_x.second);
					y2 += ((long) top_pair_y.second) * ((long) top_pair_y.second);

					index_x++;
					index_y++;
					
					if (index_x == lengh_x) {

						top_pair_x.first = Integer.MAX_VALUE;
						top_pair_x.second = -1;
					} else {
						top_pair_x.first = recordBuffer[start_x + index_x * 2];
						top_pair_x.second = recordBuffer[start_x + index_x * 2 + 1];
					}

					if (index_y == lengh_y) {

						top_pair_y.first = Integer.MAX_VALUE;
						top_pair_y.second = -1;
					} else {
						top_pair_y.first = recordBuffer[start_y + index_y * 2];
						top_pair_y.second = recordBuffer[start_y + index_y * 2 + 1];
					}
					

				}

				

			} catch (Exception e) {

				e.printStackTrace();

				System.out.println("bufferLength:" + recordBuffer.length);
				System.out.println("start_x:" + start_x);
				System.out.println("lengh_x:" + lengh_x);
				System.out.println("index_x:" + index_x);

				System.out.println("start_y:" + start_y);
				System.out.println("lengh_y:" + lengh_y);
				System.out.println("index_y:" + index_y);

				System.exit(1);
			}

		}

		if (xy == 0)
			return 0;
		sum = xy / (Math.sqrt(x2) * Math.sqrt(y2));
		return sum;
	}

	public IntVectorDig firstVector() {
		return new IntVectorDig(0);
	}

	public class IntVectorDig {

		int recordIndex;

		public IntVectorDig(int idex) {
			recordIndex = idex;
		}

		public int getRecordIndex() {
			return recordIndex;
		}

		public int getElementIndex() {
			return recordIndex + gramElementOffset;
		}

		public int getGramID() {
			return recordBuffer[recordIndex + gramIDOffset];
		}

		public String getGram() {
			return GramIndexer.get(getGramID());
		}

		public int getGramInsNum() {
			return recordBuffer[recordIndex + gramInsNumOffset];
		}

		public int getGramElementNum() {
			return recordBuffer[recordIndex + gramElementNumOffset];
		}

		public int getGramWordID(int i) {
			return recordBuffer[recordIndex + gramElementOffset + i];
		}

		public int getGramWordCounter(int i) {
			return recordBuffer[recordIndex + gramElementOffset + i + 1];
		}

		private int getNextNRecordIndex(int n) {

			int currentRecordIndex = recordIndex;

			for (int i = 0; i < n; i++) {
				currentRecordIndex = getNextRecordIndexOf(currentRecordIndex);
				if (currentRecordIndex == -1)
					return -1;
			}

			return currentRecordIndex;
		}

		private int getNextRecordIndexOf(int thisRecordIndex) {

			if (thisRecordIndex >= recordBuffer.length)
				return -1;

			int elementNum = recordBuffer[thisRecordIndex + gramElementNumOffset];

			return thisRecordIndex + gramElementOffset + gramElementSize * elementNum;
		}

		public IntVectorDig getNextIntVectorDig() {

			int recordIndex = getNextNRecordIndex(1);
			if (recordIndex == -1)
				return null;

			return new IntVectorDig(recordIndex);
		}

		public IntVectorDig getNextNIntVectorDig(int n) {

			int recordIndex = getNextNRecordIndex(n);
			if (recordIndex == -1)
				return null;

			return new IntVectorDig(recordIndex);
		}

		public String toString() {

			StringBuffer strBuffer = new StringBuffer();

			int gramElementNum = getGramElementNum();

			strBuffer.append("gramID:" + getGramID() + "(" + GramIndexer.get(getGramID()) + ")");
			strBuffer.append(" insNum:" + getGramInsNum());
			strBuffer.append(" gramElementNum:" + gramElementNum);
			strBuffer.append("::");

			for (int i = 0; i < gramElementNum; i++) {

				strBuffer.append(" " + recordBuffer[recordIndex + gramElementOffset + i * 2]);
				strBuffer.append("," + recordBuffer[recordIndex + gramElementOffset + i * 2 + 1]);

			}

			return strBuffer.toString();

		}

	}

	public IntVectorDig getIntVectorDig(int gramID) {

		Integer index = indexMap.get(gramID);
		if (index == null)
			return null;

		return new IntVectorDig(index);
	}

	public IntVectorDig getIntVectorDig(String gram) {
		int gramID = GramIndexer.indexOf(gram);

		if (gramID == -1)
			return null;

		return getIntVectorDig(gramID);
	}

	// class TopNComputer implements Runnable
	class TopNComputer extends Thread {

		int topN;
		int blockNum;
		long lastJobCost;

		IntVectorDig thisVector;
		PriorityQueue<Pair<IntVectorDig, Double>> localTopN;
		PriorityQueue<Pair<IntVectorDig, Double>> globalTopN;
		IntVectorDig startVector;
		int nextN;

		public TopNComputer(int blockNum, int topN, IntVectorDig thisVector, IntVectorDig startVector, int nextN,
				PriorityQueue<Pair<IntVectorDig, Double>> globalTopN) {

			this.blockNum = blockNum;
			this.topN = topN;

			this.thisVector = thisVector;

			this.startVector = startVector;
			this.nextN = nextN;

			this.localTopN = new PriorityQueue<Pair<IntVectorDig, Double>>(topN, new Comparator<Pair<IntVectorDig, Double>>() {
				public int compare(Pair<IntVectorDig, Double> o1, Pair<IntVectorDig, Double> o2) {
					double comResult = o1.second() - o2.second();
					if (comResult > 0)
						return 1;
					else if (comResult < 0)
						return -1;
					return 0;
				}
			});
			this.globalTopN = globalTopN;

		}

		@Override
		public void run() {

			IntVectorDig nextIntVectorDig = startVector;
			int totalRecordNum = nextN;

			long startTime = System.currentTimeMillis();

			for (int i = 0; i < totalRecordNum; i++) {

				if (i % 10000 == 0)
					logger.info("Block[" + blockNum + "]  " + i + " Vectors compared. total=" + totalRecordNum + " " + (int) ((i * 100) / totalRecordNum) + "%");

				double simlarity = computeSimlarity(thisVector, nextIntVectorDig);

				if (localTopN.size() < topN)
					localTopN.add(new Pair<IntVectorDig, Double>(nextIntVectorDig, simlarity));
				else {
					if (localTopN.peek().second < simlarity) {
						localTopN.poll();
						localTopN.add(new Pair<IntVectorDig, Double>(nextIntVectorDig, simlarity));
					}
				}

				nextIntVectorDig = nextIntVectorDig.getNextIntVectorDig();
			}

			long endTime = System.currentTimeMillis();
			long timeCost = endTime - startTime;
			logger.info("Block[" + blockNum + "] totalVecNum=" + totalRecordNum + " timeCost=" + timeCost + "'ms");
			lastJobCost = timeCost;
			logger.info("Block[" + blockNum + "] " +"Merge to Global TopN...");

			synchronized (globalTopN) {
				
				while (localTopN.size() > 0) {
					Pair<IntVectorDig, Double> localTopMin = localTopN.poll();

					if (globalTopN.size() < topN)
						globalTopN.add(localTopMin);
					else {
						if (globalTopN.peek().second < localTopMin.second) {
							globalTopN.poll();
							globalTopN.add(localTopMin);
						}
					}

				}

			}
			logger.info("Block[" + blockNum + "] " +"Merge to Global TopN Complete.");

		}
	}

	/**
	 * 
	 */

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static GramVectorModelBinBased loadFromDir(String modelDirPath) throws IOException {

		File modelDirFile = new File(modelDirPath);

		if (!modelDirFile.exists()) {
			logger.error("model:" + modelDirPath + " does not exist. create empty model.");

			GramVectorModelBinBased aGramVectorModel = new GramVectorModelBinBased();
			return aGramVectorModel;
		}

		GramVectorModelBinBased restoredModel = new GramVectorModelBinBased();

		restoredModel.GramIndexer = HashIndex.loadFromFilename(modelDirPath + "/" + "GramIndexer");
		restoredModel.WordIndexer = HashIndex.loadFromFilename(modelDirPath + "/" + "WordIndexer");

		int totalFileLength = 0;

		for (int i = 0; i <= restoredModel.NGramMAX; i++) {

			String binVectorMapFilePath = modelDirPath + "/" + i + "_GramVectorsBin";
			File binVectorMapFile = new File(binVectorMapFilePath);
			if (!binVectorMapFile.exists())
				break;

			totalFileLength += binVectorMapFile.length();
		}

		int sizeG = totalFileLength / (1024 * 1024 * 1024);
		int remain = totalFileLength % (1024 * 1024 * 1024);
		int sizeM = remain / (1024 * 1024);
		remain = remain % (1024 * 1024);
		int sizeK = remain / 1024;
		remain = remain % 1024;
		while (true) {
			if (sizeG != 0) {
				logger.info("Modle Buffer Size:" + sizeG + "." + sizeM + "G  ("+totalFileLength+")");
				break;
			}
			if (sizeM != 0) {
				logger.info("Modle Buffer Size:" +  sizeM + "." + sizeK + "M  ("+totalFileLength+")");
				break;
			}
			
			if (sizeK != 0) {
				logger.info("Modle Buffer Size:" +  sizeK + "." + remain + "K  ("+totalFileLength+")" );
				break;
			}
		
			break;
		}
		restoredModel.recordBuffer = new int[totalFileLength / 4];
		int nowIndex = 0;

		for (int i = 0; i <= restoredModel.NGramMAX; i++) {

			String binVectorMapFilePath = modelDirPath + "/" + i + "_GramVectorsBin";
			File binVectorMapFile = new File(binVectorMapFilePath);
			if (!binVectorMapFile.exists())
				break;

			nowIndex += loadFromFile(restoredModel.recordBuffer, nowIndex, binVectorMapFilePath);

		}

		logger.info("Load model complete." + restoredModel);

		logger.info("Make Index ...");
		int num = restoredModel.makeIndex();
		logger.info("Make Index complete . total:" + num + " Records.");

		return restoredModel;
	}

	public static int loadFromFile(int[] buffer, int beginIndex, String binFilePath) throws IOException {

		File vectorMapFile = new File(binFilePath);
		FileInputStream fis = new FileInputStream(vectorMapFile);

		byte[] intBytes = new byte[4];
		// Read headerLine
		int intCounter = 0;

		logger.info("Loading...  IntFile :" + binFilePath);
		while (true) {

			int read = fis.read(intBytes);
			if (read == -1) {
				break;
			} else if (read != 4) {
				System.err.println("File Read Error,Not 4 ,read:" + read);
				System.exit(1);
			}

			buffer[beginIndex++] = bytesToInt(intBytes);
			intCounter++;

		}
		logger.info("Load IntFile Complete. intCounter:" + intCounter + " file:" + binFilePath);
		fis.close();

		return intCounter;
	}

	private int makeIndex() {

		int next = 0;

		logger.info("Making... Index ");
		while (next != recordBuffer.length) {

			if (recordNum % 10000 == 0)
				logger.info("Making Index ... recordNum :" + recordNum);

			int gramID = recordBuffer[next + gramIDOffset];
			// int insNum=intBuffer[next+gramInsNumOffset];
			int gramElementNum = recordBuffer[next + gramElementNumOffset];

			indexMap.put(gramID, next);
			next = next + gramElementOffset + gramElementNum * gramElementSize;

			recordNum++;
		}

		return recordNum;
	}

	public ArrayList<Pair<IntVectorDig, Double>> getNNearestNegihborOf(String gram, int n, int threadNum) {

		int gramID = GramIndexer.indexOf(gram);

		if (gramID == -1)
			return null;

		PriorityQueue<Pair<IntVectorDig, Double>> globalTopN = new PriorityQueue<Pair<IntVectorDig, Double>>(n, new Comparator<Pair<IntVectorDig, Double>>() {
			public int compare(Pair<IntVectorDig, Double> o1, Pair<IntVectorDig, Double> o2) {
				double comResult = o1.second() - o2.second();
				if (comResult > 0)
					return 1;
				else if (comResult < 0)
					return -1;
				return 0;
			}
		});

		IntVectorDig thisIntVectorDig = getIntVectorDig(gramID);

		int recordPerThread = recordNum / threadNum;
		recordPerThread += recordNum % threadNum == 0 ? 0 : 1;

		int remain = recordNum;
		IntVectorDig nextVector = firstVector();

		int blockNum = threadNum;

		TopNComputer[] topNRunners = new TopNComputer[blockNum];

		int records = 0;
		for (int block = 0; block < blockNum; block++) {
			if (remain > recordPerThread) {
				records = recordPerThread;
				remain = remain - recordPerThread;
			} else {
				records = remain;
				remain = 0;
			}

			TopNComputer topNComputer = new TopNComputer(block, n, thisIntVectorDig, nextVector, records, globalTopN);
			topNRunners[block] = topNComputer;

			nextVector = nextVector.getNextNIntVectorDig(records);
		}

		for (TopNComputer topNRunner : topNRunners) {
			topNRunner.start();
		}

		for (TopNComputer topNRunner : topNRunners) {
			try {
				topNRunner.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ArrayList<Pair<IntVectorDig, Double>> returnList = new ArrayList<Pair<IntVectorDig, Double>>(n);

		while (!globalTopN.isEmpty())
			returnList.add(globalTopN.poll());

		return returnList;

	}

	public static byte[] intToByte(int i) {

		byte[] abyte0 = new byte[4];

		abyte0[0] = (byte) (0xff & i);

		abyte0[1] = (byte) ((0xff00 & i) >> 8);

		abyte0[2] = (byte) ((0xff0000 & i) >> 16);

		abyte0[3] = (byte) ((0xff000000 & i) >> 24);

		return abyte0;

	}

	public static int bytesToInt(byte[] bytes) {

		int addr = bytes[0] & 0xFF;

		addr |= ((bytes[1] << 8) & 0xFF00);

		addr |= ((bytes[2] << 16) & 0xFF0000);

		addr |= ((bytes[3] << 24) & 0xFF000000);

		return addr;

	}

	public static void strModel2BinModel(String strModelDirPath, String binModelDirPath, int maxGram) throws IOException {

		File strModelDir = new File(strModelDirPath);
		File binModelDir = new File(binModelDirPath);
		if (!strModelDir.exists())
			return;

		if (!binModelDir.exists())
			binModelDir.mkdir();

		// transform vector
		for (int i = 0; i < maxGram; i++) {
			String strVectorMapFilePath = strModelDirPath + "/" + i + "_GramVectors";
			File strVectorMapFile = new File(strVectorMapFilePath);
			if (!strVectorMapFile.exists())
				continue;
			String binVectorMapFilePath = binModelDirPath + "/" + i + "_GramVectorsBin";

			logger.info("Transforming... VectorMap Str to bin :" + strVectorMapFilePath + ">>" + binVectorMapFilePath);

			int vectors = strVectorMap2bin(strVectorMapFilePath, binVectorMapFilePath);
			logger.info("Transform complete VectorMap Str to bin ,total:" + vectors);
		}

		// copy GramMap

		String origialGramIndexerPath = strModelDirPath + "/GramIndexer";
		String targetGramIndexerPath = binModelDirPath + "/GramIndexer";
		File origialFile = new File(origialGramIndexerPath);
		File targetFile = new File(targetGramIndexerPath);
		if (!origialFile.exists()) {
			logger.error("GramIndexer do not exists:" + origialFile.getAbsolutePath());
			System.exit(1);
		}
		if (!targetFile.exists())
			targetFile.createNewFile();

		copyFile(origialFile, targetFile);

		// copy WordMap

		String origialWordIndexerPath = strModelDirPath + "/WordIndexer";
		String targetWordIndexerPath = binModelDirPath + "/WordIndexer";
		origialFile = new File(origialWordIndexerPath);
		targetFile = new File(targetWordIndexerPath);
		if (!origialFile.exists()) {
			logger.error("WordIndexer do not exists:" + origialFile.getAbsolutePath());
			System.exit(1);
		}
		if (!targetFile.exists())
			targetFile.createNewFile();

		copyFile(origialFile, targetFile);

	}

	public static int strVectorMap2bin(String strFilePath, String binFilePath) throws IOException {

		FileOutputStream fos = new FileOutputStream(binFilePath);

		FileInputStream fis = new FileInputStream(strFilePath);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader br = new BufferedReader(isr);

		int counter = 0;

		String aRecord = null;
		while ((aRecord = br.readLine()) != null) {

			if (counter % 1000 == 0)
				logger.info("Transform... done:" + counter);
			// System.out.println("aRecord:"+aRecord);

			String sharpedRecord = aRecord.replace(",", "");
			sharpedRecord = sharpedRecord.replace("[", "");
			sharpedRecord = sharpedRecord.replace("]", "");
			sharpedRecord = sharpedRecord.replace("(", "");
			sharpedRecord = sharpedRecord.replace(")", "");

			String[] terms = sharpedRecord.split(" ");

			int gramIndex = Integer.parseInt(terms[0]);
			byte[] intBytes = intToByte(gramIndex);
			fos.write(intBytes);

			int instCounter = Integer.parseInt(terms[1]);
			intBytes = intToByte(instCounter);
			fos.write(intBytes);

			int elementNum = terms.length - 2;
			intBytes = intToByte(elementNum);
			fos.write(intBytes);

			for (int i = 2; i < terms.length; i++) {

				// System.out.println(terms[i]);
				String[] element = terms[i].split("=");
				int wordIndex = Integer.parseInt(element[0]);
				intBytes = intToByte(wordIndex);
				fos.write(intBytes);

				int wordCounter = Integer.parseInt(element[1]);
				intBytes = intToByte(wordCounter);
				fos.write(intBytes);

			}

			counter++;

		}

		fos.close();
		br.close();

		return counter;

	}

	public static int binVectorMap2str(String binFilePath, String strFilePath) throws IOException {

		File binFile = new File(binFilePath);
		FileInputStream fis = new FileInputStream(binFilePath);

		// FileOutputStream fos = new FileOutputStream(strFilePath);
		// OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		// BufferedWriter bw = new BufferedWriter(osw);

		int counter = 0;

		byte[] intBytes = new byte[4];
		// Read headerLine
		int intNum = (int) (binFile.length() / 4);
		int[] intBuffer = new int[intNum];
		int indexNow = 0;

		logger.info("Loading...  IntFile :" + binFilePath);
		while (true) {

			int read = fis.read(intBytes);
			if (read == -1) {
				break;
			} else if (read != 4) {
				System.err.println("File Read Error,Not 4 ,read:" + read);
				System.exit(1);
			}

			intBuffer[indexNow++] = bytesToInt(intBytes);
		}
		logger.info("Load IntFile Complete.  file:" + binFilePath);

		int vectorNum = 0;
		int next = 0;

		logger.info("Making... Index Of VectorMap File:" + binFilePath);
		while (next != intNum) {

			int gramID = intBuffer[next + gramIDOffset];
			int insNum = intBuffer[next + gramInsNumOffset];
			int gramElementNum = intBuffer[next + gramElementNumOffset];

			System.out.print("gramID:" + gramID);
			System.out.print(" insNum:" + insNum);
			System.out.print(" gramElementNum:" + gramElementNum);
			System.out.print(":");
			for (int i = 0; i < gramElementNum; i++) {
				System.out.print(" " + intBuffer[next + gramElementOffset + i * 2]);
				System.out.print("," + intBuffer[next + gramElementOffset + i * 2 + 1]);
			}
			System.out.println("");

			next = next + gramElementOffset + gramElementNum * gramElementSize;

			vectorNum++;
		}

		return counter;

	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	public static void main(String[] args) throws IOException {

		// String strFilePath=args[0];
		// String binFilePath=args[1];
		DOMConfigurator.configureAndWatch("conf/log4j.xml");

		// int counter=GramVectorModelbinBased.strVectorMap2bin(strFilePath,
		// binFilePath);
		// int counter = GramVectorModelBinBased.binVectorMap2str(binFilePath,
		// "");
		// System.out.println("strVectorMap2bin：" + counter);

		// GramVectorModelBinBased.loadFromDir(modelDirPath)
		// GramVectorModelbinBased.binVectorMap2str(binFilePath,"");
	}
}
