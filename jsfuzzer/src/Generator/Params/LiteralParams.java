package Generator.Params;

public class LiteralParams extends createParams
{
	private boolean _onlyIntOrString = false;
	
	public LiteralParams(boolean onlyIntOrString) {
		_onlyIntOrString = onlyIntOrString;
	}

	public void setIncludeDefualt(boolean onlyIntOrString) {
		_onlyIntOrString = onlyIntOrString;
	}
	
	public static boolean getOnlyIntOrString(createParams params)
	{
		boolean defaultValue = false;
		return (boolean) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((LiteralParams) params)._onlyIntOrString;
			}
		});
	}
	
	
}