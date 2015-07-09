package Generator.Params;

public class GenerateExprParams extends createParams
{
	private boolean _statementExpression = false;
	
	public void setStatementExpression ()
	{
		_statementExpression = true;
	}
	
	public static boolean getStatementExpression(createParams params)
	{
		boolean defaultValue = false;
		return (boolean) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((GenerateExprParams) params)._statementExpression;
			}
		});
	}

}
