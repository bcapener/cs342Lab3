
public class BTreeObject implements Comparable<BTreeObject>{
	//Data Fields
	private int freqCount;
	private Long key;
	
	//Constructor
	public BTreeObject(Long key){
		this.key = key;
		freqCount = 1;
	}
	
	public BTreeObject(Long key, int freqCount){
		this.key = key;
		this.freqCount = freqCount;
	}
	
	public Long getKey(){
		return key;
	}
	
	public int getFreqCount(){
		return freqCount;
	}
	
	public void incFreqCount(){
		freqCount++;
	}

	@Override
	public int compareTo(BTreeObject o) {
		return key.compareTo(o.getKey());
	}
	
	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    result.append(" Key: " + key.toString());
	    result.append(" FC: " + freqCount);

	    return result.toString();
	}
	
}
