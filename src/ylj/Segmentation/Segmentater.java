package ylj.Segmentation;

import java.util.List;

public interface Segmentater {
	
	List<String> makeSegment(String sentence)throws Exception;
}
