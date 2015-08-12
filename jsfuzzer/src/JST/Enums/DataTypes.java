package JST.Enums;

public enum DataTypes
{
	UNDEFINED,
	STRING,
	NUMBER,
	BOOLEAN,
	ARRAY,
	OBJECT,
	FUNCTION;
	
	public static final DataTypes[] ALL = values();
	
	public static final DataTypes[] NONE = values();
	
	public static final DataTypes[] UNDEFINED_sig = new DataTypes[] { UNDEFINED };
	public static final DataTypes[] STRING_sig = new DataTypes[] { STRING };
	public static final DataTypes[] NUMBER_sig = new DataTypes[] { NUMBER };
	public static final DataTypes[] BOOLEAN_sig = new DataTypes[] { BOOLEAN };
	public static final DataTypes[] ARRAY_sig = new DataTypes[] { ARRAY };
	public static final DataTypes[] OBJECT_sig = new DataTypes[] { OBJECT };
	public static final DataTypes[] FUNCTION_sig = new DataTypes[] { FUNCTION };
}