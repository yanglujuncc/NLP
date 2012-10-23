package NGram;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SimilarRecognizer extends Remote{
	List<String> getSimilarOf(String[] NGrams,String targetGram,int neighborCounter)throws RemoteException; ;
	List<String> getSimilarOfMutiThreadBased(String[] NGrams,String targetGram,int neighborCounter,int threadNum)throws RemoteException; ;

}
