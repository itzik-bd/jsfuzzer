package Generator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Utils.StdRandom;
import Generator.Config.ConfigProperties;
import Generator.Config.Configs;
import Generator.Params.createParams;
import Generator.Params.GenerateExprParams;
import JST.AbsExpression;
import JST.AbsStatement;
import JST.JSTNode;

public class GenLogic
{
	private final Generator _gen;
	private final Configs _configs;
	private int _depth = 0;

	public GenLogic(Generator gen, Configs configs) {
		_gen = gen;
		_configs = configs;
	}
	
	protected JSTNode applyMethod(String methodName, Context context, createParams params)
	{
		JSTNode node;

		switch (methodName)
		{
		case "ForEach": node = _gen.createForEach(context, params); break;
		case "Switch": node = _gen.createSwitch(context, params); break;
		case "For": node = _gen.createFor(context, params); break;
		case "If": node = _gen.createIf(context, params); break;
		case "DoWhile": node = _gen.createDoWhile(context, params); break;
		case "Case": node = _gen.createCase(context, params); break;
		case "While": node = _gen.createWhile(context, params); break;
		case "Break": node = _gen.createBreak(context, params); break;
		case "Return": node = _gen.createReturn(context, params); break;
		case "Call": node = _gen.createCall(context, params); break;
		case "This": node = _gen.createThis(context, params); break;
		case "Literal": node = _gen.createLiteral(context, params); break;
		case "CaseBlock": node = _gen.createCaseBlock(context, params); break;
		case "FunctionDef": node = _gen.createFunctionDef(context, params); break;
		case "Continue": node = _gen.createContinue(context, params); break;
		case "ArrayExp": node = _gen.createArrayExp(context, params); break;
		case "Identifier": node = _gen.createIdentifier(context, params); break;
		case "VarDeclerator": node = _gen.createVarDeclerator(context, params); break;
		case "Assignment": node = _gen.createAssignment(context, params); break;
		case "StatementsBlock": node = _gen.createStatementsBlock(context, params); break;
		case "FunctionExp": node = _gen.createFunctionExp(context, params); break;
		case "MemberExp": node = _gen.createMemberExp(context, params); break;
		case "CompoundAssignment": node = _gen.createCompoundAssignment(context, params); break;
		case "ObjectExp": node = _gen.createObjectExp(context, params); break;
		case "VarDecleration":node = _gen.createVarDecleration(context, params); break;
		case "LiteralNumber": node = _gen.createLiteralNumber(context, params); break;
		case "OperationExp": node = _gen.createOperationExp(context, params); break;
		case "LiteralString": node = _gen.createLiteralString(context, params); break;

		// generate expression
		case "Expression": node = generateExpression(context, params); break;
		
		default: throw new IllegalArgumentException("JSTnode '"+methodName+"' creation method was not defined");
		}
		
		return node;
	}
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	AbsStatement generateStatement(Context context, createParams params)
	{
		HashMap<String, Double> hs = new HashMap<String, Double>();
		
		// All properties are relative to the total of all properties
		hs.put("CompoundAssignment", (double) _configs.valInt(ConfigProperties.STMT_COMPOUNDASSIGNMENT));
		hs.put("FunctionDef", (double) _configs.valInt(ConfigProperties.STMT_FUNCTIONDEFINITION));
		hs.put("If", (double) _configs.valInt(ConfigProperties.STMT_IF));
		//hs.put("OutputStatement", _configs.valInt(ConfigProperties.STMT_OUTPUTSTATEMENT));
		hs.put("Switch", (double) _configs.valInt(ConfigProperties.STMT_SWITCH));
		hs.put("VarDecleration", (double) _configs.valInt(ConfigProperties.STMT_VARDECLERATION));
		hs.put("Assignment", (double) _configs.valInt(ConfigProperties.STMT_ASSIGNMENT));
		hs.put("Expression", (double) _configs.valInt(ConfigProperties.STMT_EXPRESSION));
				
		if (context.isInFunction())
		{
			hs.put("Return", (double) _configs.valInt(ConfigProperties.STMT_RETURN));
		}
		
		// Is in loop
		if (context.isInLoop())
		{
			hs.put("Break", (double) _configs.valInt(ConfigProperties.STMT_BREAK));
			hs.put("Continue", (double) _configs.valInt(ConfigProperties.STMT_CONTINUE));
		}
		
		// Lower the probability of nested loop
		int p = _configs.valInt(ConfigProperties.NESTED_LOOPS_FACTOR) * (context.getLoopDepth()+1); // depth must starts from 1 (not 0)
		
		hs.put("ForEach", (double) (_configs.valInt(ConfigProperties.STMT_FOREACH)/p));
		hs.put("While", (double) (_configs.valInt(ConfigProperties.STMT_WHILE)/p));
		hs.put("DoWhile", (double) (_configs.valInt(ConfigProperties.STMT_DOWHILE)/p));
		hs.put("For", (double) (_configs.valInt(ConfigProperties.STMT_FOR)/p));
		
		// randomly choose statement
		String createMethod = StdRandom.choseFromProbList(hs);
		
		return (AbsStatement) applyMethod(createMethod, context, params);
	}

