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
		System.out.println(StringRecognizer.isCN_zhWord("扂+"));
		System.out.println(StringRecognizer.getRepeatCount("######++"));
		
		System.out.println(StringRecognizer.getRepeatStr("伂排伂排伂排伂排伂排伂排伂排"));
		System.out.println(StringRecognizer.getRepeatCount("伂排伂排伂排伂排伂排伂排伂排"));
		System.out.println(StringRecognizer.shortRepeatFormat("伂排伂排伂排伂排伂排伂排伂排"));
		System.out.println();
		String shrot=StringRecognizer.shortRepeatFormat("+++++++++++++++++++++");
		System.out.println("shrot="+shrot);
		System.out.println(StringRecognizer.isRepeatShortFormat(shrot));
		
		String symbol="~﹞*&＃＃%ㄓ#@ㄐ-〞〞=+﹜|▽{▼}˙ㄩ＊§ㄛ▲﹝◎﹜ˋ";
		System.out.println("Symbol test:"+symbol);
		System.out.println(StringRecognizer.isSymbol(symbol));
		String hanzi="犖趼ugfsS11";
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
