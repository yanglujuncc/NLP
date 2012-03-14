import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.Lexeme;

public class testIKAnalyzer {
	
	public static void main(String[] agvs) {
		try {
			File aFile = new File("txt.txt");
			FileInputStream fis;

			fis = new FileInputStream(aFile);

			InputStreamReader isr;
			StringReader stingReader=new StringReader("我500找对刷桃花去50了，你忙");
			
			isr = new InputStreamReader(fis, "gbk");
			
			IKSegmentation ikSegmentation = new IKSegmentation(stingReader,true);
			Lexeme tempLexeme;
			System.out.print("|");
			long begin=System.currentTimeMillis();
			while((tempLexeme=ikSegmentation.next())!=null)
			{
				System.out.print(tempLexeme.getLexemeText()+"|");
			}
			long end=System.currentTimeMillis();
			System.out.print("time cost="+(end-begin)+"'ms");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	

	}

}
