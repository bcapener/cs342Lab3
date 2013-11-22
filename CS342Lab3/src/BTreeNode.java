import java.util.ArrayList;


public class BTreeNode<E> {
	//Data Fields
	private int parentPointer;
	private int[] childPointers;
	private ArrayList<BTreeObject<E>> treeObjects;
	private int numOfObjects;	//number of objects in the node
	private int nodePointer;	//the nth byte in the file where node is located
	
	//Constructors
	public BTreeNode(int nodePointer, int maxNumOfObj){
		this.nodePointer = nodePointer;
		this.childPointers = new int[maxNumOfObj+1];
		this.treeObjects = new ArrayList<BTreeObject<E>>(maxNumOfObj);
		
	}
	
	//used for reading in a node from binary file
	public BTreeNode(int nodePointer){
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
	public void addObject(BTreeObject<E> to, int ChPtr){
		int index = this.addObject(to);
		//add child pointer to index+1;
	}
	
	public void overwriteBTreeObjects(ArrayList<BTreeObject<E>> newTreeObjects){
		
	}
	
	public void overwriteChildPointers(int[] newChildPointer){
		
	}
	
	public BTreeObject<Long> getMiddleObject(){
		//returns the middle object.
		return null;
	}
	
	public ArrayList<BTreeObject<E>> getRightObjects(){
		//returns the objects to the right of middle object
		return null;
	}
	
	public ArrayList<BTreeObject<E>> getLeftObjects(){
		//returns the objects to the Left of middle object
		return null;
	}
	
	public int[] getRightChildPointers(){
		return null;
	}
	
	public int[] getLeftChildPointers(){
		return null;
	}
	
	public int getNodePointer(){
		return nodePointer;
	}
	
	public void updateTreeObjects(BTreeObject<E>[] tos){
		//overwrite treeObjects with given array of objects
		//will be used with getLeftObjects()
		//or with getMiddleObject() when splitting the root
		//this.treeObjects = tos;
	}
	
	public boolean isFull(){
		//true if the node is full
		return false;
	}
	
	public void bTreeNodeRead(){		//read in node from file and populate object from "nodePointer"
		
	}
	public void bTreeNodeWrite(){		//write this object to the file at "nodePointer"
		
	}
	/*
	public void bTreeNodeSplit(int nextPointer){		//Split node, update parent and children nodes.
		if(!this.isRoot){
			BTreeNode<Long> newNode = new BTreeNode<Long>(nextPointer);	//Create new node
			//this.getMiddleObject()//send middle obj to parent
			  //find index i of obj bigger than mid obj.
			  //shift all obj at i and above right by 1
			  //shift all CHPtrs at (i+1) and above right by 1
			  //insert mid obj at i  in obj array.
			  //insert pointer of new node at (i+1) in CHPtrs array
		      //right half objs and CHPtrs move to new node
		      //copy curr node Parent pointer to new node
		      //go to each new nodes children
			  //update parent pointer to new node (instead of reading each node, change PPtr, then writing it back. Create method that updates PPtr in binary file.)
			}
		else{
		      //Create 2 new nodes  node1 and node2
		      //root keeps middle obj.
		      //root's child pointers are node1 and node2
		      //Left obj and CHPtrs go to node1
			  //PPtr is root
		      //Node1
			//go to children update parrent
		      //Node2
			//go to children update parrent
		}
	}
	*/
	public void updateChildernsParentPointer(){
		//update the PPtr of all children
		//dont read in all children to memory, just update PPtr in binary file.
	}
}
