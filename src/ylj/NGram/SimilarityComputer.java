package ylj.NGram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ylj.Util.KVPair;

public class SimilarityComputer {

	
	public static double computeLong(ArrayList<KVPair<Integer,Long>> v_x,ArrayList<KVPair<Integer,Long>> v_y){
		
		long xy=0;
		long x2 = 0;
		long y2=0;
		
		double sum=0.0;
		
		int index_x=0;
		int index_y=0;
		
		while(true){
			
			int cmpResult=0;
			KVPair<Integer,Long> pair_x=null;
			KVPair<Integer,Long> pair_y=null;
			
			if(index_x<v_x.size())
				pair_x=v_x.get(index_x);
			
			if(index_y<v_y.size())
				pair_y=v_y.get(index_y);

			
			if(pair_x!=null&&pair_y!=null)
				cmpResult=pair_x.key()-pair_y.key();
			else if(pair_x==null&&pair_y==null)
				break;
			else if(pair_x!=null)
				cmpResult=-1;
			else if(pair_y!=null)
				cmpResult=1;
				
			if(cmpResult<0)
			{
				x2+=pair_x.value()*pair_x.value();
				index_x++;
			}
			else if(cmpResult>0)
			{
				y2+=pair_y.value()*pair_y.value();
				index_y++;
			}
			else {
				xy+=pair_x.value()*pair_y.value();
				
				x2+=pair_x.value()*pair_x.value();
				y2+=pair_y.value()*pair_y.value();
				
				index_x++;
				index_y++;
			}
		}
		
		//System.out.println("xy="+xy);
		//System.out.println("x2="+x2);
		//System.out.println("y2="+y2);
		if(xy==0)
			return 0;
		sum=xy/(Math.sqrt(x2)*Math.sqrt(y2));
		return sum;
		
	}
public static double computeInt(ArrayList<KVPair<Integer,Integer>> v_x,ArrayList<KVPair<Integer,Integer>> v_y){
		
		long xy=0;
		long x2 = 0;
		long y2=0;
		
		double sum=0.0;
		
		int index_x=0;
		int index_y=0;
		
		while(true){
			
			int cmpResult=0;
			KVPair<Integer,Integer> pair_x=null;
			KVPair<Integer,Integer> pair_y=null;
			
			if(index_x<v_x.size())
				pair_x=v_x.get(index_x);
			
			if(index_y<v_y.size())
				pair_y=v_y.get(index_y);

			
			if(pair_x!=null&&pair_y!=null)
				cmpResult=pair_x.key()-pair_y.key();
			else if(pair_x==null&&pair_y==null)
				break;
			else if(pair_x!=null)
				cmpResult=-1;
			else if(pair_y!=null)
				cmpResult=1;
				
			if(cmpResult<0)
			{
				x2+=pair_x.value()*pair_x.value();
				index_x++;
			}
			else if(cmpResult>0)
			{
				y2+=pair_y.value()*pair_y.value();
				index_y++;
			}
			else {
				xy+=pair_x.value()*pair_y.value();
				
				x2+=pair_x.value()*pair_x.value();
				y2+=pair_y.value()*pair_y.value();
				
				index_x++;
				index_y++;
			}
		}
		
		//System.out.println("xy="+xy);
		//System.out.println("x2="+x2);
		//System.out.println("y2="+y2);
		if(xy==0)
			return 0;
		sum=xy/(Math.sqrt(x2)*Math.sqrt(y2));
		return sum;
		
	}
public static double computeDouble(ArrayList<KVPair<Integer,Double>> v_x,ArrayList<KVPair<Integer,Double>> v_y){
		
		double xy=0;
		double x2 = 0;
		double y2=0;
		
		double sum=0.0;
		
		int index_x=0;
		int index_y=0;
		
		while(true){
			
			int cmpResult=0;
			KVPair<Integer,Double> pair_x=null;
			KVPair<Integer,Double> pair_y=null;
			
			if(index_x<v_x.size())
				pair_x=v_x.get(index_x);
			
			if(index_y<v_y.size())
				pair_y=v_y.get(index_y);

			
			if(pair_x!=null&&pair_y!=null)
				cmpResult=pair_x.key()-pair_y.key();
			else if(pair_x==null&&pair_y==null)
				break;
			else if(pair_x!=null)
				cmpResult=-1;
			else if(pair_y!=null)
				cmpResult=1;
				
			if(cmpResult<0)
			{
				x2+=pair_x.value()*pair_x.value();
				index_x++;
			}
			else if(cmpResult>0)
			{
				y2+=pair_y.value()*pair_y.value();
				index_y++;
			}
			else {
				xy+=pair_x.value()*pair_y.value();
				
				x2+=pair_x.value()*pair_x.value();
				y2+=pair_y.value()*pair_y.value();
				
				index_x++;
				index_y++;
			}
		}
		
		//System.out.println("xy="+xy);
		//System.out.println("x2="+x2);
		//System.out.println("y2="+y2);
		sum=xy/(Math.sqrt(x2)*Math.sqrt(y2));
		return sum;
		
	}
}
