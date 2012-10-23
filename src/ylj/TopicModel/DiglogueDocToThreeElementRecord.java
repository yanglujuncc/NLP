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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ylj.Segmentation.ICTCLASSegmenterWraper;
import ylj.Segmentation.Segmentater;
import ylj.Segmentation.StanfordSegmenterWraper;
import ylj.Util.HashIndex;
import ylj.Util.Index;
import ylj.Util.StringRecognizer;

public class DiglogueDocToThreeElementRecord {

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
	public static void uniq(List<String> strs){
		
		Set<String> strSet=new TreeSet<String>();
		strSet.addAll(strs);
		strs.clear();
		
		strs.addAll(strSet);
		strSet.clear();
	}
	public static void  main(String[] args) throws Exception
	{
		List<DocVector> docVectorList=new LinkedList<DocVector>();
		Segmentater segmenter=new ICTCLASSegmenterWraper();
		Set<String>  stopVocabSet=loadStopVocab("StopVocab.txt");
		//Segmentater segmenter=new StanfordSegmenterWraper();
		
		Index<String> vocabIndexer=new HashIndex<String>();
		Index<String> docNameIndexer=new HashIndex<String>();
	
		int nullVocabIndex=vocabIndexer.indexOf("", true);
		int nullDocIndex=docNameIndexer.indexOf("", true);
		
		System.out.println("nullVocabIndex="+nullVocabIndex);
		System.out.println("nullDocIndex="+nullDocIndex);
		
		Map<Long,Long> dfMap=new HashMap<Long,Long>();
		
		String vocabOutputFilePath="TeamDocs500Vocab.txt";
		String docNameOutputFilePath="TeamDocs500DocName.txt";
		
		String inputFilePath="TeamDocs";
		String VectorDocOutputFilePath="TeamDocs500.dat";
		
		InputStream is=new FileInputStream(inputFilePath);
		InputStreamReader isr=new InputStreamReader(is,"gbk");
		BufferedReader bReader=new BufferedReader(isr);
		
		System.out.println("process begin..");
		
		int j=0;
		String startLine=null;
		while((startLine=bReader.readLine())!=null)
		{
			
			String contentLine=bReader.readLine();
		
			j++;
			
			if(j%1000==0)
				System.out.println("processed "+j);
			
			if(j%400!=0)
			{	
				continue;
			}
			String[] terms=startLine.split(" ");
			String docName=terms[0];
			String startTime=terms[1].substring("Start=".length());
			String endTime=terms[2].substring("Last=".length());
			
			String uniqDocName=docName+"@"+startTime;
			
			long docID=docNameIndexer.indexOf(uniqDocName, true);
		
			DocVector newDocVector=new DocVector(docID);
			
		
			if(contentLine==null)
			{
				System.err.println("something wrong !");
			}
			System.out.println("docName:"+uniqDocName);
			System.out.println("docID:"+docID);
			System.out.println("startTime:"+startTime);
			System.out.println("endTime:"+endTime);
			System.out.println("contentLine:"+contentLine);
			
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
			//System.out.println("before("+vocabs.size()+"):"+vocabs);
			uniq(vocabs);
			//System.out.println("after("+vocabs.size()+"):"+vocabs);
			for(String vocab:vocabs){
				
				long vocabIndex=vocabIndexer.indexOf(vocab);
				
				long count=1;
				Long counter=dfMap.get(vocabIndex);
				if(counter!=null)
					count+=counter;
					
				dfMap.put(vocabIndex, count);
			}
			
			
			docVectorList.add(newDocVector);
			
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
			//System.out.println(aDocVector.toThreeElement());
			bWriter.append(aDocVector.toThreeElementTF_IDF(dfMap));
			
		}
		bWriter.close();
	}
	
}
