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
			//��ʼ��
			if (ICTCLAS50Segmentater.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}


			//���ô��Ա�ע��(0 ������������ע����1 ������һ����ע����2 ���������ע����3 ����һ����ע��)
			ICTCLAS50Segmentater.ICTCLAS_SetPOSmap(2);

			/*
			//�����û��ʵ�ǰ�ִ�
			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 1);//�ִʴ���
			System.out.println(nativeBytes.length);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("δ�����û��ʵ�ķִʽ���� " + nativeStr);//��ӡ���
			*/

			//�����û��ֵ�
			int nCount = 0;
			String usrdir = "./ICTCLAS/userdict.txt"; //�û��ֵ�·��
			byte[] usrdirb = usrdir.getBytes();//��stringת��Ϊbyte����
			//�����û��ֵ�,���ص����û����������һ������Ϊ�û��ֵ�·�����ڶ�������Ϊ�û��ֵ�ı�������
			nCount = ICTCLAS50Segmentater.ICTCLAS_ImportUserDictFile(usrdirb, 0);
			System.out.println("�����û��ʸ���" + nCount);
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
		
		//System.out.println("�����û��ʵ��ķִʽ���� " + nativeStr);
		String filtratedPOS=filtratePOS(nativeStr);
		//System.out.println("filtratedPOS�� " + filtratedPOS);
		
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
			//�ͷŷִ������Դ
		 ICTCLAS50Segmentater.ICTCLAS_Exit();
	
       // other finalization code...

     }
	 public static void main(String[] args) throws Exception
	 {
		 ICTCLASSegmenterWraper aICTCLASSegmenterWraper=new ICTCLASSegmenterWraper();
		 String inputStr="���˷�ʿ��ʲô�Ķ���  �������������Ⱥ�ҽ����������������";
		 List<String>terms= aICTCLASSegmenterWraper.makeSegment(inputStr);
		 for(String term:terms){
			 System.out.println(term);
			 
		 }
	 }
}
