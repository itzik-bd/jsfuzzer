package JST.Enums;

public enum CompoundOps
{
	PLUS("+", "addition"),
	MINUS("-", "subtraction"),
	MULTIPLY("*", "multiplication"),
	DIVIDE("/", "division"),
	MOD("%", "modulo"),

	SHIFTRIGHT(">>", "sign propagating right shift"),
	SHIFTLEFT("<<", "shift left"),
	SHIFTRIGHTZERO(">>>", "zero-fill right shift"),
	
	BITXOR("^", "bitwise xor"),
	BITAND("&", "bitwise and"),
	BITOR("|", "bitwise or");
	
	private String pattern;
	
	private String description;

	private CompoundOps(String pattern, String description)
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
	
	public String getPattern() {
		return pattern;
	}
}
