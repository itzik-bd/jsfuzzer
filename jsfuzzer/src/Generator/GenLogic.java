package Generator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Utils.StdRandom;
import Generator.Config.ConfigProperties;
import Generator.Config.Configs;
import Generator.Params.GenerateExpressionParams;
import Generator.Params.GenerateOpExprParams;
import Generator.Params.createParams;
import JST.AbsExpression;
import JST.AbsStatement;
import JST.JSTNode;
import JST.Enums.DataTypes;
import JST.Enums.JSTNodes;

public class GenLogic
{
	private final Generator _gen;
	private final Configs _configs;
	private int _depth = 0;

	public GenLogic(Generator gen, Configs configs) {
		_gen = gen;
		_configs = configs;
	}
	
	protected JSTNode applyMethod(JSTNodes methodName, Context context, createParams params)
	{
		JSTNode node;

		switch (methodName)
		{
		case ForEach: node = _gen.createForEach(context, params); break;
		case Switch: node = _gen.createSwitch(context, params); break;
		case For: node = _gen.createFor(context, params); break;
		case If: node = _gen.createIf(context, params); break;
		case DoWhile: node = _gen.createDoWhile(context, params); break;
		case Case: node = _gen.createCase(context, params); break;
		case While: node = _gen.createWhile(context, params); break;
		case Break: node = _gen.createBreak(context, params); break;
		case Return: node = _gen.createReturn(context, params); break;
		case Call: node = _gen.createCall(context, params); break;
		case This: node = _gen.createThis(context, params); break;
		case Literal: node = _gen.createLiteral(context, params); break;
		case CaseBlock: node = _gen.createCaseBlock(context, params); break;
		case FunctionDef: node = _gen.createFunctionDef(context, params); break;
		case Continue: node = _gen.createContinue(context, params); break;
		case ArrayExp: node = _gen.createArrayExp(context, params); break;
		case Identifier: node = _gen.createIdentifier(context, params); break;
		case VarDeclerator: node = _gen.createVarDeclerator(context, params); break;
		case Assignment: node = _gen.createAssignment(context, params); break;
		case StatementsBlock: node = _gen.createStatementsBlock(context, params); break;
		case FunctionExp: node = _gen.createFunctionExp(context, params); break;
		case MemberExp: node = _gen.createMemberExp(context, params); break;
		case CompoundAssignment: node = _gen.createCompoundAssignment(context, params); break;
		case ObjectExp: node = _gen.createObjectExp(context, params); break;
		case VarDecleration:node = _gen.createVarDecleration(context, params); break;
		case LiteralNumber: node = _gen.createLiteralNumber(context, params); break;
		case OperationExp: node = _gen.createOperationExp(context, params); break;
		case LiteralString: node = _gen.createLiteralString(context, params); break;

		// generate expression
		case AbsExpression: node = generateExpression(context, (GenerateExpressionParams)params); break;
		
		default: throw new IllegalArgumentException("JSTnode '"+methodName+"' creation method was not defined");
		}
		
		return node;
	}
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	AbsStatement generateStatement(Context context)
	{
		HashMap<JSTNodes, Double> hs = new HashMap<JSTNodes, Double>();
	
		// Factor for making leafs commoner in deep depth parts
		double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _depth);
		
		// All properties are relative to the total of all properties
		
	// Leafs
		hs.put(JSTNodes.VarDecleration, (double) _configs.valInt(ConfigProperties.STMT_VARDECLERATION) / factorDepth);
		hs.put(JSTNodes.CompoundAssignment, (double) _configs.valInt(ConfigProperties.STMT_COMPOUNDASSIGNMENT) / factorDepth);
		hs.put(JSTNodes.Assignment, (double) _configs.valInt(ConfigProperties.STMT_ASSIGNMENT) / factorDepth);
		hs.put(JSTNodes.AbsExpression, (double) _configs.valInt(ConfigProperties.STMT_EXPRESSION) / factorDepth);
		hs.put(JSTNodes.Call, (double) _configs.valInt(ConfigProperties.STMT_CALL) / factorDepth);
		
		// Is in function
		if (context.isInFunction())
			hs.put(JSTNodes.Return, (double) _configs.valInt(ConfigProperties.STMT_RETURN) / factorDepth);
		
		// Is in loop
		if (context.isInLoop())
		{
			hs.put(JSTNodes.Break, (double) _configs.valInt(ConfigProperties.STMT_BREAK) / factorDepth);
			hs.put(JSTNodes.Continue, (double) _configs.valInt(ConfigProperties.STMT_CONTINUE) / factorDepth);
		}

	// Non-Leafs
		hs.put(JSTNodes.If, (double) _configs.valInt(ConfigProperties.STMT_IF) * factorDepth);
		hs.put(JSTNodes.Switch, (double) _configs.valInt(ConfigProperties.STMT_SWITCH) * factorDepth);
		
		// Prevent some JSTnodes from being generated within imaginary scopes
		if (!context.isImaginaryContext())
		{
			hs.put(JSTNodes.FunctionDef, (double) _configs.valInt(ConfigProperties.STMT_FUNCTIONDEFINITION) * factorDepth);
		}
		
		// Lower the probability of nested loop
		factorDepth *= _configs.valDouble(ConfigProperties.NESTED_LOOPS_FACTOR) * (context.getLoopDepth() + 1); // depth must starts from 1 (not 0)
		
