package NGram;


import java.io.IOException;



public class GramOccFileReverser {

	
	public static void main(String[] args) throws IOException
	{
	
		if (args.length != 2) {
			System.out.println("Add Increment To Model or Train A New Model  ");
			System.out.println("usage:OccFileReverser SortedOccFile   OuputFile");

			System.out.println("                       	SortedOccFile	(input SortedOccCounterFile )");
			System.out.println("                       	OuputFile	(number of store file  (MapNum) )");
		
			System.out.println("Example: ");
			System.out.println("   OccFileReverser  GramOcc  GramOcc_R ");
			System.out.println("	");

			return;
		}
		
		String sortedOccFile=args[0];
		String ouputFile=args[1];
	
		GramOccCounter.reverseOccFile(sortedOccFile, ouputFile);
		
		
		
	}
}
