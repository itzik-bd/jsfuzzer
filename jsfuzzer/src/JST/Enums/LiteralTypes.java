package JST.Enums;

import Utils.StdRandom;

public enum LiteralTypes
{
	UNDEFINED("undefined", DataTypes.UNDEFINED),

	NULL("null", DataTypes.OBJECT),

	INFINITY("Infinity", DataTypes.NUMBER),
	NAN("NaN", DataTypes.NUMBER),
	NUMBER(null, DataTypes.NUMBER),

	STRING(null, DataTypes.STRING),

	TRUE("true", DataTypes.BOOLEAN),
	FALSE("false", DataTypes.BOOLEAN);

	private String _token;
	private DataTypes _type;

	private LiteralTypes(String token, DataTypes type) {
		_token = token;
		_type = type;
	}

	public boolean isSingleValue() {
		return (_token != null);
	}

	public String getToken() {
		return _token;
	}

	public DataTypes getTypeValue() {
		return _type;
	}

	public static LiteralTypes getRandomly() {
		LiteralTypes[] values = values();

		return values[StdRandom.uniform(values.length)];
	}
	
	public static LiteralTypes getNonTrivialRandomly() {
		LiteralTypes[] values = {NUMBER, STRING, TRUE, FALSE, INFINITY};

		return values[StdRandom.uniform(values.length)];
	}
	
	@Override
	public String toString() {
		return _token;
	}
}