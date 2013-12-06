import java.io.File;

public class GeneBankSearch
{
	static boolean useCache = false;
	static File bTreeFile;
	static File queryFile;
	static int cacheSize;
	static int debugLevel;
	
	public static void main(String[] args)
	{
		if (args.length < 3 || args.length > 5)
		{
			System.out.println("Incorrect arguments.");
			argumentFormat();
		}
		
		if(args[0] == "1") useCache = true;
		
		bTreeFile = new File(args[1]);
		queryFile = new File(args[2]);
		
		
		if (args.length == 5)
		{
			cacheSize = Integer.valueOf(args[3]);
			debugLevel = Integer.valueOf(args[4]);
		}
		else if(args.length == 4)
		{
			if (args[3] == "0" | args[3] == "1")
			{
				debugLevel = Integer.valueOf(args[3]);
			}
			else
			{
				cacheSize = Integer.valueOf(args[3]);
			}
		}
		
		searchTree();
		
	}
	
	public static void argumentFormat()
	{
		System.out.println("Command line should read as follows:");
		System.out.println("java GeneBankSearch <0/1(no/with Cache) <btree file> <query file>");
		System.out.println("[<cache size>] [<debug level>");
	}
	
	public static void searchTree()
	{
		//need to add
	}
}
