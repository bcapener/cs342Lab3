import java.util.Random;


public class testBTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BTree bt = new BTree(3, false);
		Random rand = new Random();
		while(true){
			bt.add(new BTreeObject<Long>(new Long(rand.nextInt(Integer.MAX_VALUE))));
		}
	}

}
