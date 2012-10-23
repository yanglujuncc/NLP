package NGram;

import java.io.IOException;

public class testVocabInfo {
	static final int DayMS=24*60*60*1000;
	
	public static void main(String[] args) throws IOException
	{
		VocabInfo aVocabInfo=new VocabInfo();
		aVocabInfo.createTime=System.currentTimeMillis()-2*DayMS;
		aVocabInfo.lastDayOcc=10;
		
		
		long Occ=20;
		long thisTime=System.currentTimeMillis();
		
		System.out.println("aVocabInfo.createTime:"+aVocabInfo.createTime);
		System.out.println("thisTime:"+thisTime);
		System.out.println("Occ:"+Occ);

	}
}
