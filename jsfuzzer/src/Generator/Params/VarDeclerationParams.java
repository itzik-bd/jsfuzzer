package Generator.Params;

import Utils.ThreeVal;

public class VarDeclerationParams extends createParams
{
	/** null means dafault behavior */
	private final Boolean _forceNewIdentifier;

	/** null means dafault behavior (random) */
	private final Integer _numDeclerators;
	
	/** True if all declerators are initialized, False if all declerators are not init, 
	 * Undef for default behavior (randomly init) */
	private final ThreeVal _hasInitValues; 
	
	public VarDeclerationParams(Boolean forceNewIdentifier, Integer numDeclerators, Boolean hasInitValue) 
	{
		_forceNewIdentifier = forceNewIdentifier;
		_numDeclerators = numDeclerators;
		_hasInitValues = ThreeVal.parse(hasInitValue);
	}
	
	public static boolean getForceNewIdentifier(createParams params)
	{
		boolean defaultValue = false;
		return (boolean) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((VarDeclerationParams) params)._forceNewIdentifier;
			}
		});
	}
	
	public static int getNumDeclerators(createParams params)
	{
		int defaultValue = 0; //means randomly choose number of decleators 
		return (int) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((VarDeclerationParams) params)._numDeclerators;
			}
		});
	}

	public static ThreeVal getHasInitValue(createParams params)
	{
		ThreeVal defaultValue = ThreeVal.UNDEF; //means randomly choose of each var
		return (ThreeVal) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((VarDeclerationParams) params)._hasInitValues;
			}
		});
	}

}