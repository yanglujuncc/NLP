package NGram;

import java.util.LinkedList;

public class testAddCombinationDegree {
	
	public static void testCubGramGroups(){
		AddCohesionDegree aAddCombinationDegree=new AddCohesionDegree(4);
		LinkedList<LinkedList<String>>  groups=	aAddCombinationDegree.subGramGroups("�����");
		for(LinkedList<String> group:groups){
			
			System.out.println(group);
		}
	}
		
	public static void main(String[] args){
		
		testCubGramGroups();
	}

}
