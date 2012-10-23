package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ylj.Util.StringRecognizer;

public class WordRecognizer {
	
	int maxGramN;
	double counterThreshold;
	double combinedDegreeThreshold;
	double freedomDegreeThreshold;
	
	public WordRecognizer(String maxGramNValue,String counter,String combinedDegree,String freedomDegree){
		maxGramN=Integer.parseInt(maxGramNValue);
		counterThreshold=Double.parseDouble(counter);
		combinedDegreeThreshold=Double.parseDouble(combinedDegree);
		freedomDegreeThreshold=Double.parseDouble(freedomDegree);
		
	}
	
	public boolean biggerThanThreshold(long counter,double combinationDegree,double freedomDegree){
		
		if(counter<counterThreshold)
			return false;
		
		if(combinationDegree<combinedDegreeThreshold)
			return false;
		
		if(freedomDegree<freedomDegreeThreshold)
			return false;
		
		return true;
	}
void process(String inputFile, String outputFile) throws IOException{
		
		

		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isw = new InputStreamReader(fis, "gbk");
		BufferedReader br = new BufferedReader(isw);
		
		FileOutputStream fos = new FileOutputStream(outputFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw = new BufferedWriter(osr);
		
		String line=null;
		
		long i=0;
		while((line=br.readLine())!=null){
			
			if (i % 100000 == 0)
				System.out.println("processed " + i + ".");
			
			String[] terms=line.split("\t");
			String gram=terms[0];
			long counter=Long.parseLong(terms[1]);
			double combinationDegree=Double.parseDouble(terms[2]);
			double freedomDegree=Double.parseDouble(terms[3]);
			
			if(NGramSpliter.gramNum(gram)>maxGramN)
				continue;
			
			//¹ýÂË Êý×Ö
			if(StringRecognizer.isDigital(gram)){
				continue;
			}
			
			
			if(biggerThanThreshold(counter,combinationDegree,freedomDegree))
			{
				bw.append(line+"\n");
				
			}
		
			i++;
		}
		br.close();
		bw.close();
		
	}

	public static void main(String[] args) throws IOException
	{
	
		if (args.length != 6) {
			

			System.out.println("usage:NewWordRecognizer	maxGramN counterThreshold combinedDegreeThreshold freedomDegreeThreshold RecordFile OuputFile");

		
			System.out.println("       InputRecord gram:counter:CombinedDegree:FreedomDegree " );

			return;
		}
		
		String maxGramN=args[0];
		String counterThreshold=args[1];
		String combinedDegreeThreshold=args[2];
		String freedomDegreeThreshold=args[3];
		
		String inputRecordFile=args[4];
		String output=args[5];
		
		WordRecognizer aNewWordRecognizer=new WordRecognizer(maxGramN,counterThreshold,combinedDegreeThreshold,freedomDegreeThreshold);
		aNewWordRecognizer.process(inputRecordFile, output);
	
	}
}
