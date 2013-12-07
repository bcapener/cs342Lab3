import java.io.*;
import java.util.StringTokenizer;

public class GeneBankCreateBTree
{
	static BTree geneBankTree;
	static boolean useCache = false;
	static Integer degree;
	static File inputFile;
	static Integer sequenceLength; //vla
	static int cacheSize = 0;
	static int debugLevel;
	static final int METADATASIZE = 64;
	static final int POINTERSIZE = 32;
	static final int OBJECTSIZE = 96;
	
	public static void main(String[] args) throws IOException
	{
		if (args.length < 4 || args.length > 6)
		{
			System.out.println("Incorrect arguments.");
			argumentFormat();
		}
		
		if(args[0].equals("1")) useCache = true;
		
		if (args[1].equals("0"))
		{
			degree = findDegree();
		}
		else 
		{
			degree = Integer.valueOf(args[1]);
		}
		
		inputFile = new File(args[2]);
		sequenceLength = Integer.valueOf(args[3]);
		
		if (args.length == 6)
		{
			cacheSize = Integer.valueOf(args[4]);
			debugLevel = Integer.valueOf(args[5]);
		}
		else if(args.length == 5)
		{
			if (args[4].equals("0") | args[4].equals("1"))
			{
				debugLevel = Integer.valueOf(args[4]);
			}
			else
			{
				cacheSize = Integer.valueOf(args[4]);
			}
		}
		
		buildTree();
		
		if (debugLevel == 1) dumpText();
		
	}
	
	public static void argumentFormat()
	{
		System.out.println("Command line should read as follows:");
		System.out.println("java GeneBankCreateBTree <0/1(no/with Cache) <degree> <gbk file> <sequence length>");
		System.out.println("[<cache size>] [<debug level>");
	}
	
	public static int findDegree()
	{
		int nodeValue;
		int degreeValue = 0;
		
		nodeValue = (OBJECTSIZE * 2) + (POINTERSIZE * 2);
		degreeValue = (4096 - (METADATASIZE + POINTERSIZE - OBJECTSIZE))  / nodeValue;
		
		return degreeValue;
	}
	
	public static void buildTree() throws IOException
	{
		int maxObjects = (2 * degree) - 1;
		String binaryFile = inputFile.getName() + ".btree.data." + Integer.toString(sequenceLength) + "." + Integer.toString(degree);
		//String binaryFile = "/home/students/bcapener/"+inputFile.getName() + ".btree.data." + Integer.toString(sequenceLength) + "." + Integer.toString(degree);
		File binFile = new File(binaryFile);
		
		geneBankTree = new BTree(maxObjects, cacheSize, binFile, sequenceLength);
		
		BufferedReader reader;
		try {
			reader = new BufferedReader (new FileReader(inputFile));
		} catch (IOException e) {
			System.out.println("file was not found. Program Cannot run!");
			e.printStackTrace();
			return;
		}
		String line;
		String binaryString = "";
		StringTokenizer tokenizer;
		boolean searchingSequence = false;
		
		//loops through lines and tokens of input file
		try {
			while((line  = reader.readLine()) != null){
				tokenizer = new StringTokenizer (line);
				//loops through tokens of each line
				while(tokenizer.hasMoreTokens()){
					String s = tokenizer.nextToken();
					//toggles weather its currently in a sequence to parses data and create objects
					if(s.equals("ORIGIN")){
						searchingSequence = true;
						binaryString = "";
					}
					if(s.equals("//"))searchingSequence = false;
					//where it parses the 4 different DNA types to add objects o tree
					if(searchingSequence && !s.equals("ORIGIN")){
						char[] data = s.toCharArray();
						//checks characters of each token
						for(int i = 0; i < data.length; i++){
							String newData = convertCharacterToBinary(data[i]);
							//when newData is n, clear binaryString
							if(newData.equals("n")){
								binaryString = "";
							}
							else{
								binaryString = binaryString + newData;
							}
							if(binaryString.length() > sequenceLength*2){
								binaryString = binaryString.substring(2);
							}
							//adds object to tree if sequence changed and sequence is right length
							if(!newData.equals("") && binaryString.length() == sequenceLength*2){
								addSequenceToBTree(binaryString);
							}
						}
					}
				}
				
			}
		} catch (IOException e) {
			System.out.println (e.getMessage());
			e.printStackTrace();
		}
		geneBankTree.writeMetaData();
		geneBankTree.writeCacheToFile();
	}
	
	public static void dumpText()
	{
		
	}
	
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
		else if(c == 'n' || c == 'N'){
			return "n";
		}
		return "";
	}

	//needs more work
	private static void addSequenceToBTree(String sequence) throws IOException{
		
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
		BTreeObject object = new BTreeObject(key);	
		geneBankTree.add(object);
	}
}
