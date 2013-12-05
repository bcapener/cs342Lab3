import java.io.*;
import java.util.StringTokenizer;

public class GeneBankCreateBTree
{
	static BTree geneBankTree;
	static boolean useCache = false;
	static int degree;
	static File inputFile;
	static int sequenceLength;
	static int cacheSize;
	static int debugLevel;
	
	public static void main(String[] args)
	{
		if (args.length < 4 || args.length > 6)
		{
			System.out.println("Incorrect arguments.");
			argumentFormat();
		}
		
		if(args[0] == "1") useCache = true;
		
		if (args[1] == "0")
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
			if (args[4] == "0" | args[4] == "1")
			{
				debugLevel = Integer.valueOf(args[4]);
			}
			else
			{
				cacheSize = Integer.valueOf(args[4]);
			}
		}
		
		buildTree(degree, sequenceLength);
		if (debugLevel == 1)
		{
			dumpText();
		}
		
	}
	
	public static void argumentFormat()
	{
		
	}
	
	public static int findDegree()
	{
		int degreeValue = 0;
		return degreeValue;
	}
	
	public static void buildTree(int degree, int sequenceLength)
	{
		if (useCache == true)
		{
			//BTree geneBankTree = new BTree(cacheSize, degree, sequenceLength)
		}
		else 
		{
			//BTree geneBankTree = new BTree(degree, sequenceLength)
		}
		
		//parse file?
		
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
						searchingSequence = !searchingSequence;
						
						//where it parses the 4 different DNA types to add objects o tree
						if(searchingSequence){
							char[] data = s.toCharArray();
							//checks characters of each token
							for(int i = data.length; i < 0; i++){
								String newData = convertCharacterToBinary(data[i]);
								binaryString = binaryString + newData;
								if(binaryString.length() > sequenceLength*2){
									binaryString = binaryString.substring(2);
								}
								//adds object to tree if sequence changed and sequence is right length
								if(!newData.equals(null) && binaryString.length() == sequenceLength*2){
									addSequenceToBTree(binaryString);
								}
							}
						}
					}
				}
				
			}
		} catch (IOException e) {
			System.out.println (e.getMessage());
			e.printStackTrace();
		}
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
		return null;
	}

	//needs more work
	private static void addSequenceToBTree(String sequence) throws IOException{
		//convert binary string to long
		//create BTreeObject with long
		//add object to  BTree
		long key = 0;
		char[] data = sequence.toCharArray();
		for(int i = data.length - 1; i >= 0; i--){
			if(data[i] == '1'){
				key = (long) (key + Math.pow(2, i));
			}
		}
		BTreeObject object = new BTreeObject(key);	
		geneBankTree.add(object);
	}
}