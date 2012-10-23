package NGram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class VocabMapToDB {

	private static Logger logger = Logger.getLogger(VocabMapToDB.class.getName());
	Connection conn;

	public boolean connectDB(String url, String user, String password) throws SQLException {

		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 加载驱动程序

		conn = DriverManager.getConnection(url, user, password);

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

	public void loadVocabMap2DB(String datafilePath) throws IOException, SQLException {

		logger.info("load VocabMap 2DB ... VocabMapFile:" + datafilePath);

		File datafile = new File(datafilePath);
		FileInputStream fis = new FileInputStream(datafile);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader bufferedReader = new BufferedReader(isr);

		String insert_into_sql = "insert into VocabMap(vocabID,vocab) values (?,?)   on duplicate key update vocabID = values(vocabID),vocab=values(vocab)";

		PreparedStatement insertPreparedStatement = conn.prepareStatement(insert_into_sql);

		long time_begin = System.currentTimeMillis();
		int operateCount = 0;
		int insertCount = 0;
		int duplicateCount = 0;
		int existsCount = 0;
		
		String aRecord = null;
		while ((aRecord = bufferedReader.readLine()) != null) {
			// Logout time,roleID,roleLevel,account

			if (operateCount % 1000 == 0)
				logger.info("operateCount:" + operateCount + " insertCount:" + insertCount + " duplicateCount:" + duplicateCount+" existsCount:"+existsCount);

			int start = aRecord.indexOf('=');
			if (start == -1 || start == aRecord.length() - 1) {
				continue;
			}
			int vocabID = Integer.parseInt(aRecord.substring(0, start));
			String vocab = aRecord.substring(start + 1);

			// logger.info("vocabID="+vocabID);
			// logger.info("vocab="+vocab);

			if (vocab.length() >= 64) {
				logger.info("vocab too long,jump it.");
				logger.info("vocabID=" + vocabID);
				logger.info("vocab=" + vocab);
				operateCount++;
				continue;
			}

			insertPreparedStatement.setInt(1, vocabID);
			insertPreparedStatement.setString(2, vocab);

			int result = insertPreparedStatement.executeUpdate();
			if (result == 1)
				insertCount++;
			else if (result == 2) {
				duplicateCount++;
			} else if (result == 0) {
				existsCount++;
			}

			operateCount++;
		}
		long time_end = System.currentTimeMillis();
		long timeCost = time_end - time_begin;

		logger.info("load VocabMap 2DB  complete. time cost:" + timeCost + "'ms");
		logger.info("operateCount:" + operateCount + " insertCount:" + insertCount + " duplicateCount:" + duplicateCount+" existsCount:"+existsCount);
	}

	public static void main(String[] args) {
		String url;
		String user;
		String password;
		String vocabMapFileName;

		if (args.length != 4) {
			System.out.println("Argments  arg[0]:DB_URL   arg[1]:UserName   arg[2]:Password   arg[3]:VocabMapFile  ");
			return;
		} else {

		}

		url = args[0];
		user = args[1];
		password = args[2];
		vocabMapFileName = args[3];

		try {
			DOMConfigurator.configureAndWatch("conf/log4j.xml");

			VocabMapToDB aVocabMapToDB = new VocabMapToDB();
			aVocabMapToDB.connectDB(url, user, password);
			aVocabMapToDB.loadVocabMap2DB(vocabMapFileName);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
