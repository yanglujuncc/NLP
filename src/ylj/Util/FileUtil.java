package ylj.Util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileUtil {

	public static void buildParentPath(String path){

		File file = new File(path);
		File parentFile= file.getParentFile();
		
		if(!parentFile.exists()){
			buildParentPath(parentFile.getAbsolutePath());
		}
		parentFile.mkdir();
	}
	public static File[] getAllSubFiles(String dirPath){
		File dirFile=new File(dirPath);
		return getAllSubFiles(dirFile);
	}
	public static File[] getAllSubFiles(File dir) {
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
	
	public static void main(String[] args){
		
		String path="E:\\workspace3\\NLP3\\data3\\dict3";
		buildParentPath(path);
		
		File file=new File(path);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
