package il.ac.tau.jsfuzzer.Utils;

public class StringCounter
{
	private long _id = 0;
	private final String _pattern;
	
	/**
	 * Constructor for StringCounter
	 * @param pattern - pattern of the counter (should consist a single %d) 
	 */
	public StringCounter(String pattern) {
		_pattern = pattern;
	}
	
	/**
	 * function to get the next string
	 * @return string - next token
	 */
	public String getNext() {
		_id++;
		return String.format(_pattern, _id);
	}
	
	/**
	 * get the current id of the counter
	 * @return int - current id
	 */
	public long getCurrentId() {
		return _id;
	}
	
	/**
	 * restart the counter
	 */
	public void restart() {
		_id = 0;
	}
}