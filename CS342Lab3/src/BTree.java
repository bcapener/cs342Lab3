import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class BTree {
	private int rootPointer;
	private int numOfNodes;
	private BTreeNode currNode = null;
	private BTreeNode parentNode = null;
	private int nextPointer;	//next new node goes here
	private int maxNumOfObjs;
	//private cache treeCache;
	private boolean hasCache;
	private int nodeSize = 0;	//size of a node in bytes
	private File binFile;
	private int sequenceLength;
	
	//Constructor
	BTree(int maxNumOfObjects, int cacheSize, File binFile, int seqLength) throws IOException{
		if (cacheSize != 0){
			hasCache = true;
			//create cache object
			//treeCache = new cache(cacheSize);
		}
		else{
			hasCache = false;
		}
		this.rootPointer = 20;
		this.numOfNodes = 0;
		this.maxNumOfObjs = maxNumOfObjects;
		this.nodeSize = 4 + 4 + (12*maxNumOfObjects) + (4*(maxNumOfObjects+1)) + 4;
		this.binFile = binFile;
		this.sequenceLength = seqLength;
		this.nextPointer = 20;
		currNode = newNode();
		//make sure file is cleared of all previous data
		this.clearFile();
		this.writeMetaData();
		writeNode(currNode);
	}
	
	public void add(BTreeObject obj) throws IOException{
		currNode = retreiveNode(rootPointer);	//currnode = rootnode
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
				currNode = retreiveNode(currNode.getChildPointer(index));
				//currNode = retreiveNode(currNode.getChildPointer(index+1));
				if(currNode.isFull()){
					this.bTreeNodeSplit();
					//backup and process parent again
					//what to do here?
					currNode = parentNode;
				}
				index = currNode.findObjIndex(obj);
				//check if equal, and inc freq. then return
				if(currNode.getNumOfObj() != index && obj.compareTo(currNode.getObject(index)) == 0){
					currNode.getObject(index).incFreqCount();
					return;
				}
			}
		}
		currNode.add(obj, index);
		writeNode(currNode);
		
	}
	
	/**
	 * Used to write the BTree meta-data to file at the beginning and end of the tree creation.
	 * @throws IOException 
	 */
	public void writeMetaData() throws IOException{
		RandomAccessFile data = new RandomAccessFile(this.binFile, "rw");
		data.seek(0);
		data.writeInt(this.rootPointer);
		data.writeInt(this.numOfNodes);
		data.writeInt(this.maxNumOfObjs);
		data.writeInt(this.nodeSize);
		data.writeInt(this.sequenceLength);
		data.close();
	}
	
	public void clearFile() throws IOException{
		RandomAccessFile data = new RandomAccessFile(this.binFile, "rw");
		data.setLength(0);
		data.close();
	}
	
	private void writeNode(BTreeNode node) throws IOException{	//writes nodes to cache or file
		if(hasCache){
			
		}
		else{
			node.nodeWrite(this.binFile);
		}
	}
	
	private BTreeNode retreiveNode(int pointer) throws IOException{
		//BTreeNode<Long> tempNode;
		if(hasCache){
			//look for node in cache first
			//if not found in cache
				//look for node in file
				//write node to cache
		}
		else{
			return new BTreeNode(pointer, this.binFile);
		}
		return null;
		
	}
	
	private void bTreeNodeSplit() throws IOException{	//Splits current node, update parent and children nodes.
		if(currNode.getNodePointer() != rootPointer){	//If not root
			BTreeNode newNode = this.newNode();			//Create new node
			parentNode.addObject(currNode.getMiddleObject(), newNode.getNodePointer());	//send middle object to parent
			newNode.setParentPointer(parentNode.getNodePointer());				//update newNodes parent pointers
			newNode.overwriteBTreeObjects(currNode.getRightObjects());			//put currNode's objects right of middle in newNode
			newNode.overwriteChildPointers(currNode.getRightChildPointers());	//put currNode's right child pointers in newNode
			currNode.overwriteBTreeObjects(currNode.getLeftObjects());			//remove all but left objects from currNode
			currNode.overwriteChildPointers(currNode.getLeftChildPointers());	//remove all but left child pointers from currNode
			updateChildernsParentPointer(newNode);								//update the parent pointers to all newNodes children
			writeNode(newNode);
			writeNode(currNode);
			}
		else{
		    //Create 2 new nodes  node1 and node2
			BTreeNode newNode1 = this.newNode();	//Create new node
			BTreeNode newNode2 = this.newNode();	//Create new node
			newNode1.overwriteBTreeObjects(currNode.getLeftObjects());			 //Left obj and CHPtrs go to node1
			newNode1.overwriteChildPointers(currNode.getLeftChildPointers());
			newNode2.overwriteBTreeObjects(currNode.getRightObjects());			//right obj and ChPtrs go to node2
			newNode2.overwriteChildPointers(currNode.getRightChildPointers());
			newNode1.setParentPointer(currNode.getNodePointer());				//update newNodes parent pointers
			newNode2.setParentPointer(currNode.getNodePointer());
			ArrayList<BTreeObject> tempA = new ArrayList<BTreeObject>();		//root keeps middle obj.
			tempA.add(currNode.getMiddleObject());
			currNode.overwriteBTreeObjects(tempA);
			int[] tempCP = new int[maxNumOfObjs+1];		//update roots child pointers
			tempCP[0] = newNode1.getNodePointer();
			tempCP[1] = newNode2.getNodePointer();
			currNode.overwriteChildPointers(tempCP);	//root's child pointers are node1 and node2
			updateChildernsParentPointer(newNode1);		//update the parent pointers to all newNode1's children
			updateChildernsParentPointer(newNode2);		//update the parent pointers to all newNode2's children
			writeNode(newNode1);
			writeNode(newNode2);
			writeNode(currNode);
		}
	}
	
	private BTreeNode newNode(){
		BTreeNode tempNode = new BTreeNode(nextPointer, maxNumOfObjs);	//Create new node
		nextPointer += nodeSize;	//update nextPointer
		numOfNodes++;				//increment the number of nodes
		return tempNode;
	}
	
	public void updateChildernsParentPointer(BTreeNode node){
		//update the PPtr of all children
		for(int i=0; i<node.getNumOfChildPointers(); i++){
			BTreeNode tempNode;
			try {
				tempNode = retreiveNode(node.getChildPointer(i));	//get child node
				tempNode.setParentPointer(node.getNodePointer());	//update parent pointer to point to node
				writeNode(tempNode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
