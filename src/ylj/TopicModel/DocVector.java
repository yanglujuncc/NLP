package ylj.TopicModel;

public class DocVector {
	
	int docID;
	int[] indexArray;
	int[] valueArray;
	
	public DocVector(int docIDValue){
		docID=docIDValue;
	}
	
	public void setElement(int[] indexs,int[] values){
		
		indexArray=indexs;
		valueArray=values;
	}
	
	public String toString(){
		
		StringBuilder aStringBuilder=new StringBuilder();
		
		aStringBuilder.append("DocID:"+docID+",");
		for(int i=0;i<indexArray.length;i++)
		{
			aStringBuilder.append(" "+indexArray[i]+":"+valueArray[i]);
		}
		return aStringBuilder.toString();
		
	}
	
}
