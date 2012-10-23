package NGram;

import java.io.BufferedReader;

import java.io.FileInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import ylj.Util.FilesInput;
import ylj.Util.StringRecognizer;

public class GramModelTrainerVocabMapBased {

	protected static Logger logger = Logger
			.getLogger(GramModelTrainerVocabMapBased.class.getName());

	long maxLine;

	int minInstanceCounter = 5;

	Set<String> vocabSet = new HashSet<String>();
	VocabMap vocabMap = new VocabMap();

	boolean useVocabMap=true;
	
	int[] NGramsArray;

	boolean enGram = true;
	NGramSpliter nGramSpliter = new NGramSpliter(8);

	public void setMaxLine(long aMaxLine) {
		maxLine = aMaxLine;
	}

	public long loadDics(String dicPath) throws IOException {

		FilesInput aFilesInput = new FilesInput("gbk");

		aFilesInput.preLoadFromDirName(dicPath);

		String record = null;
		long vocabCount = 0;
		while ((record = aFilesInput.getLine()) != null) {

			String vocab = record.split("( |\t|:)")[0];
			// System.out.println(vocab);
			vocabSet.add(vocab);
			vocabCount++;
		}
		return vocabCount;
	}

	public long loadVocabMap(String vocabMapPath) throws IOException {

		if(vocabMapPath.equalsIgnoreCase("null"))
		{
			useVocabMap=false;
			logger.info("UseVocabMap:" + useVocabMap+" Accept All Grams");
			return -1;
		}
		else
			useVocabMap=true;
		
		logger.info("UseVocabMap:" + useVocabMap);
		
		vocabMap.loadFromFile(vocabMapPath);
		int vocabCount = vocabMap.vocabNum();

		return vocabCount;
	}

	public GramVectorModel trainModel(String path) throws IOException {
		logger.info("Trianing new Model ...");
		GramVectorModel newGramVectorModel = new GramVectorModel();

		return addIncrement(newGramVectorModel, path);
	}

	public GramVectorModel addIncrement(GramVectorModel model, String path)
			throws IOException {

		logger.info("Add Increment To Model ... InstanceFile=" + path);
		String oldModelInfo = "Old Mode Info:" + model;
		logger.info(oldModelInfo);

		File instanceFile=new File(path);
		if(!instanceFile.exists()){
			logger.info("InstanceFile Not Exist. path:" + path);
			return model;
		}
		
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		String aline = null;

		int i = 0;
		while ((aline = br.readLine()) != null) {

			if (i % 10000 == 0) {
				logger.info("processed " + i + " lines");
				logger.info("\n"+model.getState());
			}

			if (i >= maxLine)
				break;

			List<String> grams = nGramSpliter.splite(aline, NGramsArray);
			for (String gram : grams) {

				if(StringRecognizer.isRepeating(gram))
					gram=StringRecognizer.shortRepeatFormat(gram);
				
				if(useVocabMap)
				{
					if (vocabMap.vocabIdOf(gram) == -1)
						continue;
				}
					

				if (StringRecognizer.isCN_zhStr(gram)) {

					model.addHanGramInstence(gram, aline);

				} else {

					if (enGram) {

						model.addEngGramInstence(gram, aline);
					}
				}

			}

			model.instanceCounterIncrease();
			i++;

		}
		br.close();
		logger.info("Add Increment Complete ");
		logger.info(oldModelInfo);

		String updateModelInfo = "New Mode Info:" + model;
		logger.info(updateModelInfo);
		return model;
	}

	public boolean createNGramSpliter(String grams) {

		logger.info("Creating NGramSpliters ...");
		String[] NGrams = grams.split("[,，]");
		NGramsArray = new int[NGrams.length];
		int i = 0;
		for (String ngram : NGrams) {

			if (StringRecognizer.isDigital(ngram)) {
				int n = Integer.parseInt(ngram);
				NGramsArray[i] = n;
			} else {
				logger.info("input error... for grams:" + grams);
				System.exit(1);
			}

			i++;
		}
		logger.info("Create NGramSpliters complete .");
		logger.info("NGramsArray=" + Arrays.toString(NGramsArray));
		return true;
	}

