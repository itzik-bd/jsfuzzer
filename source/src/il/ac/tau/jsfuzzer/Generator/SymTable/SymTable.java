package il.ac.tau.jsfuzzer.Generator.SymTable;

import il.ac.tau.jsfuzzer.JST.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	public void newEntry(SymEntry entry)
	{
		Identifier id = entry.getIdentifier();
		
		// make sure that id is not exists
		if (contains(id))
			throw new RuntimeException(String.format("Identifier %s is already exists", id.getName()));
		
		// add according to type
		switch (entry.getType())
		{
		case VAR:
			_entriesVar.put(id, entry);
			break;
		case FUNC:
			_entriesFunc.put(id, entry);
			break;
		}
	}
	
	/**
	 * Check if the identifier is defined in the current symbol table only!
	 * @param id - identifier
	 * @return boolean - whether contains
	 */
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

	public List<SymEntry> getAvaiableEntries(SymEntryType type)
	{
		List<SymEntry> resMap = new ArrayList<SymEntry>();
		SymTable currentSymTable = this;
		
		while (currentSymTable != null)
		{			
			if (type.equals(SymEntryType.VAR) || type == null)
				resMap.addAll(currentSymTable._entriesVar.values());
			
			if (type.equals(SymEntryType.FUNC) || type == null)
				resMap.addAll(currentSymTable._entriesFunc.values());
			
			// climb up to the root
			currentSymTable = currentSymTable._parent;
		}
		
		return resMap;
	}
	
	public int getAvaiableEntriesWithLevels(SymEntryType type, Map<SymEntry,Integer> resMap)
	{		
		SymTable currentSymTable = this;
		int level = 0;
		
		while (currentSymTable != null)
		{
			List<SymEntry> currentLevelEntries = new LinkedList<SymEntry>();
			
			if (type.equals(SymEntryType.VAR) || type == null)
				currentLevelEntries.addAll(currentSymTable._entriesVar.values());
			
			if (type.equals(SymEntryType.FUNC) || type == null)
				currentLevelEntries.addAll(currentSymTable._entriesFunc.values());
			
			for (SymEntry entry : currentLevelEntries)
				resMap.put(entry, level);
			
			// climb up to the root
			currentSymTable = currentSymTable._parent;
			level++;
		}
		
		return level-1;
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
}