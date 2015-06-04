package Generator.Params;

public class IdentifierParams implements createParams
{
	private double _useExistingVarProb = 0;
	
	public IdentifierParams(double useExistingVarProb) {
		_useExistingVarProb = useExistingVarProb;
	}
	
	public static double getUseExistingVarProb(createParams params) {
		if (params == null)
			return 1; // by default all generated vars must be defined
		
		return ((IdentifierParams) params)._useExistingVarProb;
	}
}