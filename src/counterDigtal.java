import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class counterDigtal {

	

	
	
	public static void main(String[] args) throws Exception {
		
		String dig_reg="\\d+";
		Pattern digPattern=Pattern.compile(dig_reg);
		Matcher digMatcher=digPattern.matcher("");
		
		
		
		String inputFilePath="E:/workspace/NLP/Model_2012-10-16/3_GramVectors";
		File file=new File(inputFilePath);
		System.out.println("        file.length:"+file.length());
		System.out.println(" file.getTotalSpace:"+file.getTotalSpace());
		System.out.println("file.getUsableSpace:"+file.getUsableSpace());
		System.out.println("  file.getFreeSpace:"+file.getFreeSpace());
		FileInputStream fis=new FileInputStream(inputFilePath);	
		InputStreamReader isr=new InputStreamReader(fis,"gbk");		
		BufferedReader reader=new BufferedReader(isr);

		//	FileOutputStream fos=new FileOutputStream(outputFilePath);
		//	OutputStreamWriter osw=new OutputStreamWriter(fos,"gbk");
		//	BufferedWriter bw=new BufferedWriter(osw);
		
		String line=null;
		
		byte b=1;
	
		int bs=b<<2;
		
		long counter_1=0;
		long counter_2=0;
		long counter_3=0;
		long counter_4=0;
		long counter_other=0;
		
		long counter=0;
		long counter_line=0;
		while((line=reader.readLine())!=null)
		{
			if(counter_line%10000==0)
				System.out.println("prcessed "+counter_line+"lines");
			counter_line++;
			//System.out.println(line);
			
			digMatcher.reset(line);
			
			while(digMatcher.find()){
				
				String dig_str=digMatcher.group();
				counter++;
				//System.out.println(dig_str);
				if(dig_str.equals("1")){
					counter_1++;
				}
				else if(dig_str.equals("2")){
					counter_2++;
				}
				else if(dig_str.equals("3")){
					counter_3++;
				}
				else if(dig_str.equals("4")){
					counter_4++;
				}
				else {
					counter_other++;
				}
					
			
				
			}
			
						
		}
		System.out.println("counter_line:"+counter_line);
		System.out.println("counter:"+counter);
		
		System.out.println("counter_1:"+counter_1+" "+"counter_2:"+counter_2+" "+"counter_3:"+counter_3+" "+"counter_4:"+counter_4+" "+"counter_other:"+counter_other+" ");
		
		System.out.println("counter_1:"+(counter_1*100)/counter+"% "+"counter_2:"+(counter_2*100)/counter+"% "+"counter_3:"+(counter_3*100)/counter+"% "+"counter_4:"+(counter_4*100)/counter+"% "+"counter_other:"+(counter_other*100)/counter+"% ");
		
		reader.close();
	
	
	}
}
