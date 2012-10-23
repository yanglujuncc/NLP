package NGram;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



public class testSimilarRecognizerServer {

	public static void main(String[] argvs) throws RemoteException{
		
	
		
		
		//String rmi_url="rmi://10.100.83.108:8884/SimilarRecognizerServer";
		String rmi_url="rmi://10.100.83.108:8884/SRServer";
		  SimilarRecognizer aSimilarRecognizer = null;
		try {
			aSimilarRecognizer = (SimilarRecognizer) Naming.lookup(rmi_url);
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
	        
          String[] ngrams={"1","2","3","4","en"};
          String targetGram="×°±¸";
          int neighborCounter=300;
          //System.out.println(aSimilarRecognizer.getSimilarOf(ngrams, targetGram, neighborCounter)); 
          long time_start=System.currentTimeMillis();
         System.out.println(aSimilarRecognizer.getSimilarOfMutiThreadBased(ngrams, targetGram, neighborCounter,20));
         long time_end=System.currentTimeMillis();
         long time_cost=time_end-time_start;
         System.out.println("time_cost="+time_cost);
	}
}
