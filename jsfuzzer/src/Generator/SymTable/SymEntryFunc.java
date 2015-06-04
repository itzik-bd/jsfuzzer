package Generator.SymTable;

import JST.Identifier;

public class SymEntryFunc extends SymEntry
{
	private final int _paramsNumber;
	
	public SymEntryFunc(Identifier id, int paramNumber) {
		super(id, SymEntryType.FUNC);
		_paramsNumber = paramNumber;
	}
	
	public int getParamsNumber() {
		return _paramsNumber;
	}
}
