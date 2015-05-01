package JST.Enums;

public enum TrinaryOps {
	
	CONDOP("%s ? %s : %s ", "conditional operation");
	
	private String pattern;

	private String description;

	private TrinaryOps(String pattern, String description) {
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
	
	public String formatOp(String a, String b, String c)
	{
		return String.format(pattern, a, b, c);
	}
}
