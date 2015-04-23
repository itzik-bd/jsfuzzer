package JST;

public class ForEach extends AbstractStatement
{
	private AbstractStatement _item;
	private AbstractExpression _collection;
	private AbstractStatement _operation;
	
	public ForEach(AbstractStatement item, AbstractExpression collection, AbstractStatement operation) {
		_item = item;
		_collection = collection;
		_operation = operation;
	}
	
	public AbstractStatement getItem() {
		return _item;
	}
	
	public AbstractExpression getCollection() {
		return _collection;
	}
	
	public AbstractStatement getOperation() {
		return _operation;
	}
}