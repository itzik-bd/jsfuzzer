package JST.Enums;

public enum UnaryOps {

	UMINUS("-%s", "unary subtraction"), 
	UPLUS("+%s", "unary addition"),
	
	LNEG("!%s", "logical negation"),
	BITNEG("~%s", "bitwise negation"),

	PLUSPLUSlEFT("++%s", "plus plus"),
	MINUSMINUSLEFT("--%s", "minus minus"),
	PLUSPLUSRIGHT("%s++", "plus plus"),
	MINUSMINUSRIGHT("%--s", "minus minus"),
	
	DELETE("delete %s", "delete object"),
	TYPEOF("typeof %s", "type of"),
	VOID("void %s", "void");
	
	private String pattern;

	private String description;

	private UnaryOps(String pattern, String description) {
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
}
