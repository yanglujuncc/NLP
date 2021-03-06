package ylj.TopicModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ylj.Segmentation.ICTCLASSegmenterWraper;
import ylj.Segmentation.Segmentater;
import ylj.Segmentation.StanfordSegmenterWraper;
import ylj.Util.HashIndex;
import ylj.Util.Index;
import ylj.Util.StringRecognizer;

public class DiglogueDocToVectorDoc {
	 SimpleDateFormat time_format = new SimpleDateFormat(
		"yyyy-MM-dd'T'HH:mm:ss");
	 
	public static Set<String>  loadStopVocab(String stopVocabPath) throws IOException{
		
		Set<String> stopVocabMap=new HashSet<String>();
		
		InputStream is=new FileInputStream(stopVocabPath);
		InputStreamReader isr=new InputStreamReader(is,"gbk");
		BufferedReader bReader=new BufferedReader(isr);

		int i=0;
		String startLine=null;
		while((startLine=bReader.readLine())!=null)
		{
			
			String aTerm=startLine.replace(" ", "");
			//System.out.println("aTerm="+aTerm);
			stopVocabMap.add(aTerm);
			i++;
		}
		System.out.println("load "+i+" stop words.");
		
		
		return stopVocabMap;
	}
	
	public static void  main(String[] args) throws Exception
	{
		List<DocVector> docVectorList=new LinkedList<DocVector>();
		Segmentater segmenter=new ICTCLASSegmenterWraper();
		Set<String>  stopVocabSet=loadStopVocab("StopVocab.txt");
		//Segmentater segmenter=new StanfordSegmenterWraper();
		
		Index<String> vocabIndexer=new HashIndex<String>();
		Index<String> docNameIndexer=new HashIndex<String>();
	
		
	
		String vocabOutputFilePath="IM10000Vocab.txt";
		String docNameOutputFilePath="IM10000DocName.txt";
		
		String inputFilePath="IM10000Docs";
		String VectorDocOutputFilePath="IM10000Docs.dat";
		
		InputStream is=new FileInputStream(inputFilePath);
		InputStreamReader isr=new InputStreamReader(is,"gbk");
		BufferedReader bReader=new BufferedReader(isr);
		
		System.out.println("process begin..");
		int i=0;
		int j=0;
		String startLine=null;
		while((startLine=bReader.readLine())!=null)
		{
			
		
		
			if(j%1000==0)
				System.out.println("processed "+j);
			
			if(j==10000)
			{
				System.out.println("j= "+j);
				break;
			}
			String[] terms=startLine.split(" ");
			String docName=terms[0];
			
			long docID=docNameIndexer.indexOf(docName, true);
			DocVector newDocVector=new DocVector(docID);
			
			String contentLine=bReader.readLine();
			if(contentLine==null)
			{
				System.err.println("something wrong !");
			}
			
			List<String> vocabs=segmenter.makeSegment(contentLine);
			
			for(String vocab:vocabs){		
				
	
				if(vocab.equals(""))
					continue;
				if(vocab.equals(" "))
					continue;
				if(StringRecognizer.isSymbol(vocab))
					continue;
				
				
				if(stopVocabSet.contains(vocab))
					continue;
				
				long vocabID=vocabIndexer.indexOf(vocab, true);
				newDocVector.addElement(vocabID);
				
			}
			
			docVectorList.add(newDocVector);
			j++;
		}
		bReader.close();
		
		System.out.println("process ok ");
		
		System.out.println("save vocabIndexer To File "+vocabOutputFilePath);
		vocabIndexer.saveToFilename(vocabOutputFilePath);
		System.out.println("save docNameIndexer To File "+docNameOutputFilePath);
		docNameIndexer.saveToFilename(docNameOutputFilePath);
		
		
		OutputStream os=new FileOutputStream(VectorDocOutputFilePath);
		OutputStreamWriter osr=new OutputStreamWriter(os,"gbk");
		BufferedWriter bWriter=new BufferedWriter(osr);
		System.out.println("save docVectorList To File "+VectorDocOutputFilePath);
		for(DocVector aDocVector:docVectorList)
		{
			bWriter.append(aDocVector.toString()+"\n");
			
		}
		bWriter.close();
	}
	
}
