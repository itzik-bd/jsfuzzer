package Generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import Generator.Config.ConfigProperties;
import Generator.Config.Configs;
import Generator.Params.*;
import Generator.SymTable.*;
import JST.*;
import JST.Enums.CompoundOps;
import JST.Enums.DataTypes;
import JST.Enums.JSTNodes;
import JST.Enums.LiteralTypes;
import JST.Enums.Operator;
import JST.Interfaces.Assignable;
import JST.Interfaces.Caseable;
import JST.Interfaces.ObjectKeys;
import Main.JsFuzzerConfigs;
import Utils.OutLog;
import Utils.StdRandom;
import Utils.StringCounter;

public class Generator
{
	// TODO: major bug! when using the same symbol table as the parent
	// the variable deceleration is known at the outer scope!!
	// WTF - how do we gonna solve it??????
	// this is relevant for the FOR loop also.
	
	// static fields
	private final static JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private final static JST.Helper.JSTProperties _jstProp = new JST.Helper.JSTProperties();
	
	private final static String _largestJSNumber = "9007199254740991";
	
	// instance fields - one time initialize
	private final Configs _configs;
	private final GenLogic _logic;
	private final ExecFlow _flowLevel;
	
	// instance fields - one time initialize, but may be restarted
	private final StringCounter _counterVar = new StringCounter("v%d");
	private final StringCounter _counterFunc = new StringCounter("f%d");
	private final StringCounter _counterLoopVar = new StringCounter("loop_var%d");
	
	private final StringBuilder _verboseString = new StringBuilder();

	// instance fields - many time initialize (for each generated program)
	private Program _program; // generated program instance
	private Context _rootContext; // global scope
		
	public Generator(Configs configs, String seed, ExecFlow flowLevel)
	{
		// set internal properties
		_configs = configs;
		_flowLevel = flowLevel;
		_logic = new GenLogic(this, configs);

		// set seed random seed, to be able to generate the same program again
		if (seed != null) {
			StdRandom.setSeed(Long.parseLong(seed)); // development: use seed: 1436423645
		}
		OutLog.printInfo("Generate program with seed: " + StdRandom.getSeed());
	}
	
