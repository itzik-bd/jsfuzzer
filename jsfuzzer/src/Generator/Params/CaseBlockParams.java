package Generator.Params;

public class CaseBlockParams implements createParams
{
	private boolean _includeDefault = false;
	
	public void setIncludeDefualt(boolean includeDefault) {
		_includeDefault = includeDefault;
	}
	
	public static boolean getIncludeDefault(createParams params) {
		if (params == null)
			return false;
		
		return ((CaseBlockParams) params)._includeDefault;
	}
}