package Generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Generator.Config.ConfigProperties;
import Generator.Config.Configs;
import Generator.Params.*;
import Generator.SymTable.*;
import JST.*;
import JST.Enums.CompoundOps;
import JST.Enums.LiteralTypes;
import JST.Enums.Operator;
import JST.Interfaces.Caseable;
import JST.Interfaces.ObjectKeys;
import JST.Interfaces.ProgramUnit;
import Utils.OutLog;
import Utils.StdRandom;
import Utils.StringCounter;

public class Generator
{
	// static fields
	private final static JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private final static JST.Helper.JSTProperties _jstProp = new JST.Helper.JSTProperties();
	
	private final static String _largestJSNumber = "9007199254740992";
	
	// instance fields - one time initialize
	private final Configs _configs;
	private final GenLogic _logic;
	
	// instance fields - one time initialize, but may be restarted
	private final StringCounter _counterVar = new StringCounter("var%d");
	private final StringCounter _counterFunc = new StringCounter("func%d");
	private final StringCounter _counterLoopVar = new StringCounter("loop_var%d");
	
	private final StringBuilder _verboseString = new StringBuilder();

	// instance fields - many time initialize (for each generated program)
	private Program _program; // generated program instance
	private Context _rootContext; // global scope
	
	public Generator(Configs configs, String seed)
	{
		// set internal properties
		_configs = configs;
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
		
		// first of all generate variables to be used
		_program.addStatement(createVarDecleration(_rootContext, new VarDeclerationParams(true, null, null)));
		
		// choose how many statements the program will have
		int size = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.PROGRAM_SIZE_LAMBDA));
		
		// generate statements
		_program.addStatement(_logic.generateStatement(_rootContext, null, size));
		_program.addStatement(newOutputStatement("Execution is over.\n"));
		
		// add print stmts for each program identifier
		addPrintVarsStmts(_program);
		
		_program.addStatement(generateRawFunctions());

		// attach configuration used to generate program
		_program.addStatement(generateFooter());

		traceOut();
		return _program;
	}

	private List<ProgramUnit> generateRawFunctions()
	{
		List<ProgramUnit> rawList = new LinkedList<ProgramUnit>();
		
		// JSPrint
		rawList.add(new Comment("Calling proxy", true));
		rawList.add(_factoryJST.getSnippet("JSCall"));
		
		// JSCall
		rawList.add(new Comment("print proxy", true));
		rawList.add(_factoryJST.getSnippet("JSPrint"));
		
		return rawList;
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
		s.append("This program was generated by jsfuzzer" + EOL + EOL);
		s.append("seed: " + StdRandom.getSeed() + EOL);
		s.append("date: " + (new Date()) + EOL);

		return new Comment(s.toString());
	}
	
	private Comment generateFooter()
	{
		return new Comment(_configs.toString());
	}

	// ===============================================================================

	If createIf(Context context, createParams params)
	{
		traceIn("If");
		
		// create operation and boolean condition
		AbsExpression conditionExp = _logic.generateExpression(context, null);
		StatementsBlock trueOp = createStatementsBlock(context, null);
		StatementsBlock falseOp = null;
		
		// decide on "else" operation
		if (StdRandom.bernoulli())
			falseOp = createStatementsBlock(context, null);

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
		Context newContext = new Context(context, true, null, false);

		AbsStatement initStmt = createVarDecleration(newContext, new VarDeclerationParams(null, 1, null));
		AbsExpression conditionExpr = _logic.generateExpression(newContext, null);
		AbsExpression stepExpr = _logic.generateExpression(newContext, null);

		// first: loopVarDecl (added to the previous scope), second: stopping cond and step
		// loopVar will be added to the prev scope
		List<AbsStatement> stmts = getLoopCounterStmts(newContext);

		VarDecleration loopCounterDecl = (VarDecleration) stmts.get(0);
		If stopAndStepCond = (If) stmts.get(1);

		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);
		stmtsBlock.addStatementAtIndex(0, stopAndStepCond); // inject stopping condition + counter step

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

		// TODO: major bug! when using the same symbol table as the parent
		// the variable deceleration is known at the outer scope!!
		// WTF - how do we gonna solve it??????
		// this is relevant for the FOR loop also.
		
		// create new loop context that has the same symbol table as his parent
		Context newContext = new Context(context, true, null, false);

		// create one varDecl (with new identifier) wihtout init value
		VarDecleration varDecl = createVarDecleration(newContext, new VarDeclerationParams(true, 1, false));

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

		// generate operation - statement block
		StatementsBlock stmtBlock = createStatementsBlock(context, null);

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
		
		// Randomize number of parameters
		int ParamNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.FUNC_PARAMS_NUM_LAMBDA_EXP));
	
		// Send number of parameters back to caller
		if (params != null)
			((funcDefParams)params).setParamNumber(ParamNum);
				
		// create the context defined by the function (force no loop)
		Context newContext = new Context(context, false, true, true);
		
		// Generate parameters
		List<Identifier> formals = getFunctionParametersList(newContext, ParamNum);
		
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);

		FunctionExp funcExp = new FunctionExp(formals, stmtsBlock);
		
		traceOut();
		return funcExp;
	}
	
	FunctionDef createFunctionDef(Context context, createParams params)
	{
		traceIn("FunctionDef");

		// Randomize function name
		Identifier functionId = _factoryJST.getFuncIdentifier(_counterFunc.getNext());
		trace(String.format("Identifier (%s)", functionId));

		// Randomize number of parameters
		int paramsNum = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.FUNC_PARAMS_NUM_LAMBDA_EXP));
		
		// Create the context defined by the function (force no loop)
		Context newContext = new Context(context, false, true, true);
				
		List<Identifier> funcParams = getFunctionParametersList(newContext, paramsNum);

		StatementsBlock stmtsBlock = createStatementsBlock(newContext, new StatementBlockParams("Inside function: " + functionId));

		FunctionDef funcDef = new FunctionDef(functionId, funcParams, stmtsBlock);

		// Add function to current scope
		// (!) This is performed after FunctionDef() to avoid recursion (and inner defined function calling father function)
		context.getSymTable().newEntry(new SymEntryFunc(functionId, paramsNum));
		
		traceOut();
		return funcDef;
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

		AbsExpression exp = createInitExp ? _logic.generateExpression(context, null) : null;
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
		
		// add output string
		String outString = StatementBlockParams.getOutString(params);
		if (outString != null)
		{
			stmtBlock.addStatement(newOutputStatement(outString));
		}

		// decide how many statements will be generate
		int size = 0;

		if (_logic.getDepth() < _configs.valInt(ConfigProperties.MAX_JST_DEPTH))
		{
			// choose the block size
			double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _logic.getDepth());
			double lambda = _configs.valDouble(ConfigProperties.STMTS_BLOCK_SIZE_LAMBDA) / factorDepth;
			size = StdRandom.expCeiled(lambda);
		}

		// generate statements
		stmtBlock.addStatement(_logic.generateStatement(context, null, size));

		traceOut();
		return stmtBlock;
	}

	/**
	 * This function draws an identifier to be assigned. Currently we force the
	 * identifier to be defined in higher scopes (or current scope),
	 * 
	 * TODO: think if this is not too restrictive
	 */
	Assignment createAssignment(Context context, createParams params)
	{
		traceIn("Assignment");
		Assignment assignment;

		// force an existing variable (1.0) to be assigned to
		Identifier id = createIdentifier(context, new IdentifierParams(1.0)); // TODO: change to assignable instead of var
		AbsExpression expr = _logic.generateExpression(context, null);

		assignment = new Assignment(id, expr);

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
		trace("Identifier (JSCall)");

		SymTable symbols = context.getSymTable();
		
		// Get existing functions list
		List<SymEntry> functions = symbols.getAvaiableEntries(SymEntryType.FUNC);
		
		//get parameters ready
		AbsExpression func;
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
			func = funcEntry.getIdentifier();
		}
		else
		{
			// Create the anonymous function
			funcDefParams newParams = new funcDefParams();
			func = createFunctionExp(context, newParams);		
			
			// Get number of parameters
			paramNum = newParams.getParamNumber();
		}

		// create list of parameter
		List<AbsExpression> callParams = _logic.generateExpression(context, null, paramNum);
		
		// add the function name (or its implementation in case of anonymous function) 
		// as the first parameter to the proxy call function
		callParams.add(0, func);
		
		// create a proxy call to function
		Call wrappedCall = new Call(_factoryJST.getFuncIdentifier("JSCall"), callParams);
			
		traceOut();
		return wrappedCall;
	}

	MemberExp createMemberExp(Context context, createParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	ObjectExp createObjectExp(Context context, createParams params)
	{
		traceIn("ObjectExp");
		ObjectExp objExp = new ObjectExp();

		int size = StdRandom.expCeiled(_configs.valDouble(ConfigProperties.OBJECT_KEYS_LENGTH_LAMBDA_EXP));

		for (int i = 0; i < size; i++)
		{
			ObjectKeys key = (ObjectKeys) _logic.applyMethod(StdRandom.choseFromListUniform(_jstProp.getObjectKeys()), context, null);
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
			List<SymEntry> existingVars = context.getSymTable().getAvaiableEntries(SymEntryType.VAR);
			int totalVars = existingVars.size();

			// if no var is defined then throw an error.
			if (totalVars == 0)
				throw new RuntimeException("no var symbols found when thrying to get one");
			
			// select random var among the list
			id = (existingVars.get(StdRandom.uniform(totalVars))).getIdentifier();
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
		Operator operator = Operator.getRandomly(null); // TODO: use it more smart - send desired return type
		
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
			operandsList.add((AbsExpression) _logic.applyMethod(StdRandom.choseFromListUniform(_jstProp.getAssignable()), context, null));
		}
		else
		{
			operandsList = _logic.generateExpression(context, null, operator.getNumOperands());
		}

		expressionOp = new OperationExp(operator, operandsList);

		traceOut();
		return expressionOp;
	}
	
	Literal createLiteral(Context context, createParams params)
	{
		LiteralTypes litType = LiteralTypes.getRandomly();
		Literal lit = null;

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

		// Randomize string's length
		int length = Math.min(maxLength, StdRandom.expCeiled(_configs.valDouble(ConfigProperties.LITERAL_STRING_LAMBDA)));

		for (int i = 0; i < length; i++)
			strBld.append((char) StdRandom.uniform(128));

		litStr = new LiteralString(strBld.toString());

		trace("LiteralString");
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

	private Call newOutputStatement(String str)
	{
		return newOutputStatement(new LiteralString(str));
	}

	private Call newOutputStatement(AbsExpression expr)
	{
		Call call = new Call(_factoryJST.getFuncIdentifier("JSPrint"), expr);
		call.setNoneRandomBranch();
		return call;
	}
	
	// ===============================================================================

	/**
	 * handles the createWhile/createDoWhile, and returns the instance by the
	 * class param
	 */
	private AbsWhileLoop commonWhile(Class<? extends AbsWhileLoop> classWhile, Context context)
	{
		AbsWhileLoop whileLoop = null;

		// create new loop context that has the same symbol table as his parent
		Context newContext = new Context(context, true, null, false);

		// create loop condition
		AbsExpression conditionExp = _logic.generateExpression(newContext, null); 

		// first: loopVarDecl (added to the previous scope), second: stopping cond and step
		// loopVar will be added to the prev scope
		List<AbsStatement> stmts = getLoopCounterStmts(newContext);

		VarDecleration loopCounterDecl = (VarDecleration) stmts.get(0);
		If stopAndStepCond = (If) stmts.get(1);

		// create loop content
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);

		stmtsBlock.addStatementAtIndex(0, stopAndStepCond); // inject stopping condition + counter step

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
	
	private int getRandomLoopIterationsLimit()
	{
		int exp = _configs.valInt(ConfigProperties.LOOP_MAX_ITERATIONS_NORMAL_EXP);
		int stddev = _configs.valInt(ConfigProperties.LOOP_MAX_ITERATIONS_NORMAL_STDDEV);
		return (int) StdRandom.gaussian(exp, stddev);
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
	
	/***
	 * For each var x (including loopVars), add stmt: print("x = " + x); 
	 * @param program
	 */
	private void addPrintVarsStmts(Program program) 
	{
		List<Identifier> vars = _factoryJST.getAllVars();
		List<Identifier> loopVars = _factoryJST.getAllLoopVar();
		
		program.addStatement(new Comment("------------- Printing All The Program Variables -------------\n", true));
		
		for(Identifier id : vars)
			addPrintVarStmt(program, id);
		
		for(Identifier loopId: loopVars)
			addPrintVarStmt(program, loopId);
	}

	/***
	 * Adds the stmt - "print("id =  " + id);
	 * @param program
	 * @param id - to be printed
	 */
	private void addPrintVarStmt(Program program, Identifier id)
	{
		OperationExp plusExpr = new OperationExp(Operator.PLUS, new LiteralString(id.getName() + " = "), id);
		Call callToJSPrint = newOutputStatement(plusExpr);
		callToJSPrint.setNoneRandomBranch();
		
		program.addStatement(callToJSPrint);
	}
}
