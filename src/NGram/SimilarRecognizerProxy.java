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
	 * @param NGrams    要返回的类型，1表示一个字的词，2表示两个字的词...,en表示英文 ，  例如  String[] ngrams={"1","2","3","4","en"};
	 * @param targetGram   目标词
	 * @param neighborCounter  返回相似词的个数
	 * @return  返回一个string 链表，  每个String="词:相似度"  例如   "艺人:0.9959719185763029"  "射手:0.9823031428111009"   "方士:0.980454225241157" 
	 * @throws RemoteException
	 */
	
	public List<String> getSimilarOf(String[] NGrams,String targetGram,int neighborCounter) throws RemoteException{
		return remoteSimilarRecognizer.getSimilarOf(NGrams, targetGram, neighborCounter);
	}
	/**
	 * 
	 * @param NGrams    要返回的类型，1表示一个字的词，2表示两个字的词...,en表示英文 ，  例如  String[] ngrams={"1","2","3","4","en"};
	 * @param targetGram   目标词
	 * @param neighborCounter  返回相似词的个数
	 * @param threadNum  为本次调用，服务器端计算的线程数
	 * @return  返回一个string 链表，  每个String="词:相似度"  例如   "艺人:0.9959719185763029"  "射手:0.9823031428111009"   "方士:0.980454225241157" 
	 * @throws RemoteException
	 */
	public List<String> getSimilarOfMutiThreadBased(String[] NGrams,String targetGram,int neighborCounter,int threadNum) throws RemoteException{
		
		return remoteSimilarRecognizer.getSimilarOfMutiThreadBased(NGrams, targetGram, neighborCounter, threadNum);
	}

}
