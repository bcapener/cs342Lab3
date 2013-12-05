import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;


public class BTreeNode {
	//Data Fields
	private int parentPointer;
	private int[] childPointers;
	private ArrayList<BTreeObject> treeObjects;
	private int numOfObjects;	//number of objects in the node
	private int numOfPointers;	//number of child pointers
	private int nodePointer;	//the nth byte in the file where node is located
	private int maxNumOfObjects;
	
	//Constructors
	public BTreeNode(int nodePointer, int maxNumOfObj){
		this.nodePointer = nodePointer;
		this.childPointers = new int[maxNumOfObj+1];
		this.treeObjects = new ArrayList<BTreeObject>(maxNumOfObj);
		this.maxNumOfObjects = maxNumOfObj;
	}
	
	//used for reading in a node from binary file
	public BTreeNode(int nodePointer, File binFile) throws IOException{
		this.nodePointer = nodePointer;
		this.nodeRead(nodePointer, binFile);
	}
	
	public int findObjIndex(BTreeObject newTreeObj){
		Iterator<BTreeObject> it = treeObjects.iterator();
		int index=0;
		while(it.hasNext()){	//while there are still objects in the array
			BTreeObject existingTreeObj = it.next();		//next object from array
			if(newTreeObj.compareTo(existingTreeObj) <= 0){	//new object is less than or equal
				return index;
			}
			else{		//new object is greater than
				index++;
			}
		}
		return index;
	}
	
	public void add(BTreeObject newObj, int index){
		treeObjects.add(index, newObj);			//insert obj at index
		this.numOfObjects++;
	}

	//used when splitting a child node, this the parent node adds the 
	//child's middle object, and the pointer to the new node.
	public void addObject(BTreeObject to, int ChPtr){
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
	
	public void overwriteBTreeObjects(ArrayList<BTreeObject> newTreeObjects){
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
	
	public BTreeObject getMiddleObject(){
		//returns the middle object.
		int index;
		index = (int) Math.floor(treeObjects.size()/2);
		return treeObjects.get(index);
	}
	
	public ArrayList<BTreeObject> getRightObjects(){
		//returns the objects to the right of middle object
		ArrayList<BTreeObject> tempA = new ArrayList<BTreeObject>();
		int index;
		index = (int) Math.floor(treeObjects.size()/2);	//get index of middle object
		for(int i=index+1; i<treeObjects.size(); i++){
			tempA.add(treeObjects.get(i));
		}
		return tempA;
	}
	
	public ArrayList<BTreeObject> getLeftObjects(){
		//returns the objects to the Left of middle object
		ArrayList<BTreeObject> tempA = new ArrayList<BTreeObject>();
		int index;
		index = (int) Math.floor(treeObjects.size()/2);	//get index of middle object
		for(int i=0; i<index; i++){
			tempA.add(treeObjects.get(i));
		}
		return tempA;
	}
	
	public BTreeObject getObject(int index){
		return treeObjects.get(index);
	}
	
	public int getChildPointer(int index){
		return childPointers[index];
	}
	
	public int getNumOfChildPointers(){
		return this.numOfPointers;
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
	
	public int getParentPointer(){
		return parentPointer;
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
		if(maxNumOfObjects == numOfObjects){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isEmpty(){
		return treeObjects.isEmpty();
	}
	
	private void nodeRead(int pointer, File binFile) throws IOException{		//read in node from file and populate object from "nodePointer"
		RandomAccessFile data = new RandomAccessFile(binFile, "r");
		data.seek(8); 							//read in BTree meta-data
		this.maxNumOfObjects = data.readInt();
		this.childPointers = new int[maxNumOfObjects+1];
		this.treeObjects = new ArrayList<BTreeObject>(maxNumOfObjects);
		data.seek(nodePointer);					//seek to this node
		this.numOfObjects = data.readInt();		//read in BTreeNode meta-data
		this.numOfPointers = data.readInt();
		int freqCnt;
		Long key;
		for(int i=0; i<maxNumOfObjects; i++){	//read BTreeObjects
			if(i<numOfObjects){
				freqCnt = data.readInt();
				key = data.readLong();
				BTreeObject tempObject = new BTreeObject(key, freqCnt);
				this.treeObjects.add(tempObject);
			}
			else{
				data.skipBytes(12);				//skip reading in next 12 bytes, no object exists there.
			}
		}
		for(int i=0; i<maxNumOfObjects+1; i++){			//read Child Pointers
			this.childPointers[i] = data.readInt();		
		}
		this.parentPointer = data.readInt();			//read Parent Pointer
		data.close();
	}
	
	public void nodeWrite(File binFile) throws IOException{		//write this object to the file at "nodePointer"
		RandomAccessFile data = new RandomAccessFile(binFile, "rw");
		data.seek(nodePointer);					//seek to node
		data.writeInt(numOfObjects);			//write meta-data to binary file
		data.writeInt(numOfPointers);
		for(int i=0; i<maxNumOfObjects; i++){	//write TreeObjects
			if(i<numOfObjects){
				BTreeObject tempObject = treeObjects.get(i);	//get next BTreeObject
				data.writeInt(tempObject.getFreqCount());		//write the frequency count to binary file
				data.writeLong(tempObject.getKey());			//write the key to binary file
			}
			else{
				data.writeInt(0);		//where there are no BTreeObjects write zeros to binary file
				data.writeLong(0);
			}
		}
		
		for(int i=0; i<maxNumOfObjects+1; i++){		//write Child Pointers
			if(i<numOfPointers){
				data.writeInt(childPointers[i]);	//write child pointer to binary file
			}
			else{
				data.writeInt(0);		//where there are no child pointers write zeros to binary file
			}
		}
		data.writeInt(parentPointer);	//write Parent Pointer
		data.close();
	}
	
	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    result.append(this.numOfObjects + " Objects" + NEW_LINE);
	    result.append(this.numOfPointers + " Child Pointers" + NEW_LINE);
	    result.append("Node Pointer: " + this.nodePointer + NEW_LINE);
	    result.append(" BTreeObjects: " + treeObjects.toString() + NEW_LINE);
	    result.append(" Child Pointers: [ ");
	    for(int i=0; i<childPointers.length; i++){
	    	result.append(childPointers[i] + " ");
	    }
	    result.append("] " + NEW_LINE);
	    result.append("Parent Pointer: " + this.parentPointer + NEW_LINE);
	    return result.toString();
	}
}
