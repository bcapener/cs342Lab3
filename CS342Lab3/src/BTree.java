import java.io.File;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class BTree {
	private int rootPointer;
	private int numOfNodes;
	private BTreeNode rootNode = null;
	private BTreeNode currNode = null;
	private BTreeNode parentNode = null;
	private int nextPointer;	//next new node goes here
	private int maxNumOfObjs;
	private Cache<BTreeNode> bTreeCache;
	private boolean hasCache;
	private int nodeSize = 0;	//size of a node in bytes
	private File binFile;
	private int sequenceLength;
	
	//Constructor
	public BTree(int maxNumOfObjects, int cacheSize, File binFile, int seqLength) throws IOException{
		if (cacheSize != 0){
			hasCache = true;
			//create cache object
			bTreeCache = new Cache<BTreeNode>(cacheSize);
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
		rootNode = newNode();
		//make sure file is cleared of all previous data
		this.clearFile();
		this.writeMetaData();
		writeNode(rootNode);
	}
	
	//constructor used for searching a binary file
	public BTree(int cacheSize, File binFile) throws IOException{
		if (cacheSize != 0){
			hasCache = true;
			//create cache object
			bTreeCache = new Cache<BTreeNode>(cacheSize);
		}
		else{
			hasCache = false;
		}
		this.binFile = binFile;
		RandomAccessFile data = new RandomAccessFile(this.binFile, "rw");
		data.seek(0);
		this.rootPointer = data.readInt();
		this.numOfNodes = data.readInt();
		this.maxNumOfObjs = data.readInt();
		this.nodeSize = data.readInt();
		this.sequenceLength = data.readInt();
		data.close();
		//TODO throw error if sequence lengths don't match
		rootNode = this.retrieveNode(rootPointer);
		
	}
	
	public BTreeObject find(BTreeObject obj) throws IOException{
		currNode = rootNode;
		
		int index = currNode.findObjIndex(obj);
		if(!currNode.isEmpty()){
			//check if equal, and inc freq. then return
			BTreeObject tempObject;
			if(currNode.getNumOfObj() != index){
				tempObject = currNode.getObject(index);
				if(obj.compareTo(tempObject) == 0){
					return tempObject;
				}
			}
			while(currNode.hasChildren()){
				//parentNode = currNode;
				currNode = retrieveNode(currNode.getChildPointer(index));
				
				index = currNode.findObjIndex(obj);
				//check if equal, and inc freq. then return
				if(currNode.getNumOfObj() != index){
					tempObject = currNode.getObject(index);
					if(obj.compareTo(tempObject) == 0){
						return tempObject;
					}
				}
			}
		}
		else{
			//there is something wrong
		}
		return null;	//object was not found
	}
	
	public void add(BTreeObject obj) throws IOException{
		currNode = rootNode;
		if(currNode.isFull()){		//check if node is full and split
			this.bTreeNodeSplit();
		}
		int index = currNode.findObjIndex(obj);
		if(!currNode.isEmpty()){
			//check if equal, and inc freq. then return
			if(currNode.getNumOfObj() != index && obj.compareTo(currNode.getObject(index)) == 0){
				currNode.getObject(index).incFreqCount();
				writeNode(currNode);
				return;
			}
			
			while(currNode.hasChildren()){
				parentNode = currNode;
				currNode = retrieveNode(currNode.getChildPointer(index));
				if(currNode.isFull()){
					this.bTreeNodeSplit();
					currNode = parentNode;			//backup and process parent again
				}
				index = currNode.findObjIndex(obj);
				//check if equal, and inc freq. then return
				if(currNode.getNumOfObj() != index && obj.compareTo(currNode.getObject(index)) == 0){
					currNode.getObject(index).incFreqCount();
					writeNode(currNode);
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
	
	public void writeCacheToFile() throws IOException{
		if(hasCache){
			BTreeNode tempNode = bTreeCache.removeFirstObject();
			while(tempNode != null){
				tempNode.nodeWrite(this.binFile);
				tempNode = bTreeCache.removeFirstObject();
			}
		}
	}
	
	public void clearFile() throws IOException{
		RandomAccessFile data = new RandomAccessFile(this.binFile, "rw");
		data.setLength(0);
		data.close();
	}
	
	private void writeNode(BTreeNode node) throws IOException{	//writes nodes to cache or file
		if(hasCache){
			
			BTreeNode existingNode = bTreeCache.getObject(node.getNodePointer());	//check if node already exists 
			if(existingNode != null){
				bTreeCache.removeObject(existingNode);								//if so, remove it
			}
			BTreeNode tempNode = bTreeCache.addObject(node);	//add node to cache, and get node that fell of end of cache
			if(tempNode != null){								//if node fell off the end of cache
				tempNode.nodeWrite(this.binFile);				//write node to binary file
			}
		}
		else{
			node.nodeWrite(this.binFile);
		}
	}
	
	public BTreeNode retrieveNode(int pointer) throws IOException{
		if(hasCache){
			BTreeNode retNode;
			retNode = bTreeCache.getObject(pointer);			//look for node in cache first
			if(retNode != null){								//if object found in cache
				bTreeCache.removeObject(retNode);				//remove object
				bTreeCache.addObject(retNode);	//add object to cache, catch object that might of fallen off the cache
				/*
				if(tempNode != null){
					tempNode.nodeWrite(this.binFile);			//write object that fell off cache to binary file
				}
				*/
			}
			else{												//else object was not found in cache
				retNode = new BTreeNode(pointer, this.binFile);	//look for node in file
				writeNode(retNode);								//write node to cache
			}
			return retNode;
		}
		else{
			return new BTreeNode(pointer, this.binFile);
		}
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
			writeNode(parentNode);
			}
		else{	//Split the Root node
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
				tempNode = retrieveNode(node.getChildPointer(i));	//get child node
				tempNode.setParentPointer(node.getNodePointer());	//update parent pointer to point to node
				writeNode(tempNode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	public int getRootPointer(){
		return rootPointer;
	}
}
