package ylj.Dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StandardDictReader implements FileDictReader{

	List<File> dictFiles=new LinkedList<File>();
	int fileIndex=0;
	
	BufferedReader reader;
	
	
	
	/*
	 *  输入 类似 ji2
	 *  输出 ji
	 *  带声调的拼音 或者不带 
	 */
	private char[] getRawPinyin(String pinyin){
		
		if(pinyin==null)
			return null;
		if(pinyin.endsWith("1")||pinyin.endsWith("2")||pinyin.endsWith("3")||pinyin.endsWith("4"))
			return pinyin.substring(0,(pinyin.length()-1)).toCharArray();
		else
			return pinyin.toCharArray();
		
	}
	/**
	 * 得到声调
	 * @param pinyin
	 * @return
	 */
	private char getShenDiao(String pinyin){
		if(pinyin==null)
			return '0';
		if(pinyin.endsWith("1")||pinyin.endsWith("2")||pinyin.endsWith("3")||pinyin.endsWith("4"))
			return pinyin.charAt(pinyin.length()-1);
		else
			return '0';
	}
	
	
	@Override
	public void addDictFile(String filePath) throws IOException{
		
		File aFile=new File(filePath);
		
		if(aFile.exists())
		{
			dictFiles.add(aFile);
			if(dictFiles.size()==1&&fileIndex==0)
			{
				FileInputStream is=new FileInputStream(dictFiles.get(fileIndex++));
				InputStreamReader isr=new InputStreamReader(is,"gbk");
				reader=new BufferedReader(isr);
			}
		}
		else
		{
			System.err.println("FILE:"+filePath+" Don't Exist.");
		}
			
	}
	
	@Override
	public Term readTerm() throws IOException {
		
		while(reader!=null){
			
			String aline=reader.readLine();
			if(aline==null){
				
				if(fileIndex==dictFiles.size())
					return	null;
					
				FileInputStream is=new FileInputStream(dictFiles.get(fileIndex++));
				InputStreamReader isr=new InputStreamReader(is,"gbk");
				reader=new BufferedReader(isr);
				continue;
			}
			else{
				Term aTerm= line2Term(aline);
				return aTerm;
			}
		}
		
		return null;
	}
	
	@Override
	public Term line2Term(String aline) throws IOException {
		// TODO Auto-generated method stub
		
	
		if(aline!=null){
			
			//词典 格式 ：阿昌族	a1'chang1'zu2	50849
		
			String[] fields=aline.split("( |\\t)");
			
		//	System.out.print(Arrays.toString(fields));
		//	System.out.print(">>>  ");
			
			String words=fields[0].replace("，","");
			words=words.replace(",", "");
			words=words.replace(" ", "");
			words=words.replace("·", "");
			
			String pinyins=fields[1].replace(" ", "");
			String[] pinyinArray=pinyins.split("'");
			
			
			String other=fields[2].replace(" ", "");
			
			Term newTerm=new Term();
			newTerm.words=new Word[words.length()];
			for(int i=0;i<words.length();i++)
			{
				try{
					Word newWord=new Word();
					newWord.value=words.charAt(i);		
					newWord.PinYin=getRawPinyin(pinyinArray[i]);
					newWord.ShengDiao=getShenDiao(pinyinArray[i]);
					
					newTerm.words[i]=newWord;
					//System.out.print(newWord.toString());	
				}
				catch( Exception e){
					
					
					System.out.println(words);
					System.out.println(pinyins);
					System.out.print("\n");
					e.printStackTrace();
					System.exit(1);
				}
				
			}
			
		//	System.out.println("  ");
			return newTerm;
			
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		
		StandardDictReader aFileDictReader=new StandardDictReader();
		MemDict aMemDict=new MemDict();
		
		try {
			aFileDictReader.addDictFile("XianDaiHanYu.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		while(true){
			Term aTerm=null;
			try {
				aTerm = aFileDictReader.readTerm();
				aMemDict.addTerm(aTerm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(aTerm==null)
				break;
		}
		
		List<Term>  alist=aMemDict.getContains("硬");
		if(alist==null)
		{
			System.out.println("alist==null");
			
		}
		else{
			for(Term aTerm:alist){
				System.out.println(aTerm.toString());
			
			}
		}
		
		
	}
	

}
