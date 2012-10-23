package NGram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GramDiff {

	protected static Logger logger = Logger.getLogger(GramDiff.class.getName());
	
public static void main(String[] args) throws Exception {
		
		PropertyConfigurator.configure("conf/log4j.properties");
		
		
		if(args.length!=4)
		{
			System.out.println("diff Two GramFiles,Set based  ");
			System.out.println("usage:GramDiff GramFile_1 GramFile_2 AddFile LossFile");
			
			System.out.println("                       	GramFile_1	(old gram file)");
			System.out.println("                       	GramFile_2	(new gram file)");
			System.out.println("                       	AddFile		( added grams )");
			System.out.println("                       	LossFile	( lossed grams)");
		
			return ;
		}
		
		String gram_old=args[0];
		String gram_new=args[1];
		String addOutputPath=args[2];
		String lossOutputPath=args[3];
		
		
		Set<String> lossGramSet= new HashSet<String>();
		Set<String> addGramSet= new HashSet<String>();
	
		logger.info("Loading... Old Gram File");
		
		FileInputStream fis_old = new FileInputStream(gram_old);
		InputStreamReader isr_old = new InputStreamReader(fis_old);
		BufferedReader br_old = new BufferedReader(isr_old);

		String aline = null;
		int i = 0;
		
		while ((aline = br_old.readLine()) != null) {
			i++;
			String[] terms=aline.split("[ :\t]");
			String gram=terms[0];
		//	logger.info(gram);
			lossGramSet.add(gram);
			
		}
		br_old.close();
		logger.info("Load Old Gram File complete ,load("+i+")");
		
		
		
		logger.info("Loading... New Gram File");
		
		FileInputStream fis_new = new FileInputStream(gram_new );
		InputStreamReader isr_new  = new InputStreamReader(fis_new );
		BufferedReader br_new  = new BufferedReader(isr_new );
		
		
		aline = null;
		i = 0;
		
		while ((aline = br_new.readLine()) != null) {
			i++;
			String[] terms=aline.split("[ :\t]");
			String gram=terms[0];
		
			if(lossGramSet.contains(gram))
			{
			
				lossGramSet.remove(gram);
			}
			else
			{	//logger.info("add:"+gram);
				addGramSet.add(gram);
			}
			
			
		}
		br_new.close();
		logger.info("Load New Gram File complete,load("+i+")");
		logger.info("addGramSet:"+addGramSet.size());
		logger.info("lossGramSet:"+lossGramSet.size());
		
		logger.info("Write added Gram to File:"+addOutputPath);
		
		FileOutputStream fos_add = new FileOutputStream(addOutputPath );
		OutputStreamWriter osw_add  = new OutputStreamWriter(fos_add,"gbk");
		BufferedWriter bw_add = new BufferedWriter(osw_add );
		
		
		Iterator<String> it=addGramSet.iterator();

		while(it.hasNext()){
			String gram=it.next();

			bw_add.append(gram+"\n");
		}
		
		bw_add.close();
		logger.info("Write added Gram to File complete");
		
		

		logger.info("Write loss Gram to File:"+lossOutputPath);
		
		FileOutputStream fos_loss = new FileOutputStream(lossOutputPath );
		OutputStreamWriter osw_loss = new OutputStreamWriter(fos_loss,"gbk"); 
		BufferedWriter bw_loss = new BufferedWriter(osw_loss );
		
		
		it=lossGramSet.iterator();
		
		while(it.hasNext()){
			String gram=it.next();
			bw_loss.append(gram+"\n");
		}
		
		bw_loss.close();
		logger.info("Write loss Gram to File complete");
		
		
		
	}
}
