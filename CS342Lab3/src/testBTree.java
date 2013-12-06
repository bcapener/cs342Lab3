import java.io.File;
import java.io.IOException;
//import java.util.Random;


public class testBTree {

	public static void main(String[] args) throws IOException {
		File binFile = new File("binFile");
		BTree bt = new BTree(3, 100, binFile, 0);
		//Random rand = new Random();
		int[] array = {200,200,100,100,300,300,50,50,150,150,25,25,75,75,12,12,32,32,6,6,18,18,15,15,33,33,200,100,300,50,150,25,75,12,32,6,18,15,33};
		/*
		while(true){
			bt.add(new BTreeObject(new Long(rand.nextInt(Integer.MAX_VALUE))));
		}
		*/
		for(int i=0; i<array.length; i++){
			bt.add(new BTreeObject(new Long(array[i])));
			//bt.writeMetaData();
		}
		bt.writeMetaData();
		bt.writeCacheToFile();
	}

}
