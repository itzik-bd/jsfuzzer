package Generator.Params;

import java.util.HashMap;
import java.util.Map;

import JST.Enums.*;

public class GenerateExpressionParams extends createParams
{
	private boolean _include; 
	private Map<JSTNodes, createParams> _optionalExpresstions = null;
	
	/**
	 * Basic consructor for GenerateExpressionParams
	 * @param include - true if options are do be included, false for disscluded
	 */
	public GenerateExpressionParams(boolean include)
	{
		_include = include;
		_optionalExpresstions = new HashMap<JSTNodes, createParams>();
	}
	
	/**
	 * @param node - what node to include/disclude
	 * @param params - parameters to transfer to creator, if this was choosen
	 */
	public void addOption(JSTNodes node, createParams params){
		_optionalExpresstions.put(node, params);
	}
	
	public Map<JSTNodes, createParams> getOptions(){
		return _optionalExpresstions;
	}
	
	public boolean getInclude(){
		return _include;
	}
}
