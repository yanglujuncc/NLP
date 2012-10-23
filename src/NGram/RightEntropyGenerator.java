package NGram;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import ylj.Util.Pair;

import ylj.Util.StringRecognizer;

public class RightEntropyGenerator {

	public LinkedList<String> lineBuffer = new LinkedList<String>();
	public BufferedReader occFileReader;
	// NGramSpliter oneGramSpliter=new NGramSpliter(1);
	int[] oneSpliteFlag = { 1 };

	
	NGramSpliter nGramSpliter=new NGramSpliter(10);
	int maxGram;
	public RightEntropyGenerator(BufferedReader fileReader,int maxGram) {
		occFileReader = fileReader;
		this.maxGram=maxGram;
	}

	private String getNextLine() throws IOException {

		if (lineBuffer.size() > 0)
			return lineBuffer.removeFirst();
		else {

			String nextLine = occFileReader.readLine();
		
			return nextLine;
		}

	}

	public String generateLine() throws IOException {

		String line = getNextLine();
		if (line == null)
			return null;

		String[] terms = line.split("\t");
		String gram = terms[0];
		long counter=Long.parseLong(terms[1]);
		
		double entropy=Double.MAX_VALUE;
		List<Pair<String, Long>> words=null;
		
		if (StringRecognizer.isCN_zhStr(gram)) {
			
			// long counter = Long.parseLong(terms[1]);
			//System.out.println(line);
			if(gram.length()<=maxGram)
			{
				words = getStartWithWords(gram);
				words.add(0, new Pair<String, Long>("",counter));
				 //System.out.println(words);
				 entropy = computeEntropy(words);
			}
			else
				entropy=Double.MIN_VALUE;
			
			
		}

		String newLine = line + "\t" + entropy+"\t"+words;
	
		
		return newLine;
	}
	
	public String generateLine(int couterThreshold) throws IOException {

		String line = getNextLine();
		if (line == null)
			return null;

		String[] terms = line.split("\t");
		String gram = terms[0];
		long counter=Long.parseLong(terms[1]);
		
		if(counter<couterThreshold)
			return generateLine( couterThreshold );
		
		double entropy=Double.MAX_VALUE;
		List<Pair<String, Long>> words=null;
		
		if (StringRecognizer.isCN_zhStr(gram)) {
			
			// long counter = Long.parseLong(terms[1]);
			//System.out.println(line);
			if(gram.length()<=maxGram)
			{
				words = getStartWithWords(gram);
				words.add(0, new Pair<String, Long>("",counter));
				 //System.out.println(words);
				 entropy = computeEntropy(words);
			}
			else
				entropy=Double.MIN_VALUE;
			
			
		}

		String newLine = line + "\t" + entropy+"\t"+words;
	
		
		return newLine;
	}
	private double infoValue(double prob) {
		if (prob == 0)
			return 0;
		return Math.log(prob) / Math.log(2);
	}

	private double computeEntropy(List<Pair<String, Long>> words) {

		Pair<String, Long> first = words.remove(0);
		if (!first.first.equals("")) {
			System.out.println("Something wrong ... in the wordlist");
			System.out.println(words);
			System.exit(1);
			return -1;
		}

		long totalOcc = first.second;
		
		
		long wordsOccCounter = 0;
		double entropy = 0;
		for (Pair<String, Long> pair : words) {

			wordsOccCounter += pair.second;

			double probability = (pair.second * 1.0) / totalOcc;
			entropy += 0 - (probability * infoValue(probability));

		}
		long endCounter = totalOcc - wordsOccCounter;
		// System.out.println("totalOcc="+totalOcc);
		// System.out.println("wordsOccCounter="+wordsOccCounter);
		// System.out.println("endCounter="+endCounter);
		if (endCounter < 0) {
			System.out
					.println("Something wrong ... endCounter<0  in the wordlist");

			System.out.println("totalOcc=" + totalOcc);
			System.out.println("wordsOccCounter=" + wordsOccCounter);
			System.out.println("endCounter=" + endCounter);

			System.out.println(words);
			System.exit(1);
			return -1;
		}

		while (endCounter > 0) {

			double probability = 1.0 / totalOcc;
			entropy += 0 - (probability * infoValue(probability));
			endCounter--;
		}

		return entropy;
	}


	private List<Pair<String, Long>> getStartWithWords(String prefix)
			throws IOException {

		List<Pair<String, Long>> returnList = new LinkedList<Pair<String, Long>>();

		boolean noMoreLines=false;
		//if buff not contain all grams Start With prefix
		while(lineBuffer.size()==0||lineBuffer.getLast().startsWith(prefix)){
			
			//read 1000 line
			for(int i=0;i<1000;i++){
				
				String line = occFileReader.readLine();
				if (line == null)
				{
					noMoreLines=true;
					break;
				}
				lineBuffer.add(line);

			}
			if(noMoreLines)
				break;
			
			if(lineBuffer.getLast().startsWith(prefix))
				continue;
			else
				break;
		}
		
		
		for (String line : lineBuffer) {

			String[] terms = line.split("\t");
			String gram = terms[0];
			long counter = Long.parseLong(terms[1]);

			if (gram.startsWith(prefix)&&gram.length()==prefix.length()+1)
			{
				String suffixWord=gram.substring(prefix.length());
				
				returnList.add(new Pair<String, Long>(suffixWord, counter));
			}
			
			if(!gram.startsWith(prefix))
				break;
		}
		
		
		return returnList;
	}
}
