package ylj.Dict;

import java.io.File;
import java.io.IOException;



public interface FileDictReader {
	

	public void addDictFile(String filePath)throws IOException;
	public Term readTerm()throws IOException;
	public Term line2Term(String aline) throws IOException;

}
