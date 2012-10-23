package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.LinkedList;
import java.util.List;

import ylj.Util.Pair;

public class AddCohesionDegree {

	GramOccCounter gramOccCounter=new GramOccCounter(1);
	long totalWords ;
	

	int[] oneSpliteFlag={1};
	int maxGram;
	NGramSpliter nGramSpliter;
	BufferedWriter tempGramGroupProbWriter;
	
	public AddCohesionDegree(int maxGramValue){
		maxGram=maxGramValue;
		nGramSpliter=new NGramSpliter(maxGram);
	}
	
	public double computeOccProbOfGramGrop(LinkedList<String> gramGroup) throws IOException{
	
		double unionProb=1;
		for(String subGram:gramGroup){
			
			long counter = gramOccCounter.getGramCounter(subGram);
			
			double prob=(counter*1.0)/totalWords;
			
			tempGramGroupProbWriter.append("|"+subGram+" "+counter+" "+prob);
			unionProb=unionProb*prob;
		}
		tempGramGroupProbWriter.append("|"+unionProb+", T:"+totalWords+"\n");
		
		return unionProb;
		
	}
	public double computeCohesion(String gram) throws IOException {
		
		if(NGramSpliter.gramNum(gram) > maxGram)
			return Double.MIN_VALUE;
		
		if (NGramSpliter.gramNum(gram) == 1)
			return Double.MAX_VALUE;

		LinkedList<LinkedList<String>> subGramGroups = subGramGroups(gram);

		// System.out.println(subGramPairs);

		long gramCounter = gramOccCounter.getGramCounter(gram);
		if (gramCounter == 0)
			return Double.MIN_VALUE;

		double gramProb=0.0;
		//double minPMI = Double.MAX_VALUE;
		
		double maxSubGramsGroupProb=0.0;
		
		for (LinkedList<String> gramGroup : subGramGroups) {
			
			
			double gramGroupProb=computeOccProbOfGramGrop(gramGroup);
			
			
			if(gramGroup.size()==1)
			{			
				gramProb=gramGroupProb;
				continue;
			}
			if(gramGroupProb>maxSubGramsGroupProb)
				maxSubGramsGroupProb=gramGroupProb;
			
		}
		
		double cohesion=Math.log(gramProb/maxSubGramsGroupProb);
		
		tempGramGroupProbWriter.append(gram+" Prob:"+gramProb+" MaxSubProb:"+maxSubGramsGroupProb+" Cohesion:"+cohesion+"\n");
		
		return cohesion;
	}
	
	

	
	public LinkedList<LinkedList<String>> subGramGroups(String gram) {
		
		if(gram==null||gram.equals(""))
			return null;
		
		LinkedList<LinkedList<String>> subGramGroupList = new LinkedList<LinkedList<String>>();
		int gramN = NGramSpliter.gramNum(gram);
		LinkedList<String> thisGramGroup= new LinkedList<String>();
		thisGramGroup.add(gram);
		subGramGroupList.add(thisGramGroup);
		
		if (gramN == 1)
		{
			return subGramGroupList;
		}
		
		List<String> words = nGramSpliter.splite(gram,oneSpliteFlag);
		
		for (int i = 1; i < words.size(); i++) {

			String firstGram = gram.substring(0, i);
			String remain = gram.substring(i);
			
			LinkedList<LinkedList<String>>  subGroups=subGramGroups(remain);
			if(subGroups==null)
			{
				LinkedList<String> group= new LinkedList<String>();
				group.add(firstGram);
				subGramGroupList.add(group);
			}
			else
			{
				for(LinkedList<String> aGroup:subGroups){
					aGroup.push(firstGram);
					subGramGroupList.add(aGroup);
				}
				
			}
		}
		return subGramGroupList;
	}
	
	
	void process(String occFile, String cohesionFile) throws IOException{
		
		gramOccCounter=GramOccCounter.loadFromFile(occFile);
		totalWords = gramOccCounter.getTotalWordCounter();

		FileInputStream fis = new FileInputStream(occFile);
		InputStreamReader isw = new InputStreamReader(fis, "gbk");
		BufferedReader br = new BufferedReader(isw);
		
		FileOutputStream fos = new FileOutputStream(cohesionFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw = new BufferedWriter(osr);
		
		FileOutputStream fos_temp = new FileOutputStream(cohesionFile+"_subGroupProb");
		OutputStreamWriter osr_temp = new OutputStreamWriter(fos_temp,"gbk");
		tempGramGroupProbWriter = new BufferedWriter(osr_temp);
		
		
		String line=null;
		
		long i=0;
		while((line=br.readLine())!=null){
			
			if (i % 100000 == 0)
				System.out.println("Compute Cohesion : processed " + i + " grams.");
			
			String[] terms=line.split("\t");
			String gram=terms[0];
			double cohesion=computeCohesion(gram);
			//System.out.println("cohesion : "+cohesion+"\n");
			String newLine=line+"\t"+cohesion;
			
			bw.append(newLine+"\n");
			i++;
		}
		br.close();
		bw.close();
		tempGramGroupProbWriter.close();
		
	}
	
	void processFast(String occFile, String freedomFile,double cohesionThreshold,String vocabFile,String cohesion_subGroupProbFile) throws IOException{
		
		gramOccCounter=GramOccCounter.loadFromFile(occFile);
		totalWords = gramOccCounter.getTotalWordCounter();

		FileInputStream fis_freedom = new FileInputStream(freedomFile);
		InputStreamReader isw_freedom = new InputStreamReader(fis_freedom, "gbk");
		BufferedReader br_freedom = new BufferedReader(isw_freedom);
	
		FileOutputStream fos_vocab = new FileOutputStream(vocabFile);
		OutputStreamWriter osr_vocab = new OutputStreamWriter(fos_vocab,"gbk");
		BufferedWriter bw_vocab = new BufferedWriter(osr_vocab);
		
		
		FileOutputStream fos_temp = new FileOutputStream(cohesion_subGroupProbFile);
		
		OutputStreamWriter osr_temp = new OutputStreamWriter(fos_temp,"gbk");
		tempGramGroupProbWriter = new BufferedWriter(osr_temp);
		
		
		String freedomLine=null;
		
		long i=0;
		while((freedomLine=br_freedom.readLine())!=null){
			
			if (i % 100000 == 0)
				System.out.println("Compute Cohesion : processed " + i + " grams.");
			

			String[] terms=freedomLine.split("\t");
			String gram=terms[0];
			
		
			double cohesion=computeCohesion(gram);
			if(cohesion<cohesionThreshold)
				continue;
	
			//System.out.println("cohesion : "+cohesion+"\n");
			String newLine=freedomLine+"\t"+cohesion;
			
			bw_vocab.append(newLine+"\n");
			i++;
		}
		
		br_freedom.close();
		bw_vocab.close();
		tempGramGroupProbWriter.close();
		
	}
	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
	
			System.out
					.println("usage:AddInternalMinPMI	MaxGram SortedOccFile   OuputFile");
			
			System.out.println("                       	MaxGram	( MaxGram )");
			System.out
					.println("                       	SortedOccFile	(input SortedOccCounterFile )");
			System.out
					.println("                       	OuputFile	(number of store file  (MapNum) )");

			System.out.println("Example: ");
			System.out.println("   AddLeftFreedomDegree  GramOcc  GramOcc_R ");
			System.out.println("	");

			return;
		}

		int  MaxGram= Integer.parseInt(args[0]);
		String sortedOccFile = args[1];
		String ouputFile = args[2];

		AddCohesionDegree aAddCombinationDegree = new AddCohesionDegree(MaxGram);
		aAddCombinationDegree.process(sortedOccFile, ouputFile);

	}
}
