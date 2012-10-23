package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.TreeMap;

public class AddLeftFreedomDegree {

	NGramSpliter nGramSpliter=new NGramSpliter(5);
	
	public void process(String occFile, String outputFile,int maxGram) throws IOException {
		
		System.out.println("Reversing....  Occ File ");
		String reversedOccFile =occFile+"_Reversed";
		
		GramOccCounter.reverseOccFile(occFile, reversedOccFile);
		
		FileInputStream fis = new FileInputStream(reversedOccFile);
		InputStreamReader isr = new InputStreamReader(fis,"gbk");
		BufferedReader br = new BufferedReader(isr);

		String reversedOccFile_RF = reversedOccFile + "_RF";
		FileOutputStream fos = new FileOutputStream(reversedOccFile_RF);
		OutputStreamWriter osr = new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw = new BufferedWriter(osr);

		RightEntropyGenerator aRightEntropyGenerator = new RightEntropyGenerator(
				br,maxGram);

		System.out.println("Generating... RightEntropy  of Reversed Occ File ....");
		String aline = null;
		int i = 0;
		while ((aline = aRightEntropyGenerator.generateLine()) != null) {

			if (i % 100000 == 0)
				System.out.println("Generate RightEntropy Of ReversedGram:  generateLine " + i + ".");
			bw.append(aline + "\n");
			i++;

		}
		bw.flush();
		
		
		System.out.println("Load RightEntropy Of ReversedOcc to memory ....");
		
		Map<String, Double> gramLeftFreedomMap = new TreeMap<String, Double>();

		FileInputStream fis_r = new FileInputStream(reversedOccFile_RF);
		InputStreamReader isr_r = new InputStreamReader(fis_r,"gbk");
		BufferedReader br_r = new BufferedReader(isr_r);
		i = 0;
		while ((aline = br_r.readLine()) != null) {

			if (i % 100000 == 0)
				System.out.println("Load RightEntropy Of ReversedGram:  load " + i + ".");

			String[] terms = aline.split("\t");
			String reversedGram = terms[0];
			
			//just reverse hanzi
			String gram = nGramSpliter.reverseCN_zh(reversedGram);
			Double lFreedom = Double.parseDouble(terms[2]);

			gramLeftFreedomMap.put(gram, lFreedom);
			i++;

		}
		
		
		
		FileInputStream fis_input = new FileInputStream(occFile);
		InputStreamReader isr_input = new InputStreamReader(fis_input,"gbk");
		BufferedReader br_input = new BufferedReader(isr_input);
		
		FileOutputStream fos_output = new FileOutputStream(outputFile);
		OutputStreamWriter osr_output = new OutputStreamWriter(fos_output,"gbk");
		BufferedWriter bw_output = new BufferedWriter(osr_output);
		
		i=0;
		while ((aline = br_input.readLine()) != null) {

			if (i % 100000 == 0)
				System.out.println("Write LeftEntropy Of Gram:  write " + i + ".");

			String[] terms = aline.split("\t");
			String gram = terms[0];
			
			double lFreedom=gramLeftFreedomMap.get(gram);
			
			String newLine=aline+"\t"+lFreedom;
			bw_output.append(newLine+"\n");
			
			i++;

		}
		br_input.close();
		bw_output.close();
		

	}
	public void processFast(int maxCounterMap,int counterThreshold,String occFile, String gramLFPath,int maxGram) throws IOException {
		
		System.out.println("Reversing....  Occ File ");
		String reversedOccFile =occFile+"_Reversed";
		
		GramOccCounterMergeBased.reverseGramFile( maxCounterMap,occFile, reversedOccFile);
		
		FileInputStream fis = new FileInputStream(reversedOccFile);
		InputStreamReader isr = new InputStreamReader(fis,"gbk");
		BufferedReader br = new BufferedReader(isr);

		String reversedOccFile_RF = reversedOccFile + "_RF";
		FileOutputStream fos = new FileOutputStream(reversedOccFile_RF);
		OutputStreamWriter osr = new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw = new BufferedWriter(osr);

		RightEntropyGenerator aRightEntropyGenerator = new RightEntropyGenerator(br,maxGram);

		System.out.println("Generating... RightEntropy  of Reversed Occ File ...., reversedOccFile_RF:"+reversedOccFile_RF);
		String aline = null;
		int i = 0;
		while ((aline = aRightEntropyGenerator.generateLine(counterThreshold)) != null) {

			if (i % 100000 == 0)
			{
				System.out.println("Generate RightEntropy Of ReversedGram:  generateLine " + i + ".");
			}
			
			bw.append(aline + "\n");
			i++;

		}
		bw.flush();	
		
		System.out.println("reverse GramFile ...., reversedOccFile_RF:"+reversedOccFile_RF);
		
		GramOccCounterMergeBased.reverseGramFile( maxCounterMap,reversedOccFile_RF, gramLFPath);
		
		br.close();
		bw.close();
		
		

	}

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {

			System.out
					.println("usage:AddLeftFreedomDegree	SortedOccFile   OuputFile");

			System.out
					.println("                       	SortedOccFile	(input SortedOccCounterFile )");
			System.out
					.println("                       	OuputFile	( output occ file whith LeftFreedom)");

			System.out.println("Example: ");
			System.out.println("   AddLeftFreedomDegree  GramOcc  GramOcc_LF ");
			System.out.println("	");

			return;
		}

		String sortedOccFile = args[0];
		String ouputFile = args[1];

		AddLeftFreedomDegree aAddLeftFreedomDegree = new AddLeftFreedomDegree();
		aAddLeftFreedomDegree.process(sortedOccFile, ouputFile,4);

	}
}
