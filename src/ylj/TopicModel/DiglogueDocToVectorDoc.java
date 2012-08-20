package ylj.TopicModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ylj.Segmentation.ICTCLASSegmenterWraper;
import ylj.Segmentation.Segmentater;
import ylj.Segmentation.StanfordSegmenterWraper;
import ylj.Util.HashIndex;
import ylj.Util.Index;
import ylj.Util.StringRecognizer;

public class DiglogueDocToVectorDoc {

	public static void  saveToFilename(String path){
		
		
	}
	public static void  main(String[] args) throws Exception
	{
		
		Segmentater segmenter=new ICTCLASSegmenterWraper();
		//Segmentater segmenter=new StanfordSegmenterWraper();
		
		Index<String> vocabIndexer=new HashIndex<String>();
		Index<String> docNameIndexer=new HashIndex<String>();
	
		
		List<DocVector> docVectorList=new LinkedList<DocVector>();
	
		String vocabOutputFilePath="IM5000Vocab.txt";
		String docNameOutputFilePath="IM5000DocName.txt";
		
		String inputFilePath="IM5000Docs";
		String VectorDocOutputFilePath="IM5000Docs.dat";
		
		InputStream is=new FileInputStream(inputFilePath);
		InputStreamReader isr=new InputStreamReader(is,"gbk");
		BufferedReader bReader=new BufferedReader(isr);
		
		System.out.println("process begin..");
		int i=0;
		int j=0;
		String startLine=null;
		while((startLine=bReader.readLine())!=null)
		{
			

			i++;
			if(i%69!=0)
				continue;
			
			if(j%1000==0)
				System.out.println("processed "+j);
			
			if(j==10000)
				break;
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
				
				if(!StringRecognizer.isCN_zhWord(vocab))
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
