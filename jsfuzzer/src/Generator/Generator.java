package Generator;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Generator.Config.*;
import Generator.SymTable.*;
import JST.*;
import JST.Enums.*;
import JST.Interfaces.Caseable;
import JST.Interfaces.ProgramUnit;
import JST.Interfaces.Visitor;
import Utils.StdRandom;
import Utils.StringCounter;

@SuppressWarnings("unused")
public class Generator
{	
	private final JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private final Configs _configs;
	
	private final Program _program = new Program(); // generated program instance
	private final Context _rootContext = new Context(); // global scope
	
	private final StringCounter _counterVar = new StringCounter("var%d");
	private final StringCounter _counterFunc = new StringCounter("func%d");
	private final StringCounter _counterLoopVar = new StringCounter("loop_var%d");
	
	private final boolean _verbose;
	private int _depth = 0;
	
	public static Program generate(Configs configs, boolean verbose)
	{
		Generator gen = new Generator(configs, verbose);
		
		StdRandom.setSeed(1436423645); // TODO: remove set seed after development
		
		// generate program
		Program prog = gen.createProgram();
		
		return prog;
	}
	
	public Generator(Configs configs, boolean verbose)
	{
		_configs = configs;
		_verbose = verbose;
	}
	
	// ===============================================================================
	
	private void traceIn(String str)
	{	
		if (_verbose)
		{
			StringBuffer s = new StringBuffer();
			
			s.append(String.format("%5d", _depth));
			for (int i=0; i<_depth ; i++)
				s.append(" ");
			s.append(str);
			System.out.println(s);
		}
		
		_depth++;
	}
	
	private void traceOut()
	{
		_depth--;
	}
	
	private JSTNode applyMethod(String methodName, Context context)
	{
		JSTNode node;

		switch (methodName)
		{
		case "ForEach": node = createForEach(context); break;
		case "Switch": node = createSwitch(context); break;
		case "For": node = createFor(context); break;
		case "If": node = createIf(context); break;
		case "DoWhile": node = createDoWhile(context); break;
		case "Case": node = createCase(context); break;
		case "While": node = createWhile(context); break;
		case "Break": node = createBreak(context); break;
		case "Return": node = createReturn(context); break;
		case "Call": node = createCall(context); break;
		case "This": node = createThis(context); break;
		case "Literal": node = createLiteral(context); break;
		case "CaseBlock": node = createCaseBlock(context); break;
		case "FunctionDefinition": node = createFunctionDefinition(context); break;
		case "Continue": node = createContinue(context); break;
		case "ArrayExpression": node = createArrayExpression(context); break;
		case "Identifier": node = createIdentifier(context); break;
		case "VarDeclerator": node = createVarDeclerator(context); break;
		case "Assignment": node = createAssignment(context); break;
		case "StatementsBlock": node = createStatementsBlock(context); break;
		case "FunctionExpression": node = createFunctionExpression(context); break;
		case "MemberExpression": node = createMemberExpression(context); break;
		case "CompoundAssignment": node = createCompoundAssignment(context); break;
		case "ObjectExpression": node = createObjectExpression(context); break;
		case "VarDecleration":node = createVarDecleration(context); break;
		case "LiteralNumber": node = createLiteralNumber(context); break;
		case "ExpressionOp": node = createExpressionOp(context); break;
		case "LiteralString": node = createLiteralString(context); break;
		
		case "Expression": node = generateExpression(context); break;
		
		default: throw new IllegalArgumentException("JSTnode '"+methodName+"'was not found");
		}
		
		return node;
	}
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	private AbsStatement generateStatement(Context context)
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
	private AbsExpression generateExpression(Context context)
	{
		HashMap<String, Double> hs = new HashMap<String, Double>();
		
		double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _depth);
		
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

		// randomly choose expression
		String createMethod = StdRandom.choseFromProbList(hs);
		
		return (AbsExpression) applyMethod(createMethod, context);
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
		int size = (int) Math.ceil(StdRandom.exp(lambda));
		
		// generate statements
		for (int i=0 ; i<size ; i++)
			_program.addStatement(generateStatement(_rootContext));
		
		// attach configuration used to generate program
		_program.addStatement(new Comment(_configs.toString()));
		
