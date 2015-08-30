package il.ac.tau.jsfuzzer.Generator.SymTable;

import il.ac.tau.jsfuzzer.JST.Identifier;

import java.util.Objects;

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

	@Override public int hashCode()
	{
		return Objects.hash(_id, _type);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if (obj == this)
			return true;
		if(!(obj instanceof SymEntry))
			return false;
		SymEntry other = (SymEntry) obj;
		
		return _id.equals(other._id) && _type.equals(other._type);
	}
	
	@Override
	public String toString()
	{
		return String.format("%4s %s", _type, _id.getName());
	}
}