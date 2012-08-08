package ylj.Segmentation;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class StanfordSegmenterWraper implements Segmentater{

	CRFClassifier<CoreLabel> classifier;
	
	public StanfordSegmenterWraper(){
			Properties props = new Properties();
		    props.setProperty("sighanCorporaDict", "data");  //1
		    props.setProperty("serDictionary","data/dict-chris6.ser.gz");  //2
		    
		    props.setProperty("sighanPostProcessing", "true");

		    classifier = new CRFClassifier<CoreLabel>(props);
		    classifier.loadClassifierNoExceptions("data/ctb.gz", props);
		    // flags must be re-set after data is loaded
		    classifier.flags.setProperties(props);
	}
	
	@Override
	public List<String> makeSegment(String sentence) {
		// TODO Auto-generated method stub
		 
		if(sentence==null)
			return null;
		List<String> terms=classifier.segmentString(sentence);
		 
		return  terms;
	}

}
