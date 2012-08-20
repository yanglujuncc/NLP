package ylj.NGram;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import  ylj.Util.StringRecognizer;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class GramModelTrainer {
	
	protected static Logger logger = Logger.getLogger(GramModelTrainer.class.getName());
	
	long maxLine;
	List<NGramSpliter> NGramSpliters=new LinkedList<NGramSpliter>();
	boolean enGram;
	
	public void setMaxLine(long aMaxLine){
		maxLine=aMaxLine;
	}
	public GramVectorModel trainModel(String path) throws IOException{
		logger.info("Trianing Model ...");
		GramVectorModel newGramVectorModel=new GramVectorModel();
		
		return addIncrement(newGramVectorModel,path);
	}
	
	public GramVectorModel addIncrement(GramVectorModel model,String path) throws IOException{
		
		logger.info("Add Increment To Model ... InstanceFile="+path);
		String oldModelInfo="Old Mode Info:"+model;
		logger.info(oldModelInfo);
		
		FileInputStream fis=new FileInputStream(path);
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br=new BufferedReader(isr);
		
		String aline=null;
		
		
		int i=0;
		while((aline=br.readLine())!=null){
			
			if(i%1000==0)
				logger.info("processed "+i+" lines");
			
			
			if(i>=maxLine)
				break;
			for(NGramSpliter aNGramSpliter:NGramSpliters)
			{
				List<String> grams=aNGramSpliter.splite(aline);
				for(String gram:grams){
					if(StringRecognizer.isEnWord(gram))
					{
						if(enGram)
							model.addEngGramInstence(gram, aline);
					}			
					else
						model.addHanGramInstence(aNGramSpliter.getGramN(),gram, aline);
				}
			}
			model.instanceCounterIncrease();
			i++;
			
		}
		br.close();
		logger.info("Add Increment Complete ");
		logger.info(oldModelInfo);
		
		String updateModelInfo="New Mode Info:"+model;
		logger.info(updateModelInfo);
		return model;
	}
	public boolean createNGramSpliter(String grams){
		
		logger.info("Creating NGramSpliters ...");
		String[] NGrams=grams.split("[,，]");
		
		for(String ngram:NGrams){
			
			if(StringRecognizer.isADigital(ngram))
			{
				int n=Integer.parseInt(ngram);
				logger.info("Creating "+n +" GramSpliter .");
				NGramSpliter newNGramSpliter=new NGramSpliter(n);
				NGramSpliters.add(newNGramSpliter);
			}
			else if(StringRecognizer.isEnWord(ngram))
			{
				if(ngram.equalsIgnoreCase("en"))
				{
					enGram=true;
				}
				else
				{
					logger.info("args NGrams error :"+ngram);
					return false;
				}
				
			}
		}
		logger.info("Create NGramSpliters complete .");
		return true;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		PropertyConfigurator.configure("conf/log4j.properties");
		
		if(args.length!=5)
		{
			System.out.println("Add Increment To Model or Train A New Model  ");
			System.out.println("usage:GramModelTrainer	NGrams MaxLine InstancePath ModelPath ModelOuput");
			
			System.out.println("                       	NGrams	(like:1,2,en  means use 1Gram And 2Gram )");
			System.out.println("                       	MaxLine	(MaxLine readed in InstanceFile,or 0 not limtied)");
			System.out.println("                       	InstancePath	(Increment Instance File Path)");
			System.out.println("                       	ModelPath	(model to restore,or null[NULL] to create a new model)");
			System.out.println("                       	ModelOuput	(path of the new model to store)");
			System.out.println("Example: ");
			System.out.println("   Add Increment:    GramModelTrainer 1,2,en 0 InstanceFile oldeModel updatedModel");
			System.out.println(" Train New Model:    GramModelTrainer 1,2,en 0 InstanceFile NULL newModel");
			System.out.println("	");
			System.out.println("InstanceFile:");
			System.out.println("	InstanceFile is a raw txt file.A line treat as  a instance  .");
			System.out.println("Example:");
			System.out.println("	(Instance.txt)");
			System.out.println("	准备吃书了啊 ");
			System.out.println("	我靠 给我吧 ");
			System.out.println("	我刚点下修练 一百五十万没了 ");
			System.out.println("	路过乌龟旁边 刚好乌龟放群 你们就死了 ");
			System.out.println("	停早了 ");

			return;
		}
		
		String NGrams=args[0];
		long maxLine=Long.parseLong(args[1]);
		if(maxLine==0)
			maxLine=Long.MAX_VALUE;
		String instanceFilePath=args[2];
		String modelFile=args[3];
		String modelOutputPath=args[4];
		
		GramModelTrainer aGramModelTrainer=new GramModelTrainer();
		aGramModelTrainer.createNGramSpliter(NGrams);
		aGramModelTrainer.setMaxLine(maxLine);
		
		GramVectorModel gramVectorModel;
		
		if(modelFile.equals("NULL")||modelFile.equals("null"))
		{
			gramVectorModel=aGramModelTrainer.trainModel(instanceFilePath);
		}
		else
		{
			gramVectorModel=GramVectorModel.loadModel(modelFile);
			aGramModelTrainer.addIncrement(gramVectorModel, instanceFilePath);
		}
		
		GramVectorModel.writeModel(modelOutputPath, gramVectorModel);
		
		
	}
}
