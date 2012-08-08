package ylj.Dict;

import java.util.List;

public interface Dict {
	public long getTermsNum();
	
	public long addTerm(Term aTerm);
	
	public boolean isExist(String aTermStr);
	public boolean isExist(Term aTerm);
	
	public  List<String> getContainsTermsValue(String wordsValues);
	public  List<String> getStartWithTermsValue(String wordsValues);
	public  List<String> getEndWithTermsValue(String wordsValues);
	
	public  List<Term> getContains(String wordsValues);
	public  List<Term> getStartWith(String wordsValues);
	public  List<Term> getEndWith(String wordsValues);
	
}
