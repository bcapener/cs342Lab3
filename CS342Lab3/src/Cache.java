import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class that acts as a cache using a linked list given a max cache size.
 * @author daniel
 *
 * @param <T>
 */
public class Cache<T> {
	private LinkedList<T> list;
	private final int MAXSIZE;
	
	/**
	 * Constructor for the Cache class.
	 * @param maxSize Max size of the cache object.
	 */
	public Cache(int maxSize){
		this.MAXSIZE = maxSize;
		list = new LinkedList<T>();
	}
	
	/**
	 * Adds object to the head of the list. Removes last object(s) if the list 
	 * exceeds max size
	 * @param o Object to add at the head of the list.
	 * @return The object that was dropped off the end of the cache
	 */
	public T addObject(T o){
		list.addFirst((T) o);
		while(list.size() > MAXSIZE){
			return list.removeLast();
		}
		return null;
	}
	
	/**Removes first instance of an object in the list.
	 * 
	 * @param o Object to remove from list.
	 * @return Returns true if the object is found and removed.
	 */
	public boolean removeObject(T o){
		return(list.remove(o));
	}
	
	/**
	 * Clears the cache list.
	 */
	public void clearCache(){
		list.clear();
	}
	
	/**
	 * 
	 * @return Returns current size of cache.
	 */
	public int getSize(){
		return list.size();
	}
	
	/**
	 * 
	 * @return Returns max size of the cache/
	 */
	public int getMaxSize(){
		return MAXSIZE;
	}
	
	/**
	 * 
	 * @return Returns true if cache is full (current size is equal or exceeds max size).
	 */
	public boolean isFull(){
		return list.size() >= MAXSIZE;
	}
	
	/**
	 * 
	 * @param o Object to find.
	 * @return returns first instance of an object in the list.
	 */
	public T getObject(int nodePointer){
		Iterator<T> iterator = list.iterator();
		while(iterator.hasNext()){
			T object = iterator.next();
			if(((BTreeNode) object).getNodePointer() == nodePointer){
				return object;
			}
		}
		return null;
	}
	
	/**
	 * Returns object at an index.
	 * @param i Index of object
	 * @return Returns object in list at index i.
	 */
	public T getObjectAtIndex(int i){
		return list.get(i);
	}
	
	/**
	 * Removes object at an index.
	 * @param i Index of object
	 * @return Returns object removed at index i of list.
	 */
	public T removeObjectAtIndex(int i){
		return list.remove(i);
	}

}