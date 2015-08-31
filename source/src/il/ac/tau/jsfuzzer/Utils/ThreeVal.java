package il.ac.tau.jsfuzzer.Utils;

public enum ThreeVal
{
	TRUE,
	FALSE,
	UNDEF;
	
	public static ThreeVal parse(Boolean bool)
	{
		if(bool == null)
			return UNDEF;
		if(bool == true)
			return TRUE;
		return FALSE;
	}
}
