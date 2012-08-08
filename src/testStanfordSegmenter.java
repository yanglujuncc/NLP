import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;


public class testStanfordSegmenter {
	public static void main(String[] args) throws Exception {
		 
		  
		System.out.println(args.length);
	   

	    Properties props = new Properties();
	    props.setProperty("sighanCorporaDict", "data");  //1
	    props.setProperty("serDictionary","data/dict-chris6.ser.gz");  //2
	    
	    props.setProperty("sighanPostProcessing", "true");

	    CRFClassifier<CoreLabel> classifier = new CRFClassifier<CoreLabel>(props);
	    classifier.loadClassifierNoExceptions("data/ctb.gz", props);
	    // flags must be re-set after data is loaded
	    classifier.flags.setProperties(props);
	 
	    //classifier.classifyAndWriteAnswers(args[0]);
	    String inputStr="异人方士甲什么的都有好武器，就是魅和医生的武器功都不高";
	    List<String> terms=classifier.segmentString(inputStr);
	  
	    System.out.println(inputStr);
	    for(String term:terms){
	    	 System.out.print(term+"|");
	    }
	    System.out.println();
	    
	  }
}
