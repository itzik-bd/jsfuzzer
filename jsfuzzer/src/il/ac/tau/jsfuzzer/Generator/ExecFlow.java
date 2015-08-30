package il.ac.tau.jsfuzzer.Generator;

public enum ExecFlow
{
	NORMAL(1),
	EXTENSIVE(2);
	
	private int _order;
	
	private ExecFlow(int order)
	{
		_order = order;
	}
	
	public boolean isA(ExecFlow other)
	{
		return _order >= other._order;
	}
}