package JST.Enums;

public class literalTypes 
{
	public enum litTypes
	{
		LIT_UNDEFINED(-1, "undefined"),
		LIT_NULL(0, "null"),
		LIT_INFINITY(1, "positive infinity"),
		LIT_MIN_INFINITY(2, "negative infinity"),
		LIT_STRING(3, "String"),
		LIT_NUMBER(4, "Number");

		private int _value;
		private String _description;

		private litTypes(int value, String description)
		{
			this._value = value;
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
		
		public int getTypeValue() {
			return _value;
		}
	}
}
