import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Brandon Capener
 * @author Tina Crimps
 * @author Daniel Delagarza
 * 
 * Searches a BTree based on a given query file
 */
public class GeneBankSearch
{
	static boolean useCache = false;
	static File bTreeFile;
	static File queryFile;
	static int cacheSize;
	static int debugLevel;
	static BTree geneBankTree;
	
	public static void main(String[] args)
	{
		if (args.length < 3 || args.length > 5)
		{
			System.out.println("Incorrect arguments.");
			argumentFormat();
		}
		
		if(args[0].equals("1")) useCache = true;
		
		bTreeFile = new File(args[1]);
		queryFile = new File(args[2]);
		
		
		if (args.length == 5)
		{
			cacheSize = Integer.valueOf(args[3]);
			debugLevel = Integer.valueOf(args[4]);
		}
		else if(args.length == 4)
		{
			if (args[3].equals("0") | args[3].equals("1"))
			{
				debugLevel = Integer.valueOf(args[3]);
			}
			else
			{
				cacheSize = Integer.valueOf(args[3]);
			}
		}
		
		try {
			searchTree();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Presents standard argument formatting when command line
	 * does not contain correct parameters 
	 */
	public static void argumentFormat()
	{
		System.out.println("Command line should read as follows:");
		System.out.println("java GeneBankSearch <0/1(no/with Cache) <btree file> <query file>");
		System.out.println("[<cache size>] [<debug level>");
	}
	
	/**
	 * Searches tree based on query
	 * @throws IOException
	 */
	public static void searchTree() throws IOException
	{
		//need to add
		geneBankTree = new BTree(cacheSize, bTreeFile);
		BufferedReader reader;
		try {
			reader = new BufferedReader (new FileReader(queryFile));
		} catch (IOException e) {
			System.out.println("file was not found. Program Cannot run!");
			e.printStackTrace();
			return;
		}
		String line;
		String binaryString = "";
		try {
			while((line  = reader.readLine()) != null){
				char[] data = line.toCharArray();
				Integer sequenceLength = data.length;
				binaryString = "";
				//checks characters of each token
				for(int i = 0; i < data.length; i++){
					String newData = convertCharacterToBinary(data[i]);
					binaryString = binaryString + newData;
					if(binaryString.length() > sequenceLength*2){
						binaryString = binaryString.substring(2);
					}
					//adds object to tree if sequence changed and sequence is right length
					if(!newData.equals(null) && binaryString.length() == sequenceLength*2){
						findSequence(binaryString, line);
					}
				}
			}
		} catch (IOException e1) {
			System.out.println (e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println("file cannot close.");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Converts character to binary value
	 * @param c char value to be converted
	 * @return binary value of character passed
	 */	
	private static String convertCharacterToBinary(char c){
		if(c == 'a' || c == 'A'){
			return "00";
		}
		else if(c == 't' || c == 'T'){
			return "11";
		}
		else if(c == 'c' || c == 'C'){
			return "01";
		}
		else if(c == 'g' || c == 'G'){
			return "10";
		}
		return null;
	}
	
	/**
	 * Locates sequence
	 * @param sequence String sequence to be found
	 * @param line String line on which sequence is located
	 * @throws IOException
	 */
	private static void findSequence(String sequence, String line) throws IOException{
		
		long key = 0;
		
		//converts binary string to a long
		char[] data = sequence.toCharArray();
		int exp = 0;
		for(int i = data.length - 1; i >= 0; i--){
			if(data[i] == '1'){
				key = (long) (key + Math.pow(2, exp));
			}
			exp++;
		}
		BTreeObject objectToFind = new BTreeObject(key);	
		BTreeObject foundObject = geneBankTree.find(objectToFind);
		if(foundObject != null && key == foundObject.getKey()){		//redundant
			System.out.print(line + ": ");
			System.out.println(foundObject.getFreqCount());
		}
	}
	
}
