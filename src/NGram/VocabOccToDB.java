package NGram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class VocabOccToDB {


	private static Logger logger = Logger.getLogger(VocabOccToDB.class
			.getName());
	Connection conn;
	VocabMap vocabMap=new VocabMap();
	PreparedStatement insertPreparedStatement ;
	
	int operateCount=0;
	int insertCount=0;
	
	
	public boolean connectDB(String url, String user, String password)
			throws SQLException {

		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 加载驱动程序

		conn = DriverManager.getConnection(url, user, password);
		String insert_into_sql = "insert into VocabOcc(vocabID,occ,occtime) values (?,?,?) on duplicate key update occ = values(occ),occtime=values(occtime);";
		
		insertPreparedStatement = conn.prepareStatement(insert_into_sql);
	 
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
	
	public  int  vocabOccRecord2DB(PreparedStatement preparedStatement,int vocabID,int gramOcc,Timestamp timeStamp) throws SQLException{
		

		preparedStatement.setInt(1, vocabID);
		preparedStatement.setInt(2, gramOcc);
		preparedStatement.setTimestamp(3, timeStamp);
		
		return preparedStatement.executeUpdate();
		
	}
	public void loadVocabOcc2DB(String gramOccFilePath,String vocabMapFilePath,long timeStamp)
			throws IOException, SQLException {

		
		Timestamp dataTime =new Timestamp(timeStamp);
		
		vocabMap.loadFromFile(vocabMapFilePath);
		
		logger.info("Load ... GramOccFile:"+gramOccFilePath);
		
		File gramOccFile = new File(gramOccFilePath);
		FileInputStream fis = new FileInputStream(gramOccFile);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader bufferedReader = new BufferedReader(isr);
		
		
		
		long time_begin = System.currentTimeMillis();
		

	
		String aRecord = null;
		int lineNum=0;
		while ((aRecord = bufferedReader.readLine()) != null) {
			// Logout time,roleID,roleLevel,account
			lineNum++;
			
			if(lineNum%100000==0)
				logger.info("processed :"+lineNum+" lines");
			
			String[] fields = aRecord.split("\t");
		
			String gram=fields[0];
			int	vocabID=vocabMap.vocabIdOf(gram);
			if(vocabID==-1)
				continue;
			
			
			if(operateCount%5000==0)
				logger.info("operateCount:"+operateCount+" insertCount:"+insertCount);
			
			int gramOcc=Integer.parseInt(fields[1]);
			
			
			//System.out.println("Gram:"+gram);
			//System.out.println("VocabID:"+vocabID);
			//System.out.println("GramOcc:"+gramOcc);
			
			int result=vocabOccRecord2DB(insertPreparedStatement,vocabID,gramOcc,dataTime);
		
			if(result==1)
				insertCount++;
			
			operateCount++;
		}
		long time_end = System.currentTimeMillis();
		long timeCost=time_end-time_begin;
				
		logger.info("Load ... GramOccFile complete. time cost:"+timeCost+"'ms");
		logger.info("operateCount:"+operateCount+" insertCount:"+insertCount);
	}

	public static void main(String[] args) {
		
		String url;
		String user;
		String password;
		String vocabMapFilePath;
		String gramOccFilePath;
		long occFileTime = System.currentTimeMillis();
		
		if (args.length != 6) {
			System.out
					.println("Argments: DB_URL	UserName	Password	VocabMapFile	GramOccFilePath  GramOccTimeStamp");
			return;
		} else {

		}

		url = args[0];
		user = args[1];
		password = args[2];
		vocabMapFilePath = args[3];
		gramOccFilePath = args[4];
		occFileTime = Long.parseLong(args[5]);
		
		try {
			DOMConfigurator.configureAndWatch("conf/log4j.xml"); 

			VocabOccToDB aVocabOccToDB=new VocabOccToDB();
			aVocabOccToDB.connectDB(url, user, password);
			aVocabOccToDB.loadVocabOcc2DB(gramOccFilePath, vocabMapFilePath, occFileTime);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
