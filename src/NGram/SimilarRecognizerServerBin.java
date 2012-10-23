package NGram;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ylj.Util.Pair;
import NGram.GramVectorModel.WordVectorDig;
import NGram.GramVectorModelBinBased.IntVectorDig;

public class SimilarRecognizerServerBin extends UnicastRemoteObject implements SimilarRecognizer{
	
	private static Logger logger = Logger.getLogger(SimilarRecognizerServerBin.class.getName());
	int defaultComputeThreadNum=4;
	protected SimilarRecognizerServerBin(int computeThreadNum) throws RemoteException {
		this.defaultComputeThreadNum=computeThreadNum;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1083140355209617305L;
	GramVectorModelBinBased gramModel;

	public void loadModel(String modelPath) throws Exception {
		
		gramModel = GramVectorModelBinBased.loadFromDir(modelPath);
	}

	@Override
	public List<String> getSimilarOf(String[] NGrams, String targetGram,
			int neighborCounter) {

		return getSimilarOfMutiThreadBased(NGrams,targetGram,neighborCounter,defaultComputeThreadNum);
	}

	@Override
	public List<String> getSimilarOfMutiThreadBased(String[] NGrams,
			String targetGram, int neighborCounter, int threadNum) {
		logger.info("Call getSimilarOf() args:"
				+ Arrays.toString(NGrams) + "," + targetGram + ","
				+ neighborCounter+","+threadNum);
		
		long start_time = System.currentTimeMillis();

		ArrayList<Pair<IntVectorDig, Double>> resultPairs = gramModel.getNNearestNegihborOf(targetGram, neighborCounter,threadNum);
		
		

		LinkedList<String> returnList=null;
		if (resultPairs == null) {
			returnList=null;
		}

		else {
			returnList= new LinkedList<String>();
			for (Pair<IntVectorDig, Double> pair : resultPairs) {
				
				//System.out.println(pair.first.getGramValue()+":"+pair.second+":"+pair.first.instanceCounter);

				// System.out.println("[gram="+pair.first.getGramValue()+" similarity="+pair.second+"]");
				returnList.addFirst(pair.first.getGram()+":"+pair.second+":"+pair.first.getGramInsNum());
			}
		}
		long end_time = System.currentTimeMillis();
		long cost = end_time - start_time;
		logger.info("Cost "+cost+"'ms"+" Return :" + returnList);
		
		
		//System.gc();
		
		return returnList;
	}

	public static void main(String args[]) throws Exception {

		try {
			
			DOMConfigurator.configureAndWatch("conf/log4j.xml");
	
			// 创建一个远程对象

			if (args.length != 4) {
				System.out.println("start SimilarRecognizerServer RMI");
				System.out.println("usage:SimilarRecognizerServer  RMI_PORT ServerName BinModelPath DefaultThreadNum");

				System.out.println("                       	RMI_PORT	(RMI port)");
				System.out.println("                       	ServerName	    (name of obj regist in RMI)");
				System.out.println("                       	BinModelPath	(path of the gramModel)");
				System.out.println("                       	DefaultThreadNum	(Similar compute Thread number )");
				System.out.println("Example: ");
				
				return;
			}

			int port = Integer.parseInt(args[0]);
			String serverName = args[1];
			String modelPath = args[2];
			int defaultComputeThreadNum = Integer.parseInt(args[3]);
			
			// 本地主机上的远程对象注册表Registry的实例，并指定端口为8888，这一步必不可少（Java默认端口是1099），必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
			LocateRegistry.createRegistry(port);

			// 把远程对象注册到RMI注册服务器上，并命名为RHello
			// 绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
			InetAddress loalInetAddress = InetAddress.getLocalHost();
			String localIP = loalInetAddress.getHostAddress();
			System.out.println("本机的IP = " + localIP);

			String rmi_url = "rmi://" + localIP + ":" + port + "/" + serverName;

			SimilarRecognizerServerBin aSimilarRecognizerServerBin = new SimilarRecognizerServerBin(defaultComputeThreadNum);

			long time_start = System.currentTimeMillis();
			System.out.println("Loading...  model:" + "ModelDir");
			aSimilarRecognizerServerBin.loadModel(modelPath);
			long time_end = System.currentTimeMillis();
			System.out.println("Load model complete. cost="
					+ (time_end - time_start) + "'ms");

			Naming.bind(rmi_url, aSimilarRecognizerServerBin);

			System.out.println(">>>>>INFO: 对象绑定成功！");
			System.out.println(">>>>>INFO: rmi_url=" + rmi_url);
			// client test

			/*
			 * SimilarRecognizer aSimilarRecognizer =(SimilarRecognizer)
			 * Naming.lookup(rmi_url);
			 * 
			 * String[] ngrams={"1","2","3","4","en"}; String targetGram="网易";
			 * int neighborCounter=50;
			 * System.out.println(aSimilarRecognizer.getSimilarOf(ngrams,
			 * targetGram, neighborCounter));
			 */
			return;

		} catch (RemoteException e) {
			System.out.println("创建远程对象发生异常！");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.out.println("发生重复绑定对象异常！");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("发生URL畸形异常！");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
