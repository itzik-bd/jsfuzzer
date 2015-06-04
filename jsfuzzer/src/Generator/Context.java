package Generator;

import Generator.SymTable.SymTable;

public class Context
{
	private final Context _parentContext;
	private final SymTable _symTable;

	private final int _loopDepth;
	private final boolean _inFunction;
	private final int _contextDepth;

	// vars that can be changed
	public double identifierUseExistingVarProb = 1;
	public boolean varDecleratorForceNewIdentifier = false;
	public boolean caseBlockIncludeDefault = false;

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
	 * @param inLoop - flag if it's a loop context
	 */
	public Context(Context parent, Boolean inLoop, Boolean inFunction) {
		_parentContext = parent;
		_symTable = new SymTable(parent._symTable);
		_loopDepth = parent._loopDepth
				+ ((inLoop != null && inLoop == true) ? 1 : 0);
		_inFunction = (inFunction == null || parent._inFunction == true) ? parent._inFunction
				: inFunction;
		_contextDepth = parent._contextDepth + 1;
	}

	/**
	 * Constructor for inner node in the tree
	 * 
	 * @param parent - the parent of the node
	 */
	public Context(Context parent) {
		this(parent, null, null);
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
