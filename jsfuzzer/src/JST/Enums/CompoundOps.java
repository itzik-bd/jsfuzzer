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
	
	private String token;
	
	private String description;

	private CompoundOps(String token, String description)
	{
		this.token = token;
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
	
	public String getToken() {
		return token;
	}
}
