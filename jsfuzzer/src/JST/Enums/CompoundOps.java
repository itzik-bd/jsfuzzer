package JST.Enums;

import Utils.StdRandom;

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
	
	private String _token;
	private String _description;

	private CompoundOps(String token, String description) {
		this._token = token;
		this._description = description;
	}
	
	public String getDescription() {
		return _description;
	}
	
	public String getToken() {
		return _token;
	}
	
	public static CompoundOps getRandomly()	{
		CompoundOps[] values = values();
		 
		return values[StdRandom.uniform(values.length)];
	}
}
