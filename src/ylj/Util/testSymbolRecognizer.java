package ylj.Util;

public class testSymbolRecognizer {

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
		
		System.out.println("test SymbolRecognizer ");
		System.out.println(SymbolRecognizer.isSpecialSymbol("#123"));
		System.out.println(SymbolRecognizer.isSpecialSymbol("#c123456"));
		
	}
}
