package NGram;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class SimilarRecognizerProxy {

	 SimilarRecognizer remoteSimilarRecognizer = null;
	public boolean connectToServer(String serverRMI_URL)
	{
		
			try {
				remoteSimilarRecognizer = (SimilarRecognizer) Naming.lookup(serverRMI_URL);
				return true;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return false;
	}
	/**
	 * 
	 * @param NGrams    Ҫ���ص����ͣ�1��ʾһ���ֵĴʣ�2��ʾ�����ֵĴ�...,en��ʾӢ�� ��  ����  String[] ngrams={"1","2","3","4","en"};
	 * @param targetGram   Ŀ���
	 * @param neighborCounter  �������ƴʵĸ���
	 * @return  ����һ��string ����  ÿ��String="��:���ƶ�"  ����   "����:0.9959719185763029"  "����:0.9823031428111009"   "��ʿ:0.980454225241157" 
	 * @throws RemoteException
	 */
	
	public List<String> getSimilarOf(String[] NGrams,String targetGram,int neighborCounter) throws RemoteException{
		return remoteSimilarRecognizer.getSimilarOf(NGrams, targetGram, neighborCounter);
	}
	/**
	 * 
	 * @param NGrams    Ҫ���ص����ͣ�1��ʾһ���ֵĴʣ�2��ʾ�����ֵĴ�...,en��ʾӢ�� ��  ����  String[] ngrams={"1","2","3","4","en"};
	 * @param targetGram   Ŀ���
	 * @param neighborCounter  �������ƴʵĸ���
	 * @param threadNum  Ϊ���ε��ã��������˼�����߳���
	 * @return  ����һ��string ����  ÿ��String="��:���ƶ�"  ����   "����:0.9959719185763029"  "����:0.9823031428111009"   "��ʿ:0.980454225241157" 
	 * @throws RemoteException
	 */
	public List<String> getSimilarOfMutiThreadBased(String[] NGrams,String targetGram,int neighborCounter,int threadNum) throws RemoteException{
		
		return remoteSimilarRecognizer.getSimilarOfMutiThreadBased(NGrams, targetGram, neighborCounter, threadNum);
	}

}
