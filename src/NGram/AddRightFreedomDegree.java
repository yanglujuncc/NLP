package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AddRightFreedomDegree {
	
	public void process(String inputFile,String outputFile,int maxGram) throws IOException
	{
		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis,"gbk");
		BufferedReader br = new BufferedReader(isr);

		
		FileOutputStream fos= new FileOutputStream(outputFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw = new BufferedWriter(osr);

		RightEntropyGenerator aRightEntropyGenerator=new RightEntropyGenerator(br,maxGram);
		
		String aline = null;
		int i = 0;
		while ((aline =aRightEntropyGenerator.generateLine()) != null) {
			
			if(i%100000==0)
				System.out.println("Generate RightEntropy Of Gram:  write " + i + ".");
			bw.append(aline+"\n");
			i++;

		}
		
		bw.flush();
		bw.close();
		br.close();
	}
	
	public void processFast(int counterThreshold,String inputFile,String outputFile,int maxGram) throws IOException
	{
		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis,"gbk");
		BufferedReader br = new BufferedReader(isr);

		
		FileOutputStream fos= new FileOutputStream(outputFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw = new BufferedWriter(osr);

		RightEntropyGenerator aRightEntropyGenerator=new RightEntropyGenerator(br,maxGram);
		
		String aline = null;
		int i = 0;
		while ((aline =aRightEntropyGenerator.generateLine(counterThreshold)) != null) {
			
			if(i%100000==0)
				System.out.println("Generate RightEntropy Of Gram:  write " + i + ".");
			bw.append(aline+"\n");
			i++;

		}
		
		bw.flush();
		bw.close();
		br.close();
	}
	
	public static void main(String[] args) throws IOException
	{
	
		if (args.length != 2) {
			System.out.println("Add Increment To Model or Train A New Model  ");
			System.out.println("usage:RightFreedomDegree	SortedOccFile   OuputFile");

			System.out.println("                       	SortedOccFile	(input SortedOccCounterFile )");
			System.out.println("                       	OuputFile	(number of store file  (MapNum) )");
		
			System.out.println("Example: ");
			System.out.println("   GramOccCounter  GramOcc  GramOcc_R ");
			System.out.println("	");

			return;
		}
		
		String sortedOccFile=args[0];
		String ouputFile=args[1];
		
		AddRightFreedomDegree aAddRightFreedomDegree=new AddRightFreedomDegree();
		aAddRightFreedomDegree.process(sortedOccFile, ouputFile,4);
	
	}

}
