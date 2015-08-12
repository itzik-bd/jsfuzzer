package JST.Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Utils.StdRandom;

public enum Operator
{
	// Unary Operations
	UMINUS("unary subtraction", DataTypes.NUMBER_sig, "-%s", 1, DataTypes.NUMBER_sig), 
	UPLUS("unary addition", DataTypes.NUMBER_sig, "+%s", 1, DataTypes.NUMBER_sig),
	
	LNEG("logical negation", DataTypes.BOOLEAN_sig, "!%s", 1, new DataTypes[] { DataTypes.BOOLEAN, DataTypes.NUMBER }),
	BITNEG("bitwise negation", DataTypes.NUMBER_sig, "~%s", 1, DataTypes.NUMBER_sig),

	PLUSPLUSLEFT("plus plus", DataTypes.NUMBER_sig, "++%s", 1, (DataTypes[]) null),
	MINUSMINUSLEFT("minus minus", DataTypes.NUMBER_sig, "--%s", 1, (DataTypes[]) null),
	PLUSPLUSRIGHT("plus plus", DataTypes.NUMBER_sig, "%s++", 1, (DataTypes[]) null),
	MINUSMINUSRIGHT("minus minus", DataTypes.NUMBER_sig, "%s--", 1, (DataTypes[]) null),
	
	TYPEOF("type of", DataTypes.STRING_sig, "typeof %s", 1, DataTypes.ALL),
	VOID("void", DataTypes.UNDEFINED_sig, "void %s", 1, DataTypes.ALL),
	
	// Binary Operations
	PLUS("addition", DataTypes.ALL, "%s + %s", 2, new DataTypes[] {DataTypes.NUMBER, DataTypes.STRING, DataTypes.BOOLEAN}),
	MINUS("subtraction", DataTypes.NUMBER_sig, "%s - %s", 2, DataTypes.NUMBER_sig),
	MULTIPLY("multiplication", DataTypes.NUMBER_sig, "%s * %s", 2, DataTypes.NUMBER_sig),
	DIVIDE("division", DataTypes.NUMBER_sig, "%s / %s", 2, DataTypes.NUMBER_sig),
	MOD("modulo", DataTypes.NUMBER_sig, "%s %% %s", 2, DataTypes.NUMBER_sig),
	
	LAND("logical and", DataTypes.BOOLEAN_sig, "%s && %s", 2, DataTypes.BOOLEAN_sig),
	LOR("logical or", DataTypes.BOOLEAN_sig, "%s || %s", 2, DataTypes.BOOLEAN_sig),
	LT("less than", DataTypes.BOOLEAN_sig, "%s < %s", 2, DataTypes.NUMBER_sig),
	LTE("less than or equal to", DataTypes.BOOLEAN_sig, "%s <= %s", 2, DataTypes.NUMBER_sig),
	GT("greater than", DataTypes.BOOLEAN_sig, "%s > %s", 2, DataTypes.NUMBER_sig),
	GTE("greater than or equal to", DataTypes.BOOLEAN_sig, "%s >= %s", 2, DataTypes.NUMBER_sig),
	
	EQUAL("equality", DataTypes.BOOLEAN_sig, "%s == %s", 2, DataTypes.ALL),
	EQUALTYPE("equality + type", DataTypes.BOOLEAN_sig, "%s === %s", 2, DataTypes.ALL),
	NEQUALTYPE("inequality or different type", DataTypes.BOOLEAN_sig, "%s !== %s", 2, DataTypes.ALL),
	NEQUAL("inequality", DataTypes.BOOLEAN_sig, "%s != %s", 2, DataTypes.ALL),
	
	SHIFTRIGHT("sign propagating right shift", DataTypes.NUMBER_sig, "%s >> %s", 2, DataTypes.NUMBER_sig),
	SHIFTLEFT("shift left", DataTypes.NUMBER_sig, "%s << %s", 2, DataTypes.NUMBER_sig),
	SHIFTRIGHTZERO("zero-fill right shift", DataTypes.NUMBER_sig, "%s >>> %s", 2, DataTypes.NUMBER_sig),
	
	BITXOR("bitwise xor", DataTypes.NUMBER_sig, "%s ^ %s", 2, DataTypes.NUMBER_sig),
	BITAND("bitwise and", DataTypes.NUMBER_sig, "%s & %s", 2, DataTypes.NUMBER_sig),
	BITOR("bitwise or", DataTypes.NUMBER_sig, "%s | %s", 2, DataTypes.NUMBER_sig),
	
	// INSTANCEOF is not relevant for now because we do not allow "var x = FuncName()"
	// INSTANCEOF(2, DataTypes.BOOLEAN, "%s instanceof %s", "instance of"),
	
	// TODO: decide what to do with these two, and do it...
	// IN(2, DataTypes.BOOLEAN, "%s in %s", "in"),
	// DELETE(1, DataTypes.BOOLEAN,"delete %s", "delete object"),
	
	// Ternary Operations
	CONDOP("conditional operation", DataTypes.ALL, "%s ? %s : %s", 3, DataTypes.BOOLEAN_sig, DataTypes.ALL, DataTypes.ALL);
	
	private final String _pattern;
	private final String _description;
	private final Set<DataTypes> _outputTypeSet;
	private final DataTypes[][] _argsInputTypes;

	private Operator(String description, DataTypes[] outputTypeArr, String pattern, int numOperands, DataTypes[]... argsInputTypes) {
		_description = description;
		_outputTypeSet = new HashSet<DataTypes>(Arrays.asList(outputTypeArr));
		_pattern = pattern;
		
		if (numOperands == argsInputTypes.length) {
			_argsInputTypes = argsInputTypes;
		}
		else if (argsInputTypes.length == 1) {
			// create an array of size numOperands and each element will be the single value
			_argsInputTypes = new DataTypes[numOperands][];
			for (int i=0; i<numOperands; i++) {
				_argsInputTypes[i] = argsInputTypes[0];
			}
		}
		else { throw new IllegalArgumentException("numOperands and argsInputTypes are not consistent"); }
	}
	
	public int getNumOperands() {
		return _argsInputTypes.length;
	}
	
	public Set<DataTypes> getOutputTypeSet() {
		return _outputTypeSet;
	}
	
	public DataTypes[][] getArgsInputTypes()
	{
		return _argsInputTypes;
	}
	
	public String formatOp(List<String> params) {
		return String.format(_pattern, params.toArray(new Object[params.size()]));
	}

	public static Operator getRandomly(Set<DataTypes> returnTypesSet)
	{
		if (returnTypesSet == null || returnTypesSet.size() == 0) {
			throw new IllegalArgumentException("returnTypesSet must be a non-empty valid set");
		}
		
		List<Operator> values = new ArrayList<Operator> (Arrays.asList(values()));
				
		// remove operator whose return value type is other then desired type
		for (Iterator<Operator> iter = values.iterator(); iter.hasNext() ; ) {
			Operator op = iter.next();
		    if (!returnTypesSet.containsAll(op._outputTypeSet)) {
		        iter.remove();
		    }
		}
 
		return values.get(StdRandom.uniform(values.size()));
	}
	
	@Override
	public String toString() {
		return _description;
	}
}