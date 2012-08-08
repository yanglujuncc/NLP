package ylj.Dict;

import java.io.IOException;
import java.util.List;

public class SogouFileDictReader extends StandardFileDictReader{

	@Override
	public Term line2Term(String aline) throws IOException {
		
		
		if(aline!=null){
			
			//词典 格式 ：阿昌族	a1'chang1'zu2	50849
		
			String[] fields=aline.split("( |\\t)");
			
		//	System.out.print(Arrays.toString(fields));
		//	System.out.print(">>>  ");
			
			String words=fields[0].replace("，","");
			words=words.replace(",", "");
			words=words.replace(" ", "");
			words=words.replace("·", "");
		
			
			Term newTerm=new Term();
			newTerm.words=new Word[words.length()];
			for(int i=0;i<words.length();i++)
			{
				try{
					Word newWord=new Word();
					newWord.value=words.charAt(i);		
					
					
					newTerm.words[i]=newWord;
					//System.out.print(newWord.toString());	
				}
				catch( Exception e){
					
					System.out.println(aline);
					System.out.println(words);
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
		
		SogouFileDictReader aFileDictReader=new SogouFileDictReader();
		MemDict aMemDict=new MemDict();
		
		try {
			//aFileDictReader.addDictFile("SogouLabDic.dic");
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
		
		
		System.out.println("Dict termCount="+aMemDict.getTermsNum());
		
		List<Term>  alist=aMemDict.getContains("大");
		
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



