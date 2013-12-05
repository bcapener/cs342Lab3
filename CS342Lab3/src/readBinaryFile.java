import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class readBinaryFile {

	public static void main(String[] args) throws IOException {
		RandomAccessFile data = new RandomAccessFile("binFile", "r");
		data.seek(0); 							//read in BTree meta-data
		int rootPointer = data.readInt();
		int numOfNodes = data.readInt();
		int maxNumOfObj = data.readInt();
		int nodeSize = data.readInt();
		int seqLength = data.readInt();
		System.out.println("Root Pointer: " + rootPointer);
		System.out.println("Number of Nodes: " + numOfNodes);
		System.out.println("Max number of objects: " + maxNumOfObj);
		System.out.println("node size: " + nodeSize);
		System.out.println("Sequence Length: " + seqLength);
		data.close();
		System.out.println();
		
		int nextNode = 20;
		File newFile = new File("binFile");
		for(int j=0; j<numOfNodes; j++){
			BTreeNode tempNode = new BTreeNode(nextNode, newFile);
			System.out.println(tempNode.toString());
			nextNode += nodeSize;
		}
		
	}

}
