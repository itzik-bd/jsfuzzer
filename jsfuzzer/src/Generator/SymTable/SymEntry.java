package Generator.SymTable;

import JST.Identifier;

public abstract class SymEntry
{
	private final Identifier _id;
	private final SymEntryType _type;
	
	public SymEntry(Identifier id, SymEntryType type)
	{
		_id = id;
		_type = type;
	}
	
	public Identifier getIdentifier()
	{
		return _id;
	}
	
	public SymEntryType getType()
	{
		return _type;
	}

	@Override
	public String toString()
	{
		return String.format("%4s %s", _type, _id.getName());
	}
}