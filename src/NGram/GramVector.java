package NGram;

import java.util.ArrayList;


import ylj.Util.KVPair;

public class GramVector<T> {
	
	String gramValue;
	int index;
	
	ArrayList<KVPair<Integer, T>> vector;
	
	public 	ArrayList<KVPair<Integer, T>> getVectorList(){
		return vector;
	}
	public 	String getGramValue(){
		return gramValue;
	}
	public String toString(){
		return "gram="+gramValue+" index="+index+"\n"+vector;
	}
	
}
