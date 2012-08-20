package ylj.TopicModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DocVector {
	long docID;
	Map<Long,Long> elements=new HashMap<Long,Long>();
	
	public DocVector(long docIDValue){
		docID=docIDValue;
	}
	public void addElement(long vocabIndex){
		long old=0;
		Long counter=elements.get(vocabIndex);
		if(counter!=null)
			old=counter;
		elements.put(vocabIndex, old+1);
	}
	public String toString(){
		StringBuilder aStringBuilder=new StringBuilder();
		
		
		aStringBuilder.append(elements.size());
		for(Entry<Long, Long> entry:elements.entrySet())
		{
			aStringBuilder.append(" "+entry.getKey()+":"+entry.getValue());
		}
		return aStringBuilder.toString();
		
	}
}
