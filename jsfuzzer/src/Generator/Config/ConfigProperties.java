package Generator.Config;

public enum ConfigProperties
{
	// in Switch, how many cases will be
	SWITCH_BLOCK_NUM_LAMBDA_EXP("switch_block_num_lambda_exp", Double.class),
	// in Switch, how many values will lead to said case
	CASE_NUM_LAMBDA_EXP("case_num_lambda_exp", Double.class),
	// in Switch, will there be a default case 
	CASE_BLOCK_INCLUDE_DEFAULT_BERNOULLY_P("case_block_include_default_bernoully_p", Double.class),
	
	RETURN_VALUE_BERNOULLY_P("return_value_bernoully_p", Double.class),
	STMTS_BLOCK_SIZE_LAMBDA("stmts_block_size_lambda", Double.class),
	ARRAY_LENGTH_LAMBDA_EXP("array_length_lambda_exp", Double.class),
	OBJECT_KEYS_LENGTH_LAMBDA_EXP("object_keys_length_lambda_exp", Double.class),
	
	// in varDeclerator, prob to reDeclare an existing variable
	VAR_RE_DECL_EXISTING_VAR("var_redeclare_existing_var", Double.class),
	// in VarDeclerator, Parameter for number of vairables you define 
	VAR_DECL_NUM_LAMBDA_EXP("var_decl_num_lambda_exp", Double.class),
	// in VarDeclerator, the chance to put value in the new defined var
	VAR_DECL_INIT_VAL_BERNOULLY_P("var_decl_init_val_bernoully_p", Double.class),
	 
	// in FuncDecleration, Parameter for deciding how many parameters will the function recieve
	FUNC_PARAMS_NUM_LAMBDA_EXP("func_params_num_lambda_exp", Double.class),
	
	// in Call, use existing identifier as parameter
	FUNC_PARAM_USE_EXISTING_VAR_BERNOULLY_P("func_param_use_existing_var_bernoully_p", Double.class),
	
	// in Call, the probability of using existing function (not anonymous one)
	CALL_EXISTING_FUNCTION_BERNOULLY_P("call_existing_function_bernoully_p", Double.class),
	
	// in StringLiteral, Parameters for string length
	LITERAL_STRING_LAMBDA("literal_string_lambda", Double.class),
	LITERAL_STRING_MAX_LENGTH("literal_string_max_length", Integer.class),

	// in NumberLiteral, probability to get infinity
	LITERAL_NUMBER_MAX_PROBABILITY("literal_number_max_probability", Double.class),
	// in NumberLiteral, parameter of number's length
	LITERAL_NUMBER_LAMBDA("literal_number_lambda", Double.class),
	
	// 
	IN_SWITCH_IDENTIGIER_PROB("in_switch_identifier_prob", Integer.class),
	
	// Relative probabilities to shufle each expression
	EXPR_EXPRESSIONOP("expr_ExpressionOp", Integer.class),
	EXPR_ARRAYEXPRESSION("expr_ArrayExpression", Integer.class),
	EXPR_CALL("expr_Call", Integer.class),
	EXPR_IDENTIFIER("expr_Identifier", Integer.class),
	EXPR_LITERAL("expr_Literal", Integer.class),
	EXPR_MEMBEREXPRESSION("expr_MemberExpression", Integer.class),
	EXPR_THIS("expr_This", Integer.class),
	EXPR_OBJECTEXPRESSION("expr_ObjectExpression", Integer.class),
	EXPR_FUNCTIONEXPRESSION("expr_FunctionExpression", Integer.class),

	// Relative probabilities to shufle each statment
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
	STMT_CALL("stmt_call", Integer.class),
	
	// make nested loops less common
	NESTED_LOOPS_FACTOR("nested_loops_factor", Double.class),
		
	// Factor for number of statements under program
	PROGRAM_SIZE_LAMBDA("program_size_lambda", Double.class),
	
	// making sure deep nodes have less (or non at all) children
	FACTOR_DEPTH("factor_depth", Double.class),
	MAX_JST_DEPTH("max_jst_depth", Integer.class),
	
	// Properties for injected code to limit loops
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
