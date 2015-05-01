package JST;

public class Literal extends AbstractExpression 
{
	public enum literalType {LIT_UNDEFINED, LIT_NULL, LIT_INFINITY, LIT_STRING, LIT_NUMBER};
	
	private literalType _type;
	
	public Literal(literalType type)
	{
		_type = type;
	}
	
	public literalType getType()
	{
		return _type; 
	}
}
