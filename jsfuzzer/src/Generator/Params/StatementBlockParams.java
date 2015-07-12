package Generator.Params;

import JST.AbsStatement;

public class StatementBlockParams extends createParams
{
	private AbsStatement _firstSatement = null;
	
	public StatementBlockParams(AbsStatement firstSatement) {
		_firstSatement = firstSatement;
	}

	public static AbsStatement getFirstStatement(createParams params)
	{
		AbsStatement defaultValue = null;
		return (AbsStatement) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((StatementBlockParams) params)._firstSatement;
			}
		});
	}
}