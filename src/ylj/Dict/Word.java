package ylj.Dict;

public class Word implements Comparable<Word>{
	
	char value;			
	
	char[] PinYin;   //×ÖÄ¸
	char ShengDiao; //0 1 2 3 4
	
	
	
	@Override
	public int compareTo(Word o) {
		// TODO Auto-generated method stub
		
		if(o==null)
			return 1;
		
		
		if(value>o.value)
			return 1;
		else if(value<o.value)
			return -1;
		return 0;
	}
	
	public String toString(){
		StringBuffer aStringBuffer=new StringBuffer();
		aStringBuffer.append("["+value+",");
		aStringBuffer.append(PinYin);
		aStringBuffer.append(","+ShengDiao);
		aStringBuffer.append("]");
		return aStringBuffer.toString();
	}
	
}