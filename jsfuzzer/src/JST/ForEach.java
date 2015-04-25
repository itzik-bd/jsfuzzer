package JST;

public class ForEach extends AbsStatement
{
	private AbsStatement _item;
	private AbsExpression _collection;
	private AbsStatement _operation;
	
	public ForEach(AbsStatement item, AbsExpression collection, AbsStatement operation) {
		_item = item;
		_collection = collection;
		_operation = operation;
	}
	
	public AbsStatement getItem() {
		return _item;
	}
	
	public AbsExpression getCollection() {
		return _collection;
	}
	
	public AbsStatement getOperation() {
		return _operation;
	}
}