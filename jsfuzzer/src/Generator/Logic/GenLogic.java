package Generator.Logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Utils.StdRandom;
import Generator.Context;
import Generator.Generator;
import Generator.Config.ConfigProperties;
import Generator.Config.Configs;
import JST.AbsExpression;
import JST.AbsStatement;
import JST.JSTNode;

public class GenLogic
{
	private final Generator _gen;
	private final Configs _configs;

	public GenLogic(Generator gen, Configs configs) {
		_gen = gen;
		_configs = configs;
	}
	
	private JSTNode applyMethod(String methodName, Context context)
	{
		JSTNode node;

		switch (methodName)
		{
		case "ForEach": node = _gen.createForEach(context); break;
		case "Switch": node = _gen.createSwitch(context); break;
		case "For": node = _gen.createFor(context); break;
		case "If": node = _gen.createIf(context); break;
		case "DoWhile": node = _gen.createDoWhile(context); break;
		case "Case": node = _gen.createCase(context); break;
		case "While": node = _gen.createWhile(context); break;
		case "Break": node = _gen.createBreak(context); break;
		case "Return": node = _gen.createReturn(context); break;
		case "Call": node = _gen.createCall(context); break;
		case "This": node = _gen.createThis(context); break;
		case "Literal": node = _gen.createLiteral(context); break;
		case "CaseBlock": node = _gen.createCaseBlock(context); break;
		case "FunctionDefinition": node = _gen.createFunctionDefinition(context); break;
		case "Continue": node = _gen.createContinue(context); break;
		case "ArrayExpression": node = _gen.createArrayExpression(context); break;
		case "Identifier": node = _gen.createIdentifier(context); break;
		case "VarDeclerator": node = _gen.createVarDeclerator(context); break;
		case "Assignment": node = _gen.createAssignment(context); break;
		case "StatementsBlock": 
			// TODO: create new conetxt and pass to create method
			node = _gen.createStatementsBlock(context); break;
		case "FunctionExpression": node = _gen.createFunctionExpression(context); break;
		case "MemberExpression": node = _gen.createMemberExpression(context); break;
		case "CompoundAssignment": node = _gen.createCompoundAssignment(context); break;
		case "ObjectExpression": node = _gen.createObjectExpression(context); break;
		case "VarDecleration":node = _gen.createVarDecleration(context); break;
		case "LiteralNumber": node = _gen.createLiteralNumber(context); break;
		case "ExpressionOp": node = _gen.createExpressionOp(context); break;
		case "LiteralString": node = _gen.createLiteralString(context); break;

		// generate expression
		case "Expression": node = generateExpression(context); break;
		
		default: throw new IllegalArgumentException("JSTnode '"+methodName+"' creation method was not defined");
		}
		
		return node;
	}
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	public AbsStatement generateStatement(Context context)
	{
		HashMap<String, Integer> hs = new HashMap<String, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put("CompoundAssignment", _configs.valInt(ConfigProperties.STMT_COMPOUNDASSIGNMENT));
		hs.put("FunctionDefinition", _configs.valInt(ConfigProperties.STMT_FUNCTIONDEFINITION));
		hs.put("If", _configs.valInt(ConfigProperties.STMT_IF));
		//hs.put("OutputStatement", _configs.valInt(ConfigProperties.STMT_OUTPUTSTATEMENT));
		hs.put("Switch", _configs.valInt(ConfigProperties.STMT_SWITCH));
		hs.put("VarDecleration", _configs.valInt(ConfigProperties.STMT_VARDECLERATION));
		hs.put("Assignment", _configs.valInt(ConfigProperties.STMT_ASSIGNMENT));
		hs.put("Expression", _configs.valInt(ConfigProperties.STMT_EXPRESSION));
				
		if (context.isInFunction())
		{
			hs.put("Return", _configs.valInt(ConfigProperties.STMT_RETURN));
		}
		
		// Is in loop
		if (context.isInLoop())
		{
			hs.put("Break", _configs.valInt(ConfigProperties.STMT_BREAK));
			hs.put("Continue", _configs.valInt(ConfigProperties.STMT_CONTINUE));
		}
		
		// Lower the probability of nested loop
		int p = _configs.valInt(ConfigProperties.NESTED_LOOPS_FACTOR) * (context.getLoopDepth()+1); // depth must starts from 1 (not 0)
		
		hs.put("ForEach", _configs.valInt(ConfigProperties.STMT_FOREACH)/p);
		hs.put("While", _configs.valInt(ConfigProperties.STMT_WHILE)/p);
		hs.put("DoWhile", _configs.valInt(ConfigProperties.STMT_DOWHILE)/p);
		hs.put("For", _configs.valInt(ConfigProperties.STMT_FOR)/p);
		
		// randomly choose statement
		String createMethod = StdRandom.choseFromProbList(hs);
		
		return (AbsStatement) applyMethod(createMethod, context);
	}

	/**
	 * This is an initial and non complex solution
	 * Get all pr  obabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	public AbsExpression generateExpression(Context context)
	{
		HashMap<String, Double> hs = new HashMap<String, Double>();
		
		double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), 4444444); // replace 44444 with _depth
		
		// leafs - probability increase as depth grows
		hs.put("Identifier", _configs.valInt(ConfigProperties.EXPR_IDENTIFIER)/factorDepth);
		hs.put("Literal", _configs.valInt(ConfigProperties.EXPR_LITERAL)/factorDepth);
		hs.put("This", _configs.valInt(ConfigProperties.EXPR_THIS)/factorDepth);
		
		// non-leafs - probability decrease as depth grows
		hs.put("ExpressionOp", _configs.valInt(ConfigProperties.EXPR_EXPRESSIONOP)*factorDepth);
		
		//hs.put("ArrayExpression", _configs.valInt(ConfigProperties.EXPR_ARRAYEXPRESSION)*factorDepth);
		//hs.put("Call", _configs.valInt(ConfigProperties.EXPR_CALL)*factorDepth);
		//hs.put("MemberExpression", _configs.valInt(ConfigProperties.EXPR_MEMBEREXPRESSION)*factorDepth);
		//hs.put("ObjectExpression", _configs.valInt(ConfigProperties.EXPR_OBJECTEXPRESSION)*factorDepth);
		//hs.put("FunctionExpression", _configs.valInt(ConfigProperties.EXPR_FUNCTIONEXPRESSION)*factorDepth);

		
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
		
		return (AbsExpression) applyMethod(createMethod, context);
	}

	public List<AbsExpression> generateExpression(Context context, int size)
	{
		List<AbsExpression> stmtList = new LinkedList<AbsExpression>();
		
		for (int i=0 ; i<size ; i++) {
			stmtList.add(generateExpression(context));
		}
		
		return stmtList;
	}
	
	public List<AbsStatement> generateStatement(Context context, int size)
	{
		List<AbsStatement> expList = new LinkedList<AbsStatement>();
		
		for (int i=0 ; i<size ; i++) {
			expList.add(generateStatement(context));
		}
		
		return expList;
	}
	
}