	/**
	 * This is an initial and non complex solution
	 * Get all pr  obabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	AbsExpression generateExpression(Context context, createParams params)
	{
		HashMap<String, Double> hs = new HashMap<String, Double>();
		
		double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _depth);
		boolean isStatementExpression = GenerateExprParams.getStatementExpression(params);
		
		// leafs - probability increase as depth grows
		hs.put("Identifier", _configs.valInt(ConfigProperties.EXPR_IDENTIFIER)/factorDepth);
		hs.put("Literal", _configs.valInt(ConfigProperties.EXPR_LITERAL)/factorDepth);
		hs.put("This", _configs.valInt(ConfigProperties.EXPR_THIS)/factorDepth);
		
		// non-leafs - probability decrease as depth grows
		hs.put("OperationExp", _configs.valInt(ConfigProperties.EXPR_EXPRESSIONOP)*factorDepth);
		
		//hs.put("ArrayExp", _configs.valInt(ConfigProperties.EXPR_ARRAYEXPRESSION)*factorDepth);
		hs.put("Call", _configs.valInt(ConfigProperties.EXPR_CALL)*factorDepth);
		//hs.put("MemberExp", _configs.valInt(ConfigProperties.EXPR_MEMBEREXPRESSION)*factorDepth);
		
		//ObjectExp is illegal statement
		if (isStatementExpression)
			hs.put("ObjectExp", _configs.valInt(ConfigProperties.EXPR_OBJECTEXPRESSION)*factorDepth);
		
		//hs.put("FunctionExp", _configs.valInt(ConfigProperties.EXPR_FUNCTIONEXPRESSION)*factorDepth);

		
		// Change special values acording to the input map
//		for (String probName : specialProbs.keySet())
//		{
//			if (hs.containsKey(probName))
//			{
//				hs.put(probName, specialProbs.get(probName));
//			}
//		}
		
		// randomly choose expression
		String createMethod = StdRandom.choseFromProbList(hs);
		
		return (AbsExpression) applyMethod(createMethod, context, params);
	}

	public List<AbsExpression> generateExpression(Context context, createParams params, int size)
	{
		List<AbsExpression> stmtList = new LinkedList<AbsExpression>();
		
		for (int i=0 ; i<size ; i++) {
			stmtList.add(generateExpression(context, params));
		}
		
		return stmtList;
	}
	
	public List<AbsStatement> generateStatement(Context context, createParams params, int size)
	{
		List<AbsStatement> expList = new LinkedList<AbsStatement>();
		
		for (int i=0 ; i<size ; i++) {
			expList.add(generateStatement(context, params));
		}
		
		return expList;
	}

	public void increaseDepth() {
		_depth++;		
	}
	
	public void decreaseDepth() {
		_depth--;		
	}
	
	public int getDepth() {
		return _depth;
	}
	
}