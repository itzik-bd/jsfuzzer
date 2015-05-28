package Generator.Config;

public enum ConfigProperties
{
	CASE_NUM_NORMAL_EXP("cases_num_normal_exp", Integer.class),
	CASES_NUM_NORMAL_STDDEV("cases_num_normal_stddev",Integer.class),
	CASE_BLOCK_STMTS_NUM_NORMAL_EXP("case_block_stmts_num_normal_exp",Integer.class),
	CASE_BLOCK_STMTS_NUM_NORMAL_STDDEV("case_block_stmts_num_normal_stddev",Integer.class),
	CASES_BLOCKS_NUM_NORMAL_EXP("cases_blocks_num_normal_exp",Integer.class),
	CASES_BLOCKS_NUM_NORMAL_STDDEV("cases_blocks_num_normal_stddev", Integer.class);
//	case_block_include_default_bernoully_p=0.1
//	return_value_bernoully_p=0.9
//	assignment_use_existing_var_bernoully_p=0.5
//	stmts_block_size_normal_exp=20
//	stmts_block_size_normal_stddev=5
	;
	
	private String _name;
	private Class<?> _classType;
	
	private ConfigProperties(String name, Class<?> classType)
	{
		_name = name;
		_classType = classType;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Class<?> getClassType()
	{
		return _classType;
	}

}
