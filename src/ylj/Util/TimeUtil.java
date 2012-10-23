package ylj.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

	static SimpleDateFormat qn_time_format = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss,SSS Z");

	public static final long HourMS=60*60*1000;
	public static final long DayMS=24*60*60*1000;
	
	public static long localDayTime(long time ){
		
		
		
		
		TimeZone aTimeZone=TimeZone.getDefault();
		long timeOffset=aTimeZone.getRawOffset();
		return ((time+timeOffset)/DayMS)*DayMS-timeOffset;
	
	}
	
	private static void testTimeZone(){
		
		
		TimeZone aTimeZone=TimeZone.getDefault();
		System.out.println("aTimeZone getID "+aTimeZone.getID());
		System.out.println("aTimeZone getDisplayName "+aTimeZone.getDisplayName());
		System.out.println("aTimeZone getDSTSavings "+aTimeZone.getDSTSavings());
		System.out.println("aTimeZone getRawOffset "+aTimeZone.getRawOffset()); //加了8个小时
		System.out.println("aTimeZone getAvailableIDs "+Arrays.toString(TimeZone.getAvailableIDs()));
		
		
		//System.out.println(qn_time_format.format(today));
	}
	private static void testTimeZone2(){
		
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		long nowTimeStamp=System.currentTimeMillis();
		
		TimeZone aTimeZone=TimeZone.getDefault();
		long modfiedTimeStamp=nowTimeStamp-aTimeZone.getRawOffset();
		System.out.println("timstamp:"+nowTimeStamp+" "+qn_time_format.format(nowTimeStamp));
		System.out.println("timstamp:"+modfiedTimeStamp+" "+qn_time_format.format(modfiedTimeStamp));
		
		
	}
	private static void testDayTime(){
		
		long nowTimeStamp=System.currentTimeMillis();
		long hourNum=nowTimeStamp/HourMS;
		long hourReam=hourNum%24;
		System.out.println("hourNum:"+hourNum);
		System.out.println("hourReam:"+hourReam);
	
		long modfiedTimeStamp=localDayTime(nowTimeStamp);
		
		System.out.println("timstamp:"+nowTimeStamp+" "+qn_time_format.format(nowTimeStamp));
		
		System.out.println("timstamp:"+modfiedTimeStamp+" "+qn_time_format.format(modfiedTimeStamp));	
		
	}
	private static void testSimpleDateFormat(){
		long nowTimeStamp=System.currentTimeMillis();
		String localTimeString=qn_time_format.format(nowTimeStamp);
		
		long hourNum=nowTimeStamp/HourMS;
		long hourReam=hourNum%24;
		System.out.println("hourNum:"+hourNum);
		System.out.println("hourReam:"+hourReam);
		
		System.out.println("    NowTimStamp:"+nowTimeStamp+" localTimeString:"+localTimeString);
		
		localTimeString="2012-09-29 13:23:15,281 +0900";
		long parsedTimeStamp=0;
		try {
			Date date=qn_time_format.parse(localTimeString);
			parsedTimeStamp=date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ParsedTimeStamp:"+parsedTimeStamp+" localTimeString:"+localTimeString);
		
		hourNum=parsedTimeStamp/HourMS;
		hourReam=hourNum%24;
		System.out.println("hourNum:"+hourNum);
		System.out.println("hourReam:"+hourReam);
		
	}
	
	public static void main(String[] args){
		
		testSimpleDateFormat();
	}
}
