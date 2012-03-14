import java.io.FileNotFoundException;

import toolbox.wordsegment.CSegmentor;


public class testCSegmentor {
	public static void main(String[] agvs) {
		
		CSegmentor seg = new CSegmentor();
		
		String[] terms = seg.doSegment("我500找对刷桃花去50了，你忙");
		System.out.print("|");
		for(String term:terms)
		{
			System.out.print(term+"|");
		}
	}
}
