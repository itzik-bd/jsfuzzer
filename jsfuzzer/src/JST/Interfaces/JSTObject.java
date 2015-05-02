package JST.Interfaces;

public interface JSTObject
{
	public Object accept(Visitor visitor, Object context);
}