package Generator.Params;

public class VarDeclerationParams implements createParams
{
	private final boolean _forceNewIdentifier;
	
	public VarDeclerationParams(boolean forceNewIdentifier) {
		_forceNewIdentifier = forceNewIdentifier;
	}
	
	public static boolean getForceNewIdentifier(createParams params) {
		if (params == null)
			return false;
		
		return ((VarDeclerationParams) params)._forceNewIdentifier;
	}
}