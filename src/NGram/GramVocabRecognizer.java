package NGram;

import java.io.File;
import java.io.IOException;

public class GramVocabRecognizer {

	public static void main(String[] args) throws IOException {

		if (args.length != 6) {

			System.out.println("usage:GramVocabRecognizer CounterThreshold CohesionThreshold FreedomDegreeThreshold GramDir RemoveTempFiles");
			System.exit(1);
			return ;
		}

		String maxGramNStr = args[0];
		int maxGramN=Integer.parseInt(maxGramNStr);
		String counterThresholdStr = args[1];
		int counterThreshold = Integer.parseInt(counterThresholdStr);
		String cohesionThresholdStr = args[2];
		double cohesionThreshold = Double.parseDouble(cohesionThresholdStr);
		String freedomDegreeThresholdStr = args[3];
		double freedomDegreeThreshold = Double.parseDouble(freedomDegreeThresholdStr);

		String gramDir = args[4];
		String removeTemp = args[5];

		File gramDirFile = new File(gramDir);
		if (!gramDirFile.exists())
			gramDirFile.mkdir();

		String gramOccPath = gramDir + "/GramOcc";
		String gramRFPath = gramDir + "/GramRF";
		
		String gramReversedPath = gramDir + "/GramOcc_Reversed";
		String gramReversedRFPath = gramDir + "/GramOcc_Reversed_RF";
		String gramLFPath = gramDir + "/GramLF";
		String gramFreedomPath = gramDir + "/GramFreedom";
		// String gramCohesionPath=gramDir+"/GramCohesion";
		// String gramRecordPath=gramDir+"/GramRecord";
		String gramWordsPath = gramDir + "/GramVocabs";
		// String gramCohesionPath=gramDir+"/GramCohesion";
		String gramCohesionPath = gramDir + "/GramCohesion_subGroupProb";

		System.out.println("Begin GramVocabRecognizer .....");

		File occFile = new File(gramOccPath);

		if (occFile.exists())
			System.out.println("Occ File Exists . path:" + gramOccPath);
		else {
			System.out.println("Occ File not Exists . path:" + gramOccPath);
			System.exit(1);
		}

		System.out.println("Begin AddRightFreedomDegree .....");
		AddRightFreedomDegree aAddRightFreedomDegree = new AddRightFreedomDegree();
		aAddRightFreedomDegree.processFast(counterThreshold, gramOccPath, gramRFPath, maxGramN);
		System.out.println("AddRightFreedomDegree Done .");

		aAddRightFreedomDegree = null;
		System.gc();

		System.out.println("Begin AddLeftFreedomDegree .....");
		AddLeftFreedomDegree aAddLeftFreedomDegree = new AddLeftFreedomDegree();
		aAddLeftFreedomDegree.processFast(1000000,counterThreshold, gramOccPath, gramLFPath, maxGramN);
		System.out.println("AddLeftFreedomDegree Done .");
		aAddLeftFreedomDegree = null;
		System.gc();

		System.out.println("Begin GramFreedomRecordMerger .....");
		GramFreedomRecordMerger aGramFreedomRecordMerger = new GramFreedomRecordMerger();
		aGramFreedomRecordMerger.processFast(freedomDegreeThreshold, gramLFPath, gramRFPath, gramFreedomPath);
		System.out.println("GramFreedomRecordMerger Done .");
		aGramFreedomRecordMerger = null;
		System.gc();

		System.out.println("Begin aAddCohesionDegree .....");
		AddCohesionDegree aAddCohesionDegree = new AddCohesionDegree(maxGramN);
		aAddCohesionDegree.processFast(gramOccPath, gramFreedomPath, cohesionThreshold, gramWordsPath, gramCohesionPath);
		
		System.out.println("AddCombinationDegree Done .");
		aAddCohesionDegree = null;
		System.gc();

		if (removeTemp.equalsIgnoreCase("true")) {
			
			System.out.println("Remove Temp Files ..");

			File temFile = new File(gramRFPath);
			temFile.deleteOnExit();
			System.out.println("Remove temp Files :"+temFile.getAbsolutePath());
			
			temFile = new File(gramReversedPath);
			temFile.deleteOnExit();
			System.out.println("Remove temp Files :"+temFile.getAbsolutePath());
			
			temFile = new File(gramReversedRFPath);
			temFile.deleteOnExit();
			System.out.println("Remove temp Files :"+temFile.getAbsolutePath());

			temFile = new File(gramLFPath);
			temFile.deleteOnExit();
			System.out.println("Remove temp Files :"+temFile.getAbsolutePath());
			
			temFile = new File(gramFreedomPath);
			temFile.deleteOnExit();
			System.out.println("Remove temp Files :"+temFile.getAbsolutePath());
			
			temFile = new File(gramCohesionPath);
			temFile.deleteOnExit();
			System.out.println("Remove temp Files :"+temFile.getAbsolutePath());

			// String gramCohesionPath=gramDir+"/GramCohesion";
			// String gramRecordPath=gramDir+"/GramRecord";


		}

		System.out.println("GramVocabRecognizer  Complete .....");

	}
}
