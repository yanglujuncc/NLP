package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ylj.Util.Pair;
import NGram.GramVectorModel.WordVectorDig;

public class VocabTopNProcessor {

	private static Logger logger = Logger.getLogger(VocabTopNProcessor.class.getName());
	Connection conn;
	VocabMap vocabMap = new VocabMap();

	PreparedStatement occInsertPreparedStatement;
	PreparedStatement rateByLastDayInsertPreparedStatement;
	PreparedStatement rateByAverageInsertPreparedStatement;
	
	
	
	int occInsertCount = 0;
	
	
	Map<Integer, VocabInfo> vocabInfoMap = new TreeMap<Integer, VocabInfo>();

	int MinOccInTopN;

	int TopN= 0;
	PriorityQueue<Pair<Integer, VocabInfo>> TopN_IncrNumByLastDay;
	
	
	PriorityQueue<Pair<Float, VocabInfo>> TopN_IncrRateByLastDay;
	

	
	PriorityQueue<Pair<Integer, VocabInfo>> TopN_IncrNumByAverage;

	
	PriorityQueue<Pair<Float, VocabInfo>> TopN_IncrRateByAverage;
	
	

	public boolean connectDB(String url, String user, String password) throws SQLException {

		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 加载驱动程序

		conn = DriverManager.getConnection(url, user, password);
	
		String rateByLastDay_insert_into_sql = "insert into VocabIncrTopByLastDay(vocabID,rate,incr,occ,time) values (?,?,?,?,?) on duplicate key update rate=values(rate),incr=values(incr),occ = values(occ),time=values(time);";
		rateByLastDayInsertPreparedStatement = conn.prepareStatement(rateByLastDay_insert_into_sql);
		
		String rateByAverage_insert_into_sql = "insert into VocabIncrTopByAverage(vocabID,rate,incr,occ,time) values (?,?,?,?,?) on duplicate key update rate=values(rate),incr=values(incr),occ = values(occ),time=values(time);";
		rateByAverageInsertPreparedStatement = conn.prepareStatement(rateByAverage_insert_into_sql);
		
		
		
		if (!conn.isClosed()) {
			logger.info("Succeeded connecting to the Database!");
			// System.out.println("Succeeded connecting to the Database!"); //
			// 验证是否连接成功

			return true;

		} else
			return false;

	}

	// 2012-05-22T21:06:27,3543150030,46
	// Login time,roleID,roleLevel
	private int loadVocabMapFromFile(String vocabMapFilePath) throws IOException {
		
		return vocabMap.loadFromFile(vocabMapFilePath);
		
	}

	private int loadVocabInfoFromFile(String vocabInfoFilePath) throws IOException {

		logger.info("Load ... VocabInfoFromFile:" + vocabInfoFilePath);

		File vocabInfoFile = new File(vocabInfoFilePath);

		if (!vocabInfoFile.exists()) {
			logger.info("File do not exists ,Path:" + vocabInfoFilePath);
			return 0;
		}

		FileInputStream fis = new FileInputStream(vocabInfoFile);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader bufferedReader = new BufferedReader(isr);

		String record = null;
		int lineNum = 0;
		while ((record = bufferedReader.readLine()) != null) {
			// Logout time,roleID,roleLevel,account

			VocabInfo restoredVocabInfo = VocabInfo.fromString(record);

			if (restoredVocabInfo != null)
				vocabInfoMap.put(restoredVocabInfo.vocabID, restoredVocabInfo);

			lineNum++;
		}
		bufferedReader.close();
		
		return lineNum;
	}

