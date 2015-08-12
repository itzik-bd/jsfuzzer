package JST.Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	
	public static final LiteralTypes[] ALL = values(); 

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

	public static LiteralTypes getRandomly(Set<LiteralTypes> acceptedLiteralTypes)
	{
		if (acceptedLiteralTypes == null || acceptedLiteralTypes.size() == 0) {
			throw new IllegalArgumentException("acceptedLiteralTypes must be a non-empty valid set");
		}
		
		List<LiteralTypes> values = new ArrayList<LiteralTypes> (Arrays.asList(values()));
				
		// remove operator whose return value type is other then desired type
		for (Iterator<LiteralTypes> iter = values.iterator(); iter.hasNext() ; ) {
			LiteralTypes litType = iter.next();
		    if (!acceptedLiteralTypes.contains(litType)) {
		        iter.remove();
		    }
		}
 
		return values.get(StdRandom.uniform(values.size()));
	}
	
	@Override
	public String toString() {
		return _token;
	}
}