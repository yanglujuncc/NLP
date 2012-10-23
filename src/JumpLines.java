import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JumpLines {

	
	public static void main(String[] args) throws Exception {
		
		
		if(args.length!=4)
		{
			System.out.println("JumpLines ");
			System.out.println("usage:JumpLines	acceptNUm spiltNum inputFile outputFile");
			
			System.out.println("                       	inputFile	(path of the inputFile)");
			System.out.println("                       	outputFile	(path of the outputFile)");
			
			return ;
		}
		int JumpNum=Integer.parseInt(args[0]);
		int SpiltNum=Integer.parseInt(args[1]);
		String inputFilePath=args[2];
		String outputFilePath=args[3];
		
		FileInputStream fis=new FileInputStream(inputFilePath);	
		InputStreamReader isr=new InputStreamReader(fis,"gbk");		
		BufferedReader reader=new BufferedReader(isr);

		FileOutputStream fos=new FileOutputStream(outputFilePath);
		OutputStreamWriter osw=new OutputStreamWriter(fos,"gbk");
		BufferedWriter bw=new BufferedWriter(osw);
		
		String line=null;
		long counter=0;
		int now=0;

		while((line=reader.readLine())!=null)
		{
			if(counter%100000==0)
				System.out.println("prcessed "+counter+"lines");
			now++;
			counter++;
			
			if(now<=JumpNum)
			{
				bw.append(line+"\n");
			}
			if(now==SpiltNum)
			{
				now=0;
			}
			
		}
		reader.close();
		bw.close();
	
	}
}
