package Generator;

import Generator.SymTable.SymTable;

public class Context
{
	private final Context _parentContext;
	private final SymTable _symTable;

	private final int _loopDepth;
	private final boolean _inFunction;
	private final int _contextDepth;

	/**
	 * Constructor for root of the tree
	 */
	public Context() {
		_parentContext = null;
		_symTable = new SymTable(null);
		_loopDepth = 0;
		_inFunction = false;
		_contextDepth = 0;
	}
	
	/**
	 * Constructor for inner node in the tree
	 * 
	 * @param parent - the parent of the node
	 * @param inLoop - flag if its a loop context - null means use the parent's inLoop
	 * @param inFunction - flag if its a function context - null means use the parent's inFunction  
	 * @param createNewSymTable - whether to create new symbol table or to use parent's 
	 */
	public Context(Context parent, Boolean inLoop, Boolean inFunction, boolean createNewSymTable) {
		_parentContext = parent;
		_symTable = createNewSymTable ? new SymTable(parent._symTable) : parent._symTable;
		_loopDepth = calculateLoopDepth(parent, inLoop);
		_inFunction = calculateInFunction(parent, inFunction);
		_contextDepth = parent._contextDepth + 1;
	}

	private boolean calculateInFunction(Context parent, Boolean inFunction)
	{
		if (inFunction == null || parent._inFunction == true) {
			return parent._inFunction;
		} else {
			return inFunction;
		}
	}

	private int calculateLoopDepth(Context parent, Boolean inLoop)
	{
		if (inLoop == null) {
			return parent._loopDepth;
		} else if (inLoop == true) {
			return parent._loopDepth + 1;
		} else {
			return 0; // inLoop==false, so reset count
		}
	}

	public SymTable getSymTable() {
		return _symTable;
	}

	public boolean isInLoop() {
		return (_loopDepth > 1);
	}

	public int getLoopDepth() {
		return _loopDepth;
	}

	public boolean isInFunction() {
		return _inFunction;
	}

	public int getDepth() {
		return _contextDepth;
	}

	public Context getParent() {
		return _parentContext;
	}
}
