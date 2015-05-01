package JST.Enums;

public enum OutputType 
{
	OUT_ALERT("a window message box"),
	OUT_LOG("write to the console log"),
	LIT_DOCUMENT("write on the document - in browser");

	private String _description;

	private OutputType(String description)
	{
		this._description = description;
	}
	
	/**
	 * Returns a description of the type.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return _description;
	}
}
