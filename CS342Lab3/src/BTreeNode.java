import java.util.ArrayList;


public class BTreeNode<E> {
	//Data Fields
	private int parentPointer;
	private int[] childPointers;
	private ArrayList<BTreeObject<E>> treeObjects;
	private int numOfObjects;	//number of objects in the node
	private int byteOffset;		//the nth byte in the file where node is located
	
	//Constructors
	public BTreeNode(int byteOffset, int maxNumOfObj){
		this.byteOffset = byteOffset;
		this.childPointers = new int[maxNumOfObj+1];
		this.treeObjects = new ArrayList<BTreeObject<E>>(maxNumOfObj);
		
	}
	
	//used for reading in a node from binary file
	public BTreeNode(int byteOffset){
		this.bTreeNodeRead();
	}
	
	//used when adding object to a leaf node
	public int addObject(BTreeObject<E> to){
		//add new object
		//return index it was added to
		return 0;
		
	}
	//used when splitting a child node, this the parent node adds the 
	//child's middle object, and the pointer to the new node.
	public void addObject(BTreeObject<E> to, Long ChPtr){
		int index = this.addObject(to);
		//add child pointer to index+1;
	}
	
	public BTreeObject<Long> getMiddleObject(){
		//returns the middle object.
		return null;
	}
	
	public BTreeObject<Long>[] getRightObjects(){
		//returns the objects to the right of middle object
		return null;
	}
	
	public BTreeObject<Long>[] getLeftObjects(){
		//returns the objects to the Left of middle object
		return null;
	}
	
	public void updateTreeObjects(BTreeObject<E>[] tos){
		//overwrite treeObjects with given array of objects
		//will be used with getLeftObjects()
		//or with getMiddleObject() when splitting the root
		this.treeObjects = tos;
	}
	
	public boolean isFull(){
		//true if the node is full
		return false;
	}
	
	
	public void bTreeNodeRead(){		//read in node from file and populate object from "byteOffset"
		
	}
	public void bTreeNodeWrite(){		//write this object to the file at "byteOffset"
		
	}
	public void bTreeNodeSplit(){		//Split node, update parent and children nodes.
		
	}
	
	public void updateChildernsParentPointer(){
		//update the PPtr of all children
		//dont read in all children to memory, just update PPtr in binary file.
	}
}
