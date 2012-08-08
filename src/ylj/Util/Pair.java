package ylj.Util;

import java.io.Serializable;




public class Pair <T1,T2>  {

	  /**
	   * Direct access is deprecated.  Use first().
	   *
	   * @serial
	   */
	  public T1 first;

	  /**
	   * Direct access is deprecated.  Use second().
	   *
	   * @serial
	   */
	  public T2 second;

	  public Pair() {
	    // first = null; second = null; -- default initialization
	  }

	  public Pair(T1 first, T2 second) {
	    this.first = first;
	    this.second = second;
	  }

	  public T1 first() {
	    return first;
	  }

	  public T2 second() {
	    return second;
	  }

	  public void setFirst(T1 o) {
	    first = o;
	  }

	  public void setSecond(T2 o) {
	    second = o;
	  }

	  @Override
	  public String toString() {
	    return "(" + first + "," + second + ")";
	  }

	  @Override
	  public boolean equals(Object o) {
	    if (o instanceof Pair) {
	      @SuppressWarnings("rawtypes")
	      Pair p = (Pair) o;
	      return (first == null ? p.first() == null : first.equals(p.first())) && (second == null ? p.second() == null : second.equals(p.second()));
	    } else {
	      return false;
	    }
	  }


}