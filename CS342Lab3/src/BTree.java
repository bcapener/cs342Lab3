import java.util.ArrayList;


public class BTree {
	private int rootPointer;
	private int numOfNodes;
	private int maxNumOfNodes;
	private BTreeNode<Long> currNode;
	private BTreeNode<Long> parentNode;
	private int nextPointer;	//next new node goes here
	
	//Constructor
	BTree(int maxNumOfObjects){
		this.maxNumOfNodes = maxNumOfNodes;
		
	}
	
	
	public void bTreeNodeSplit(){		//Splits current node, update parent and children nodes.
		if(currNode.getNodePointer() != rootPointer){	//If not root
			BTreeNode<Long> newNode = new BTreeNode<Long>(nextPointer);	//Create new node
			parentNode.addObject(currNode.getMiddleObject(), newNode.getNodePointer());//send middle obj to parent
			  //find index i of obj bigger than mid obj.
			  //shift all obj at i and above right by 1
			  //shift all CHPtrs at (i+1) and above right by 1
			  //insert mid obj at i  in obj array.
			  //insert pointer of new node at (i+1) in CHPtrs array
		      //right half objs and CHPtrs move to new node
		      //copy curr node Parent pointer to new node
		      //go to each new nodes children
			  //update parent pointer to new node (instead of reading each node, change PPtr, then writing it back. Create method that updates PPtr in binary file.)
			newNode.overwriteBTreeObjects(currNode.getRightObjects());			//put objects right of middle in new node
			newNode.overwriteChildPointers(currNode.getRightChildPointers());	//put right child pointers in new node
			currNode.overwriteBTreeObjects(currNode.getLeftObjects());			//remove all but left objects from current node
			currNode.overwriteChildPointers(currNode.getLeftChildPointers());	//remove all but left child pointers from current node
			newNode.updateChildernsParentPointer();	//update the parent pointers to all newNodes children
			//TODO write all node to file
			}
		else{
		      //Create 2 new nodes  node1 and node2
			BTreeNode<Long> newNode1 = new BTreeNode<Long>(nextPointer);	//Create new node
			BTreeNode<Long> newNode2 = new BTreeNode<Long>(nextPointer);	//Create new node
			newNode1.overwriteBTreeObjects(currNode.getLeftObjects());
			newNode1.overwriteChildPointers(currNode.getLeftChildPointers());
			newNode2.overwriteBTreeObjects(currNode.getRightObjects());
			newNode2.overwriteChildPointers(currNode.getRightChildPointers());
			//root keeps middle obj.
			ArrayList<BTreeObject<Long>> tempA = new ArrayList<BTreeObject<Long>>();
			tempA.add(currNode.getMiddleObject());
			currNode.overwriteBTreeObjects(tempA);
		      //root's child pointers are node1 and node2
		      //Left obj and CHPtrs go to node1
			  //PPtr is root
		      //Node1
			//go to children update parrent
		      //Node2
			//go to children update parrent
		}
	}
	
}
