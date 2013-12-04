import java.io.File;
import java.io.IOException;
import java.util.Random;


public class testBTree {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File binFile = new File("binFile");
		BTree bt = new BTree(3, 0, binFile, 0);
		Random rand = new Random();
		int[] array = {10,20,30,5,11,6};
		/*
		while(true){
			bt.add(new BTreeObject(new Long(rand.nextInt(Integer.MAX_VALUE))));
		}
		*/
		for(int i=0; i<array.length; i++){
			bt.add(new BTreeObject(new Long(array[i])));
		}
	}

}
