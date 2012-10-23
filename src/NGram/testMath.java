package NGram;

import java.io.IOException;

public class testMath {

	public static double entrp(double prob){
		
		double result=prob*Math.log(prob)/Math.log(2);
		if(result==0)
			return 0;
		else
			return -result;
	
	}
	public static void main(String[] args) throws IOException
	{
	
		int[] elmensts={
				150,2,1,1,3,1,1,1,11,14,4
		};
		int total=190;
		
		double entropy=0.0;
		int totalCount=0;
		for(int element:elmensts){
			totalCount+=element;
			double p=(element*1.0)/total;
			entropy+=0-p*(Math.log(p));
		}
		
		System.out.println(entropy);
		
		String str="sdsf	dsfsd dff";
		String[] terms=str.split("[\t ]");
		for(String term:terms){
			System.out.println(term);
		}
		
		double vs= 3*entrp(1.0/3);
		double vs2= 1*entrp(1.0);
		System.out.println(vs+vs2);
	}
}
