package JST.Enums;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Utils.StdRandom;

public enum Operator
{
	// Unary Operations
	UMINUS(1, DataTypes.NUMBER, "-%s", "unary subtraction"), 
	UPLUS(1, DataTypes.NUMBER, "+%s", "unary addition"),
	
	LNEG(1, DataTypes.BOOLEAN, "!%s", "logical negation"),
	BITNEG(1, DataTypes.NUMBER, "~%s", "bitwise negation"),

	PLUSPLUSLEFT(1, DataTypes.NUMBER, "++%s", "plus plus"),
	MINUSMINUSLEFT(1, DataTypes.NUMBER, "--%s", "minus minus"),
	PLUSPLUSRIGHT(1, DataTypes.NUMBER, "%s++", "plus plus"),
	MINUSMINUSRIGHT(1, DataTypes.NUMBER, "%s--", "minus minus"),
	
	DELETE(1, DataTypes.BOOLEAN,"delete %s", "delete object"),
	TYPEOF(1, DataTypes.STRING,"typeof %s", "type of"),
	VOID(1, DataTypes.UNDEFINED,"void %s", "void"),
	
	// Binary Operations
	PLUS(2, null, "%s + %s", "addition"),
	MINUS(2, DataTypes.NUMBER, "%s - %s", "subtraction"),
	MULTIPLY(2, DataTypes.NUMBER, "%s * %s", "multiplication"),
	DIVIDE(2, DataTypes.NUMBER, "%s / %s", "division"),
	MOD(2, DataTypes.NUMBER, "%s %% %s", "modulo"),
	
	LAND(2, DataTypes.BOOLEAN, "%s && %s", "logical and"),
	LOR(2, DataTypes.BOOLEAN, "%s || %s", "logical or"),
	LT(2, DataTypes.BOOLEAN, "%s < %s", "less than"),
	LTE(2, DataTypes.BOOLEAN, "%s <= %s", "less than or equal to"),
	GT(2, DataTypes.BOOLEAN, "%s > %s", "greater than"),
	GTE(2, DataTypes.BOOLEAN, "%s >= %s", "greater than or equal to"),
	
	EQUAL(2, DataTypes.BOOLEAN, "%s == %s", "equality"),
	EQUALTYPE(2, DataTypes.BOOLEAN, "%s === %s", "equality + type"),
	NEQUALTYPE(2, DataTypes.BOOLEAN, "%s !== %s", "inequality or different type"),
	NEQUAL(2, DataTypes.BOOLEAN, "%s != %s", "inequality"),
	
	SHIFTRIGHT(2, DataTypes.NUMBER, "%s >> %s", "sign propagating right shift"),
	SHIFTLEFT(2, DataTypes.NUMBER, "%s << %s", "shift left"),
	SHIFTRIGHTZERO(2, DataTypes.NUMBER, "%s >>> %s", "zero-fill right shift"),
	
	BITXOR(2, DataTypes.NUMBER, "%s ^ %s", "bitwise xor"),
	BITAND(2, DataTypes.NUMBER, "%s & %s", "bitwise and"),
	BITOR(2, DataTypes.NUMBER, "%s | %s", "bitwise or"),
	
	// TODO: decide what to do with these two, and do it...
	// INSTANCEOF(2, DataTypes.BOOLEAN, "%s instanceof %s", "instance of"),
	// IN(2, DataTypes.BOOLEAN, "%s in %s", "in"),
	
	// Ternary Operations
	CONDOP(3, null,"%s ? %s : %s", "conditional operation");
	
	private final int _numOperands;
	private final DataTypes _outputType;
	private final String _pattern;
	private final String _description;

	private Operator(int numOperands, DataTypes outputType, String pattern, String description) {
		_numOperands = numOperands;
		_outputType = outputType;
		_pattern = pattern;
		_description = description;
	}
	
	public int getNumOperands() {
		return _numOperands;
	}
	
	public DataTypes getOutputType() {
		return _outputType;
	}
	
	public String formatOp(List<String> params) {
		return String.format(_pattern, params.toArray(new Object[params.size()]));
	}

	public static Operator getRandomly(DataTypes type) {
		List<Operator> values = Arrays.asList(values());
		
		// remove operator whose return value type is other then desired type
		if (type != null) {
			for (Iterator<Operator> iter = values.iterator(); iter.hasNext() ; ) {
				Operator op = iter.next();
			    if (op != null && op._outputType != type) {
			        iter.remove();
			    }
			}
		}
 
		return values.get(StdRandom.uniform(values.size()));
	}
	
	@Override
	public String toString() {
		return _description;
	}
}