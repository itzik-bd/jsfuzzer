package Generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import Utils.StdRandom;
import Utils.StringCounter;

public class Generator
{	
	private final JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private final Configs _configs;
	
	private final GenLogic _logic;
	
	private final Program _program = new Program(); // generated program instance
	private final Context _rootContext = new Context(); // global scope
	
	private final StringCounter _counterVar = new StringCounter("var%d");
	private final StringCounter _counterFunc = new StringCounter("func%d");
	private final StringCounter _counterLoopVar = new StringCounter("loop_var%d");
	
	private final boolean _verbose;
	
	public static Program generate(Configs configs, String seed, boolean verbose)
	{
		Generator gen = new Generator(configs, verbose);
		
		if (seed != null) {
			StdRandom.setSeed(Long.parseLong(seed)); // development: use seed: 1436423645
		}
		
		// generate program
		Program prog = gen.createProgram();
		
		return prog;
	}
	
	public Generator(Configs configs, boolean verbose)
	{
		_configs = configs;
		_logic = new GenLogic(this, configs);
		_verbose = verbose;
	}
	
	// ===============================================================================
	
	private void traceIn(String str)
	{	
		if (_verbose)
		{
			StringBuffer s = new StringBuffer();
			
			s.append(String.format("%5d", _logic.getDepth()));
			for (int i=0; i<_logic.getDepth() ; i++)
				s.append(" ");
			s.append(str);
			System.out.println(s);
		}
		
		_logic.increaseDepth();
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
	
	// ===============================================================================	
	
	private Program createProgram()
	{
		traceIn("Program");
		
		_program.addStatement(generateHeader());
		
		// choose how many statements the program will have
		// actually, the actual number may be a bit greater (statements may added during runtime)
		double lambda = _configs.valDouble(ConfigProperties.PROGRAM_SIZE_LAMBDA);
		int size = StdRandom.expCeiled(lambda);
		
		// generate statements
		_program.addStatement(_logic.generateStatement(_rootContext, null, size));
		
		// attach configuration used to generate program
		_program.addStatement(new Comment(_configs.toString()));
		
		traceOut();
		return _program;
	}

	If createIf(Context context, createParams params)
	{
		traceIn("If");
		
		If ifStmt;
		AbsExpression conditionExp = _logic.generateExpression(context, null);
		StatementsBlock trueOp = createStatementsBlock(context, null);
		
		// decide whether to have an "else" operation
		if (StdRandom.bernoulli())
		{
			StatementsBlock falseOp = createStatementsBlock(context, null);
			ifStmt = new If(conditionExp, trueOp, falseOp);
		}
		
		ifStmt = new If(conditionExp, trueOp);
		
		traceOut();
		return ifStmt;
	}

	While createWhile(Context context, createParams params)
	{
		return (While) commonWhile (While.class, context);		
	}

	DoWhile createDoWhile(Context context, createParams params)
	{
		return (DoWhile) commonWhile (DoWhile.class, context);
	}

	/** handles the createWhile/createDoWhile, and returns the instance by the class param */
	private AbsWhileLoop commonWhile(Class<? extends AbsWhileLoop> classWhile, Context context)
	{
		traceIn(classWhile.getName());
		AbsWhileLoop whileLoop = null;
				
		//the new context defined by the loop
		Context newContext = new Context(context, true, null);
		
		AbsExpression conditionExp = _logic.generateExpression(newContext, null); //the while condition
		
		//first: loopVarDecl (added to the previous scope), second: stopping cond and step
		//loopVar will be added to the prev scope
		List<AbsStatement> stmts = getLoopCounterStmts(newContext); 
		
		VarDecleration loopCounterDecl = (VarDecleration) stmts.get(0);
		If stopAndStepCond = (If) stmts.get(1);
		
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);
		
		stmtsBlock.addStatementAtIndex(0, stopAndStepCond); //inject stopping condition + counter step

		try {
			//create new while/doWhile instance 
			whileLoop = classWhile.getConstructor(AbsExpression.class, StatementsBlock.class ,VarDecleration.class)
					.newInstance(conditionExp, stmtsBlock, loopCounterDecl);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		traceOut();
		return whileLoop;
	}

	For createFor(Context context, createParams params)
	{
		traceIn("For");
		
		Context newContext = new Context(context, true, null);
		
		// TODO: currently first for stmt is always VarDecleration. may want to change it (to add only assignment)
		AbsStatement initStmt = createVarDecleration(newContext, null);
		AbsExpression conditionExpr = _logic.generateExpression(newContext, null);
		AbsExpression stepExpr = _logic.generateExpression(newContext, null);

		//first: loopVarDecl (added to the previous scope), second: stopping cond and step
		//loopVar will be added to the prev scope
		List<AbsStatement> stmts = getLoopCounterStmts(newContext); 
		
		VarDecleration loopCounterDecl = (VarDecleration) stmts.get(0);
		If stopAndStepCond = (If) stmts.get(1);
		
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);
		stmtsBlock.addStatementAtIndex(0, stopAndStepCond); //inject stopping condition + counter step
		
		//create for loop: for(random var decl; random expr; random expr)
		For forLoop = new For(initStmt, conditionExpr, stepExpr, stmtsBlock, loopCounterDecl);
		
		traceOut();
		return forLoop;
	}
	
