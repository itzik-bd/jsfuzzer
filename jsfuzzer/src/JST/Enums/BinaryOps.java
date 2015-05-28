package JST.Enums;

import JST.Helper.*;

public enum BinaryOps {

	PLUS("%s + %s", "addition"),
	MINUS("%s - %s", "subtraction"),
	MULTIPLY("%s * %s", "multiplication"),
	DIVIDE("%s / %s", "division"),
	MOD("%s %% %s", "modulo"),
	
	LAND("%s && %s", "logical and"),
	LOR("%s || %s", "logical or"),
	LT("%s < %s", "less than"),
	LTE("%s <= %s", "less than or equal to"),
	GT("%s > %s", "greater than"),
	GTE("%s >= %s", "greater than or equal to"),
	
	EQUAL("%s == %s", "equality"),
	EQUALTYPE("%s === %s", "equality + type"),
	NEQUALTYPE("%s !== %s", "inequality or different type"),
	NEQUAL("%s != %s", "inequality"),
	
	SHIFTRIGHT("%s >> %s", "sign propagating right shift"),
	SHIFTLEFT("%s << %s", "shift left"),
	SHIFTRIGHTZERO("%s >>> %s", "zero-fill right shift"),
	
	BITXOR("%s ^ %s", "bitwise xor"),
	BITAND("%s & %s", "bitwise and"),
	BITOR("%s | %s", "bitwise or"),
	
	INSTANCEOF("%s instanceof %s", "instance of"),
	IN("%s in %s", "in");
	
	private String pattern;
	
	private String description;

	private BinaryOps(String pattern, String description)
	{
		this.pattern = pattern;
		this.description = description;
	}
	
	/**
	 * Returns a description of the operator.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	public String formatOp(String a, String b)
	{
		return String.format(pattern, a, b);
	}

	public static BinaryOps getRandomly() 
	{
		BinaryOps[] values = values();
		 
		return values[StdRandom.uniform(values.length)];
	}
}