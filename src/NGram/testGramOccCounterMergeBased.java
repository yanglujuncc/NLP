package NGram;

import java.io.IOException;

public class testGramOccCounterMergeBased {

	public static void testAll(String[] args) throws IOException{
		
		if (args.length != 4) {

			System.out.println("Compute GramOccCounter  ");
			System.out.println("usage:GramOccCounter	MaxGram MaxOccMapSize InstancePath  outputFile");
			System.out.println("                       	MaxGram	(like:4  means use 1,2,3,4Gram )");
			System.out.println("                       	MaxOccMapSize	(  )");
			System.out.println("                       	InstancePath	(Increment Instance File Path)");
			System.out.println("                       	outputFile	(file to store counter)");
			System.out.println("Example: ");
			System.out.println("     GramOccCounter 4 1000000 IMESSAGE GramOcc");
			System.out.println("	");

			return;
		}

		int MaxGram = Integer.parseInt(args[0]);
		int MaxOccMapSize = Integer.parseInt(args[1]);
		String path = args[2];
		String occFilePath = args[3];
		String reversedOccFilePath=occFilePath+"_reversed";
		String reversedOccFilePath2=occFilePath+"_reversed2";
		GramOccCounterMergeBased aGramOccCounter = new GramOccCounterMergeBased(MaxGram);
		aGramOccCounter.process(MaxOccMapSize, path, occFilePath);

		GramOccCounterMergeBased.reverseGramFile(MaxOccMapSize, occFilePath, reversedOccFilePath);
		GramOccCounterMergeBased.reverseGramFile(MaxOccMapSize, occFilePath, reversedOccFilePath2);
	}
	public static void testReverseGram() throws IOException{
		
		int MaxOccMapSize=1000000;
		
		String originFilePath="GramDir/GramOcc_Reversed_RF";
		String reverseOriginFilePath="GramDir/GramOcc_LF_reverse";
		
		GramOccCounterMergeBased.reverseGramFile(MaxOccMapSize, originFilePath, reverseOriginFilePath);
		GramOccCounterMergeBased.reverseGramFile(MaxOccMapSize, originFilePath, reverseOriginFilePath);
	}
	public static void main(String[] args) throws IOException {

		testReverseGram() ;
	}
}