	/** returns two new stmts, loopCounter init, stopping condition and step (in one stmt).
	 *  IMPORTANT: param context is the context the loop defines */
	private List<AbsStatement> getLoopCounterStmts(Context context)
	{
		//explicitly create a var decleration: var new_loop_var = 0;
		String newName = _counterLoopVar.getNext();
		Identifier loopCounter = _factoryJST.getLoopIdentifier(newName);
		VarDecleration loopCounterDecl = new VarDecleration();
		loopCounterDecl.addDeclerator(new VarDeclerator(loopCounter, new LiteralNumber("0")));
		
		//the var will be declared in outside scope
		context.getParent().getSymTable().newEntry(new SymEntryVar(loopCounter));
		
		//explicitly create binaryOp: new_var++ < max_iterations 
		int loopIterationsLimit = getRandomLoopIterationsLimit();
		LiteralNumber loopIterationsLiteral = new LiteralNumber(new Integer(loopIterationsLimit).toString());
		OperationExp loopStopCond = new OperationExp(Operator.LT, new OperationExp(Operator.PLUSPLUSRIGHT, loopCounter), loopIterationsLiteral); 
		
		//explicitly create If: if(new_var++ < max_iterations) break;
		StatementsBlock stmtsBlock = new StatementsBlock();
		stmtsBlock.addStatement((AbsStatement)_factoryJST.getConstantNode("break"));
		If ifStoppingCondition = new If(new OperationExp(Operator.LNEG, loopStopCond), stmtsBlock);	
				
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

	ForEach createForEach(Context context, createParams params) 
	{
		traceIn("ForEach");
		ForEach forEachStmt;
		
		Identifier id = createIdentifier(context, new IdentifierParams(1)); // TODO: Temporary solution (could also be a var decleration)
		
		// Temporary solution (could also be any existing iteratable var)
		ArrayExp arr = createArrayExpression(context, null);
		
		// Generate StatementsBlock
		StatementsBlock stmtsBlock = createStatementsBlock(context, null);
		
		forEachStmt = new ForEach(id, arr, stmtsBlock);
		
		traceOut();
		return forEachStmt;
	}

	Switch createSwitch(Context context, createParams params)
	{
		traceIn("Switch");
		Switch switchStmt;
		
		// Make Identifier more probable for this generateExpression()
		int dentifierProb = _configs.valInt(ConfigProperties.IN_SWITCH_IDENTIGIER_PROB);
		Map<String, Integer> specialProbs = new HashMap<String, Integer>();
		specialProbs.put("Identifier", dentifierProb);
		
		AbsExpression expr = _logic.generateExpression(context, null);
		switchStmt = new Switch(expr);
		
		int exp = _configs.valInt(ConfigProperties.CASES_BLOCKS_NUM_NORMAL_EXP);
		int stddev = _configs.valInt(ConfigProperties.CASES_BLOCKS_NUM_NORMAL_STDDEV);
		int caseBlocksNum = (int) StdRandom.gaussian(exp, stddev);

		double defaultProb = _configs.valDouble(ConfigProperties.CASE_BLOCK_INCLUDE_DEFAULT_BERNOULLY_P);
		boolean defaultWasIncluded = false;
		
		CaseBlockParams caseParam = new CaseBlockParams();
		
		for(int i = 0; i < caseBlocksNum; i++)
		{
			//generate default case only once
			if(!defaultWasIncluded)
			{
				if(StdRandom.bernoulli(defaultProb))
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
		
		//TODO: do we need to start a new context for switch? for case?
		
		traceOut();
		return switchStmt;
	}

	CaseBlock createCaseBlock(Context context, createParams params)
	{
		traceIn("CaseBlock");
		CaseBlock caseBlock;
		
		/***** generate cases *****/
		int exp = _configs.valInt(ConfigProperties.CASE_NUM_NORMAL_EXP);
		int stddev = _configs.valInt(ConfigProperties.CASES_NUM_NORMAL_STDDEV);
		int casesNum = (int) StdRandom.gaussian(exp, stddev);
		
		//TODO : maybe 0 cases
		//in case we got zero or less cases
		casesNum = (casesNum > 0) ? casesNum : 1; 
				
		List<Caseable> cases = new LinkedList<Caseable>();
		for(int i = 0; i < casesNum; i++)
			cases.add(createCase(context, null));

		if(CaseBlockParams.getIncludeDefault(params))
			cases.add((Default)_factoryJST.getConstantNode("default"));
		
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

	FunctionDef createFunctionDefinition(Context context, createParams params) 
	{
		traceIn("FunctionDefinition");
		
		// Randomize function name
		String funcName = _counterFunc.getNext();
		Identifier functionId = _factoryJST.getFuncIdentifier(funcName);
		
		// Randomize number of parameters 
		double lambda = _configs.valDouble(ConfigProperties.FUNC_PARAMS_NUM_LAMBDA_EXP);
		int paramsNum = StdRandom.expCeiled(lambda);
		
		// add function to current scope
		context.getSymTable().newEntry(new SymEntryFunc(functionId, paramsNum));
		
		// create the context defined by the function
		Context newContext = new Context(context, null, true);

		// param for create identifier
		IdentifierParams idParams = new IdentifierParams(_configs.valDouble(ConfigProperties.FUNC_PARAM_USE_EXISTING_VAR_BERNOULLY_P));

		List<Identifier> funcParams = new LinkedList<Identifier>();
		for(int i = 0; i < paramsNum; i++)
		{
			Identifier id;
			//keep generate id's until 
			do {
				id = createIdentifier(newContext, idParams);
			} while (funcParams.contains(id));
			
			funcParams.add(id);
		}
				
		StatementsBlock stmtsBlock = createStatementsBlock(newContext, null);
		
		FunctionDef funcDef = new FunctionDef(functionId, funcParams, stmtsBlock);
		
		traceOut();
		return funcDef;
	}

	VarDecleration createVarDecleration(Context context, createParams params)
	{
		traceIn("VarDecleration");
		VarDecleration varDecleration;
		
		double lambda = _configs.valDouble(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP);
		int decleratorsNum = StdRandom.expCeiled(lambda);
		varDecleration = new VarDecleration();
		
		for (int i=0; i<decleratorsNum ; i++)
			varDecleration.addDeclerator(createVarDeclerator(context, null));
		
		traceOut();
		return varDecleration;
	}

	VarDeclerator createVarDeclerator(Context context, createParams params)
	{
		traceIn("VarDeclerator");
		VarDeclerator varDeclerator;
		
		Identifier id;
		
		// decide on probability if define new var
		IdentifierParams idParams;
				
		if (VarDeclerationParams.getForceNewIdentifier(params))
			idParams = new IdentifierParams(0);
		else
			idParams = new IdentifierParams(_configs.valDouble(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP));
		
		// check if the identifier is defined in the current scope
		do
		{
			id = createIdentifier(context, idParams);	
		} while (context.getSymTable().contains(id));
		
		// add identifier to current scope
		context.getSymTable().newEntry(new SymEntryVar(id));
		
		// generate expression to be assigned to the var
		AbsExpression exp = _logic.generateExpression(context, null);
		
		varDeclerator = new VarDeclerator(id, exp);
		
		traceOut();
		return varDeclerator;
	}

	Continue createContinue(Context context, createParams params)
	{
		traceIn("Continue");
		Continue continueStmt = (Continue) _factoryJST.getConstantNode("continue");
		
		traceOut();
		return continueStmt;
	}
	
	Break createBreak(Context context, createParams params)
	{
		traceIn("Break");
		Break breakStmt = ((Break) _factoryJST.getConstantNode("break"));
		
		traceOut();
		return breakStmt;
	}

	Return createReturn(Context context, createParams params)
	{
		traceIn("Return");
		Return returnStmt;
		
		double returnValueProb = _configs.valDouble(ConfigProperties.RETURN_VALUE_BERNOULLY_P);
		
		//decide whether to return a value
		if(StdRandom.bernoulli(returnValueProb))
			returnStmt = new Return(_logic.generateExpression(context, null));
		else
			returnStmt = new Return();
		
		traceOut();
		return returnStmt;
	}

	/**IMPORTANT: context is the context defined by this StatementBlock */
	StatementsBlock createStatementsBlock(Context context, createParams params)
	{
		traceIn("StatementsBlock");
		StatementsBlock stmtBlock = new StatementsBlock();
		
		// decide how many statements will be generate
		int size = 0;
		
		if (_logic.getDepth() < _configs.valInt(ConfigProperties.MAX_JST_DEPTH))
		{
			//choose the block size
			double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _logic.getDepth());
			double lambda = _configs.valDouble(ConfigProperties.STMTS_BLOCK_SIZE_LAMBDA) / factorDepth;
			size = StdRandom.expCeiled(lambda);
		}
		
		// generate statements
		stmtBlock.addStatement(_logic.generateStatement(context, null, size));

		traceOut();
		return stmtBlock;
	}

	/*
	 * This function draws an identifier to be assigned.
	 * Currently we are allowing the identifier not to be defined in higher scopes,
	 * which in such case it will be defined in the root scope.
	 * 
	 * TODO: think if this behavior is sound for future analysis. 
	 */
	Assignment createAssignment(Context context, createParams params) 
	{
		traceIn("Assignment");
		Assignment assignment;
		
		double useExistingVarProb = _configs.valDouble(ConfigProperties.ASSIGNMENT_USE_EXISTING_VAR_BERNOULLY_P);
		
		Identifier id = createIdentifier(context, new IdentifierParams(useExistingVarProb));
		AbsExpression expr = _logic.generateExpression(context, null);
		
		// make sure identifier is defined (if not add it to top level scope)
		SymEntry entry = context.getSymTable().lookup(id);
		if (entry == null)
			_rootContext.getSymTable().newEntry(new SymEntryVar(id));
		
		assignment = new Assignment(id, expr);
		
		traceOut();
		return assignment;
	}

	CompoundAssignment createCompoundAssignment(Context context, createParams params)
	{
		traceIn("CompoundAssignment");
		CompoundAssignment compAsssignment;
		
		Identifier var = createIdentifier(context, new IdentifierParams(1)); // use only existing variables
		AbsExpression expr = _logic.generateExpression(context, null);
		CompoundOps op = CompoundOps.getRandomly();
		
		compAsssignment = new CompoundAssignment(var, op, expr);
		
		traceOut();
		return compAsssignment;
	}

	Call createCall(Context context, createParams params) 
	{
		traceIn("Call");
		Call call;
		
		SymTable symbols = context.getSymTable();
		
		List<SymEntry> functions = symbols.getAvaiableEntries(SymEntryType.FUNC);
		int funcIndex = StdRandom.uniform(functions.size());
		SymEntryFunc funcEntry = (SymEntryFunc) functions.get(funcIndex);
		
		// create list of parameter
		int paramNum = funcEntry.getParamsNumber();
		// TODO mabye make identifier more likely (like in switch)
		List<AbsExpression> callParams = new ArrayList<AbsExpression>(_logic.generateExpression(context, null, paramNum));
		
		// create the call node
		call = new Call(funcEntry.getIdentifier(), callParams);
		
		traceOut();
		return call;
	}

	FunctionExp createFunctionExpression(Context context, createParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	MemberExp createMemberExpression(Context context, createParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	ObjectExp createObjectExpression(Context context, createParams params)
	{
		traceIn("ObjectExpression");
		ObjectExp objExp = new ObjectExp();
		
		double lambda = _configs.valDouble(ConfigProperties.OBJECT_KEYS_LENGTH_LAMBDA_EXP);
		int size = StdRandom.expCeiled(lambda);
		
		for (int i=0 ; i<size ; i++) {
			ObjectKeys key = createIdentifier(context, null); // TODO: generate all object keys (strings also)
			AbsExpression val = _logic.generateExpression(context, null);
			
			objExp.addToMap(key, val);
		}		
		
		traceOut();
		return objExp;
	}
	
	ArrayExp createArrayExpression(Context context, createParams params) 
	{
		traceIn("ArrayExpression");
		ArrayExp arrayExp;
		
		double lambda = _configs.valDouble(ConfigProperties.ARRAY_LENGTH_LAMBDA_EXP);
		int size = (int)Math.ceil(StdRandom.exp(lambda));
		
		arrayExp = new ArrayExp(_logic.generateExpression(context, null, size));
		
		traceOut();
		return arrayExp;
	}
	
	Identifier createIdentifier(Context context, createParams params) 
	{
		traceIn("Identifier");
		Identifier id;

		//decide whether to use an existing variable
		if(StdRandom.bernoulli(IdentifierParams.getUseExistingVarProb(params)))
		{
			List<SymEntry> existingVars = context.getSymTable().getAvaiableEntries(SymEntryType.VAR);
			int totalVars = existingVars.size();
			
			// if no var is defined then - create new var decleration at root scope!
			if (totalVars == 0)
			{
				_program.addStatement(createVarDecleration(_rootContext, new VarDeclerationParams(true)));
				
				// re-get available vars
				existingVars = context.getSymTable().getAvaiableEntries(SymEntryType.VAR);
				totalVars = existingVars.size();
			}
			
			id = (existingVars.get(StdRandom.uniform(totalVars))).getIdentifier();
		}
		else
		{
			String newName = _counterVar.getNext();
			id = _factoryJST.getIdentifier(newName);
		}
		
		traceOut();
		return id;
	}
	
	This createThis(Context context, createParams params)
	{
		traceIn("This");
		This thisExp = ((This) _factoryJST.getConstantNode("this"));
		
		traceOut();
		return thisExp;
	}

	OperationExp createExpressionOp(Context context, createParams params) 
	{
		traceIn("ExpressionOp");
		OperationExp expressionOp;
		
		Operator operator = Operator.getRandomly(null); // TODO: use it more smart - send desired return type
		
		// generate operands array
		List<AbsExpression> operandsArray = _logic.generateExpression(context, null, operator.getNumOperands()); 
	
		expressionOp = new OperationExp(operator, operandsArray);
		
		traceOut();
		return expressionOp;
	}

	Literal createLiteral(Context context, createParams params) 
	{
		traceIn("Literal");
		Literal lit = null;
		
		LiteralTypes litType = LiteralTypes.getRandomly();
		
		if (litType.isSingleValue())
			lit = (new Literal(litType));
		else if(litType.equals(LiteralTypes.NUMBER))
			lit = (createLiteralNumber(context, null));
		else if(litType.equals(LiteralTypes.STRING))
			lit = (createLiteralString(context, null));
		
		traceOut();
		return lit;
	}

	LiteralString createLiteralString(Context context, createParams params) 
	{
		traceIn("LiteralString");
		LiteralString litStr;
		
		double lambda = _configs.valDouble(ConfigProperties.LITERAL_STRING_LAMBDA);
		int maxLength = _configs.valInt(ConfigProperties.LITERAL_STRING_MAX_LENGTH);
		StringBuilder strBld = new StringBuilder();
		
		// Randomize string's length
		int length = (int) Math.round(StdRandom.exp(lambda));
		if (length > maxLength)
			length = maxLength;
		
		for (int i=0 ; i < length ; i++)
		{
			strBld.append((char) StdRandom.uniform(128));
		}
		
		litStr = new LiteralString(strBld.toString());
		
		traceOut();
		return litStr;
	}

	LiteralNumber createLiteralNumber(Context  context, createParams params) 
	{
		traceIn("LiteralNumber");
		LiteralNumber litNum;
		
		// return infinity?
		double infinity = _configs.valDouble(ConfigProperties.LITERAL_NUMBER_MAX_PROBABILITY);
		if (StdRandom.bernoulli(infinity))
		{
			litNum = (new LiteralNumber("9007199254740992"));
		}
		else
		{
			double lambda = _configs.valDouble(ConfigProperties.LITERAL_NUMBER_LAMBDA);
			StringBuilder strBld = new StringBuilder();
			
			// Randomize number's length
			int length = (int) Math.round(StdRandom.exp(lambda));
			for (int i=0 ; i < length ; i++)
			{
				strBld.append(StdRandom.uniform(10));
			}
			
			litNum = new LiteralNumber(strBld.toString());
		}
		
		traceOut();
		return litNum;
	}

}
