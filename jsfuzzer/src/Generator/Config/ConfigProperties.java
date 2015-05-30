package Generator.Config;

public enum ConfigProperties
{
	CASE_NUM_NORMAL_EXP("cases_num_normal_exp", Integer.class),
	CASES_NUM_NORMAL_STDDEV("cases_num_normal_stddev",Integer.class),
	CASES_BLOCKS_NUM_NORMAL_EXP("cases_blocks_num_normal_exp",Integer.class),
	CASES_BLOCKS_NUM_NORMAL_STDDEV("cases_blocks_num_normal_stddev", Integer.class),
	CASE_BLOCK_INCLUDE_DEFAULT_BERNOULLY_P("case_block_include_default_bernoully_p", Double.class),
	RETURN_VALUE_BERNOULLY_P("return_value_bernoully_p", Double.class),
	ASSIGNMENT_USE_EXISTING_VAR_BERNOULLY_P("assignment_use_existing_var_bernoully_p", Double.class),
	STMTS_BLOCK_SIZE_LAMBDA("stmts_block_size_lambda", Double.class),
	VAR_DECL_NUM_LAMBDA_EXP("var_decl_num_lambda_exp", Double.class),
	ARRAY_LENGTH_LAMBDA_EXP("array_length_lambda_exp", Double.class),
	FUNC_PARAMS_NUM_LAMBDA_EXP("func_params_num_lambda_exp", Double.class),
	FUNC_PARAM_USE_EXISTING_VAR_BERNOULLY_P("func_param_use_existing_var_bernoully_p", Double.class),
	
	LITERAL_STRING_LAMBDA("literal_string_lambda", Double.class),
	LITERAL_STRING_MAX_LENGTH("literal_string_max_length", Integer.class),

	LITERAL_NUMBER_MAX_PROBABILITY("literal_number_max_probability", Double.class),
	LITERAL_NUMBER_LAMBDA("literal_number_lambda", Double.class),
	
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
	
	STMT_EXPRESSION("stmt_Expression", Integer.class),
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
	STMT_ASSIGNMENT("stmt_Assignment", Integer.class),
	
	PROGRAM_SIZE_LAMBDA("program_size_lambda", Double.class),
	
	FACTOR_DEPTH("factor_depth", Double.class),
	MAX_JST_DEPTH("max_jst_depth", Integer.class),
	
	LOOP_MAX_ITERATIONS_NORMAL_EXP("loop_max_iterations_normal_exp", Integer.class),
	LOOP_MAX_ITERATIONS_NORMAL_STDDEV("loop_max_iterations_normal_stddev", Integer.class);

	
	private String _name;
	private Class<?> _classType;
	
	private ConfigProperties(String name, Class<?> classType)
	{
		_name = name;
		_classType = classType;
	}
	
	public String toString()
	{
		return _name;
	}
	
	public Class<?> getClassType()
	{
		return _classType;
	}

}
