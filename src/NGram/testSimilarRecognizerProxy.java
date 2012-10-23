package NGram;


import java.rmi.RemoteException;
import java.util.List;

public class testSimilarRecognizerProxy {

public static void main(String[] argvs) throws RemoteException{
		
	
		
		
		//String rmi_url="rmi://10.100.83.108:8884/SimilarRecognizerServer";
	String rmi_url="rmi://10.100.83.108:8884/SRServer";
	//String rmi_url="rmi://localhost:8884/SRServer";
		SimilarRecognizerProxy proxy=new SimilarRecognizerProxy();
	
		if(!proxy.connectToServer(rmi_url))
		{
			System.out.println("can not connect to server");
			return ;
		}
	        
          String[] ngrams={"1","2","3","4","en"};
          String targetGram="ÆßÏ¦ÈÎÎñ";
          int neighborCounter=100;
          //System.out.println(aSimilarRecognizer.getSimilarOf(ngrams, targetGram, neighborCounter)); 
          long time_start=System.currentTimeMillis();
          List<String>  strs=proxy.getSimilarOfMutiThreadBased(ngrams, targetGram, neighborCounter,20);
          long time_end=System.currentTimeMillis();
          long time_cost=time_end-time_start;
          System.out.println("time_cost="+time_cost+"'ms");
          
          
          if(strs==null)
          {
        	  System.out.println(strs);
        	  
        	  return ;
          }
          for(String str:strs){
        	  System.out.println(str);
          }
        
        
	}
}
