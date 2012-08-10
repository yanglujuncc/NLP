import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import ylj.Segmentation.SimpleWordSegmentater;
import ylj.Util.FilesInput;
import ylj.Util.StringRecognizer;

public class wordCounter {

	public static void sharpTerms(String[] terms) {

	}

	public static void main(String[] args) throws Exception {
		
		PropertyConfigurator.configure("conf/log4j.properties");

		Map<String, Integer> counterMap = new HashMap<String, Integer>();
		Map<String, Integer> lineCounterMap = new HashMap<String, Integer>();
		Map<String, Integer> linkedCounterMap = new HashMap<String, Integer>();
		SimpleWordSegmentater aSimpleWordSegmentator = new SimpleWordSegmentater();

		// NormalSentenceSpliter aNormalSentenceParser = new
		// NormalSentenceSpliter();

		 //String dataDir = "E:\\Ÿª≈Æ\\Data\\qnlog_0226\\test";
		String dataDir = "";
	//	String dataDir=args[0];
		FilesInput inputLog = new FilesInput("gbk");
		inputLog.loadFromDirName(dataDir);
		String line;
		long total = inputLog.remains_num();
		System.out.println("total=" + total);
		int i = 0;
		while ((line = inputLog.getLine()) != null) {

			// System.out.println(line);
			if (i == 10000) {
				System.out.println("Line read=" + inputLog.read_num());
				System.out.println("Line remains=" + inputLog.remains_num());
				System.out.println("TermCount =" + counterMap.size());
				System.out.println("LinkedTermCount ="
						+ linkedCounterMap.size());
				i = 0;
			}
			i++;
		

			// aNormalSentenceParser.setRawText(content);
			// System.out.println(content);
			// String sentence = null;

			List<String> terms = aSimpleWordSegmentator.makeSegment(line);
			if (terms == null)
				continue;

			List<String> sharpedList = new LinkedList<String>();
			sharpedList.add("Begin");
			String preTerm1 = null;
			for (String term : terms) {

				// System.out.println("term="+term);
				if (preTerm1 == null) {
					preTerm1 = term;
				}

				if (term.equals("#")) {
					preTerm1 = term;
					continue;
				}

				if (preTerm1.equals("#")) {
					
					if (StringRecognizer.isADigital(term)||StringRecognizer.isEnWord(term)) {
						String newTerm = preTerm1 + term;
						sharpedList.add(newTerm);
						preTerm1 = newTerm;
					} else {
						sharpedList.add(preTerm1);
						sharpedList.add(term);
						preTerm1 = term;
					}

					continue;
				}

				if (StringRecognizer.isADigital(term)
						|| StringRecognizer.isEnWord(term)) {
					preTerm1 = term;
					continue;
				}

				sharpedList.add(term);
				preTerm1 = term;

			}
			sharpedList.add("End");
			
			String preTerm2 = null;
			preTerm1=null;
			for (String term : sharpedList) {
				// System.out.println("term="+term);

				if (counterMap.containsKey(term)) {
					// System.out.println("before term:"+term+" count="+counterMap.get(term));
					// System.out.println("before counterMap.size()="+counterMap.size());
					Integer count = counterMap.get(term);
					counterMap.put(term, new Integer(1 + count.intValue()));

					// System.out.println("after term:"+term+" count="+counterMap.get(term));
					// System.out.println("after counterMap.size()="+counterMap.size());
				} else {
					// System.out.println("content="+content);
					// System.out.println("new term="+term);
					// System.out.println("before counterMap.size()="+counterMap.size());
					counterMap.put(term, new Integer(1));
					// System.out.println("after counterMap.size()="+counterMap.size());
				}

				
				
				if(preTerm2==null)
				{
					preTerm2=term;
					continue;
				}
				
				if(preTerm1==null)
				{
					preTerm1=term;
					continue;
				}
				
				/*
				if (preTerm1 != null) {

					String linkedTerm = preTerm1 + term;

					if (linkedCounterMap.containsKey(linkedTerm)) {
						Integer count = linkedCounterMap.get(linkedTerm);
						linkedCounterMap.put(linkedTerm,
								new Integer(1 + count.intValue()));
					} else {
						linkedCounterMap.put(linkedTerm, new Integer(1));
					}
				}
				*/
			

				String linkedTerm = preTerm2+preTerm1+term;

				if (linkedCounterMap.containsKey(linkedTerm)) {
						Integer count = linkedCounterMap.get(linkedTerm);
						linkedCounterMap.put(linkedTerm,
								new Integer(1 + count.intValue()));
				} else {
						linkedCounterMap.put(linkedTerm, new Integer(1));
				}
				
				preTerm2=preTerm1;
				preTerm1 = term;
			}
			
		}

		// termCount≈≈–Ú

		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
				counterMap.entrySet());

		Collections.sort(entryList,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						return (o2.getValue() - o1.getValue());
					}
				});
		int j = 0;
		for (Map.Entry<String, Integer> e : entryList) {
			j++;
			System.out.println(j + "@" + e.getKey() + ":" + e.getValue());
			if (j == 1000)
				break;
		}

		// linkedTermCount≈≈–Ú
		ArrayList<Map.Entry<String, Integer>> entryList2 = new ArrayList<Map.Entry<String, Integer>>(
				linkedCounterMap.entrySet());
		Collections.sort(entryList2,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						return (o2.getValue() - o1.getValue());
					}
				});
		j = 0;
		for (Map.Entry<String, Integer> e : entryList2) {
			j++;
			System.out.println(j + "@" + e.getKey() + ":" + e.getValue());
			if (j == 1000)
				break;
		}

	}
}
