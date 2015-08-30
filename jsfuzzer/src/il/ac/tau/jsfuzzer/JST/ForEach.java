package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class ForEach extends AbsStatement
{
	private AbsStatement _item;
	private AbsExpression _collection;
	private StatementsBlock _stmtsBlock;
	
	public ForEach(AbsStatement item, AbsExpression collection, StatementsBlock stmtsBlock) {
		_item = item;
		_collection = collection;
		_stmtsBlock = stmtsBlock;
	}
	
	public AbsStatement getItem() {
		return _item;
	}
	
	public AbsExpression getCollection() {
		return _collection;
	}
	
	public AbsStatement getStatementsBlock() {
		return _stmtsBlock;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}