	public static void main(String[] args) throws Exception {

		DOMConfigurator.configureAndWatch("conf/log4j.xml");
		if (args.length != 6) {
			System.out.println("Add Increment To Model or Train A New Model  ");
			System.out
					.println("usage:GramModelTrainer	NGrams MaxLine DicDir VocabMap InstancePath ModelPath ModelOuput");

			System.out
					.println("                       	NGrams	(like:1,2  means use 1Gram And 2Gram )");
			System.out
					.println("                       	MaxLine	(MaxLine readed in InstanceFile,or 0 not limtied)");

			System.out.println("                       	VocabMapFile	(xx) or NULL accept all grams");
			System.out
					.println("                       	InstancePath	(Increment Instance File Path)");
			System.out
					.println("                       	ModelPath	(model to restore,or null[NULL] to create a new model)");
			System.out
					.println("                       	ModelOuput	(path of the new model to store)");
			System.out.println("Example: ");
			System.out
					.println("   Add Increment:    GramModelTrainer 1,2,en 0 vocabmapfiles InstanceFile oldeModel updatedModel");
			System.out
					.println(" Train New Model:    1,2,3,4  0 Vocab\\VocabMap IMESSAGE NULL imessageModel");
			System.out.println("	");
			System.out.println("InstanceFile:");
			System.out
					.println("	InstanceFile is a raw txt file.A line treat as  a instance  .");
			System.out.println("Example:");
			System.out.println("	(Instance.txt)");
			System.out.println("	准备吃书了啊 ");
			System.out.println("	我靠 给我吧 ");
			System.out.println("	我刚点下修练 一百五十万没了 ");
			System.out.println("	路过乌龟旁边 刚好乌龟放群 你们就死了 ");
			System.out.println("	停早了 ");

			return;
		}

		String NGrams = args[0];
		long maxLine = Long.parseLong(args[1]);
		if (maxLine == 0)
			maxLine = Long.MAX_VALUE;

		String vocabMapFilePath = args[2];

		String instanceFilePath = args[3];
		String modelFile = args[4];
		String modelOutputPath = args[5];

		GramModelTrainerVocabMapBased aGramModelTrainer = new GramModelTrainerVocabMapBased();
		aGramModelTrainer.createNGramSpliter(NGrams);
		aGramModelTrainer.setMaxLine(maxLine);

		GramVectorModel gramVectorModel;

		long vocabNum = aGramModelTrainer.loadVocabMap(vocabMapFilePath);
		logger.info("Load vocabNum=" + vocabNum);

		long timeBegin = 0;
		long timeEnd = 0;
		if (modelFile.equalsIgnoreCase("NULL")) {
			timeBegin = System.currentTimeMillis();

			gramVectorModel = aGramModelTrainer.trainModel(instanceFilePath);

			timeEnd = System.currentTimeMillis();
			long trainModelCost = timeEnd - timeBegin;
			logger.info("trainModelCost=" + trainModelCost + "'ms");

		} else {

			// /gramVectorModel=GramVectorModel.loadModel(modelFile);
			timeBegin = System.currentTimeMillis();
			logger.info("Loading Model from :"+modelFile);
			if (modelFile.endsWith(".obj"))
				gramVectorModel= GramVectorModel.loadModelObj(modelFile);
			else
				gramVectorModel= GramVectorModel.loadFromDir(modelFile);
			
			timeEnd = System.currentTimeMillis();
			long loadModelCost = timeEnd - timeBegin;
			logger.info("loadModelCost=" + loadModelCost + "'ms");

			timeBegin = System.currentTimeMillis();
			aGramModelTrainer.addIncrement(gramVectorModel, instanceFilePath);
			timeEnd = System.currentTimeMillis();
			long trainModelCost = timeEnd - timeBegin;
			logger.info("trainModelCost=" + trainModelCost + "'ms");
		}

		// ******** save file model *********//
		timeBegin = System.currentTimeMillis();
		logger.info("Writing Model to :"+modelOutputPath);
		if(modelOutputPath.endsWith(".obj"))
			GramVectorModel.writeModel(modelOutputPath, gramVectorModel);
		else
			gramVectorModel.saveToDir(modelOutputPath);
		
		timeEnd = System.currentTimeMillis();
		long saveToDirCost = timeEnd - timeBegin;
		logger.info("saveToModelDirCost=" + saveToDirCost + "'ms");

		// ******** save obj model *********//
		// timeBegin=System.currentTimeMillis();
		// GramVectorModel.writeModel(modelOutputPath+".obj", gramVectorModel);
		// timeEnd=System.currentTimeMillis();
		// long writeModelCost=timeEnd-timeBegin;
		// logger.info("writeModelObjCost="+writeModelCost+"'ms");
	}
}
