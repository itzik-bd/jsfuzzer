package il.ac.tau.jsfuzzer.Generator.SymTable;

import il.ac.tau.jsfuzzer.JST.Identifier;

public class SymEntryVar extends SymEntry
{
	public SymEntryVar (Identifier id) {
		super(id, SymEntryType.VAR);
	}
}
