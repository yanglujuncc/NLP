package ylj.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;





public class FilesInput {
	
	private static Logger logger = Logger
	.getLogger(FilesInput.class.getName());
	
	String characterSet;
	File dataDir ;
	File[] dataFiles;
	
	
	// 读log日志文件流 ，输入
	//当前读取的文件
	int currentFileIndex=-1;
	BufferedReader currentReader = null;
	
	
	
	long readline=0;
	long totalLine=0;
	
	public FilesInput(String CharacterSet){
		characterSet=CharacterSet;
	}
	
	
	public void setCharacterSet(String newCharacterSet ){
		characterSet=newCharacterSet;
	}
	
	public long read_num(){
		return readline;
	}
	public long remains_num(){
		return totalLine-readline;
	}
	public long total_num(){
		return totalLine;
	}
	
	//获取所有子文件
	private static File[] getAllSubFiles(File dir) {
		if (dir == null || !dir.isDirectory())
			return null;

		List<File> allSubFiles = new LinkedList<File>();

		Queue<File> unProcessFiles = new LinkedList<File>();

		unProcessFiles.add(dir);

		while (true) {
			File curentFile = unProcessFiles.poll();
			if (curentFile == null)
				break;
			if (curentFile.isDirectory()) {
				for (File subFile : curentFile.listFiles()) {
					unProcessFiles.add(subFile);
				}
			} else {
				allSubFiles.add(curentFile);
			}
		}

		return (File[]) allSubFiles.toArray(new File[allSubFiles.size()]);

	}
	
	
	public long loadFromDirName(String dirName) throws IOException {

		// System.out.println(dirName);
		 
		 dataDir = new File(dirName);
		 dataFiles = getAllSubFiles(dataDir);
		
	//	 System.out.println(dataFiles.length);
		 totalLine=0;
		 if(dataFiles==null)
				return  0;
		 //按文件名排序，linux 系统大小写敏感，windows 不敏感
		 Arrays.sort(dataFiles);
		
		 
		 //遍历所有文件，计算总共可读取的行数
		for (int i = 0; i < dataFiles.length; i++) {
			File file = dataFiles[i];
			
			logger.info("Reading File(" + i + "/" + dataFiles.length + ")  :"
					+ file.getName());
			
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, characterSet);
			BufferedReader br = new BufferedReader(isr);

			while (br.readLine() != null)
				totalLine++;
			br.close();

		}
		
		if (dataFiles.length > 0) {

			FileInputStream fis = new FileInputStream(dataFiles[0]);
			InputStreamReader isr = new InputStreamReader(fis, "gbk");
			currentReader = new BufferedReader(isr);
			currentFileIndex = 0;
			

		}else
		{
			currentReader =null;
			currentFileIndex = -1;
	
		}
		return totalLine;

	}
	
	public void addFile(String path) throws IOException{
		 
		int newFileIndex=0;
		if(dataFiles!=null)
		{
			File[]	newFiles=new File[dataFiles.length+1];
			for(int i=0;i<dataFiles.length;i++)
				newFiles[i]=dataFiles[i];
			newFileIndex=dataFiles.length;
			dataFiles=newFiles;
		}
		else
		{
			dataFiles=new File[1];
			newFileIndex=0;
		}
		
		logger.info("Reading File:" +path + " ...");
				
		dataFiles[newFileIndex]=new File(path);
		
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, characterSet);
		BufferedReader br = new BufferedReader(isr);

		while (br.readLine() != null)
			totalLine++;
		br.close();
		logger.info(" File:" +totalLine + "  lines .");
		if (dataFiles.length == 1&&currentReader==null) {
			FileInputStream fis_cur = new FileInputStream(path);
			InputStreamReader isr_cur = new InputStreamReader(fis_cur, characterSet);
			BufferedReader br_cur = new BufferedReader(isr_cur);
			currentReader=br_cur;
			currentFileIndex = 0;
			
		}
		
			
		
		
		
	}
	
	public String getLine() throws IOException {

		while (currentReader != null) {

			String aLine = null;

			aLine = currentReader.readLine();

			if (aLine == null) {

				currentReader.close();
				
				/*
				 * if (fileReaderIterator.hasNext()) currentReader =
				 * fileReaderIterator.next(); else currentReader = null;
				 */
				currentFileIndex++;

				if (currentFileIndex < dataFiles.length) {
					FileInputStream fis = new FileInputStream(
							dataFiles[currentFileIndex]);
					InputStreamReader isr = new InputStreamReader(fis, "gbk");
					currentReader = new BufferedReader(isr);

				} else {
					currentReader = null;
				}

			} else {
				readline++;
				return aLine;
			}

		}

		return null;

	}
}
