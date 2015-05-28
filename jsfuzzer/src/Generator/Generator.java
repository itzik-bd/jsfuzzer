package Generator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Generator.Config.*;
import Generator.SymTable.*;
import JST.*;
import JST.Enums.*;
import JST.Helper.StdRandom;
import JST.Interfaces.Caseable;

@SuppressWarnings("unused")
public class Generator
{	
	private JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private Configs _configs;
	private Context _rootContext = new Context(); // global scope
	
	public static Program generate(Configs configs)
	{
		Generator gen = new Generator(configs);
		
		// generate program
		Program prog = gen.createProgram();
		
		return prog;
	}
	
	public Generator(Configs configs)
	{
		_configs = configs;
	}
	
	private Method getMethod(String methodName)
	{
		Class<?>[] sig = { Context.class };
		try {
			return Generator.class.getMethod(methodName, sig);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	private Object applyMethod(Method method, Context context)
	{
		try {
			return method.invoke(this, context);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	private AbsStatement generateStatement(Context context)
	{
		HashMap<Method, Integer> hs = new HashMap<Method, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put(getMethod("createCompoundAssignment"), _configs.valInt(ConfigProperties.STMT_COMPOUNDASSIGNMENT));
		hs.put(getMethod("createFunctionDefinition"), _configs.valInt(ConfigProperties.STMT_FUNCTIONDEFINITION));
		hs.put(getMethod("createIf"), _configs.valInt(ConfigProperties.STMT_IF));
		hs.put(getMethod("createOutputStatement"), _configs.valInt(ConfigProperties.STMT_OUTPUTSTATEMENT));
		hs.put(getMethod("createStatementsBlock"), _configs.valInt(ConfigProperties.STMT_STATEMENTSBLOCK));
		hs.put(getMethod("createSwitch"), _configs.valInt(ConfigProperties.STMT_SWITCH));
		hs.put(getMethod("createVarDecleration"), _configs.valInt(ConfigProperties.STMT_VARDECLERATION));
		hs.put(getMethod("createAssignment"), _configs.valInt(ConfigProperties.STMT_ASSIGNMENT));
		hs.put(getMethod("createExpression"), _configs.valInt(ConfigProperties.STMT_EXPRESSION));
				
		if (context.isInFunction())
		{
			hs.put(getMethod("createReturn"), _configs.valInt(ConfigProperties.STMT_RETURN));
		}
		
		// Is in loop
		if (context.isInLoop())
		{
			hs.put(getMethod("createBreak"), _configs.valInt(ConfigProperties.STMT_BREAK));
			hs.put(getMethod("createContinue"), _configs.valInt(ConfigProperties.STMT_CONTINUE));
		}
		
		// Lower the probability of nested loop
		int p = _configs.valInt(ConfigProperties.NESTED_LOOPS_FACTOR) * context.getLoopDepth();
		
		hs.put(getMethod("createForEach"), _configs.valInt(ConfigProperties.STMT_FOREACH)/p);
		hs.put(getMethod("createWhile"), _configs.valInt(ConfigProperties.STMT_WHILE)/p);
		hs.put(getMethod("createDoWhile"), _configs.valInt(ConfigProperties.STMT_DOWHILE)/p);
		hs.put(getMethod("createFor"), _configs.valInt(ConfigProperties.STMT_FOR)/p);
		
		// randomly choose statement
		Method createMethod = StdRandom.choseFromProbList(hs);
		
		return (AbsStatement) applyMethod(createMethod, context);
	}
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the config and chose randomly with respect to their relations
	 * @return
	 */
	private AbsExpression generateExpression(Context context)
	{
		HashMap<Method, Integer> hs = new HashMap<Method, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put(getMethod("createUnaryOp"), _configs.valInt(ConfigProperties.EXPR_UNARYOP));
		hs.put(getMethod("createBinaryOp"), _configs.valInt(ConfigProperties.EXPR_BINARYOP));
		hs.put(getMethod("createTrinaryOp"), _configs.valInt(ConfigProperties.EXPR_TRINARYOP));
		hs.put(getMethod("createArrayExpression"), _configs.valInt(ConfigProperties.EXPR_ARRAYEXPRESSION));
		hs.put(getMethod("createCall"), _configs.valInt(ConfigProperties.EXPR_CALL));
		
		hs.put(getMethod("createIdentifier"), _configs.valInt(ConfigProperties.EXPR_IDENTIFIER));
		hs.put(getMethod("createLiteral"), _configs.valInt(ConfigProperties.EXPR_LITERAL));
		hs.put(getMethod("createMemberExpression"), _configs.valInt(ConfigProperties.EXPR_MEMBEREXPRESSION));
		hs.put(getMethod("createThis"), _configs.valInt(ConfigProperties.EXPR_THIS));
		hs.put(getMethod("createObjectExpression"), _configs.valInt(ConfigProperties.EXPR_OBJECTEXPRESSION));
		hs.put(getMethod("createFunctionExpression"), _configs.valInt(ConfigProperties.EXPR_FUNCTIONEXPRESSION));

		// randomly choose expression
		Method createMethod = StdRandom.choseFromProbList(hs);
		
		return (AbsExpression) applyMethod(createMethod, context); // TODO: createIdentifier has 2 args!
	}
	
	// ===============================================================================	
	
	private Program createProgram()
	{
		Program prog = new Program();
		AbsStatement stmt;
		
		while ((stmt = generateStatement(_rootContext)) != null)
		{
			prog.addStatement(stmt);
		}
		
		return prog;
	}

	private If createIf(Context context)
	{
		AbsExpression conditionExp = generateExpression(context);
		StatementsBlock trueOp = createStatementsBlock(context);
		
		// decide whether to have an "else" operation
		if (StdRandom.bernoulli())
		{
			StatementsBlock falseOp = createStatementsBlock(context);
			return new If(conditionExp, trueOp, falseOp);
		}
		
		return new If(conditionExp, trueOp);
	}

	private While createWhile(Context context)
	{
		// TODO: add an if to make sure the loop stops
		AbsExpression conditionExp = generateExpression(context);
		StatementsBlock op = createStatementsBlock(context);
		
		return new While(conditionExp, op);
	}

	private DoWhile createDoWhile(Context context)
	{
		// TODO: add an if to make sure the loop stops
		AbsExpression conditionExp = generateExpression(context);
		StatementsBlock op = createStatementsBlock(context);
		
		return new DoWhile(conditionExp, op);
	}

	private For createFor(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private ForEach createForEach(Context context) 
	{
		// Temporary solution (could also be a var decleration)
		Identifier id = createIdentifier(context, 1);
		
		// Temporary solution (could also be any existing iteratable var)
		ArrayExpression arr = createArrayExpression(context);
		
		// Generate StatementsBlock
		Context newCont = new Context(context, true, null);
		StatementsBlock stmtsBlock = createStatementsBlock(newCont);
		
		return (new ForEach(id, arr, stmtsBlock));
	}

	private Switch createSwitch(Context context)
	{
		//TODO: we may want expr to be an identifier with high prob.
		AbsExpression expr = generateExpression(context);
		Switch switchStmt = new Switch(expr);
		
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
					includeDefault = true;
					switchStmt.addCaseOp(createCaseBlock(context, includeDefault));
				}
			}
			
			switchStmt.addCaseOp(createCaseBlock(context, false));			
		}
		
		return switchStmt;
	}

	private CaseBlock createCaseBlock(Context context, boolean includeDefault)
	{
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

		if(includeDefault)
			cases.add((Default)_factoryJST.getConstantNode("default"));
		
		/***** generate statements *****/
		exp = _configs.valInt(ConfigProperties.CASE_BLOCK_STMTS_NUM_NORMAL_EXP);
		stddev = _configs.valInt(ConfigProperties.CASE_BLOCK_STMTS_NUM_NORMAL_STDDEV);
		int stmtsNum = (int) StdRandom.gaussian(exp, stddev);

		//in case we got negative number of stmts
		stmtsNum = Math.max(stmtsNum, 0); 
		
		List<AbsStatement> stmts = new LinkedList<AbsStatement>();
		for(int i = 0; i < stmtsNum; i++)
			stmts.add(generateStatement(context));
		
		return new CaseBlock(cases, stmts);
	}
	
	private Case createCase(Context context)
	{
		return new Case(generateExpression(context));
	}

	private FunctionDefinition createFunctionDefinition(Context context) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	private VarDecleration createVarDecleration(Context context)
	{
		double lambda = _configs.valDouble(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP);
		int decleratorsNum = (int) Math.ceil(StdRandom.exp(lambda));
		VarDecleration varDecleration = new VarDecleration();
		
		for (int i=0; i<decleratorsNum ; i++)
		{
			varDecleration.addDeclerator(createVarDeclerator(context));
		}
		
		return varDecleration;
	}

	private VarDeclerator createVarDeclerator(Context context)
	{
		Identifier id;
		
		// check if the identifier is defined in the current scope
		do
		{
			id = createIdentifier(context, _configs.valInt(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP));	
		} while (context.getSymTable().contains(id));
		
		// add identifier to current scope
		context.getSymTable().newEntry(id, SymEntryType.VAR);
		
		return new VarDeclerator(id, generateExpression(context));
	}

	private Continue createContinue(Context context)
	{
		return ((Continue) _factoryJST.getConstantNode("continue"));
	}
	
	private Break createBreak(Context context)
	{
		return ((Break) _factoryJST.getConstantNode("break"));
	}

	private Return createReturn(Context context)
	{
		double returnValueProb = _configs.valDouble(ConfigProperties.RETURN_VALUE_BERNOULLY_P);
		
		//decide whether to return a value
		if(StdRandom.bernoulli(returnValueProb))
			return new Return(generateExpression(context));
		
		return new Return();
	}

	private StatementsBlock createStatementsBlock(Context context)
	{
		//choose the block size
		double exp = _configs.valDouble(ConfigProperties.STMTS_BLOCK_SIZE_NORMAL_EXP);
		double stddev = _configs.valDouble(ConfigProperties.STMTS_BLOCK_SIZE_NORMAL_STDDEV);
		double size = StdRandom.gaussian(exp, stddev);
		
		StatementsBlock block = new StatementsBlock();
		
		for(int i = 0; i < size; i++)
			block.addStatement(generateStatement(context));
		
		return block;
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
		double useExistingVarProb = _configs.valDouble(ConfigProperties.ASSIGNMENT_USE_EXISTING_VAR_BERNOULLY_P);
		
		Identifier id = createIdentifier(context, useExistingVarProb);
		AbsExpression expr = generateExpression(context);
		
		// make sure identifier is defined (if not add it to top level scope)
		SymEntry entry = context.getSymTable().lookup(id);
		if (entry == null)
		{
			_rootContext.getSymTable().newEntry(id, SymEntryType.VAR);
		}
		
		return new Assignment(id, expr);
	}

	private CompoundAssignment createCompoundAssignment(Context context)
	{
		//use only existing variables
		Identifier var = createIdentifier(context, 1);
		AbsExpression expr = generateExpression(context);
		CompoundOps op = CompoundOps.getRandomly();
		
		return new CompoundAssignment(var, op, expr);
	}

	private Call createCall(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private FunctionExpression createFunctionExpression(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private MemberExpression createMemberExpression(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private ObjectExpression createObjectExpression(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ArrayExpression createArrayExpression(Context context) 
	{
		int p = _configs.valInt(ConfigProperties.ARRAY_LENGTH_LAMBDA_EXP);
		
		int length = (int)Math.ceil(StdRandom.exp(p));
		ArrayExpression retVal = new ArrayExpression(); 
		
		for (int i=0 ; i< length ; i++)
		{
			retVal.addItem(generateExpression(context));
		}
		
		return retVal;
	}
	
	private Identifier createIdentifier(Context context, double useExistingVarProb) 
	{
		Identifier var = null;
		
		//decide whether to use an existing variable
		if(StdRandom.bernoulli(useExistingVarProb))
		{
			List<Identifier> existingVars = context.getSymTable().getAvaiableIdentifiers(SymEntryType.VAR);
			var = existingVars.get(StdRandom.uniform(existingVars.size()));
		}
		else
		{
			String newName = IdNameGenerator.getNextVarFreeName();
			var = _factoryJST.getIdentifier(newName);
		}
		
		return var;
	}
	
	private This createThis(Context context)
	{
		return ((This) _factoryJST.getConstantNode("this"));
	}
	
	private UnaryOp createUnaryOp(Context context) 
	{
		AbsExpression absExp = generateExpression(context);
		
		return (new UnaryOp(UnaryOps.getRandomly(), absExp));
	}

	private BinaryOp createBinaryOp(Context context) 
	{		
		AbsExpression absExp1 = generateExpression(context);
		AbsExpression absExp2 = generateExpression(context);
	
		return(new BinaryOp(BinaryOps.getRandomly(), absExp1, absExp2));
	}

	private TrinaryOp createTrinaryOp(Context context) 
	{		
		AbsExpression absExp1 = generateExpression(context);
		AbsExpression absExp2 = generateExpression(context);
		AbsExpression absExp3 = generateExpression(context);
	
		return(new TrinaryOp(TrinaryOps.getRandomly(), absExp1, absExp2, absExp3));
	}

	private Literal createLiteral(Context context) 
	{
		LiteralTypes litType = LiteralTypes.getRandomly();
		
		if (litType.isSingleValue())
			return (new Literal(litType));
		else if(litType.equals(LiteralTypes.NUMBER))
			return (createLiteralNumber(context));
		else if(litType.equals(LiteralTypes.STRING))
			return (createLiteralString(context));
		else
			// This should never happen (no literal is neither of the above)
			return null;
	}

	private LiteralString createLiteralString(Context context) 
	{
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
		
		return (new LiteralString(strBld.toString()));
	}

	private LiteralNumber createLiteralNumber(Context  context) 
	{
		// return infinity?
		double infinity = _configs.valDouble(ConfigProperties.LITERAL_NUMBER_MAX_PROBABILITY);
		if (StdRandom.bernoulli(infinity))
		{
			return (new LiteralNumber("9007199254740992"));
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
			
			return (new LiteralNumber(strBld.toString()));
		}
	}

}