		traceOut();
		return _program;
	}

	private If createIf(Context context)
	{
		traceIn("If");
		
		If ifStmt;
		AbsExpression conditionExp = generateExpression(context);
		StatementsBlock trueOp = createStatementsBlock(context);
		
		// decide whether to have an "else" operation
		if (StdRandom.bernoulli())
		{
			StatementsBlock falseOp = createStatementsBlock(context);
			ifStmt = new If(conditionExp, trueOp, falseOp);
		}
		
		ifStmt = new If(conditionExp, trueOp);
		
		traceOut();
		return ifStmt;
	}

	private While createWhile(Context context)
	{
		traceIn("While");
		While whileStmt;
		
		AbsExpression conditionExp = generateExpression(context);
		
		//in while loops, the context they define is the same as the stmtblock
		List<AbsStatement> stmts = getLoopCounterStmts(context, null, true);

		StatementsBlock op = createStatementsBlock(context);
		
		op.addStatementAtIndex(0, stmts.get(2)); //inject counter step
		op.addStatementAtIndex(1, stmts.get(1)); //inject stopping condition (if.. break) 

		whileStmt = new While(conditionExp, op);
		
		whileStmt.setLoopCounterInit((VarDecleration)stmts.get(0)); //set the loop counter init
		
		traceOut();
		return whileStmt;
	}

	private DoWhile createDoWhile(Context context)
	{
		traceIn("DoWhile");
		DoWhile doWhileStmt;
		
		AbsExpression conditionExp = generateExpression(context);

		//in while loops, the context they define is the same as the stmtblock
		List<AbsStatement> stmts = getLoopCounterStmts(context, null, true);
		
		StatementsBlock op = createStatementsBlock(context);
		
		op.addStatementAtIndex(0, stmts.get(2)); //inject counter step
		op.addStatementAtIndex(1, stmts.get(1)); //inject stopping condition (if.. break) 

		doWhileStmt = new DoWhile(conditionExp, op);
		
		doWhileStmt.setLoopCounterInit((VarDecleration)stmts.get(0)); //set the loop counter init
		
		traceOut();
		return doWhileStmt;
	}

	private For createFor(Context context)
	{
		traceIn("For");
		
		Context newContext = new Context(context, true, null);

		List<AbsStatement> forStmts = getLoopCounterStmts(context, newContext, false);

		StatementsBlock stmtsBlock = createStatementsBlock(newContext);
		
		//create for loop: for(var new_var=0; new_var<random_number; new_var++)
		For forLoop = new For(forStmts.get(0), (AbsExpression)forStmts.get(1), (AbsExpression)forStmts.get(2), stmtsBlock);
		
		traceOut();
		return forLoop;
	}
	
	/** returns three new stmts, loopCounter init, stopping condition, and step.
	 *  if isWhileLoop, the stopping condition will be "if (condition) break;" */
	private List<AbsStatement> getLoopCounterStmts(Context context, Context loopContext, boolean isWhileLoop)
	{
		//explicitly create a var decleration: var new_loop_var = 0;
		String newName = _counterLoopVar.getNext();
		Identifier loopCounter = _factoryJST.getLoopIdentifier(newName);
		VarDecleration loopCounterDecl = new VarDecleration();
		loopCounterDecl.addDeclerator(new VarDeclerator(loopCounter, new LiteralNumber("0")));
		
		//in while, the var will be declared in outside scope, else - inside loop (for) scope
		if(isWhileLoop)
			context.getSymTable().newEntry(loopCounter, SymEntryType.VAR);
		else
			loopContext.getSymTable().newEntry(loopCounter, SymEntryType.VAR);
		
		//explicitly create binaryOp: new_var < max_iterations 
		int loopIterationsLimit = getRandomLoopIterationsLimit();
		LiteralNumber loopIterationsLiteral = new LiteralNumber(new Integer(loopIterationsLimit).toString());
		OperationExp loopStopCond = new OperationExp(Operator.LT, loopCounter, loopIterationsLiteral); 
		
		//explicitly create If: if(new_var < max_iterations) break;
		StatementsBlock stmtsBlock = new StatementsBlock();
		stmtsBlock.addStatement((AbsStatement)_factoryJST.getConstantNode("break"));
		If ifStoppingCondition = new If(new OperationExp(Operator.LNEG, loopStopCond), stmtsBlock);	
		
		//explicitly create UnaryOp: new_var++
		OperationExp loopStepExpr = new OperationExp(Operator.PLUSPLUSRIGHT, loopCounter);
		
		List<AbsStatement> stmts = new LinkedList<AbsStatement>();
		stmts.add(loopCounterDecl);
		
		if(isWhileLoop)
			stmts.add(ifStoppingCondition);
		else
			stmts.add(loopStopCond);
		
		stmts.add(loopStepExpr);
		
		return stmts;
	}
	
	private int getRandomLoopIterationsLimit()
	{
		int exp = _configs.valInt(ConfigProperties.LOOP_MAX_ITERATIONS_NORMAL_EXP);
		int stddev = _configs.valInt(ConfigProperties.LOOP_MAX_ITERATIONS_NORMAL_STDDEV);
		return (int) StdRandom.gaussian(exp, stddev);
	}

	private ForEach createForEach(Context context) 
	{
		traceIn("ForEach");
		ForEach forEachStmt;
		
		// Temporary solution (could also be a var decleration)
		context.identifierUseExistingVarProb = 1;
		Identifier id = createIdentifier(context);
		
		// Temporary solution (could also be any existing iteratable var)
		ArrayExp arr = createArrayExpression(context);
		
		// Generate StatementsBlock
		StatementsBlock stmtsBlock = createStatementsBlock(context);
		
		forEachStmt = new ForEach(id, arr, stmtsBlock);
		
		traceOut();
		return forEachStmt;
	}

	private Switch createSwitch(Context context)
	{
		traceIn("Switch");
		Switch switchStmt;
		
		//TODO: we may want expr to be an identifier with high prob.
		AbsExpression expr = generateExpression(context);
		switchStmt = new Switch(expr);
		
		int exp = _configs.valInt(ConfigProperties.CASES_BLOCKS_NUM_NORMAL_EXP);
		int stddev = _configs.valInt(ConfigProperties.CASES_BLOCKS_NUM_NORMAL_STDDEV);
		int caseBlocksNum = (int) StdRandom.gaussian(exp, stddev);

		double defaultProb = _configs.valDouble(ConfigProperties.CASE_BLOCK_INCLUDE_DEFAULT_BERNOULLY_P);
		boolean includeDefault = false;
		
		for(int i = 0; i < caseBlocksNum; i++)
		{
			//generate default case only once
			if(!includeDefault)
			{
				if(StdRandom.bernoulli(defaultProb))
				{
					context.caseBlockIncludeDefault = true;
					switchStmt.addCaseOp(createCaseBlock(context));
				}
			}
			
			context.caseBlockIncludeDefault = false;
			switchStmt.addCaseOp(createCaseBlock(context));			
		}
		
		//TODO: do we need to start a new context for switch? for case?
		
		traceOut();
		return switchStmt;
	}

	private CaseBlock createCaseBlock(Context context)
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
			cases.add(createCase(context));

		if(context.caseBlockIncludeDefault)
			cases.add((Default)_factoryJST.getConstantNode("default"));
		
		// generate operation - statement block
		StatementsBlock stmtBlock = createStatementsBlock(context);
		
		caseBlock = new CaseBlock(cases, stmtBlock);
		
		traceOut();
		return caseBlock;
	}
	
	private Case createCase(Context context)
	{
		traceIn("Case");
		Case caseObj = new Case(generateExpression(context));
		
		traceOut();
		return caseObj;
	}

	private FunctionDef createFunctionDefinition(Context context) 
	{
		// TODO Auto-generated method stub
		String funcName = _counterFunc.getNext();
		Identifier functionId = _factoryJST.getFuncIdentifier(funcName);
		context.getSymTable().newEntry(functionId, SymEntryType.FUNC);
		
		double lambda = _configs.valDouble(ConfigProperties.FUNC_PARAMS_NUM_LAMBDA_EXP);
		int paramsNum = (int) Math.ceil(StdRandom.exp(lambda));
		
		//the context defined by the function
		Context newContext = new Context(context, null, true);
		
		double prevVal = newContext.identifierUseExistingVarProb;
		newContext.identifierUseExistingVarProb =  _configs.valDouble(ConfigProperties.FUNC_PARAM_USE_EXISTING_VAR_BERNOULLY_P);

		List<Identifier> params = new LinkedList<Identifier>();
		for(int i = 0; i < paramsNum; i++)
			params.add(createIdentifier(newContext));
		
		newContext.identifierUseExistingVarProb = prevVal;
		
		StatementsBlock stmtsBlock = createStatementsBlock(newContext);
		
		return new FunctionDef(functionId, params, stmtsBlock, paramsNum);
	}

	private VarDecleration createVarDecleration(Context context)
	{
		traceIn("VarDecleration");
		VarDecleration varDecleration;
		
		double lambda = _configs.valDouble(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP);
		int decleratorsNum = (int) Math.ceil(StdRandom.exp(lambda));
		varDecleration = new VarDecleration();
		
		for (int i=0; i<decleratorsNum ; i++)
		{
			varDecleration.addDeclerator(createVarDeclerator(context));
		}
		
		traceOut();
		return varDecleration;
	}

	private VarDeclerator createVarDeclerator(Context context)
	{
		traceIn("VarDeclerator");
		VarDeclerator varDeclerator;
		
		Identifier id;
		
		// decide on probability if define new var
		if (context.varDecleratorForceNewIdentifier)
			context.identifierUseExistingVarProb = 0;
		else
			context.identifierUseExistingVarProb = _configs.valDouble(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP);
		
		// check if the identifier is defined in the current scope
		do
		{
			id = createIdentifier(context);	
		} while (context.getSymTable().contains(id));
		
		// add identifier to current scope
		context.getSymTable().newEntry(id, SymEntryType.VAR);
		
		// generate expression to be assigned to the var
		context.identifierUseExistingVarProb = 1; // all vars must be exist
		AbsExpression exp = generateExpression(context);
		
		varDeclerator = new VarDeclerator(id, exp);
		
		traceOut();
		return varDeclerator;
	}

	private Continue createContinue(Context context)
	{
		traceIn("Continue");
		Continue continueStmt = (Continue) _factoryJST.getConstantNode("continue");
		
		traceOut();
		return continueStmt;
	}
	
	private Break createBreak(Context context)
	{
		traceIn("Break");
		Break breakStmt = ((Break) _factoryJST.getConstantNode("break"));
		
		traceOut();
		return breakStmt;
	}

	private Return createReturn(Context context)
	{
		traceIn("Return");
		Return returnStmt;
		
		double returnValueProb = _configs.valDouble(ConfigProperties.RETURN_VALUE_BERNOULLY_P);
		
		//decide whether to return a value
		if(StdRandom.bernoulli(returnValueProb))
			returnStmt = new Return(generateExpression(context));
		else
			returnStmt = new Return();
		
		traceOut();
		return returnStmt;
	}

	private StatementsBlock createStatementsBlock(Context context)
	{
		traceIn("StatementsBlock");
		StatementsBlock stmtBlock = new StatementsBlock();
		
		// decide how many statements will be generate
		int size = 0;
		
		if (_depth < _configs.valInt(ConfigProperties.MAX_JST_DEPTH))
		{
			//choose the block size
			double factorDepth = Math.pow(_configs.valDouble(ConfigProperties.FACTOR_DEPTH), _depth);
			double lambda = _configs.valDouble(ConfigProperties.STMTS_BLOCK_SIZE_LAMBDA) / factorDepth;
			size = (int) Math.ceil(StdRandom.exp(lambda));
		}
		
		// create new context
		Context newContext = new Context(context, null, null); // TODO: set flags (loop/function)
		
		// generate statements
		for(int i = 0; i < size; i++)
			stmtBlock.addStatement(generateStatement(newContext));
		
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
	private Assignment createAssignment(Context context) 
	{
		traceIn("Assignment");
		Assignment assignment;
		
		double useExistingVarProb = _configs.valDouble(ConfigProperties.ASSIGNMENT_USE_EXISTING_VAR_BERNOULLY_P);
		
		double prevVal = context.identifierUseExistingVarProb;
		context.identifierUseExistingVarProb = useExistingVarProb;
		Identifier id = createIdentifier(context);
		AbsExpression expr = generateExpression(context);
		
		context.identifierUseExistingVarProb = prevVal;
		
		// make sure identifier is defined (if not add it to top level scope)
		SymEntry entry = context.getSymTable().lookup(id);
		if (entry == null)
		{
			_rootContext.getSymTable().newEntry(id, SymEntryType.VAR);
		}
		
		assignment = new Assignment(id, expr);
		
		traceOut();
		return assignment;
	}

	private CompoundAssignment createCompoundAssignment(Context context)
	{
		traceIn("CompoundAssignment");
		CompoundAssignment compAsssignment;
		
		//use only existing variables
		context.identifierUseExistingVarProb = 1;
		Identifier var = createIdentifier(context);
		AbsExpression expr = generateExpression(context);
		CompoundOps op = CompoundOps.getRandomly();
		
		compAsssignment = new CompoundAssignment(var, op, expr);
		
		traceOut();
		return compAsssignment;
	}

	private Call createCall(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private FunctionExp createFunctionExpression(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private MemberExp createMemberExpression(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private ObjectExp createObjectExpression(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ArrayExp createArrayExpression(Context context) 
	{
		traceIn("ArrayExpression");
		ArrayExp arrayExp = new ArrayExp();
		
		double p = _configs.valDouble(ConfigProperties.ARRAY_LENGTH_LAMBDA_EXP);
		
		int length = (int)Math.ceil(StdRandom.exp(p));
		
		for (int i=0 ; i< length ; i++)
		{
			arrayExp.addItem(generateExpression(context));
		}
		
		traceOut();
		return arrayExp;
	}
	
	private Identifier createIdentifier(Context context) 
	{
		traceIn("Identifier");
		Identifier id;

		//decide whether to use an existing variable
		if(StdRandom.bernoulli(context.identifierUseExistingVarProb))
		{
			List<Identifier> existingVars = context.getSymTable().getAvaiableIdentifiers(SymEntryType.VAR);
			int totalVars = existingVars.size();
			
			// if no var is defined then - create new var decleration at root scope!
			if (totalVars == 0)
			{
				_rootContext.varDecleratorForceNewIdentifier = true;
				_program.addStatement(createVarDecleration(_rootContext));
				_rootContext.varDecleratorForceNewIdentifier = false;
				
				// re-get available vars
				existingVars = context.getSymTable().getAvaiableIdentifiers(SymEntryType.VAR);
				totalVars = existingVars.size();
			}
			
			id = existingVars.get(StdRandom.uniform(totalVars));
		}
		else
		{
			String newName = _counterVar.getNext();
			id = _factoryJST.getIdentifier(newName);
		}
		
		traceOut();
		return id;
	}
	
	private This createThis(Context context)
	{
		traceIn("This");
		This thisExp = ((This) _factoryJST.getConstantNode("this"));
		
		traceOut();
		return thisExp;
	}

	private OperationExp createExpressionOp(Context context) 
	{
		traceIn("ExpressionOp");
		OperationExp expressionOp;
		
		Operator operator = Operator.getRandomly();
		
		// generate operands array
		AbsExpression[] operandsArray = new AbsExpression[operator.getNumOperands()];
		
		for(int i=0 ; i<operandsArray.length ; i++)
			operandsArray[i] = generateExpression(context);
	
		expressionOp = new OperationExp(operator, operandsArray);
		
		traceOut();
		return expressionOp;
	}

	private Literal createLiteral(Context context) 
	{
		traceIn("Literal");
		Literal lit = null;
		
		LiteralTypes litType = LiteralTypes.getRandomly();
		
		if (litType.isSingleValue())
			lit = (new Literal(litType));
		else if(litType.equals(LiteralTypes.NUMBER))
			lit = (createLiteralNumber(context));
		else if(litType.equals(LiteralTypes.STRING))
			lit = (createLiteralString(context));
		
		traceOut();
		return lit;
	}

	private LiteralString createLiteralString(Context context) 
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

	private LiteralNumber createLiteralNumber(Context  context) 
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
