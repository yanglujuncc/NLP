package ylj.Segmentation;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class ICTCLASSegmenterWraper implements Segmentater{

	ICTCLAS50 ICTCLAS50Segmentater;
	String POS_reg="\\/\\w+";
	Pattern POS_Pattern=Pattern.compile(POS_reg);
	Matcher POS_Mather=POS_Pattern.matcher("");
	
	public ICTCLASSegmenterWraper(){
		
		try
		{
			ICTCLAS50Segmentater= new ICTCLAS50();
			String argu = "./ICTCLAS";
			//初始化
			if (ICTCLAS50Segmentater.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}


			//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
			ICTCLAS50Segmentater.ICTCLAS_SetPOSmap(2);

			/*
			//导入用户词典前分词
			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 1);//分词处理
			System.out.println(nativeBytes.length);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("未导入用户词典的分词结果： " + nativeStr);//打印结果
			*/

			//导入用户字典
			int nCount = 0;
			String usrdir = "./ICTCLAS/userdict.txt"; //用户字典路径
			byte[] usrdirb = usrdir.getBytes();//将string转化为byte类型
			//导入用户字典,返回导入用户词语个数第一个参数为用户字典路径，第二个参数为用户字典的编码类型
			nCount = ICTCLAS50Segmentater.ICTCLAS_ImportUserDictFile(usrdirb, 0);
			System.out.println("导入用户词个数" + nCount);
			nCount = 0;
			
			
			
		}
		catch (Exception ex)
		{
		}
	}

	@Override
	public List<String> makeSegment(String sentence) throws Exception {
		
		byte[] nativeBytes = ICTCLAS50Segmentater.ICTCLAS_ParagraphProcess(sentence.getBytes("GB2312"), 2, 1);
		//System.out.println(nativeBytes.length);
		String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
		
		//System.out.println("导入用户词典后的分词结果： " + nativeStr);
		String filtratedPOS=filtratePOS(nativeStr);
		//System.out.println("filtratedPOS： " + filtratedPOS);
		
		// TODO Auto-generated method stub
		return splitByBlank(filtratedPOS);
	}
	
	public String filtratePOS(String segmentedStr){
		POS_Mather.reset(segmentedStr);
	
		return POS_Mather.replaceAll("");
		
	}
	public List<String> splitByBlank(String str){
		List<String> returnList=new LinkedList<String>();
		String[] terms=str.split(" ");
		
		for(String term:terms)
		{
			returnList.add(term);
		}
		return returnList;
	}
	 protected void finalize()
     {

		 ICTCLAS50Segmentater.ICTCLAS_SaveTheUsrDic();
			//释放分词组件资源
		 ICTCLAS50Segmentater.ICTCLAS_Exit();
	
       // other finalization code...

     }
	 public static void main(String[] args) throws Exception
	 {
		 ICTCLASSegmenterWraper aICTCLASSegmenterWraper=new ICTCLASSegmenterWraper();
		 String inputStr="异人方士甲什么的都有  好武器，就是魅和医生的武器功都不高";
		 List<String>terms= aICTCLASSegmenterWraper.makeSegment(inputStr);
		 for(String term:terms){
			 System.out.println(term);
			 
		 }
	 }
}