	public void writeVocabInfoToFile(String vocabInfoFilePath) throws IOException {

		logger.info("Writing ... VocabInfo to File:" + vocabInfoFilePath);

		File vocabInfoFile = new File(vocabInfoFilePath);
		FileOutputStream fos = new FileOutputStream(vocabInfoFile);
		OutputStreamWriter osw= new OutputStreamWriter(fos, "gbk");
		BufferedWriter bufferedWriter  = new BufferedWriter(osw);
		
		
		for(Entry<Integer, VocabInfo>  entry:vocabInfoMap.entrySet()){
			VocabInfo vocabInfo=entry.getValue();
			vocabInfo.lastDayOcc=vocabInfo.todyDayOcc;
			vocabInfo.lastTotalOcc=vocabInfo.lastTotalOcc+vocabInfo.todyDayOcc;
			
			bufferedWriter.append(vocabInfo.toString()+"\n");
		}
		bufferedWriter.close();
		
		logger.info("Write  VocabInfo to File complete. VocabInfo:"+vocabInfoMap.size());
		
	}

	
	public void topN_ByLastDay2DB(long time) throws SQLException{
		
		logger.info("TopN_IncrNum By LastDay 2DB, Size:"+TopN_IncrNumByLastDay.size());
		while(TopN_IncrNumByLastDay.size()>0)
		{
			Pair<Integer, VocabInfo> pair=TopN_IncrNumByLastDay.poll();
			VocabInfo vocabInfo=pair.second;
			vocabIncrByLastDayRecord2DB(vocabInfo.vocabID,vocabInfo.incrRateByLastDay(),vocabInfo.incrNumByLastDay(),vocabInfo.todyDayOcc,new Timestamp(time));
		}
		logger.info("TopN_IncrNum By LastDay 2DB complete.");
		
	
		logger.info("TopN_IncrRate By LastDay 2DB, Size:"+TopN_IncrRateByLastDay.size());
		while(TopN_IncrRateByLastDay.size()>0)
		{
			Pair<Float, VocabInfo> pair=TopN_IncrRateByLastDay.poll();
			VocabInfo vocabInfo=pair.second;
			vocabIncrByLastDayRecord2DB(vocabInfo.vocabID,vocabInfo.incrRateByLastDay(),vocabInfo.incrNumByLastDay(),vocabInfo.todyDayOcc,new Timestamp(time));
		}
		logger.info("TopN_IncrRate By LastDay 2DB complete.");
		
		
		
	}
	
	public int vocabIncrByLastDayRecord2DB(int vocabID,float rate,int incr, int gramOcc, Timestamp timeStamp) throws SQLException {
		
		
		rateByLastDayInsertPreparedStatement.setInt(1, vocabID);
		rateByLastDayInsertPreparedStatement.setFloat(2, rate);
		rateByLastDayInsertPreparedStatement.setInt(3, incr);
		rateByLastDayInsertPreparedStatement.setInt(4, gramOcc);
		rateByLastDayInsertPreparedStatement.setTimestamp(5, timeStamp);
		
		return rateByLastDayInsertPreparedStatement.executeUpdate();
		

	}


	//vocabID,rate,occ,time
	public void topN_ByAverage2DB(long time) throws SQLException{
		
		logger.info("TopN_IncrNum By ByAverage 2DB, Size:"+TopN_IncrNumByAverage.size());
		while(TopN_IncrNumByAverage.size()>0)
		{
			Pair<Integer, VocabInfo> pair=TopN_IncrNumByAverage.poll();
			VocabInfo vocabInfo=pair.second;
			vocabIncrByAverageRecord2DB(vocabInfo.vocabID,vocabInfo.incrRateByAverage(),vocabInfo.incrNumByAverage(),vocabInfo.todyDayOcc,new Timestamp(time));
		}
		logger.info("TopN_IncrNum By ByAverage 2DB complete.");
		
	
		logger.info("TopN_IncrRate By ByAverage 2DB, Size:"+TopN_IncrRateByAverage.size());
		while(TopN_IncrRateByAverage.size()>0)
		{
			Pair<Float, VocabInfo> pair=TopN_IncrRateByAverage.poll();
			VocabInfo vocabInfo=pair.second;
			vocabIncrByAverageRecord2DB(vocabInfo.vocabID,vocabInfo.incrRateByAverage(),vocabInfo.incrNumByAverage(),vocabInfo.todyDayOcc,new Timestamp(time));
		}
		logger.info("TopN_IncrRate By ByAverage 2DB complete.");
		
		
	}

	public int vocabIncrByAverageRecord2DB(int vocabID,float rate, int add,int gramOcc, Timestamp timeStamp) throws SQLException {
		
		rateByAverageInsertPreparedStatement.setInt(1, vocabID);
		rateByAverageInsertPreparedStatement.setFloat(2, rate);
		rateByAverageInsertPreparedStatement.setInt(3, add);
		rateByAverageInsertPreparedStatement.setInt(4, gramOcc);
		rateByAverageInsertPreparedStatement.setTimestamp(5, timeStamp);
		
		return rateByAverageInsertPreparedStatement.executeUpdate();

	}
	

