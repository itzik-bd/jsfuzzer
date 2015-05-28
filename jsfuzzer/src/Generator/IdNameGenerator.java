package Generator;

public abstract class IdNameGenerator 
{
	private static long _nameIdCounter = 0;
	
	public static String getNextFreeName()
	{
		++_nameIdCounter;
		return ("var" + _nameIdCounter);
	}
}
