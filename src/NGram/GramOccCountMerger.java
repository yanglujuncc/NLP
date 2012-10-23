package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;


public class GramOccCountMerger {


	public GramOccCountMerger() {

	}

	private void addGramToMap(Map<String, Integer> map, String gram, int add) {

		int oldCounter = 0;
		Integer longCounter = map.get(gram);
		if (longCounter != null)
			oldCounter = longCounter;
		map.put(gram, oldCounter + add);

	}

	private void fulshToFile(Map<String, Integer> occMap, String filePath) throws IOException {

		File file = new File(filePath);
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
		BufferedWriter bw = new BufferedWriter(osw);

		for (Entry<String, Integer> entry : occMap.entrySet()) {
			bw.append(entry.getKey() + ":" + entry.getValue() + "\n");
		}
		bw.flush();
		bw.close();

	}


	public void process(String inputFile1,String inputFile2, String outputFile)
			throws IOException {
		
		
		Map<String, Integer> mergedOccMap= new TreeMap<String, Integer>();
		
		FileInputStream fis1 = new FileInputStream(inputFile1);
		InputStreamReader isr1 = new InputStreamReader(fis1);
		BufferedReader br1 = new BufferedReader(isr1);

		FileInputStream fis2 = new FileInputStream(inputFile2);
		InputStreamReader isr2 = new InputStreamReader(fis2);
		BufferedReader br2 = new BufferedReader(isr2);

		
		String aline = null;

		long i = 0;
		while ((aline = br1.readLine()) != null) {

			if(i%100000==0)
				System.out.println("processed "+i+" lines");
			
			
			String[] terms=aline.split(":");
			String gram=terms[0];
			int counter=Integer.parseInt(terms[1]);
			
			addGramToMap(mergedOccMap,  gram, counter) ;
			i++;
		}
		br1.close();
		
		while ((aline = br2.readLine()) != null) {
			
			if(i%100000==0)
				System.out.println("processed "+i+" lines");
			
			
			String[] terms=aline.split(":");
			String gram=terms[0];
			int counter=Integer.parseInt(terms[1]);
			
			addGramToMap(mergedOccMap,  gram, counter) ;
			i++;
		}
		
		br2.close();
		
		fulshToFile(mergedOccMap, outputFile);
		mergedOccMap.clear();
		
	}

	public static void main(String[] args) throws IOException {

		
		if (args.length != 3) {
			
			System.out.println("Compute GramOccCountMerger  ");
			System.out.println("usage:GramOccCountMerger	OccFile_1 OccFile_2  OuputPath");
			System.out.println("                       	OccFile_1	(OccRecord format  Gram:Count)");
			System.out.println("                       	OccFile_2	(............................)");
			System.out.println("                       	OuputPath	(merged file.)");
			System.out.println("Example: ");
			System.out.println("     GramOccCountMerger	GramOcc_1 GramOcc_2  GramOcc_12");
			System.out.println("	");

			return;
		}

		
		String OccFilePath_1 = args[0];
		String OccFilePath_2 = args[1];
		String outputPath = args[2];

		GramOccCountMerger aGramOccCountMerger = new GramOccCountMerger();
		aGramOccCountMerger.process(OccFilePath_1,OccFilePath_2, outputPath);

	}
}
