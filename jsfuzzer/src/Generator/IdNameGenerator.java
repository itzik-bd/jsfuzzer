package Generator;

public abstract class IdNameGenerator 
{
	private static long _nameIdCounter = 0;
	private static long _nameFuncCounter = 0;
	
	public static String getNextVarFreeName()
	{
		++_nameIdCounter;
		return ("var" + _nameIdCounter);
	}
	
	public static String getNextFunctionFreeName()
	{
		++_nameFuncCounter;
		return ("func" + _nameFuncCounter);
	}
}