package Generator.Params;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import JST.Enums.LiteralTypes;

public class LiteralParams extends createParams
{
	private static final Set<LiteralTypes> DEFAULT_LITERALTYPE_SET = new HashSet<LiteralTypes>(Arrays.asList(LiteralTypes.ALL));
	
	private Set<LiteralTypes> _acceptedLiteralTypes = DEFAULT_LITERALTYPE_SET;
	
	public LiteralParams(LiteralTypes[] acceptedLiteralTypes) {
		_acceptedLiteralTypes = new HashSet<LiteralTypes>(Arrays.asList(acceptedLiteralTypes));
	}

	@SuppressWarnings("unchecked")
	public static Set<LiteralTypes> getAcceptedLiteralTypes(createParams params)
	{
		Set<LiteralTypes> defaultValue = DEFAULT_LITERALTYPE_SET;
		return (Set<LiteralTypes>) decide(params, defaultValue, new getParamField() {
			@Override
			public Object fetch(createParams params) {
				return ((LiteralParams) params)._acceptedLiteralTypes;
			}
		});
	}
	
	
}