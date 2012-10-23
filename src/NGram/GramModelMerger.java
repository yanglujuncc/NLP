package NGram;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class GramModelMerger {
	
	protected static Logger logger = Logger.getLogger(GramModelMerger.class.getName());
	
	public GramVectorModel doMerge(GramVectorModel model_1,GramVectorModel model_2){
		
		
		GramVectorModel mergedModel=null;
		String statusModel_1="model_1:"+model_1;
		String statusModel_2="model_2:"+model_2;
		
		if(model_1.getGramNum()>model_2.getGramNum())
			mergedModel=model_1.mergeModel(model_2);
		else
			mergedModel=model_2.mergeModel(model_1);
		
		logger.info(statusModel_1);
		logger.info(statusModel_2);
		logger.info("mergedModel:"+mergedModel);
		return mergedModel;
	}
	
public static void main(String[] args) throws Exception {
		
	DOMConfigurator.configureAndWatch("conf/log4j.xml"); 

		if(args.length!=3)
		{
			System.out.println("Merge Two Model  ");
			System.out.println("usage:GramModelMerger	Model_1 Model_2 MergedModelOutput");
			
			System.out.println("                       	Model_1	(path of the Model_1)");
			System.out.println("                       	Model_2	(path of the Model_1)");
			System.out.println("                       	MergedModelOutput	(path of the MergedModel to store)");
			System.out.println("Example: ");
			System.out.println("   MergeTwoModel:   Model_1 Model_2 mergedModel");
			return ;
		}
		
		String model_1_path=args[0];
		String model_2_path=args[1];
		String newModelOutputPath=args[2];
		
		GramVectorModel model_1=GramVectorModel.loadModel(model_1_path);
		GramVectorModel model_2=GramVectorModel.loadModel(model_2_path);
		
		GramModelMerger aGramModelMerger=new GramModelMerger();
		GramVectorModel mergedModel=aGramModelMerger.doMerge(model_1, model_2);
		
		GramVectorModel.writeModel(newModelOutputPath, mergedModel);
		
	}
}
