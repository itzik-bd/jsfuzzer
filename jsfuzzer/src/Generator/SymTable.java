package Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JST.Identifier;

public class SymTable
{
	private final SymTable _parent;
	private final Map<Identifier, SymEntry> _entriesVar;
	private final Map<Identifier, SymEntry> _entriesFunc;
	
	public SymTable(SymTable parent)
	{
		_parent = parent;
		_entriesVar = new HashMap<Identifier, SymEntry>();
		_entriesFunc = new HashMap<Identifier, SymEntry>();
	}
	
	public void newEntry(Identifier id, SymEntryType type)
	{
		// make sure that id is not exists
		if (contains(id))
			throw new RuntimeException(String.format("Identifier %s is already exists", id.getName()));
		
		// add according to type
		if (type.equals(SymEntryType.VAR))
			_entriesVar.put(id, new SymEntry(id, type));
		else if (type.equals(SymEntryType.FUNC))
			_entriesFunc.put(id, new SymEntry(id, type));
	}
	
	/** Check if the identifier is defined in the current symbol table only! */
	public boolean contains(Identifier id)
	{
		return (_entriesVar.containsKey(id) || _entriesFunc.containsKey(id));
	}
	
	/* lookup for identifier in the symbol table hierarchy */
	public SymEntry lookup(Identifier id)
	{
		// check if the current symbol table contains the identifier
		if (_entriesVar.containsKey(id))
			return _entriesVar.get(id);
		if (_entriesFunc.containsKey(id))
			return _entriesFunc.get(id);
		
		// check for identifier in higher symbol table
		if (_parent != null)
			return _parent.lookup(id);
		
		// no symbol has been found
		return null;
	}
	
	public List<Identifier> getAvaiableIdentifiers(SymEntryType type)
	{
		List<Identifier> resList = new ArrayList<Identifier>();
		
		SymTable currentSymTable = this;
		
		while (currentSymTable != null)
		{
			if (type.equals(SymEntryType.VAR) || type == null)
				resList.addAll(currentSymTable._entriesVar.keySet());
			
			if (type.equals(SymEntryType.FUNC) || type == null)
				resList.addAll(currentSymTable._entriesFunc.keySet());
			
			// climb up to the root
			currentSymTable = currentSymTable._parent;
		}
		
		return resList;
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		
		for (SymEntry e : _entriesVar.values())
			s.append(e+"\n");
		for (SymEntry e : _entriesFunc.values())
			s.append(e+"\n");
		
		if (_parent != null)
		{
			s.append("--------------\n");
			s.append(_parent);
		}
		
		return s.toString();
	}
	
	public class SymEntry
	{
		private Identifier _id;
		private SymEntryType _type;
		
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
			return String.format("%10s %s", _type, _id.getName());
		}
	}
	
	public enum SymEntryType
	{
		FUNC, VAR;
	}
}