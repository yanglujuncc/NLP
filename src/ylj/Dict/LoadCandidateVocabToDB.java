package ylj.Dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoadCandidateVocabToDB {


	private static Logger logger = Logger.getLogger(LoadCandidateVocabToDB.class
			.getName());

	Connection conn;
	PreparedStatement insertCandidateVocabStatement;
	PreparedStatement queryGlobalDicStatement;
	PreparedStatement queryCandidateVocabStopedStatement;
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

			String insert_into_CandidateVocab = "insert into  CandidateVocab(vocab) values (?) on duplicate key update vocab=VALUES(vocab);";

			insertCandidateVocabStatement = (PreparedStatement) conn
					.prepareStatement(insert_into_CandidateVocab);
			
			String query_GlobalDic="select * from GlobalDic where vocab=?";
			queryGlobalDicStatement = (PreparedStatement) conn.prepareStatement(query_GlobalDic);
			
			String query_CandidateVocabStoped="select * from CandidateVocabStoped where vocab=?";
			queryCandidateVocabStopedStatement = (PreparedStatement) conn.prepareStatement(query_CandidateVocabStoped);
			return true;

		} else
			return false;

	}


	public void loadFile2DB(String candidateVocabfile) throws IOException, SQLException {

		File user_level_file = new File(candidateVocabfile);
		FileInputStream fis = new FileInputStream(user_level_file);
		InputStreamReader isr = new InputStreamReader(fis, "gbk");
		BufferedReader bufferedReader = new BufferedReader(isr);

		String aRecord = null;
		int filteratedCounter=0;
		int insertCounter=0;
		int duplicateCounter=0;
		
		
		int loop=0;
		long time_begin = System.currentTimeMillis();
		while ((aRecord = bufferedReader.readLine()) != null) {
			// Logout time,roleID,roleLevel,account
			loop++;
			if (loop % 100 == 0 ) {

				long time_end = System.currentTimeMillis();
				long time_cost = time_end - time_begin;
				
				long perOperate = 0;
				if (loop != 0)
					perOperate = (time_cost/loop);
				else
					perOperate = 0;

				logger.info("Operate("+loop+") InsertCounter("+insertCounter+") DuplicateCounter("+duplicateCounter+") FilteratedCounter("+filteratedCounter+") TimeCost(" + time_cost + "'ms)  PerDBOperate("
						+ perOperate + "'ms)");
				
			}
			
			
			String vocab=aRecord.split("\\t")[0];
			
			queryGlobalDicStatement.setString(1, vocab);
			ResultSet queryResult=queryGlobalDicStatement.executeQuery();
			if(queryResult.next())
			{
				System.out.println("GlobalDic contain:"+vocab);
				filteratedCounter++;
				continue;
			}
			
			System.out.println(vocab);
			if(vocab.length()>=64)
			{
				System.out.println("too long:"+vocab);
				filteratedCounter++;
				continue;
			}
			queryCandidateVocabStopedStatement.setString(1, vocab);
			queryResult=queryCandidateVocabStopedStatement.executeQuery();
			if(queryResult.next())
			{
				System.out.println("CandidateVocabStoped contain:"+vocab);
				filteratedCounter++;
				continue;
			}
			
			insertCandidateVocabStatement.setString(1, vocab);
			int updateReturn=insertCandidateVocabStatement.executeUpdate();
			
			if(updateReturn==1)
				insertCounter++;
			else 
				duplicateCounter++;
			
			
			
			
		}
		
		bufferedReader.close();
	}
	public void close() throws SQLException{
		
		 insertCandidateVocabStatement.close();
		 queryGlobalDicStatement.close();
		 queryCandidateVocabStopedStatement.close();
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

		LoadCandidateVocabToDB aLoadCandidateVocabToDB = new LoadCandidateVocabToDB();

		try {
		
			aLoadCandidateVocabToDB.connectDB(url, user, password);
			aLoadCandidateVocabToDB.loadFile2DB(fileName);
			aLoadCandidateVocabToDB.close();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
