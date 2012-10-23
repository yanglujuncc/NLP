package ylj.Dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class LoadVocabsToDB {

	private static Logger logger = Logger.getLogger(LoadVocabsToDB.class
			.getName());

	Connection conn;
	PreparedStatement aPreparedStatement;

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

		if (!conn.isClosed()) {
			logger.info("Succeeded connecting to the Database!");

			String insert_into_GlobalDic = "insert into  GlobalDic(vocab) values (?) on duplicate key update vocab=VALUES(vocab);";

			aPreparedStatement = (PreparedStatement) conn
					.prepareStatement(insert_into_GlobalDic);

			return true;

		} else
			return false;

	}


	public void loadFile2DB(String vocabfile) throws IOException, SQLException {

		File user_level_file = new File(vocabfile);
		FileInputStream fis = new FileInputStream(user_level_file);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader bufferedReader = new BufferedReader(isr);

		String aRecord = null;
		int insertCounter=0;
		int duplicateCounter=0;
		
		
		int loop=0;
		long time_begin = System.currentTimeMillis();
		while ((aRecord = bufferedReader.readLine()) != null) {
			// Logout time,roleID,roleLevel,account
			loop++;
			String vocab=aRecord.split("\\t")[0];
			
			aPreparedStatement.setString(1, vocab);
			
			int updateReturn=aPreparedStatement.executeUpdate();
			if(updateReturn==1)
				insertCounter++;
			else 
				duplicateCounter++;
			
			
			if (loop % 100 == 0 ) {

				long time_end = System.currentTimeMillis();
				long time_cost = time_end - time_begin;
				
				long perOperate = 0;
				if (loop != 0)
					perOperate = (time_cost/loop);
				else
					perOperate = 0;

				logger.info("Operate("+loop+") InsertCounter("+insertCounter+") DuplicateCounter("+duplicateCounter+") TimeCost(" + time_cost + "'ms)  PerDBOperate("
						+ perOperate + "'ms)");
				
			}
			
		}
		
		bufferedReader.close();
	}
	public void close() throws SQLException{
		
		 aPreparedStatement.close();
		 conn.close();
	}
	
	public static void main(String[] args) {

		PropertyConfigurator.configure("conf/log4j.properties");
		
		String url;
		String user;
		String password;
		String fileName;

		if (args.length != 4) {
			System.out
					.println("Argments  arg[0]:DB_URL   arg[1]:UserName   arg[2]:password   arg[3]:login file  ");
			return;
		} else {

		}

		url = args[0];
		user = args[1];
		password = args[2];
		fileName = args[3];

		LoadVocabsToDB aLoadVocabsToDB = new LoadVocabsToDB();

		try {
		
			aLoadVocabsToDB.connectDB(url, user, password);
			aLoadVocabsToDB.loadFile2DB(fileName);
			aLoadVocabsToDB.close();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
