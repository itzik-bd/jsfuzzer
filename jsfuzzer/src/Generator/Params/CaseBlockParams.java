package Generator.Params;

public class CaseBlockParams extends createParams
{
	private boolean _includeDefault = false;
	
	public void setIncludeDefualt(boolean includeDefault) {
		_includeDefault = includeDefault;
	}
	
	public static boolean getIncludeDefault(createParams params)
	{
		boolean defaultValue = false;
		return (boolean) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((CaseBlockParams) params)._includeDefault;
			}
		});
	}
	
	
}