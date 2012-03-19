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
		
		File mapFile=new File("E:\\┬ы▒э\\result_gbk\\result_gbk.TXT");		
		FileInputStream fis=new FileInputStream(mapFile);	
		InputStreamReader isr=new InputStreamReader(fis,"unicode");		
		BufferedReader reader=new BufferedReader(isr);
		
		File noSTmapFile=new File("E:\\┬ы▒э\\result_gbk\\noSTresult_gbk.TXT");
		FileOutputStream fos=new FileOutputStream(noSTmapFile);
		OutputStreamWriter osr=new OutputStreamWriter(fos); 
	
		
		String shengdiao_a="aибивигид";
		String shengdiao_o="oиниоипи░";
		String shengdiao_e="eиеижизии";
		String shengdiao_i="iийикилим";
		String shengdiao_u="uи▒и▓и│и┤";
		String shengdiao_v="и╣и╡и╢и╖и╕";
		
		
		String line=null;
		
		while((line=reader.readLine())!=null)
		{
			String newLine=line;
			System.out.println(line);
			newLine=newLine.replaceAll("[ибивигид]", "a");
			newLine=newLine.replaceAll("[иниоипи░]", "o");
			newLine=newLine.replaceAll("[иеижизии]", "e");
			newLine=newLine.replaceAll("[ийикилим]", "i");
			newLine=newLine.replaceAll("[и▒и▓и│и┤]", "u");
			newLine=newLine.replaceAll("[и╣и╡и╢и╖и╕]", "v");
			
			osr.append(newLine+"\r\n");
			
		}
		osr.flush();
		osr.close();
	}
}
