import java.io.*;

public class GeneBankCreateBTree
{
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
		
	}
	
	public static void dumpText()
	{
		
	}
}
