package Generator.Params;

public class StatementBlockParams extends createParams
{
	private String _outputString = null;
	
	public StatementBlockParams(String outputString) {
		_outputString = outputString;
	}

	public static String getOutString(createParams params)
	{
		String defaultValue = null;
		return (String) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((StatementBlockParams) params)._outputString;
			}
		});
	}
}