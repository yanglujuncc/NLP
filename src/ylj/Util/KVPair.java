package ylj.Util;

public class KVPair <T1,T2>  {

		  /**
		   * Direct access is deprecated.  Use first().
		   *
		   * @serial
		   */
		  public T1 key;

		  /**
		   * Direct access is deprecated.  Use second().
		   *
		   * @serial
		   */
		  public T2 value;

		  public KVPair() {
		    // first = null; second = null; -- default initialization
		  }

		  public KVPair(T1 key, T2 value) {
		    this.key = key;
		    this.value = value;
		  }

		  public T1 key() {
		    return key;
		  }

		  public T2 value() {
		    return value;
		  }

		  public void setKey(T1 o) {
			  key = o;
		  }

		  public void setValue(T2 o) {
			  value = o;
		  }

		  @Override
		  public String toString() {
		    return "(" + key + "=" + value + ")";
		  }

		  @Override
		  public boolean equals(Object o) {
		    if (o instanceof KVPair) {
		      @SuppressWarnings("rawtypes")
		      KVPair e = (KVPair) o;
		      return (key == null ? e.key()== null : key.equals(e.key())) && (value == null ? e.value() == null : value.equals(e.value));
		    } else {
		      return false;
		    }
		  }

}
