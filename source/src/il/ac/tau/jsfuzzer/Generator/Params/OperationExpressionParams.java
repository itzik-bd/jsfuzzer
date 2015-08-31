package il.ac.tau.jsfuzzer.Generator.Params;

import il.ac.tau.jsfuzzer.JST.Enums.DataTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OperationExpressionParams extends createParams
{
	private static final Set<DataTypes> DEFAULT_DATATYPE_SET = new HashSet<DataTypes>(Arrays.asList(DataTypes.ALL));
	private Set<DataTypes> _dataTypesSet;
	
	public OperationExpressionParams(DataTypes... dtArr)
	{
		_dataTypesSet = new HashSet<DataTypes>(Arrays.asList(dtArr));
	}
	
	@SuppressWarnings("unchecked")
	public static Set<DataTypes> getDataType(createParams params)
	{
		Set<DataTypes> defaultValue = DEFAULT_DATATYPE_SET;
		return (Set<DataTypes>) decide(params, defaultValue, new getParamField() 
		{
			@Override
			public Object fetch(createParams params) {
				return ((OperationExpressionParams) params)._dataTypesSet;
			}
		});
	}

}
