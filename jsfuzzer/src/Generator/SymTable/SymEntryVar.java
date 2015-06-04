package Generator.SymTable;

import JST.Identifier;

public class SymEntryVar extends SymEntry
{
	public SymEntryVar (Identifier id) {
		super(id, SymEntryType.FUNC);
	}
}
