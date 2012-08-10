package ylj.NGram;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ylj.Segmentation.SimpleWordSegmentater;
import  ylj.Util.StringRecognizer;

public class NGramSpliter implements Serializable{
	private int N;
	private Map<String,Integer> counterMap=new HashMap<String,Integer>();
	
	
	String subSentenceSplitSign_reg = "((#((\\d)|(\\w))*)|,|\\.|;|!|\\?|~| |·|，|、|。|；|！|？|\t|\\+|　)+";
	Pattern aSplitPattern=Pattern.compile(subSentenceSplitSign_reg);
	//SimpleWordSegmentater wordSegmentater=new SimpleWordSegmentater();
	
	
	public NGramSpliter(int n){
		N=n;
	}
	public int getGramN(){
		return N;
	}
	public void addCounter(String term){
		
		int counter=0;
		Integer counterObj=counterMap.get(term);
		if(counterObj!=null)
			counter=counterObj+counter;
		counterMap.put(term, counter);
		
		
	}
	public List<String> splite(String str){
		if(str==null)
			return null;
		List<String> instanceList=new LinkedList<String>();
		List<String> preWords=new LinkedList<String>();
		
		String[] subSentences= aSplitPattern.split(str);
	
		
		for(String sentence:subSentences){
			preWords.clear();
			//得到单个汉字 或  英文单词
			List<String> words=SimpleWordSegmentater.makeSegmentGlobal(sentence);
			
			for(String word:words){
				if(StringRecognizer.isCN_zhWord(word))
				{
					preWords.add(word);
					if(preWords.size()==N)
					{
						StringBuilder term =new StringBuilder();
						for(String preWord:preWords)
							term.append(preWord);
						
						//System.out.println("instance="+term.toString());
						instanceList.add(term.toString());
						
						preWords.remove(0);
					}
					
				}
				
				else if(StringRecognizer.isEnWord(word))
				{
					instanceList.add(word.toString());
					preWords.clear();
				}
				
				else
				{
					preWords.clear();
				}
			}
			
		}
		return instanceList;
		
	}
	
	public static void main(String[] args) throws Exception {
		

		

		NGramSpliter aNGramCounter=new NGramSpliter(2);
		String input="boss路过乌龟旁fuck边 刚好乌1龟放群 你们就#213死了";
		List<String>  instances=aNGramCounter.splite(input);
		System.out.println(input);
		for(String instance:instances){
			
			System.out.println(instance);
		}
		
	
	}
}
