package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GramFreedomRecordMerger {

	
	public void process(String input_LF,String input_RF,String outputFile) throws IOException
	{
		
		FileInputStream fis_LF = new FileInputStream(input_LF);
		InputStreamReader isr_LF = new InputStreamReader(fis_LF,"gbk");
		BufferedReader br_LF = new BufferedReader(isr_LF);

		FileInputStream fis_RF = new FileInputStream(input_RF);
		InputStreamReader isr_RF = new InputStreamReader(fis_RF,"gbk");
		BufferedReader br_RF = new BufferedReader(isr_RF);

		FileOutputStream fos= new FileOutputStream(outputFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osr);

	
		
		String aline_LF = null;
		String aline_RF = null;
		
		int i = 0;
		while ((aline_LF =br_LF.readLine()) != null) {
			
			
			String[] terms=aline_LF.split("\t");
			String gram=terms[0];
			long counter=Long.parseLong(terms[1]);
			
			String lf_str=aline_LF.split("\t")[2];
			double lf=Double.parseDouble(lf_str);
			
			aline_RF =br_RF.readLine();
			String rf_str=aline_RF.split("\t")[2];
			double rf=Double.parseDouble(rf_str);
			
			double f=lf<rf?lf:rf;
			
			
			if(i%100000==0)
				System.out.println("Merge Freedom Record: merged "+i+" records.");
			
			String combineLine=gram+"\t"+counter+"\t"+f;
			
			bw.append(combineLine+"\n");
			i++;

		}
		br_LF.close();
		br_RF.close();
		
		bw.flush();
		bw.close();
		
		
	}
	public void processFast(double freedomThreshold,String input_LF,String input_RF,String outputFile) throws IOException
	{
		
		FileInputStream fis_LF = new FileInputStream(input_LF);
		InputStreamReader isr_LF = new InputStreamReader(fis_LF,"gbk");
		BufferedReader br_LF = new BufferedReader(isr_LF);

		FileInputStream fis_RF = new FileInputStream(input_RF);
		InputStreamReader isr_RF = new InputStreamReader(fis_RF,"gbk");
		BufferedReader br_RF = new BufferedReader(isr_RF);

		FileOutputStream fos= new FileOutputStream(outputFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osr);

	
		
		String aline_LF = null;
		String aline_RF = null;
		
		int i = 0;
		while ((aline_LF =br_LF.readLine()) != null) {
			
			i++;
			
			String[] terms=aline_LF.split("\t");
			String gram_lf=terms[0];
			String counterStr=terms[1];
			String lf_str=terms[2];
			double lf=Double.parseDouble(lf_str);
			
			aline_RF =br_RF.readLine();
			
			terms=aline_RF.split("\t");
			String gram_rf=terms[0];
			String rf_str=terms[2];
			double rf=Double.parseDouble(rf_str);
			
			if(!gram_lf.equals(gram_rf))
			{
				System.err.println("Gram_RF:"+gram_rf+" != "+"Gram_LF:"+gram_lf);
				System.exit(1);
			}
			double f=lf<rf?lf:rf;
			
			if(f<freedomThreshold)
				continue;
			
			if(i%100000==0)
				System.out.println("Merge Freedom Record: merged "+i+" records.");
			
			String combineLine=gram_lf+"\t"+counterStr+"\t"+f;
			
			bw.append(combineLine+"\n");
			

		}
		br_LF.close();
		br_RF.close();
		
		bw.flush();
		bw.close();
		
		
	}
	public static void main(String[] args) throws IOException
	{
	
		if (args.length != 3) {
			
			System.out.println("usage:RecordMerger	RF LF Cohesion MergedFile");

			System.out.println("                       	RF	  (input OccFile with RightFreedomDegree )");
			System.out.println("                       	LF	  (input OccFile with LeftFreedomDegree )");
			System.out.println("                       	OuputRecordFile	( )");
		
			System.out.println("   OuputRecord gram:counter:CombinedDegree:FreedomDegree " );

			return;
		}
		
		String RF=args[0];
		String LF=args[1];
		String output=args[2];
		
		GramFreedomRecordMerger aGramFreedomRecordMerger=new GramFreedomRecordMerger();
		aGramFreedomRecordMerger.process(RF, LF,output);
	
	}
}
