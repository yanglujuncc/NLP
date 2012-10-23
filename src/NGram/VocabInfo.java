package NGram;

public class VocabInfo {

	int vocabID;
	int lastDayOcc;
	int lastTotalOcc;
	long createTime;
	
	int todyDayOcc;
	long todayTimeStamp;
	
	static final int DayMS=24*60*60*1000;
	
	
	public int incrNumByLastDay(){
		
		if(todayTimeStamp==createTime)
			return todyDayOcc;
	
		return todyDayOcc-lastDayOcc;
	}

	
	public float incrRateByLastDay(){
		
		if(todayTimeStamp==createTime)
			return (float) todyDayOcc/1;
		
		return (float) (((todyDayOcc-lastDayOcc)*1.0)/(lastDayOcc+1));
	}
	
	
	public int incrNumByAverage(){
		
		if(todayTimeStamp==createTime)
			return 0;
		int days=dayBetween(createTime,todayTimeStamp)+2;
		int average=(lastTotalOcc+todyDayOcc)/days;
		
		return todyDayOcc-average;
	}
	
	public float incrRateByAverage(){
		
		if(todayTimeStamp==createTime)
			return (float) 0.0;
		
		
		long days=dayBetween(createTime,todayTimeStamp)+2;
	//	System.out.println("days:"+days);
		double average=(lastTotalOcc+todyDayOcc)*1.0/days;
		
	//	System.out.println("average:"+average);
		return (float) ((todyDayOcc-average)/(average+1));
	}
	
	public int dayBetween(long begin,long end){
		
		if(begin>=end)
			return 0;
		
		return (int) ((end-begin-1)/DayMS);
		
	}
	
	public String toString(){
		return vocabID+"\t"+lastDayOcc+"\t"+lastTotalOcc+"\t"+createTime;
	}
	
	// vocabID+"\t"+lastDayOcc+"\t"+totalOcc+"\t"+createTime;
	public static VocabInfo fromString(String recordStr){
		
		String[] terms=recordStr.split("\t");
		VocabInfo newVocabInfo=new VocabInfo();
		
		if(terms.length!=4)
			return null;
		
		newVocabInfo.vocabID=Integer.parseInt(terms[0]);
		newVocabInfo.lastDayOcc=Integer.parseInt(terms[1]);
		newVocabInfo.lastTotalOcc=Integer.parseInt(terms[2]);
		newVocabInfo.createTime=Long.parseLong(terms[3]);
		
		return newVocabInfo;
		
	}
	
}
