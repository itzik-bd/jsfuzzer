package Generator.Params;

import JST.AbsStatement;

public class StatementBlockParams extends createParams
{
	private AbsStatement _firstSatement = null;
	private AbsStatement _lastSatement = null;
	
	public StatementBlockParams(AbsStatement firstSatement, AbsStatement lastSatement) {
		_firstSatement = firstSatement;
		_lastSatement = lastSatement;
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
	
	public static AbsStatement getLastStatement(createParams params)
	{
		AbsStatement defaultValue = null;
		return (AbsStatement) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((StatementBlockParams) params)._lastSatement;
			}
		});
	}
}