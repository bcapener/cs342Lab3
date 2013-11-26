
public class BTreeObject<E extends Comparable<E>> implements Comparable<BTreeObject<E>>{
	//Data Fields
	private int freqCount;
	private E key;
	
	//Constructor
	public BTreeObject(E key){
		this.key = key;
		freqCount = 1;
	}
	
	public E getKey(){
		return key;
	}
	
	public int getFreqCount(){
		return freqCount;
	}
	
	public void incFreqCount(){
		freqCount++;
	}

	@Override
	public int compareTo(BTreeObject<E> o) {
		return key.compareTo(o.getKey());
	}
	
	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");

	    //result.append(this.getClass().getName());
	    result.append(" Key: " + key.toString() + NEW_LINE);
	    result.append(" Frequency Count: " + freqCount + NEW_LINE);

	    return result.toString();
	}
	
}
