package ylj.Segmentation;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class testICTCLASSegmenterWraper {

	public static void main(String[] args) throws Exception {
		 
		  
		System.out.println(args.length);
	   
		ICTCLASSegmenterWraper aICTCLASSegmenterWraper=new ICTCLASSegmenterWraper();
		String inputStr="异人方士甲什么的都有好武器，就是魅和医生的武器功都不高";
	    List<String> terms=aICTCLASSegmenterWraper.makeSegment(inputStr);
	  
	    System.out.println(inputStr);
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println();
	    
	  }
}
