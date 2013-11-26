import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;


public class BTreeNode<E extends Comparable<E>> {
	//Data Fields
	private int parentPointer;
	private int[] childPointers;
	private ArrayList<BTreeObject<E>> treeObjects;
	private int numOfObjects;	//number of objects in the node
	private int numOfPointers;	//number of child pointers
	private int nodePointer;	//the nth byte in the file where node is located
	private int maxNumOfObjects;
	
	//Constructors
	public BTreeNode(int nodePointer, int maxNumOfObj){
		this.nodePointer = nodePointer;
		this.childPointers = new int[maxNumOfObj+1];
		this.treeObjects = new ArrayList<BTreeObject<E>>(maxNumOfObj);
		this.maxNumOfObjects = maxNumOfObj;
	}
	
	//used for reading in a node from binary file
	public BTreeNode(int nodePointer){
		this.bTreeNodeRead();
	}
	
	public int findObjIndex(BTreeObject<E> newTreeObj){
		Iterator<BTreeObject<E>> it = treeObjects.iterator();
		int index=0;
		while(it.hasNext()){	//while there are still objects in the array
			BTreeObject<E> existingTreeObj = it.next();		//next object from array
			if(newTreeObj.compareTo(existingTreeObj) <= 0){	//new object is less than or equal
				return index;
			}
			else{		//new object is greater than
				index++;
			}
		}
		return index;
	}
	
	public void add(BTreeObject<E> newObj, int index){
		treeObjects.add(index, newObj);			//insert obj at index
		this.numOfObjects++;
	}
	/*
	//used when adding object to a leaf node
	public int addObject(BTreeObject<E> newTreeObj){
		//non full node is expected
		Iterator<BTreeObject<E>> it = treeObjects.iterator();
		int index=0;
		while(it.hasNext()){	//while there are still objects in the array
			BTreeObject<E> existingTreeObj = it.next();		//next object from array
			if(newTreeObj.compareTo(existingTreeObj) < 0){	//new object is less
				treeObjects.add(index, newTreeObj);			//insert obj at index
				return index;
			}
			else if(newTreeObj.compareTo(existingTreeObj) == 0){	//objects are equal
				existingTreeObj.incFreqCount(); 	//Increment the frequency count of the object.
				return -1;
			}
			else{		//new object is greater than
				index++;
			}
		}
		treeObjects.add(newTreeObj);		//insert obj at end
		return index;
		
	}
	*/
	//used when splitting a child node, this the parent node adds the 
	//child's middle object, and the pointer to the new node.
	public void addObject(BTreeObject<E> to, int ChPtr){
		//int index = this.addObject(to);
		int index = this.findObjIndex(to);
		this.add(to, index);
		
		//add child pointer to index+1;
		//TODO add some bounds checking
		for(int i=numOfPointers; i>index+1; i--){
			childPointers[i] = childPointers[i-1];
		}
		childPointers[index+1] = ChPtr;
		numOfPointers++;
	}
	
	public void overwriteBTreeObjects(ArrayList<BTreeObject<E>> newTreeObjects){
		treeObjects = newTreeObjects;
		numOfObjects = treeObjects.size();
	}
	
	public void overwriteChildPointers(int[] newChildPointer){
		childPointers = newChildPointer;
		numOfPointers=0;
		for(int i=0; i<childPointers.length; i++){
			if(childPointers[i] > 0){
				numOfPointers++;
			}
		}
	}
	
	public BTreeObject<E> getMiddleObject(){
		//returns the middle object.
		int index;
		index = (int) Math.floor(treeObjects.size()/2);
		return treeObjects.get(index);
	}
	
	public ArrayList<BTreeObject<E>> getRightObjects(){
		//returns the objects to the right of middle object
		ArrayList<BTreeObject<E>> tempA = new ArrayList<BTreeObject<E>>();
		int index;
		index = (int) Math.floor(treeObjects.size()/2);	//get index of middle object
		for(int i=index+1; i<treeObjects.size(); i++){
			tempA.add(treeObjects.get(i));
		}
		return tempA;
	}
	
	public ArrayList<BTreeObject<E>> getLeftObjects(){
		//returns the objects to the Left of middle object
		ArrayList<BTreeObject<E>> tempA = new ArrayList<BTreeObject<E>>();
		int index;
		index = (int) Math.floor(treeObjects.size()/2);	//get index of middle object
		for(int i=0; i<index; i++){
			tempA.add(treeObjects.get(i));
		}
		return tempA;
	}
	
	public BTreeObject<E> getObject(int index){
		return treeObjects.get(index);
	}
	
	public int getChildPointer(int index){
		return childPointers[index];
	}
	
	public int[] getRightChildPointers(){
		int index;
		int[] tempA = new int[maxNumOfObjects+1];			//make sure this array is init correctly###########
		//TODO make sure length of array is greater than 4
		index = numOfPointers/2;
		int j=0;
		for(int i=index; i<numOfPointers; i++){
			tempA[j]  = childPointers[i];
			j++;
		}
		return tempA;
	}
	
	public int[] getLeftChildPointers(){
		int index;
		int[] tempA = new int[maxNumOfObjects+1];			//make sure this array is init correctly###########
		//TODO make sure length of array is greater than 4
		index = numOfPointers/2;
		for(int i=0; i<index; i++){
			tempA[i]  = childPointers[i];
		}
		return tempA;
	}
	
	public int getNodePointer(){	//Returns the pointer to this node
		return nodePointer;
	}
	
	public void setParentPointer(int newPP){
		parentPointer = newPP;
	}
	
	public boolean hasChildren(){
		if(numOfPointers > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getNumOfObj(){
		return numOfObjects;
	}
	
	public boolean isFull(){
		//true if the node is full
		if(maxNumOfObjects == treeObjects.size()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isEmpty(){
		return treeObjects.isEmpty();
	}
	
	public void bTreeNodeRead(){		//read in node from file and populate object from "nodePointer"
		
	}
	public void bTreeNodeWrite(){		//write this object to the file at "nodePointer"
		
	}
	
	public void updateChildernsParentPointer(){
		//update the PPtr of all children
		//dont read in all children to memory, just update PPtr in binary file.
	}
	
	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");

	    //result.append(this.getClass().getName());
	    result.append(" BTreeObjects: " + treeObjects.toString() + NEW_LINE);
	    result.append(" Child Pointers: [ ");
	    for(int i=0; i<childPointers.length; i++){
	    	result.append(childPointers[i] + " ");
	    }
	    result.append("] " + NEW_LINE);
	    return result.toString();
	}
}