		hs.put(JSTNodes.ForEach, (double) (_configs.valInt(ConfigProperties.STMT_FOREACH) * factorDepth));
		hs.put(JSTNodes.While, (double) (_configs.valInt(ConfigProperties.STMT_WHILE) * factorDepth));
		hs.put(JSTNodes.DoWhile, (double) (_configs.valInt(ConfigProperties.STMT_DOWHILE) * factorDepth));
		hs.put(JSTNodes.For, (double) (_configs.valInt(ConfigProperties.STMT_FOR) * factorDepth));
		
		// randomly choose statement
		JSTNodes createMethod = StdRandom.chooseFromProbList(hs);
		
		GenerateExpressionParams params = null;
		
		// if expression was selected then prevent object expression and anonymous function
		if (createMethod==JSTNodes.AbsExpression) {
			params = new GenerateExpressionParams(false);
			params.addOption(JSTNodes.ObjectExp, null);
			params.addOption(JSTNodes.FunctionExp, null);
		}
		
		return (AbsStatement) applyMethod(createMethod, context, params);
	}

	/**
	 * This is an initial and non complex solution
	 * Get all pr  obabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	AbsExpression generateExpression(Context context, GenerateExpressionParams params)
	{
		// Get expression type options from params
		HashMap<JSTNodes, Double> hs = new HashMap<JSTNodes, Double>();
		
		double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _depth);
		
		// leafs - probability increase as depth grows
		testAndPut(params, hs, JSTNodes.Identifier, _configs.valInt(ConfigProperties.EXPR_IDENTIFIER)/factorDepth);
		testAndPut(params, hs, JSTNodes.Literal, _configs.valInt(ConfigProperties.EXPR_LITERAL)/factorDepth);
		testAndPut(params, hs, JSTNodes.This, _configs.valInt(ConfigProperties.EXPR_THIS)/factorDepth);
		
		// non-leafs - probability decrease as depth grows
		testAndPut(params, hs, JSTNodes.OperationExp, _configs.valInt(ConfigProperties.EXPR_EXPRESSIONOP)*factorDepth);
		testAndPut(params, hs, JSTNodes.Call, _configs.valInt(ConfigProperties.EXPR_CALL)*factorDepth);
		//testAndPut(params, hs, JSTNodes.ArrayExp, _configs.valInt(ConfigProperties.EXPR_ARRAYEXPRESSION)*factorDepth);
		testAndPut(params, hs, JSTNodes.MemberExp, _configs.valInt(ConfigProperties.EXPR_MEMBEREXPRESSION)*factorDepth);
		
		//ObjectExp is illegal statement
		testAndPut(params, hs, JSTNodes.ObjectExp, _configs.valInt(ConfigProperties.EXPR_OBJECTEXPRESSION)*factorDepth);
		
		testAndPut(params, hs, JSTNodes.FunctionExp, _configs.valInt(ConfigProperties.EXPR_FUNCTIONEXPRESSION)*factorDepth);
		
		// randomly choose expression
		JSTNodes createMethod = StdRandom.chooseFromProbList(hs);
		
		// If this is a special expresstion transfer its special parameters
		createParams applyParams = (params!= null) ? params.getOptions().get(createMethod) : null;
		
		return (AbsExpression) applyMethod(createMethod, context, applyParams);
	}

	/**
	 * @param context - current Context
	 * @param params - reserved parameter, null expected
	 * @return an AbsExpression, suitable to be used as condition
	 */
	AbsExpression generateCondition(Context context, createParams params)
	{
		// create params for all options
		GenerateExpressionParams GenExpParams = new GenerateExpressionParams(true);
		
		GenExpParams.addOption(JSTNodes.Identifier, null);
		GenExpParams.addOption(JSTNodes.Call, null);
		
		// OperationExp that returns boolean value
		GenerateOpExprParams opExpParams = new GenerateOpExprParams(DataTypes.BOOLEAN);
		GenExpParams.addOption(JSTNodes.OperationExp, opExpParams);
		
		return generateExpression(context, GenExpParams);
	}
	
	private void testAndPut(createParams params, HashMap<JSTNodes, Double> hs, JSTNodes node, double val) 
	{
		Map<JSTNodes, createParams> options = null;
		
		// include if no parameter is given
		if (!(params instanceof GenerateExpressionParams))
		{
			hs.put(node, val);
		}
		else
		{
			GenerateExpressionParams ExpParams = (GenerateExpressionParams)params; 
			options = ExpParams.getOptions();
		
			// If no options were given or include this or disclude other than this
			if ((options == null) || (ExpParams.getInclude() == options.containsKey(node)))
			{
				hs.put(node, val);
			}
		}
	}

	public List<AbsExpression> generateExpression(Context context, GenerateExpressionParams params, int size)
	{
		List<AbsExpression> expList = new LinkedList<AbsExpression>();
		
		for (int i=0 ; i<size ; i++) {
			expList.add(generateExpression(context, params));
		}
		
		return expList;
	}
	
	public List<AbsStatement> generateStatement(Context context, int size)
	{
		List<AbsStatement> stmtList = new LinkedList<AbsStatement>();
		
		for (int i=0 ; i<size ; i++) {
			stmtList.add(generateStatement(context));
		}
		
		return stmtList;
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