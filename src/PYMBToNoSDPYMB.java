import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class PYMBToNoSDPYMB {
	
	public static void main(String[] agvs) throws IOException {
		
		File mapFile=new File("E:\\���\\result_gbk\\result_gbk.TXT");		
		FileInputStream fis=new FileInputStream(mapFile);	
		InputStreamReader isr=new InputStreamReader(fis,"unicode");		
		BufferedReader reader=new BufferedReader(isr);
		
		File noSTmapFile=new File("E:\\���\\result_gbk\\noSTresult_gbk.TXT");
		FileOutputStream fos=new FileOutputStream(noSTmapFile);
		OutputStreamWriter osr=new OutputStreamWriter(fos); 
	
		
		String shengdiao_a="a��������";
		String shengdiao_o="o��������";
		String shengdiao_e="e��������";
		String shengdiao_i="i��������";
		String shengdiao_u="u��������";
		String shengdiao_v="����������";
		
		
		String line=null;
		
		while((line=reader.readLine())!=null)
		{
			String newLine=line;
			System.out.println(line);
			newLine=newLine.replaceAll("[��������]", "a");
			newLine=newLine.replaceAll("[��������]", "o");
			newLine=newLine.replaceAll("[��������]", "e");
			newLine=newLine.replaceAll("[��������]", "i");
			newLine=newLine.replaceAll("[��������]", "u");
			newLine=newLine.replaceAll("[����������]", "v");
			
			osr.append(newLine+"\r\n");
			
		}
		osr.flush();
		osr.close();
	}
}