	private void initPriorityQueue(int minOccInTopN, int topn) {

		MinOccInTopN = minOccInTopN;

		TopN=topn;
		TopN_IncrRateByLastDay = new PriorityQueue<Pair<Float, VocabInfo>>(TopN, new Comparator<Pair<Float, VocabInfo>>() {
			public int compare(Pair<Float, VocabInfo> o1, Pair<Float, VocabInfo> o2) {
				float comResult = o1.first - o2.first;
				if (comResult > 0)
					return 1;
				else if (comResult < 0)
					return -1;
				return 0;
			}
		});
		TopN_IncrNumByLastDay = new PriorityQueue<Pair<Integer, VocabInfo>>(TopN, new Comparator<Pair<Integer, VocabInfo>>() {
			public int compare(Pair<Integer, VocabInfo> o1, Pair<Integer, VocabInfo> o2) {
				int comResult = o1.first - o2.first;
				if (comResult > 0)
					return 1;
				else if (comResult < 0)
					return -1;
				return 0;
			}
		});

		
		TopN_IncrRateByAverage = new PriorityQueue<Pair<Float, VocabInfo>>(TopN, new Comparator<Pair<Float, VocabInfo>>() {
			public int compare(Pair<Float, VocabInfo> o1, Pair<Float, VocabInfo> o2) {
				float comResult = o1.first - o2.first;
				if (comResult > 0)
					return 1;
				else if (comResult < 0)
					return -1;
				return 0;
			}
		});
		
		TopN_IncrNumByAverage = new PriorityQueue<Pair<Integer, VocabInfo>>(TopN, new Comparator<Pair<Integer, VocabInfo>>() {
			public int compare(Pair<Integer, VocabInfo> o1, Pair<Integer, VocabInfo> o2) {
				int comResult = o1.first - o2.first;
				if (comResult > 0)
					return 1;
				else if (comResult < 0)
					return -1;
				return 0;
			}
		});
	}

	private void processTopN(VocabInfo vocabInfo) {

		if (vocabInfo.todyDayOcc < MinOccInTopN)
			return;

		
		int incrNumByLastDay=vocabInfo.incrNumByLastDay();
		int absIncrNumByLastDay=incrNumByLastDay<0?-incrNumByLastDay:incrNumByLastDay;	
	
		if (TopN_IncrNumByLastDay.size() < TopN) {
			
			TopN_IncrNumByLastDay.add(new Pair<Integer, VocabInfo>(absIncrNumByLastDay, vocabInfo));
			
		} else {
			
			if (TopN_IncrNumByLastDay.peek().first < absIncrNumByLastDay) {
				TopN_IncrNumByLastDay.poll();
				TopN_IncrNumByLastDay.add(new Pair<Integer, VocabInfo>(absIncrNumByLastDay, vocabInfo));
			}
		}

		float incrRateByLastDay = vocabInfo.incrRateByLastDay();
		float absIncrRateByLastDay=incrRateByLastDay<0?-incrRateByLastDay:incrRateByLastDay;	
		
		if (TopN_IncrRateByLastDay.size() < TopN) {
			
			TopN_IncrRateByLastDay.add(new Pair<Float, VocabInfo>(absIncrRateByLastDay, vocabInfo));
			
		} else {
			if (TopN_IncrRateByLastDay.peek().first < absIncrRateByLastDay) {
				TopN_IncrRateByLastDay.poll();

				TopN_IncrRateByLastDay.add(new Pair<Float, VocabInfo>(absIncrRateByLastDay, vocabInfo));
			}
		}

		
		int incrNumByAverage=vocabInfo.incrNumByAverage();
		int absIncrNumByAverage=incrNumByAverage<0?-incrNumByAverage:incrNumByAverage;	
		
		if (TopN_IncrNumByAverage.size() < TopN) {
			
			TopN_IncrNumByAverage.add(new Pair<Integer, VocabInfo>(absIncrNumByAverage, vocabInfo));
			
		} else {
			if (TopN_IncrNumByAverage.peek().first < absIncrNumByAverage) {
				TopN_IncrNumByAverage.poll();
				TopN_IncrNumByAverage.add(new Pair<Integer, VocabInfo>(absIncrNumByAverage, vocabInfo));
			}
		}

	
		float incrRateByAverage = vocabInfo.incrRateByAverage();
		float absIncrRateByAverage=incrRateByAverage<0?-incrRateByAverage:incrRateByAverage;	
		if (TopN_IncrRateByAverage.size() < TopN) {
			
			TopN_IncrRateByAverage.add(new Pair<Float, VocabInfo>(absIncrRateByAverage, vocabInfo));
			
			
		} else {
			
			if (TopN_IncrRateByAverage.peek().first < absIncrRateByAverage) {
				TopN_IncrRateByAverage.poll();

				TopN_IncrRateByAverage.add(new Pair<Float, VocabInfo>(absIncrRateByAverage, vocabInfo));
			}
		}

	}

