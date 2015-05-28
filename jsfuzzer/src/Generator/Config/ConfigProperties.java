package Generator.Config;

public enum ConfigProperties
{
	CASE_NUM_NORMAL_EXP("cases_num_normal_exp", Integer.class),
	CASES_NUM_NORMAL_STDDEV("cases_num_normal_stddev",Integer.class),
	CASE_BLOCK_STMTS_NUM_NORMAL_EXP("case_block_stmts_num_normal_exp",Integer.class),
	CASE_BLOCK_STMTS_NUM_NORMAL_STDDEV("case_block_stmts_num_normal_stddev",Integer.class),
	CASES_BLOCKS_NUM_NORMAL_EXP("cases_blocks_num_normal_exp",Integer.class),
	CASES_BLOCKS_NUM_NORMAL_STDDEV("cases_blocks_num_normal_stddev", Integer.class),
	CASE_BLOCK_INCLUDE_DEFAULT_BERNOULLY_P("case_block_include_default_bernoully_p", Double.class),
	RETURN_VALUE_BERNOULLY_P("return_value_bernoully_p", Double.class),
	ASSIGNMENT_USE_EXISTING_VAR_BERNOULLY_P("assignment_use_existing_var_bernoully_p", Double.class),
	STMTS_BLOCK_NORMAL_EXP("stmts_block_size_normal_exp", Integer.class),
	STMTS_BLOCK_SIZE_NORMAL_EXP("stmts_block_size_normal_exp", Integer.class),
	STMTS_BLOCK_SIZE_NORMAL_STDDEV("stmts_block_size_normal_stddev", Integer.class),
	VAR_DECL_NUM_LAMBDA_EXP("var_decl_num_lambda_exp", Double.class),
	
	EXPR_UNARYOP("expr_UnaryOp", Integer.class),
	EXPR_BINARYOP("expr_BinaryOp", Integer.class),
	EXPR_TRINARYOP("expr_TrinaryOp", Integer.class),
	EXPR_ARRAYEXPRESSION("expr_ArrayExpression", Integer.class),
	EXPR_CALL("expr_Call", Integer.class),
	EXPR_IDENTIFIER("expr_Identifier", Integer.class),
	EXPR_LITERAL("expr_Literal", Integer.class),
	EXPR_MEMBEREXPRESSION("expr_MemberExpression", Integer.class),
	EXPR_THIS("expr_This", Integer.class),
	EXPR_OBJECTEXPRESSION("expr_ObjectExpression", Integer.class),
	EXPR_FUNCTIONEXPRESSION("expr_FunctionExpression", Integer.class),

	NESTED_LOOPS_FACTOR("nested_loops_factor", Integer.class),
	
	STMT_COMPOUNDASSIGNMENT("stmt_CompoundAssignment", Integer.class),
	STMT_FUNCTIONDEFINITION("stmt_FunctionDefinition", Integer.class),
	STMT_IF("stmt_If", Integer.class),
	STMT_OUTPUTSTATEMENT("stmt_OutputStatement", Integer.class),
	STMT_STATEMENTSBLOCK("stmt_StatementsBlock", Integer.class),
	STMT_SWITCH("stmt_Switch", Integer.class),
	STMT_VARDECLERATION("stmt_VarDecleration", Integer.class),
	STMT_BREAK("stmt_Break", Integer.class),
	STMT_CONTINUE("stmt_Continue", Integer.class),
	STMT_RETURN("stmt_Return", Integer.class),
	STMT_FOREACH("stmt_ForEach", Integer.class),
	STMT_WHILE("stmt_While", Integer.class),
	STMT_DOWHILE("stmt_DoWhile", Integer.class),
	STMT_FOR("stmt_For", Integer.class),
	STMT_ASSIGNMENT("stmt_Assignment", Integer.class);

	
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