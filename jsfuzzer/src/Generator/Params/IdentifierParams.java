package Generator.Params;

public class IdentifierParams extends createParams
{
	private double _useExistingVarProb;
	
	public IdentifierParams(double useExistingVarProb) {
		_useExistingVarProb = useExistingVarProb;
	}

	public static double getUseExistingVarProb(createParams params)
	{
		double defaultValue = 1.0;
		return (double) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((IdentifierParams) params)._useExistingVarProb;
			}
		});
	}
}