	public Program createProgram()
	{
		initNewProgram();
		traceIn("Program");

		_program.addStatement(generateHeader());
		_program.addStatement(_factoryJST.getSnippet("rawUtilsFunctions"));
		_program.addStatement(new Comment("--------------------------------- [ Generated Program ] ---------------------------------", 2, 1));
		
		// first of all generate variables to be used
		int numAutoGenVars = _configs.valInt(ConfigProperties.NUM_FORCED_GENERATED_VARS);
		_program.addStatement(createVarDecleration(_rootContext, new VarDeclerationParams(true, numAutoGenVars, null, false)));
		
		int numAutoGenFuncs = _configs.valInt(ConfigProperties.NUM_FORCED_GENERATED_FUNCS);
		for (int i = 0 ; i < numAutoGenFuncs ; i++)
		{
			_program.addStatement(createFunctionDef(_rootContext, null));
		}
		
		// choose how many statements the program will have
		int size = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.PROGRAM_SIZE_LAMBDA));
		
		// generate random statements
		_program.addStatement(_logic.generateStatement(_rootContext, size));
		
		// add statement that print "Execution is over"		
		Call printExecutionIsOverCall = new Call(getApiMethod(ApiOptions.PRINT), new LiteralString("\n\nExecution is over.\n\n"));
		printExecutionIsOverCall.setNoneRandomBranch();
		_program.addStatement(printExecutionIsOverCall);
		
		// add print stmts for each program's top scope identifier
		List<Identifier> idList = _rootContext.getSymTable().getAvaiableEntries(SymEntryType.VAR).stream().map(element->element.getIdentifier()).collect(Collectors.toList());
		_program.addStatement(new Comment("-------------------------- Printing All The Program Variables --------------------------", false, 3, 1));
		_program.addStatement(generateTraceDebugVarsCall(idList, null));

		// attach configuration used to generate program
		_program.addStatement(generateFooter());

		traceOut();
		return _program;
	}

	private void initNewProgram()
	{
		_program = new Program();
		_rootContext = new Context();
		
		// restart all counters
		_counterVar.restart();
		_counterLoopVar.restart();
		_counterFunc.restart();
		
		// restart verbose string
		_verboseString.setLength(0);
	}

	public String getVerboseOutput()
	{
		return _verboseString.toString();
	}
	
	// ===============================================================================

	private void trace(String str)
	{
		// add to verbose message string
		_verboseString.append(String.format("%3d", _logic.getDepth()));
		for (int i = 0; i < _logic.getDepth(); i++)
			_verboseString.append(" ");
		_verboseString.append(str);
		_verboseString.append("\n");
	}
	
	private void traceIn(String str)
	{
		trace(str);
		_logic.increaseDepth();
	}
	
	private void traceRemoveLine()
	{
		_verboseString.setLength(_verboseString.length() - 1); // remove last "\n"
		_verboseString.setLength(_verboseString.lastIndexOf("\n") + 1); // remove last line
	}

	private void traceOut()
	{
		_logic.decreaseDepth();
	}

	private Comment generateHeader()
	{
		StringBuffer s = new StringBuffer();
		String EOL = "\n";
		s.append("This program was generated by jsfuzzer version " + JsFuzzerConfigs.getVersion() + EOL + EOL);
		s.append("seed: " + StdRandom.getSeed() + EOL);
		s.append("date: " + (new Date()) + EOL);

		return new Comment(s.toString(), 0, 2);
	}
	
	private Comment generateFooter()
	{
		return new Comment(_configs.toString());
	}

	// ===============================================================================

	If createIf(Context context, createParams params)
	{
		traceIn("If");
				
		// Genersate condition
		AbsExpression conditionExp = _logic.generateCondition(context, params);
		
		// create the imaginary context defined by the if (note: same context for if and else)
		Context newContext = new Context(context, null, null, true);
		
		// create Statements Block
		StatementsBlock trueOp = createStatementsBlock(newContext, null);
		StatementsBlock falseOp = null;
		
		// decide on "else" operation
		if (StdRandom.bernoulli())
			falseOp = createStatementsBlock(newContext, null);

		// create If JST Node
		If ifStmt = new If(conditionExp, trueOp, falseOp);
		
		traceOut();
		return ifStmt;
	}

	While createWhile(Context context, createParams params)
	{
		traceIn("While");
		While whileLoop = (While) commonWhile(While.class, context);
		
		traceOut();
		return whileLoop;
	}

	DoWhile createDoWhile(Context context, createParams params)
	{
		traceIn("DoWhile");
		DoWhile doWhileLoop = (DoWhile) commonWhile(DoWhile.class, context);
		
		traceOut();
		return doWhileLoop;
	}

	For createFor(Context context, createParams params)
	{
		traceIn("For");

		// create new loop context that has the same symbol table as his parent
		Context newContext = new Context(context, true, null, true);

		AbsStatement initStmt = createVarDecleration(newContext, new VarDeclerationParams(null, 1, null, false));
		AbsExpression stepExpr = _logic.generateExpression(newContext, null);
		
		// Generate condition
		AbsExpression conditionExpr = _logic.generateCondition(newContext, null);

		// first: loopVarDecl (added to the previous scope), second: stopping cond and step
		// loopVar will be added to the prev scope
		List<AbsStatement> stmts = getLoopCounterStmts(newContext);

		VarDecleration loopCounterDecl = (VarDecleration) stmts.get(0);
		If stopAndStepCond = (If) stmts.get(1);

		StatementsBlock stmtsBlock = createStatementsBlock(newContext, new StatementBlockParams(stopAndStepCond, null));

		// create for loop: for(random var decl; random expr; random expr)
		For forLoop = new For(initStmt, conditionExpr, stepExpr, stmtsBlock, loopCounterDecl);

		traceOut();
		return forLoop;
	}

	ForEach createForEach(Context context, createParams params)
	{
		traceIn("ForEach");
		ForEach forEachStmt;

		// Temporary solution (could also be any existing iteratable var)
		ArrayExp arr = createArrayExp(context, null);
		
		// create new loop context that has the same symbol table as his parent
		Context newContext = new Context(context, true, null, true);

		// create one varDecl (with new identifier) wihtout init value
		VarDecleration varDecl = createVarDecleration(newContext, new VarDeclerationParams(true, 1, false, false));

		// Generate StatementsBlock
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);

		forEachStmt = new ForEach(varDecl, arr, stmtsBlock);

		traceOut();
		return forEachStmt;
	}

	Switch createSwitch(Context context, createParams params)
	{
		traceIn("Switch");
		Switch switchStmt;

		// Make Identifier more probable for this generateExpression()
		Map<String, Integer> specialProbs = new HashMap<String, Integer>();
		specialProbs.put("Identifier", _configs.valInt(ConfigProperties.IN_SWITCH_IDENTIGIER_PROB));

		AbsExpression expr = _logic.generateExpression(context, null);
		switchStmt = new Switch(expr);

		int caseBlocksNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.SWITCH_BLOCK_NUM_LAMBDA_EXP));
		boolean defaultWasIncluded = false;
		CaseBlockParams caseParam = new CaseBlockParams();

		for (int i = 0; i < caseBlocksNum; i++)
		{
			// generate default case only once
			if (!defaultWasIncluded)
			{
				if (StdRandom.bernoulli(_configs.valDouble(ConfigProperties.CASE_BLOCK_INCLUDE_DEFAULT_BERNOULLY_P)))
				{
					caseParam.setIncludeDefualt(true);
					switchStmt.addCaseOp(createCaseBlock(context, caseParam));
					defaultWasIncluded = true;
					continue;
				}
			}

			caseParam.setIncludeDefualt(false);
			switchStmt.addCaseOp(createCaseBlock(context, caseParam));
		}

		traceOut();
		return switchStmt;
	}

	CaseBlock createCaseBlock(Context context, createParams params)
	{
		traceIn("CaseBlock");
		CaseBlock caseBlock;

		// generate cases
		int casesNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.CASE_NUM_LAMBDA_EXP));
		List<Caseable> cases = new LinkedList<Caseable>();

		if (CaseBlockParams.getIncludeDefault(params))
		{
			trace("Default");
			cases.add((Default) _factoryJST.getConstantNode("default"));
			casesNum--;
		}

		for (int i = 0; i < casesNum; i++)
			cases.add(createCase(context, null));
		
		// create the imaginary context defined by the case block
		Context newContext = new Context(context, null, null, true);

		// generate operation - statement block
		StatementsBlock stmtBlock = createStatementsBlock(newContext, null);

		caseBlock = new CaseBlock(cases, stmtBlock);

		traceOut();
		return caseBlock;
	}

	Case createCase(Context context, createParams params)
	{
		traceIn("Case");
		Case caseObj = new Case(_logic.generateExpression(context, null));

		traceOut();
		return caseObj;
	}

	FunctionExp createFunctionExp(Context context, createParams params) 
	{
		traceIn("FunctionExp");
		_logic.increaseFuncDepth(3);
		
		// Randomize number of parameters
		int ParamNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.FUNC_PARAMS_NUM_LAMBDA_EXP));
	
		// Send number of parameters back to caller
		if (params != null)
			((funcDefParams)params).setParamNumber(ParamNum);
				
		// create the context defined by the function (force no loop)
		Context newContext = new Context(context, false, true, false);
		
		// Generate parameters
		List<Identifier> funcParams = getFunctionParametersList(newContext, ParamNum);
		
		StatementBlockParams stmtParams = null;
		
		if (_flowLevel.isA(ExecFlow.EXTENSIVE))
		{	
			Call debugParamsCall = generateTraceDebugVarsCall(funcParams, "param %s");	
			stmtParams = new StatementBlockParams(debugParamsCall, null);
		}
		
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, stmtParams);

		FunctionExp funcExp = new FunctionExp(funcParams, stmtsBlock);
		
		_logic.decreaseFuncDepth(3);
		traceOut();
		return funcExp;
	}
	
	FunctionDef createFunctionDef(Context context, createParams params)
	{
		traceIn("FunctionDef");
		_logic.increaseFuncDepth(1);

		// Randomize function name
		Identifier functionId = _factoryJST.getFuncIdentifier(_counterFunc.getNext());
		trace(String.format("Identifier (%s)", functionId));

		// Randomize number of parameters
		int paramsNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.FUNC_PARAMS_NUM_LAMBDA_EXP));
		
		// Create the context defined by the function (force no loop)
		Context newContext = new Context(context, false, true, false);
		
		// create function params identifiers
		List<Identifier> funcParams = getFunctionParametersList(newContext, paramsNum);
		
		StatementBlockParams stmtParams = null;
		
		if (_flowLevel.isA(ExecFlow.EXTENSIVE))
		{	
			Call debugParamsCall = generateTraceDebugVarsCall(funcParams, "param %s");	
			stmtParams = new StatementBlockParams(debugParamsCall, null);
		}

		StatementsBlock stmtsBlock = createStatementsBlock(newContext, stmtParams);
		Call registerFunctionInRuntimeCall = createRegisterFunctionInRuntimeCall(functionId);

		FunctionDef funcDef = new FunctionDef(functionId, funcParams, stmtsBlock, registerFunctionInRuntimeCall);

		// Add function to current scope
		// (!) This is performed after FunctionDef() to avoid recursion (and inner defined function calling father function)
		context.getSymTable().newEntry(new SymEntryFunc(functionId, paramsNum));
		
		_logic.decreaseFuncDepth(1);
		traceOut();
		return funcDef;
	}

	VarDecleration createVarDecleration(Context context, createParams params)
	{
		traceIn("VarDecleration");
		VarDecleration varDecleration;

		// decide on declerators number
		int decleratorsNum = VarDeclerationParams.getNumDeclerators(params);
		if (decleratorsNum == 0) 
			decleratorsNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP));

		// create var declaration and add new declerators
		varDecleration = new VarDecleration();

		for (int i = 0; i < decleratorsNum; i++)
			varDecleration.addDeclerator(createVarDeclerator(context, params));

		traceOut();
		return varDecleration;
	}

	VarDeclerator createVarDeclerator(Context context, createParams params)
	{
		traceIn("VarDeclerator");
		VarDeclerator varDeclerator;

		// decide on probability if define new var
		IdentifierParams idParams;

		if (VarDeclerationParams.getForceNewIdentifier(params))
			idParams = new IdentifierParams(0);
		else
			idParams = new IdentifierParams(_configs.valDouble(ConfigProperties.VAR_RE_DECL_EXISTING_VAR));

		// create identifier that does not exists in current scope
		Identifier id = createVarIdentifierNotInCurrentScope(context, idParams);

		// add identifier to current scope
		context.getSymTable().newEntry(new SymEntryVar(id));

		// generate expression to be assigned to the var
		boolean createInitExp;
		switch (VarDeclerationParams.getHasInitValue(params))
		{
			case TRUE: createInitExp = true; break;
			case UNDEF: createInitExp = StdRandom.bernoulli(_configs.valDouble(ConfigProperties.VAR_DECL_INIT_VAL_BERNOULLY_P)); break;
			default: createInitExp = false;
		}

		// If right side cannot be call, exclude it
		GenerateExpressionParams genExpPar = null;
		if (!VarDeclerationParams.getIsCallAllowed(params))
		{
			genExpPar = new GenerateExpressionParams(false);
			genExpPar.addOption(JSTNodes.FunctionExp, null);
			genExpPar.addOption(JSTNodes.Call, null);
		}
		
		AbsExpression exp = createInitExp ? _logic.generateExpression(context, genExpPar) : null;
		varDeclerator = new VarDeclerator(id, exp);

		traceOut();
		return varDeclerator;
	}

	Continue createContinue(Context context, createParams params)
	{
		trace("Continue");
		return (Continue) _factoryJST.getConstantNode("continue");
	}

	Break createBreak(Context context, createParams params)
	{
		trace("Break");
		return ((Break) _factoryJST.getConstantNode("break"));
	}

	Return createReturn(Context context, createParams params)
	{
		traceIn("Return");
		Return returnStmt;

		// decide whether to return a value
		if (StdRandom.bernoulli(_configs.valDouble(ConfigProperties.RETURN_VALUE_BERNOULLY_P)))
			returnStmt = new Return(_logic.generateExpression(context, null));
		else
			returnStmt = new Return();

		traceOut();
		return returnStmt;
	}

	/** IMPORTANT: context is the context defined by this StatementBlock */
	StatementsBlock createStatementsBlock(Context context, createParams params)
	{
		traceIn("StatementsBlock");
		StatementsBlock stmtBlock = new StatementsBlock();
		
		stmtBlock.addStatement(new Comment("block id: " + stmtBlock.getUniqueId()));
		
		if (_flowLevel.isA(ExecFlow.EXTENSIVE))
		{
			Call debugCall = new Call(getApiMethod(ApiOptions.PRINT), new LiteralString("block " + stmtBlock.getUniqueId()));
			debugCall.setNoneRandomBranch();
			stmtBlock.addStatement(debugCall);
		}
		
		// add first statement from params
		AbsStatement firstStatement = StatementBlockParams.getFirstStatement(params);
		if (firstStatement != null)
			stmtBlock.addStatement(firstStatement);

		// decide how many statements will be generate
		int size = 0;

		if (_logic.getDepth() < _configs.valInt(ConfigProperties.MAX_JST_DEPTH))
		{
			// choose the block size
			double factorDepth;
			if (StatementBlockParams.getIsFunction(params))
				factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _logic.getDepth());
			else
				factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _logic.getFuncDepth());
			
			
			double lambda = _configs.valDouble(ConfigProperties.STMTS_BLOCK_SIZE_LAMBDA) / factorDepth;
			size = StdRandom.expCeiled(lambda);
		}

		// generate statements
		stmtBlock.addStatement(_logic.generateStatement(context, size));
		
		// add last statement from params
		AbsStatement lastStatement = StatementBlockParams.getLastStatement(params);
		if (lastStatement != null)
			stmtBlock.addStatement(lastStatement);

		traceOut();
		return stmtBlock;
	}

	/**
	 * This function draws an identifier to be assigned. Currently we force the
	 * identifier to be defined in higher scopes (or current scope),
	 */
	Assignment createAssignment(Context context, createParams params)
	{
		traceIn("Assignment");
		Assignment assignment;

		// Left side
		Assignable leftHandSide = createIdentifier(context, new IdentifierParams(1.0)); //force existing var
		
		// Right side (with GenerateExpressionParams)
		GenerateExpressionParams expParam = new GenerateExpressionParams(null);
		expParam.addOption(JSTNodes.Literal, new LiteralParams(new LiteralTypes[] {LiteralTypes.NUMBER, LiteralTypes.STRING, LiteralTypes.TRUE, LiteralTypes.FALSE, LiteralTypes.INFINITY}));
		AbsExpression expr = _logic.generateExpression(context, expParam);

		assignment = new Assignment(leftHandSide, expr);

		traceOut();
		return assignment;
	}

	CompoundAssignment createCompoundAssignment(Context context, createParams params)
	{
		CompoundOps op = CompoundOps.getRandomly();
		
		traceIn(String.format("CompoundAssignment (%s)", op));
		CompoundAssignment compAsssignment;

		Identifier var = createIdentifier(context, new IdentifierParams(1.0)); // use only existing variables
		AbsExpression expr = _logic.generateExpression(context, null);

		compAsssignment = new CompoundAssignment(var, op, expr);

		traceOut();
		return compAsssignment;
	}

	Call createCall(Context context, createParams params)
	{
		traceIn("Call");

		SymTable symbols = context.getSymTable();
		
		// Get existing functions list
		List<SymEntry> functions = symbols.getAvaiableEntries(SymEntryType.FUNC);
		
		//get parameters ready
		AbsExpression func;
		LiteralString funcName;
		int paramNum;
		
		// choose randomly between existing or anonymous function
		double p = _configs.valDouble(ConfigProperties.CALL_EXISTING_FUNCTION_BERNOULLY_P);
		boolean chooseExisting = StdRandom.bernoulli(p);
		if (chooseExisting && functions.size() > 0)
		{
			// choose a function between existing
			int funcIndex = StdRandom.uniform(functions.size());
			SymEntryFunc funcEntry = (SymEntryFunc) functions.get(funcIndex);
			
			// trace function name
			trace(String.format("Identifier (%s)", funcEntry.getIdentifier()));
			
			// Get number of parameters
			paramNum = funcEntry.getParamsNumber();
			
			// create the call node
			Identifier funcId = funcEntry.getIdentifier();
			funcName = new LiteralString(funcId.getName());
			
			OperationExp compare = new OperationExp(Operator.EQUALTYPE, new OperationExp(Operator.TYPEOF, funcId), (LiteralString)_factoryJST.getConstantNode("lit-function"));
			func = new OperationExp(Operator.CONDOP, compare, funcId, _factoryJST.getSingleLiteralNode(LiteralTypes.UNDEFINED));
		}
		else
		{
			// Create the anonymous function
			funcDefParams newParams = new funcDefParams();
			FunctionExp funcExp = createFunctionExp(context, newParams);
			funcName = new LiteralString("anonymous f" + funcExp.getUniqueId());
			func = funcExp;
			
			// Get number of parameters
			paramNum = newParams.getParamNumber();
		}

		// Do not allow abstract function as parameter
		GenerateExpressionParams genExpParams = new GenerateExpressionParams(false);
		genExpParams.addOption(JSTNodes.FunctionExp, null);
		
		// create list of parameter
		List<AbsExpression> callParams = _logic.generateExpression(context, genExpParams, paramNum);
		
		// create a proxy call to function
		Call wrappedCall = new Call(getApiMethod(ApiOptions.PROXY_CALL), callParams);
		
		// adding to the wrapped call the following information:
		// 1) the call unique id (for later debug purpose)
		// 2) as the first parameter to the proxy call function
		// 3) add the function name (or its implementation in case of anonymous function) 
		callParams.add(0, new LiteralNumber(wrappedCall.getUniqueId()+"")); // +"" is to convert int to string
		callParams.add(0, funcName);
		callParams.add(0, func);
					
		traceOut();
		return wrappedCall;
	}


	Call createMemberExp(Context context, createParams params)
	{
		traceIn("MemberExpr");
		
		AbsExpression baseId = createIdentifier(context, new IdentifierParams(1.0)); //force existing var
		
		// key will be in {k_1, .... , k_n} , where n is configurabe
		int totalKeysNum = _configs.valInt(ConfigProperties.MEMBER_EXPR_TOTAL_KEYS_NUM);
		int keyNum = StdRandom.uniform(1, totalKeysNum + 1);

		LiteralString keyStr = _factoryJST.getObjectKeyLiteralString("k" + keyNum);
		
		traceOut();
		return new Call(getApiMethod(ApiOptions.MEMBER_EXPR), baseId, keyStr); //use api method [ $.mem(baseId, 'key') ]
	}

	ObjectExp createObjectExp(Context context, createParams params)
	{
		traceIn("ObjectExp");
		ObjectExp objExp = new ObjectExp();

		int size = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.OBJECT_KEYS_LENGTH_LAMBDA_EXP));

		for (int i = 0; i < size; i++)
		{
			ObjectKeys key = (ObjectKeys) _logic.applyMethod(StdRandom.chooseFromListUniform(_jstProp.getObjectKeys()), context, null);
			AbsExpression val = _logic.generateExpression(context, null);

			objExp.addToMap(key, val);
		}

		traceOut();
		return objExp;
	}

	ArrayExp createArrayExp(Context context, createParams params)
	{
		traceIn("ArrayExp");
		ArrayExp arrayExp;

		int size = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.ARRAY_LENGTH_LAMBDA_EXP));
		
		arrayExp = new ArrayExp(_logic.generateExpression(context, null, size));

		traceOut();
		return arrayExp;
	}

	Identifier createIdentifier(Context context, createParams params)
	{
		Identifier id;

		// decide whether to use an existing variable
		if (StdRandom.bernoulli(IdentifierParams.getUseExistingVarProb(params)))
		{
			// fetch all available vars in the current scope or higher
			Map<SymEntry,Integer> existingVarsLevels = new HashMap<SymEntry,Integer>();
			int maxLevel = context.getSymTable().getAvaiableEntriesWithLevels(SymEntryType.VAR, existingVarsLevels);
			int totalVars = existingVarsLevels.size();

			// if no var is defined then throw an error.
			if (totalVars == 0)
				throw new RuntimeException("no var symbols found when thrying to get one");
			
			// build probability map for each var (prefering nested vars = low level)
			double factor = _configs.valDouble(ConfigProperties.VARIABLE_FUNCTION_WISE_FACTOR);
			
			Map<SymEntry,Double> probMap = new HashMap<SymEntry, Double>();
			for (SymEntry entry : existingVarsLevels.keySet())
			{
				// compute p^(n-i) where p is the factor, n is max level, and i is the current level
				double prob = Math.pow(factor, maxLevel - existingVarsLevels.get(entry));
				probMap.put(entry, prob);
			}
			
			// select random var among the list
			id = StdRandom.chooseFromProbList(probMap).getIdentifier();
		}
		else
		{
			String newName = _counterVar.getNext();
			id = _factoryJST.getIdentifier(newName);
		}

		trace(String.format("Identifier (%s)", id));
		return id;
	}

	This createThis(Context context, createParams params)
	{
		trace("This");
		return ((This) _factoryJST.getConstantNode("this"));
	}

	OperationExp createOperationExp(Context context, createParams params)
	{
		Set<DataTypes> desiredType = OperationExpressionParams.getDataType(params);
		Operator operator = Operator.getRandomly(desiredType);
		
		traceIn(String.format("OperationExp (%s)", operator));
		OperationExp expressionOp;

		Operator[] unaryOpAssignable = { Operator.PLUSPLUSLEFT, Operator.PLUSPLUSRIGHT, 
				Operator.MINUSMINUSLEFT, Operator.MINUSMINUSRIGHT };

		// Generate operands array
		List<AbsExpression> operandsList;

		// Does operand forces identifier as operand
		if (Arrays.asList(unaryOpAssignable).contains(operator))
		{
			operandsList = new ArrayList<AbsExpression>();
			operandsList.add((AbsExpression) _logic.applyMethod(StdRandom.chooseFromListUniform(_jstProp.getAssignable()), context, null));
		}
		else
		{
			DataTypes[][] operatorArgsTypes = operator.getArgsInputTypes();
			operandsList = new ArrayList<AbsExpression>();
			
			for (int i=0; i<operator.getNumOperands(); i++)
			{
				GenerateExpressionParams opParams = new GenerateExpressionParams(null);
				opParams.addOption(JSTNodes.OperationExp, new OperationExpressionParams(operatorArgsTypes[i]));
				
				operandsList.add(_logic.generateExpression(context, opParams));
			}
		}

		expressionOp = new OperationExp(operator, operandsList);

		traceOut();
		return expressionOp;
	}

	Literal createLiteral(Context context, createParams params)
	{
		Literal lit = null;
		LiteralTypes litType = LiteralTypes.getRandomly(LiteralParams.getAcceptedLiteralTypes(params));
		
		if (litType.isSingleValue())
		{
			trace(String.format("Literal (%s)", litType));
			lit = new Literal(litType);
		}
		else
		{			
			if (litType.equals(LiteralTypes.NUMBER))
				lit = (createLiteralNumber(context, null));
			else if (litType.equals(LiteralTypes.STRING))
				lit = (createLiteralString(context, null));
		}
		
		return lit;
	}

	LiteralString createLiteralString(Context context, createParams params)
	{
		LiteralString litStr;

		int maxLength = _configs.valInt(ConfigProperties.LITERAL_STRING_MAX_LENGTH);
		StringBuilder strBld = new StringBuilder();

		// get alphabet set from configs
		String alphabet = _configs.valString(ConfigProperties.STRING_CHARS);
		
		// Randomize string's length
		int length = Math.min(maxLength, StdRandom.expCeiled(_configs.valDouble(ConfigProperties.LITERAL_STRING_LAMBDA)));

		for (int i = 0; i < length; i++)
			strBld.append(alphabet.charAt(StdRandom.uniform(alphabet.length())));

		litStr = new LiteralString(strBld.toString());

		trace(String.format("LiteralString (%s)", litStr.getValue()));
		return litStr;
	}

	LiteralNumber createLiteralNumber(Context context, createParams params)
	{
		LiteralNumber litNum;
		
		// return infinity?
		double infinity = _configs.valDouble(ConfigProperties.LITERAL_NUMBER_MAX_PROBABILITY);
		if (StdRandom.bernoulli(infinity)) {
			litNum = (new LiteralNumber("9007199254740992"));
		}
		else
		{
			StringBuilder strBld = new StringBuilder();
			int length;
			
			//keep randomize number's length until legal length
			while(true)
			{
				// Randomize number's length
				length = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.LITERAL_NUMBER_LAMBDA));
				if (length <= _largestJSNumber.length())
					break;
			}
			
			//keep randomize number to get a legal one
			while (true)
			{
				strBld.setLength(0);
				for (int i = 0; i < length; i++)
				{
					if (i == 0) //first digit must be > 0
						strBld.append(StdRandom.uniform(1,10));
					else
						strBld.append(StdRandom.uniform(10));				
				}
				
				if(isLegalNum(strBld))
					break;
			}
			litNum = new LiteralNumber(strBld.toString());
		}

		trace(String.format("LiteralNumber (%s)", litNum));
		return litNum;
	}

	// ===============================================================================

	/**
	 * handles the createWhile/createDoWhile, and returns the instance by the
	 * class param
	 */
	private AbsWhileLoop commonWhile(Class<? extends AbsWhileLoop> classWhile, Context context)
	{
		AbsWhileLoop whileLoop = null;

		// create new loop imaginary context that has the same symbol table as his parent
		Context newContext = new Context(context, true, null, true);

		// create loop condition
		AbsExpression conditionExp = _logic.generateCondition(newContext, null);

		// first: loopVarDecl (added to the previous scope), second: stopping cond and step
		// loopVar will be added to the prev scope
		List<AbsStatement> stmts = getLoopCounterStmts(newContext);

		VarDecleration loopCounterDecl = (VarDecleration) stmts.get(0);
		If stopAndStepCond = (If) stmts.get(1);

		// create loop content
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, new StatementBlockParams(stopAndStepCond, null));

		try {
			// create new while/doWhile instance
			whileLoop = classWhile.getConstructor(AbsExpression.class,
					StatementsBlock.class, VarDecleration.class).newInstance(
					conditionExp, stmtsBlock, loopCounterDecl);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		return whileLoop;
	}
	
	/**
	 * returns two new stmts, loopCounter init, stopping condition and step (in
	 * one stmt). IMPORTANT: param context is the context the loop defines
	 */
	private List<AbsStatement> getLoopCounterStmts(Context context)
	{
		// explicitly create a var decleration: var new_loop_var = 0;
		String newName = _counterLoopVar.getNext();
		Identifier loopCounter = _factoryJST.getLoopIdentifier(newName);
		VarDecleration loopCounterDecl = new VarDecleration();
		loopCounterDecl.addDeclerator(new VarDeclerator(loopCounter, new LiteralNumber("0")));

		// explicitly create binaryOp: new_var++ < max_iterations
		int loopIterationsLimit = getRandomLoopIterationsLimit();
		LiteralNumber loopIterationsLiteral = new LiteralNumber(new Integer(loopIterationsLimit).toString());
		OperationExp loopStopCond = new OperationExp(Operator.LT, new OperationExp(Operator.PLUSPLUSRIGHT, loopCounter), loopIterationsLiteral);

		// explicitly create If: if(new_var++ < max_iterations) break;
		StatementsBlock stmtsBlock = new StatementsBlock();
		stmtsBlock.addStatement((AbsStatement) _factoryJST.getConstantNode("break"));
		If ifStoppingCondition = new If(new OperationExp(Operator.LNEG, loopStopCond), stmtsBlock, null);
		ifStoppingCondition.setNoneRandomBranch(); //this is a non randomized node

		List<AbsStatement> stmts = new LinkedList<AbsStatement>();
		stmts.add(loopCounterDecl);
		stmts.add(ifStoppingCondition);

		return stmts;
	}
	
	private List<Identifier> getFunctionParametersList(Context context, Integer paramsNum)
	{
		// param for create identifier
		IdentifierParams idParams = new IdentifierParams(_configs.valDouble(ConfigProperties.FUNC_PARAM_USE_EXISTING_VAR_BERNOULLY_P));

		List<Identifier> funcParams = new LinkedList<Identifier>();
		for (int i = 0; i < paramsNum; i++)
		{
			Identifier id = createVarIdentifierNotInCurrentScope(context, idParams);
			
			// add param id to function symbol table
			context.getSymTable().newEntry(new SymEntryVar(id));

			// add param id to params list
			funcParams.add(id);
		}
		
		return funcParams;
	}
	
	private int getRandomLoopIterationsLimit()
	{
		int exp = _configs.valInt(ConfigProperties.LOOP_MAX_ITERATIONS_NORMAL_EXP);
		int stddev = _configs.valInt(ConfigProperties.LOOP_MAX_ITERATIONS_NORMAL_STDDEV);
		return (int) StdRandom.gaussian(exp, stddev);
	}

	private Call createRegisterFunctionInRuntimeCall(Identifier functionId)
	{
		Call debugCall = new Call(getApiMethod(ApiOptions.REGISTER_FUNCTION_IN_RUNTIME), new LiteralString(functionId.getName()));
		debugCall.setNoneRandomBranch();
		return debugCall;
	}
	
	private Identifier createVarIdentifierNotInCurrentScope(Context context, createParams params)
	{
		Identifier id = createIdentifier(context, params);
		
		while (context.getSymTable().contains(id))
		{
			traceRemoveLine();
			id = createIdentifier(context, params);
		}
		
		return id;
	}
	
	/** returns true iff the number represented by strNum is a legal JS number */
	private boolean isLegalNum(StringBuilder strNum) 
	{
		if(strNum.charAt(0) == '0')
			return false;
		
		if (strNum.length() > _largestJSNumber.length())
			return true;
		
		if (strNum.length() > _largestJSNumber.length())
			return false;
		
		//strNum.length == _largestJSNumber.length
		for(int i = 0; i < strNum.length(); i++)
		{
			if (strNum.charAt(i) < _largestJSNumber.charAt(i))
				return true;
			if (strNum.charAt(i) > _largestJSNumber.charAt(i))
				return false;
		}
		
		//we get here only if strNum == _largestJSNumber
		return true;
	}
	
	/**
	 * generate a call to traceDebugVars function that prints all variables with their data:
	 * it constructs an array of pairs: (id title, id val) and pass it to the function
	 */
	private Call generateTraceDebugVarsCall(List<Identifier> identifierList, String titleTemplate) 
	{
		if (titleTemplate == null)
			titleTemplate = "%s";
			
		// create array of pairs: (id name, id val)
		List<AbsExpression> list = new LinkedList<AbsExpression>();
		for(Identifier id: identifierList)
		{
			String title = String.format(titleTemplate, id.getName());
			list.add(new ArrayExp(new LiteralString(title), id));
		}		
		
		// create call to predefined function to print vars
		Call call = new Call(getApiMethod(ApiOptions.TRACE_DEBUG_VARS), new ArrayExp(list));
		call.setNoneRandomBranch();
		
		return call;
	}

	private MemberExp getApiMethod(ApiOptions apiMethod)
	{
		return new MemberExp(_factoryJST.getIdentifier("$"), _factoryJST.getFuncIdentifier(apiMethod.getApiName()));
	}
}
