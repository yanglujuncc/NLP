import java.util.List;

import ylj.Segmentation.ICTCLASSegmenterWraper;
import ylj.Segmentation.MaxForwardSegmentater;
import ylj.Segmentation.StanfordSegmenterWraper;


public class testAllSegmenter {
	public static void main(String[] args) throws Exception {
		 
		StanfordSegmenterWraper aStanfordSegmenter=new StanfordSegmenterWraper();
		
		ICTCLASSegmenterWraper aICTCLASSegmenterWraper=new ICTCLASSegmenterWraper();
		
		MaxForwardSegmentater aMaxForwardSegmentater=new MaxForwardSegmentater("Dic");
	 
		
	    
	    String inputStr="嗯嗯。5285785。来YY。来哇。好的 。马上";
	    System.out.println(inputStr);
	    System.out.println("******************************");
	    long start=System.currentTimeMillis();
	    List<String> terms=aStanfordSegmenter.makeSegment(inputStr);
	    long end=System.currentTimeMillis();
	    System.out.print("Stanford:");
	   
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println("   cost "+(end-start)+"'ms");
	    
	    start=System.currentTimeMillis();
	    terms=aICTCLASSegmenterWraper.makeSegment(inputStr);
	    end=System.currentTimeMillis();
	    System.out.print("ICTCLAS:");
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println("   cost "+(end-start)+"'ms");
	    
	    start=System.currentTimeMillis();
	   terms=aMaxForwardSegmentater.makeSegment(inputStr);
	   end=System.currentTimeMillis();
	    System.out.print("MaxForward:");
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println("   cost "+(end-start)+"'ms");
	  }
}
