
public class BTreeObject<E> {
	//Data Fields
	private int freqCount;
	private Long key;
	
	//Constructor
	public BTreeObject(long key){
		this.key = key;
		freqCount = 1;
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
	
}
