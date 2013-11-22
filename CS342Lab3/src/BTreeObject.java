
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
}
