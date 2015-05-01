package JST.Enums;

public class literalTypes 
{
	public enum litTypes
	{
		LIT_UNDEFINED(-1, "addition"),
		LIT_NULL(0, "subtraction"),
		LIT_INFINITY(1, "multiplication"),
		LIT_STRING(2, "division"),
		LIT_NUMBER(3, "modulo");

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