	private void processVocabOccFile(String gramOccFilePath, long timeStamp) throws IOException, SQLException {

		logger.info("process ... GramOccFile:" + gramOccFilePath);

		File gramOccFile = new File(gramOccFilePath);
		FileInputStream fis = new FileInputStream(gramOccFile);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader bufferedReader = new BufferedReader(isr);

		long time_begin = System.currentTimeMillis();

		String aRecord = null;
		int lineNum = 0;
		while ((aRecord = bufferedReader.readLine()) != null) {
			// Logout time,roleID,roleLevel,account
			lineNum++;

			if (lineNum % 1000000 == 0)
			{
				logger.info("processed :" + lineNum );
				
			}

			String[] fields = aRecord.split("\t");

			String gram = fields[0];
			int vocabID = vocabMap.vocabIdOf(gram);
			int gramOcc = Integer.parseInt(fields[1]);

			if (vocabID == -1)
				continue;

			// update occ table
			//vocabOccRecord2DB(vocabID, gramOcc, dataTime);

			VocabInfo vocabInfo = vocabInfoMap.get(vocabID);
			if (vocabInfo == null) {
				vocabInfo = new VocabInfo();

				vocabInfo.vocabID = vocabID;
				vocabInfo.createTime = timeStamp;
				vocabInfo.lastDayOcc = 0;
				vocabInfo.lastTotalOcc = 0;

				vocabInfoMap.put(vocabID, vocabInfo);
			}
			vocabInfo.todyDayOcc=gramOcc;
			vocabInfo.todayTimeStamp=timeStamp;
			
			processTopN(vocabInfo);

			// update vocabInfo

		}

		//update IncrRate
		
		
		
		long time_end = System.currentTimeMillis();
		long timeCost = time_end - time_begin;

		logger.info("Process GramOccFile complete. time cost:" + timeCost + "'ms");

	}

	public static void main(String[] args) {

		String url;
		String user;
		String password;
		String vocabMapFilePath;
		String vocabInfoFilePath;
		String gramOccFilePath;
		String newvocabInfoFilePath;
		long occFileTime = System.currentTimeMillis();
		int topN;
		
		if (args.length != 9) {
			
			System.out.println("Argments: DB_URL	UserName	Password	VocabMapFile	VocabInfoFile	newVocabInfoFile	GramOccFilePath   GramOccTimeStamp TOPN");
			System.out.println("input agrSize:"+args.length);
			System.exit(1);
			
			return;
		} else {

		}

		url = args[0];
		user = args[1];
		password = args[2];
		
		vocabMapFilePath = args[3];
		
		vocabInfoFilePath=args[4];
		newvocabInfoFilePath=args[5];
		
		gramOccFilePath = args[6];
		occFileTime = Long.parseLong(args[7]);
		
		topN = Integer.parseInt(args[8]);
		//occFileTime = System.currentTimeMillis();
		
		System.out.println(occFileTime);
		try {
			DOMConfigurator.configureAndWatch("conf/log4j.xml");

			logger.info("VocabProcessor start .." );
			
			VocabTopNProcessor aVocabProcessor= new VocabTopNProcessor();
			
			aVocabProcessor.initPriorityQueue(100, topN);
			
			aVocabProcessor.connectDB(url, user, password);
			
			aVocabProcessor.loadVocabMapFromFile(vocabMapFilePath);
			
			aVocabProcessor.loadVocabInfoFromFile(vocabInfoFilePath);
			
			aVocabProcessor.processVocabOccFile(gramOccFilePath, occFileTime);
			
			
			aVocabProcessor.topN_ByLastDay2DB(occFileTime);
			aVocabProcessor.topN_ByAverage2DB(occFileTime);
			
			aVocabProcessor.writeVocabInfoToFile(newvocabInfoFilePath);
			
			logger.info("VocabProcessor complete ." );
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
