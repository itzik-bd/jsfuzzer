package JST.Enums;

import java.util.List;

import Utils.StdRandom;

public enum Operator
{
	// Unary Operations
	UMINUS(1, "-%s", "unary subtraction"), 
	UPLUS(1, "+%s", "unary addition"),
	
	LNEG(1, "!%s", "logical negation"),
	BITNEG(1, "~%s", "bitwise negation"),

	PLUSPLUSLEFT(1, "++%s", "plus plus"),
	MINUSMINUSLEFT(1, "--%s", "minus minus"),
	PLUSPLUSRIGHT(1, "%s++", "plus plus"),
	MINUSMINUSRIGHT(1, "%s--", "minus minus"),
	
	DELETE(1, "delete %s", "delete object"),
	TYPEOF(1, "typeof %s", "type of"),
	VOID(1, "void %s", "void"),
	
	// Binary Operations
	PLUS(2, "%s + %s", "addition"),
	MINUS(2, "%s - %s", "subtraction"),
	MULTIPLY(2, "%s * %s", "multiplication"),
	DIVIDE(2, "%s / %s", "division"),
	MOD(2, "%s %% %s", "modulo"),
	
	LAND(2, "%s && %s", "logical and"),
	LOR(2, "%s || %s", "logical or"),
	LT(2, "%s < %s", "less than"),
	LTE(2, "%s <= %s", "less than or equal to"),
	GT(2, "%s > %s", "greater than"),
	GTE(2, "%s >= %s", "greater than or equal to"),
	
	EQUAL(2, "%s == %s", "equality"),
	EQUALTYPE(2, "%s === %s", "equality + type"),
	NEQUALTYPE(2, "%s !== %s", "inequality or different type"),
	NEQUAL(2, "%s != %s", "inequality"),
	
	SHIFTRIGHT(2, "%s >> %s", "sign propagating right shift"),
	SHIFTLEFT(2, "%s << %s", "shift left"),
	SHIFTRIGHTZERO(2, "%s >>> %s", "zero-fill right shift"),
	
	BITXOR(2, "%s ^ %s", "bitwise xor"),
	BITAND(2, "%s & %s", "bitwise and"),
	BITOR(2, "%s | %s", "bitwise or"),
	
	INSTANCEOF(2, "%s instanceof %s", "instance of"),
	IN(2, "%s in %s", "in"),
	
	// Ternary Operations
	CONDOP(3, "%s ? %s : %s ", "conditional operation");
	
	private int _numOperands;
	private String _pattern;
	private String _description;

	private Operator(int numOperands, String pattern, String description) {
		_numOperands = numOperands;
		_pattern = pattern;
		_description = description;
	}
	
	public int getNumOperands() {
		return _numOperands;
	}
	
	public String formatOp(List<String> params) {
		return String.format(_pattern, params.toArray(new Object[params.size()]));
	}

	public static Operator getRandomly() {
		Operator[] values = values();
 
		return values[StdRandom.uniform(values.length)];
	}
	
	@Override
	public String toString() {
		return _description;
	}
}