package ylj.Util;

public class testStringRecognizer {


	public static void main(String[] args)
	{
		/*
		FilesInput aFilesInput=new FilesInput("gbk");
		try {
			
			aFilesInput.loadFromDirName("E:\\workspace\\SomeJavaCodes\\log");
			String aline=null;
			System.out.println(aFilesInput.remains_num());
			
			while((aline=aFilesInput.getLine())!=null){
				
				System.out.println(aline +"remains:"+aFilesInput.remains_num());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		System.out.println(StringRecognizer.isCN_zhWord("��+"));
		System.out.println(StringRecognizer.getRepeatCount("######++"));
		
		System.out.println(StringRecognizer.getRepeatStr("ɵ��ɵ��ɵ��ɵ��ɵ��ɵ��ɵ��"));
		System.out.println(StringRecognizer.getRepeatCount("ɵ��ɵ��ɵ��ɵ��ɵ��ɵ��ɵ��"));
		System.out.println(StringRecognizer.shortRepeatFormat("ɵ��ɵ��ɵ��ɵ��ɵ��ɵ��ɵ��"));
		System.out.println();
		String shrot=StringRecognizer.shortRepeatFormat("+++++++++++++++++++++");
		System.out.println("shrot="+shrot);
		System.out.println(StringRecognizer.isRepeatShortFormat(shrot));
		
		String symbol="~��*&����%��#@��-����=+��|��{��}��������������������";
		System.out.println("Symbol test:"+symbol);
		System.out.println(StringRecognizer.isSymbol(symbol));
		String hanzi="����ugfsS11";
		System.out.println("hanzi:"+hanzi);
		System.out.println(StringRecognizer.isCN_zhWord(hanzi));
		System.out.println(StringRecognizer.isCN_zhStr(hanzi));
		System.out.println(StringRecognizer.upCase(hanzi));
		System.out.println(Math.log(10) / Math.log(2));
		/*
		System.out.println(StringRecognizer.shortRepeatFormat("##"));
		System.out.println(StringRecognizer.shortRepeatFormat("###"));
		System.out.println(StringRecognizer.shortRepeatFormat("####"));
		System.out.println(StringRecognizer.shortRepeatFormat("#####"));
		System.out.println(StringRecognizer.shortRepeatFormat("+++"));
		System.out.println(StringRecognizer.shortRepeatFormat("======="));
		System.out.println(StringRecognizer.shortRepeatFormat("++++++"));
		System.out.println(StringRecognizer.shortRepeatFormat("+++++++++++++++"));
		System.out.println(StringRecognizer.shortRepeatFormat("=4+"));
		*/
	}
}
