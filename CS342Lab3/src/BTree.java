import java.util.ArrayList;


public class BTree {
	private int rootPointer;
	private int numOfNodes;
	private int maxNumOfNodes;
	private BTreeNode<Long> currNode = null;
	private BTreeNode<Long> parentNode = null;
	private int nextPointer;	//next new node goes here
	private int maxNumOfObjs;
	private boolean hasCache;
	
	//Constructor
	BTree(int maxNumOfObjects, int maxNumOfNodes, boolean hasCache){
		this.maxNumOfNodes = maxNumOfNodes;
		this.hasCache = hasCache;
		this.maxNumOfObjs = maxNumOfObjects;
		currNode = new BTreeNode<Long>(nextPointer, 3);
	}
	
	public void add(BTreeObject<Long> obj){
		//currNode = retreiveNode(rootPointer);	//currnode = rootnode
		//check if node is full and split
		if(currNode.isFull()){
			this.bTreeNodeSplit();
		}
		int index = currNode.findObjIndex(obj);
		
		if(!currNode.isEmpty()){
			//check if equal, and inc freq. then return
			if(currNode.getNumOfObj() != index && obj.compareTo(currNode.getObject(index)) == 0){
				currNode.getObject(index).incFreqCount();
				return;
			}
			
			while(currNode.hasChildren()){
				parentNode = currNode;
				currNode = retreiveNode(currNode.getChildPointer(index+1));
				index = currNode.findObjIndex(obj);
				//check if equal, and inc freq. then return
				if(obj.compareTo(currNode.getObject(index)) == 0){
					currNode.getObject(index).incFreqCount();
					return;
				}
			}
		}
		currNode.add(obj, index);
		writeNode(currNode);
		//find index where object should be inserted
		//get child pointer at index+1
		//currnode == child node
		//repeat until no children
		//insert in to this childless node.
		
	}
	
	private void writeNode(BTreeNode<Long> node){	//writes nodes to cache or file
		if(hasCache){
			
		}
		else{
			
		}
	}
	
	private BTreeNode<Long> retreiveNode(int pointer){
		if(hasCache){
			//look for node in cache first
			//if not found in cache
				//look for node in file
				//write node to cache
		}
		else{
			
		}
		return null;
		
	}
	
	private void bTreeNodeSplit(){		//Splits current node, update parent and children nodes.
		if(currNode.getNodePointer() != rootPointer){	//If not root
			BTreeNode<Long> newNode = new BTreeNode<Long>(nextPointer);	//Create new node
			parentNode.addObject(currNode.getMiddleObject(), newNode.getNodePointer());//send middle obj to parent
			newNode.overwriteBTreeObjects(currNode.getRightObjects());			//put objects right of middle in new node
			newNode.overwriteChildPointers(currNode.getRightChildPointers());	//put right child pointers in new node
			currNode.overwriteBTreeObjects(currNode.getLeftObjects());			//remove all but left objects from current node
			currNode.overwriteChildPointers(currNode.getLeftChildPointers());	//remove all but left child pointers from current node
			newNode.updateChildernsParentPointer();	//update the parent pointers to all newNodes children
			//TODO write all node to file or cache
			}
		else{
		      //Create 2 new nodes  node1 and node2
			BTreeNode<Long> newNode1 = new BTreeNode<Long>(nextPointer);	//Create new node
			BTreeNode<Long> newNode2 = new BTreeNode<Long>(nextPointer);	//Create new node
			newNode1.overwriteBTreeObjects(currNode.getLeftObjects());			 //Left obj and CHPtrs go to node1
			newNode1.overwriteChildPointers(currNode.getLeftChildPointers());
			newNode2.overwriteBTreeObjects(currNode.getRightObjects());			//right obj and ChPtrs go to node2
			newNode2.overwriteChildPointers(currNode.getRightChildPointers());
			//update newNodes parent pointers
			newNode1.setParentPointer(currNode.getNodePointer());
			newNode2.setParentPointer(currNode.getNodePointer());
			//root keeps middle obj.
			ArrayList<BTreeObject<Long>> tempA = new ArrayList<BTreeObject<Long>>();
			tempA.add(currNode.getMiddleObject());
			currNode.overwriteBTreeObjects(tempA);
			//update roots child pointers
			int[] tempCP = new int[maxNumOfObjs];
			tempCP[0] = newNode1.getNodePointer();
			tempCP[1] = newNode2.getNodePointer();
			currNode.overwriteChildPointers(tempCP);	//root's child pointers are node1 and node2
			//update newNodes's Children's Parent pointer
			newNode1.updateChildernsParentPointer();	//update the parent pointers to all newNode1's children
			newNode2.updateChildernsParentPointer();	//update the parent pointers to all newNode2's children
			//TODO write all nodes to file or cache
		}
	}
	
}
