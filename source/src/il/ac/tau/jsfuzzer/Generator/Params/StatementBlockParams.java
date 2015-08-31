package il.ac.tau.jsfuzzer.Generator.Params;

import il.ac.tau.jsfuzzer.JST.AbsStatement;

public class StatementBlockParams extends createParams
{
	private AbsStatement _firstSatement = null;
	private AbsStatement _lastSatement = null;
	private boolean		_isFunction = false;
	
	public StatementBlockParams(AbsStatement firstSatement, AbsStatement lastSatement) {
		_firstSatement = firstSatement;
		_lastSatement = lastSatement;
	}
	
	public StatementBlockParams(AbsStatement firstSatement, AbsStatement lastSatement, boolean isFunction) {
		_firstSatement = firstSatement;
		_lastSatement = lastSatement;
		_isFunction = isFunction;
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
	
	public static boolean getIsFunction(createParams params)
	{
		boolean defaultValue = false;
		return (boolean) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((StatementBlockParams) params)._isFunction;
			}
		});
	}
}