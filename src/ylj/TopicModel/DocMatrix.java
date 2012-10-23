package ylj.TopicModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ylj.Util.HashIndex;
import ylj.Util.Index;

public class DocMatrix {

	
		Index<String> docNameIndexer=new HashIndex<String>();
		Index<String> vocabIndexer=new HashIndex<String>();
		TreeMap<Integer,Integer> vocabDF=new TreeMap<Integer,Integer>();
		
		Map<Integer,DocVector> docMap=new HashMap<Integer,DocVector>();
		
		public long addDoc(String docName,List<String> vocabs)
		{
			
			TreeMap<Integer,Integer> counterMap=new TreeMap<Integer,Integer>();
		
			for(String vocab:vocabs){
				
				int vocabID=vocabIndexer.indexOf(vocab, true);
				Integer counter=counterMap.get(vocabID);
				if(counter==null)
					counterMap.put(vocabID,1);
				else
					counterMap.put(vocabID, counter+1);
				
				
			}
			
			int docID=docNameIndexer.indexOf(docName, true);	
			
			int[] vocabIDArray=new int[counterMap.size()];
			int[] counterArray=new int[counterMap.size()];
			
			int i=0;
			for(Entry<Integer, Integer> entry:counterMap.entrySet()){
				
				vocabIDArray[i]=entry.getKey();
				counterArray[i]=entry.getValue();
				
				//upate vocab DF 
				Integer counter=vocabDF.get(vocabIDArray[i]);
				if(counter==null)
					vocabDF.put(vocabIDArray[i],1);
				else
					vocabDF.put(vocabIDArray[i], counter+1);
				
				i++;
			}
			
			
			DocVector docVector=docMap.get(docID);
			if(docVector==null)
			{
				docVector=new DocVector(docID);
				docMap.put(docID, docVector);
			}
			
			docVector.setElement(vocabIDArray, counterArray);
			
			
			
			return docID;
			
		}
	
}
