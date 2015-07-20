package Generator.Params;

import JST.Enums.DataTypes;

public class GenerateOpExprParams extends createParams
{
	private DataTypes _dataType;
	
	public GenerateOpExprParams(DataTypes dt)
	{
		_dataType = dt;
	}
	
	public void setDataType (DataTypes dt)
	{
		_dataType = dt;
	}
	
	public static DataTypes getDataType(createParams params)
	{
		DataTypes defaultValue = DataTypes.UNDEFINED;
		return (DataTypes) decide(params, defaultValue, new getParamField() 
		{
			@Override
			public Object fetch(createParams params) {
				return ((GenerateOpExprParams) params)._dataType;
			}
		});
	}

}
