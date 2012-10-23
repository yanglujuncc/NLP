package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RecordMerger {

	
	
	public void process(String input_LF,String input_RF,String input_Cohesion,String outputFile) throws IOException
	{
		
		FileInputStream fis_LF = new FileInputStream(input_LF);
		InputStreamReader isr_LF = new InputStreamReader(fis_LF,"gbk");
		BufferedReader br_LF = new BufferedReader(isr_LF);

		FileInputStream fis_RF = new FileInputStream(input_RF);
		InputStreamReader isr_RF = new InputStreamReader(fis_RF,"gbk");
		BufferedReader br_RF = new BufferedReader(isr_RF);

		FileInputStream fis_Cohesion = new FileInputStream(input_Cohesion);
		InputStreamReader isr_Cohesion = new InputStreamReader(fis_Cohesion,"gbk");
		BufferedReader br_Cohesion = new BufferedReader(isr_Cohesion);


		FileOutputStream fos= new FileOutputStream(outputFile);
		OutputStreamWriter osr = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osr);

	
		
		String aline_LF = null;
		String aline_RF = null;
		String aline_Cohesion = null;
		
		int i = 0;
		while ((aline_LF =br_LF.readLine()) != null) {
			
			
			
			String lf_str=aline_LF.split("\t")[2];
			double lf=Double.parseDouble(lf_str);
			
			aline_RF =br_RF.readLine();
			String rf_str=aline_RF.split("\t")[2];
			double rf=Double.parseDouble(rf_str);
			
			double f=lf<rf?lf:rf;
			
			aline_Cohesion =br_Cohesion.readLine();
			//String IMPMI_str=aline_IMPMI.split(":")[2];
			
			
			if(i%100000==0)
				System.out.println("Merge Record: merged "+i+" records.");
			
			
			String combineLine=aline_Cohesion+"\t"+f;
			bw.append(combineLine+"\n");
			i++;

		}
		br_LF.close();
		br_RF.close();
		br_Cohesion.close();
		
		bw.flush();
		bw.close();
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
	
		if (args.length != 4) {
			
			System.out.println("usage:RecordMerger	RF LF Cohesion MergedFile");

			System.out.println("                       	RF	  (input OccFile with RightFreedomDegree )");
			System.out.println("                       	LF	  (input OccFile with LeftFreedomDegree )");
			System.out.println("                       	Cohesion (input OccFule with GramCohesion)");
			System.out.println("                       	OuputRecordFile	( )");
		
			System.out.println("   OuputRecord gram:counter:CombinedDegree:FreedomDegree " );

			return;
		}
		
		String RF=args[0];
		String LF=args[1];
		String Cohesion=args[2];
		String output=args[3];
		
		RecordMerger aRecordMerger=new RecordMerger();
		aRecordMerger.process(RF, LF,Cohesion,output);
	
	}
}
