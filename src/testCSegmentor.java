import java.io.FileNotFoundException;

import toolbox.wordsegment.CSegmentor;


public class testCSegmentor {
	public static void main(String[] agvs) {
		
		CSegmentor seg = new CSegmentor();
		
		String[] terms = seg.doSegment("��500�Ҷ�ˢ�һ�ȥ50�ˣ���æ");
		String[] terms2 = seg.doSegment("�Ҿ����Ⱥ����棬��æ");
		System.out.print("|");
		for(String term:terms2)
		{
			System.out.print(term+"|");
		}
	}
}
