import java.util.List;
import ylj.Segmentation.StanfordSegmenterWraper;

public class testWraper {
	public static void main(String[] args) throws Exception {
		 
		StanfordSegmenterWraper aStanfordSegmenter=new StanfordSegmenterWraper();
		

	 
	    
	    String inputStr="15��81��������7��85��������10��Ҫ�Ĵ���";
	    
	    List<String> terms=aStanfordSegmenter.makeSegment(inputStr);
	    System.out.println(inputStr);
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println();
	    
	  }
}
