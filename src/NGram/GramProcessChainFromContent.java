package NGram;

import java.io.File;
import java.io.IOException;

public class GramProcessChainFromContent {

	public static void main(String[] args) throws IOException
	{
	
		if (args.length != 6) {

			System.out.println("usage:NewWordRecognizer	MaxGramN CounterThreshold InternalMinThreshold FreedomDegreeThreshold ContentFile OuputDir");



			return;
		}
		
		String maxGramN=args[0];
		String counterThreshold=args[1];
		String IMPMIDegreeThreshold=args[2];
		String freedomDegreeThreshold=args[3];
		
		String inputPath=args[4];
		
		
		String gramDir=args[5];
		File gramDirFile=new File(gramDir);
		if(!gramDirFile.exists())
			gramDirFile.mkdir();
		
		String gramOccPath=gramDir+"/GramOcc";
		String gramRFPath=gramDir+"/GramRF";
		String gramLFPath=gramDir+"/GramLF";
		String gramCohesionPath=gramDir+"/GramCohesion";
		String gramRecordPath=gramDir+"/GramRecord";
		String gramWordsPath=gramDir+"/GramWords";
		
		
		System.out.println("Begin GramOccCounter .....");
		
		GramOccCounter aGramOccCounter = new GramOccCounter(Integer.parseInt(maxGramN)+1);
		aGramOccCounter.process(inputPath, gramOccPath);
		System.out.println("GramOccCounter Done .");
		aGramOccCounter=null;
		System.gc();
		
		System.out.println("Begin AddRightFreedomDegree .....");
		AddRightFreedomDegree aAddRightFreedomDegree=new AddRightFreedomDegree();
		aAddRightFreedomDegree.process(gramOccPath, gramRFPath,Integer.parseInt(maxGramN));
		System.out.println("AddRightFreedomDegree Done .");
		aAddRightFreedomDegree=null;
		System.gc();
		
		System.out.println("Begin AddLeftFreedomDegree .....");
		AddLeftFreedomDegree aAddLeftFreedomDegree = new AddLeftFreedomDegree();
		aAddLeftFreedomDegree.process(gramOccPath, gramLFPath,Integer.parseInt(maxGramN));
		System.out.println("AddLeftFreedomDegree Done .");
		aAddLeftFreedomDegree=null;
		System.gc();
		
		System.out.println("Begin aAddCohesionDegree .....");
		AddCohesionDegree aAddCohesionDegree = new AddCohesionDegree(Integer.parseInt(maxGramN));
		aAddCohesionDegree.process(gramOccPath, gramCohesionPath);
		System.out.println("AddCombinationDegree Done .");
		aAddCohesionDegree=null;
		System.gc();
		
		System.out.println("Begin RecordMerger .....");
		RecordMerger aRecordMerger=new RecordMerger();
		aRecordMerger.process(gramRFPath, gramLFPath,gramCohesionPath,gramRecordPath);
		System.out.println("RecordMerger Done .");
		aRecordMerger=null;
		System.gc();
		
		System.out.println("Begin WordRecognizer .....");
		WordRecognizer aNewWordRecognizer=new WordRecognizer(maxGramN,counterThreshold,IMPMIDegreeThreshold,freedomDegreeThreshold);
		aNewWordRecognizer.process(gramRecordPath, gramWordsPath);
		System.out.println("Word Recognizse Done .");
		
	
	}
}
