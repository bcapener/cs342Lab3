
public class BTreeObject<E> {
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
	
}
