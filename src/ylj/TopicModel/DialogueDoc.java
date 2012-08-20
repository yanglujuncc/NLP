package ylj.TopicModel;

import java.text.SimpleDateFormat;

public class DialogueDoc {

	
	static long MaxIntervalTime=300000;
	
	static  SimpleDateFormat ISO_time_format = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");
	
	
	String key;
	StringBuilder content;
	long startTime;
	long lastTime;
	int stenceNum;
	
	public DialogueDoc(){
		content=new StringBuilder();
	}
	public void setKey(String keyValue){
		key=keyValue;
	}
	public DialogueDoc(String keyValue,String firstSentence,long time){
		key=keyValue;
		content=new StringBuilder();
		//content.append(firstSentence+"  @"+ISO_time_format.format(time));
		content.append(firstSentence);
		content.append("¡£");
		startTime=time;
		lastTime=time;
		stenceNum=1;
	}
	
	public boolean isInDialogueTime(long logTime){
		
		if(startTime-logTime>MaxIntervalTime)
			return false;
		
		if(logTime-lastTime>MaxIntervalTime)
			return false;
		
		return true;
	}
	

	private void setTime(long time){
		if(time<startTime)
		{
			startTime=time;
			return ;
		}
		if(time>lastTime)
		{
			lastTime=time;
			return ;
		}
	}
	public void addSentence(String sentence,long time){
		setTime(time);
		//content.append(sentence+"  @"+ISO_time_format.format(time));
		content.append(sentence);
		content.append("¡£");
		stenceNum++;
	}
	public int getStenceNum(){
		return stenceNum;
	}
	
	public String toString(){
	
		 
		 String start=ISO_time_format.format(startTime);
		 String last=ISO_time_format.format(lastTime);
		 
		 return key+" Start="+start+" Last="+last+" stenceNum="+stenceNum+" \n"+content;
		
	}
	
	
}
