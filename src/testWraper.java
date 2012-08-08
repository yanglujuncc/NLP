import java.util.List;
import ylj.Segmentation.StanfordSegmenterWraper;

public class testWraper {
	public static void main(String[] args) throws Exception {
		 
		StanfordSegmenterWraper aStanfordSegmenter=new StanfordSegmenterWraper();
		

	 
	    
	    String inputStr="15万81级经验书7万85级经验书10万要的窗口";
	    
	    List<String> terms=aStanfordSegmenter.makeSegment(inputStr);
	    System.out.println(inputStr);
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println();
	    
	  }
}
