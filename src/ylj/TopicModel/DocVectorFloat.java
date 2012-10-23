package ylj.TopicModel;

public class DocVectorFloat {

	
	int docID;
	int[] indexArray;
	float[] valueArray;
	
	public DocVectorFloat(int docIDValue){
		docID=docIDValue;
	}
	
	public void setElement(int[] indexs,float[] values){
		
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