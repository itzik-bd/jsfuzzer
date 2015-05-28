package Generator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import Generator.Config.ConfigProperties;
import Generator.SymTable.SymEntry;
import Generator.SymTable.SymEntryType;
import Generator.Config.ConfigProperties;
import Generator.Config.Configs;
import JST.*;
import JST.VarDecleration.VarDeclerator;
import JST.Enums.BinaryOps;
import JST.Enums.CompoundOps;
import JST.Enums.LiteralTypes;
import JST.Enums.TrinaryOps;
import JST.Enums.UnaryOps;
import JST.Helper.StdRandom;
import JST.Interfaces.Caseable;

public class Generator
{	
	private JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
<<<<<<< HEAD
	private Configs _configs;
=======
	private Properties _configs;
	private Context _rootContext = new Context(); // global scope
>>>>>>> vardeclerator in generator
	
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
	
	/**
	 * This is an initial and non complex solution
	 * Get all probabilities from the conf and chose rendomly with respect to their relations
	 * @return
	 */
	public String getRndExpressionType()
	{
		HashMap<String, Integer> hs = new HashMap<String, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put("UnaryOp", _configs.valInt(ConfigProperties.EXPR_UNARYOP));
		hs.put("BinaryOp", _configs.valInt(ConfigProperties.EXPR_BINARYOP));
		hs.put("TrinaryOp", _configs.valInt(ConfigProperties.EXPR_TRINARYOP));
		hs.put("ArrayExpression", _configs.valInt(ConfigProperties.EXPR_ARRAYEXPRESSION));
		hs.put("Call", _configs.valInt(ConfigProperties.EXPR_CALL));
		
		hs.put("Identifier", Integer.parseInt(_configs.getProperty("expr_Identifier")));
		hs.put("Literal", Integer.parseInt(_configs.getProperty("expr_Literal")));
		hs.put("MemberExpression", Integer.parseInt(_configs.getProperty("expr_MemberExpression")));
		hs.put("This", Integer.parseInt(_configs.getProperty("expr_This")));
		hs.put("ObjectExpression", Integer.parseInt(_configs.getProperty("expr_ObjectExpression")));
		hs.put("FunctionExpression", Integer.parseInt(_configs.getProperty("expr_FunctionExpression")));

		// randomly chose from values
		return StdRandom.choseFromProbList(hs);
	}

	public String getRndStatementType(Context context)
	{
		HashMap<String, Integer> hs = new HashMap<String, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put("CompoundAssignment", Integer.parseInt(_configs.getProperty("stmt_CompoundAssignment")));
		hs.put("FunctionDefinition", Integer.parseInt(_configs.getProperty("stmt_FunctionDefinition")));
		hs.put("If", Integer.parseInt(_configs.getProperty("stmt_If")));
		hs.put("OutputStatement", Integer.parseInt(_configs.getProperty("stmt_OutputStatement")));
		hs.put("StatementsBlock", Integer.parseInt(_configs.getProperty("stmt_StatementsBlock")));
		hs.put("Switch", Integer.parseInt(_configs.getProperty("stmt_Switch")));
		hs.put("VarDecleration", Integer.parseInt(_configs.getProperty("stmt_VarDecleration")));
		hs.put("Assignment", Integer.parseInt(_configs.getProperty("stmt_Assignment")));
		hs.put("Expression", Integer.parseInt(_configs.getProperty("stmt_Expression")));
				
		// Is in loop
		if (context.isInWhileLoop())
		{
			hs.put("Break", Integer.parseInt(_configs.getProperty("stmt_Break")));
			hs.put("Continue", Integer.parseInt(_configs.getProperty("stmt_Continue")));
			hs.put("Return", Integer.parseInt(_configs.getProperty("stmt_Return")));
			
			// Lower the probability of nested loop
			int p = Integer.parseInt(_configs.getProperty("stmt_ForEach"));
			hs.put("ForEach", Integer.parseInt(_configs.getProperty("stmt_ForEach"))/p);
			hs.put("While", Integer.parseInt(_configs.getProperty("stmt_While"))/p);
			hs.put("DoWhile", Integer.parseInt(_configs.getProperty("stmt_DoWhile"))/p);
			hs.put("For", Integer.parseInt(_configs.getProperty("stmt_For"))/p);
		}
		else
		{
			hs.put("ForEach", Integer.parseInt(_configs.getProperty("stmt_ForEach")));
			hs.put("While", Integer.parseInt(_configs.getProperty("stmt_While")));
			hs.put("DoWhile", Integer.parseInt(_configs.getProperty("stmt_DoWhile")));
			hs.put("For", Integer.parseInt(_configs.getProperty("stmt_For")));
		}
						
		// Sould never get here.
		return StdRandom.choseFromProbList(hs);
	}
	
	private AbsStatement generateStatement(Context context)
	{
		String stmtType = getRndStatementType(context);
		AbsStatement retStmt;
		
		switch(stmtType)
		{
			case "CompoundAssignment": retStmt = createCompoundAssignment(context); break;
			case "FunctionDefinition": retStmt = createFunctionDefinition(context); break;
			case "If": retStmt = createIf(context); break;
			case "StatementsBlock": retStmt = createStatementsBlock(context); break;
			case "Switch": retStmt = createSwitch(context); break;
			case "VarDecleration": retStmt = createVarDecleration(context); break;
			case "Break": retStmt = createBreak(context); break;
			case "Continue": retStmt = createContinue(context); break;
			case "Return": retStmt = createReturn(context); break;
			case "ForEach": retStmt = createForEach(context); break;
			case "While": retStmt = createWhile(context); break;
			case "DoWhile": retStmt = createDoWhile(context); break;
			case "For": retStmt = createFor(context); break;
			case "Assignment": retStmt = createAssignment(context); break;
			
			case "Expression": retStmt = generateExpression(context); break;
			//case "stmt_OutputStatement": retStmt = createOu; break;
			
			// Should not happend
			default: retStmt=null;
		}
		
		return retStmt;
	}
	
	private AbsExpression generateExpression(Context context)
	{
		String exprType = getRndExpressionType();
		AbsExpression retExpr;
		
		switch(exprType)
		{
			case "expr_UnaryOp": retExpr = createUnaryOp(context); break;
			case "expr_BinaryOp": retExpr = createBinaryOp(context); break;
			case "expr_TrinaryOp": retExpr = createTrinaryOp(context); break;
			case "expr_ArrayExpression": retExpr = createArrayExpression(context); break;
			case "expr_Call": retExpr = createCall(context); break;
			case "expr_MemberExpression": retExpr = createMemberExpression(context); break;
			case "expr_This": retExpr = createThis(context); break;
			case "expr_Literal": retExpr = createLiteral(context); break;
			case "expr_ObjectExpression": retExpr = createObjectExpression(context); break;
			case "expr_FunctionExpression": retExpr = createFunctionExpression(context); break;
			
			case "expr_Identifier":
				double useExistingVarProb = Double.parseDouble(_configs.getProperty("assignment_use_existing_var_bernoully_p"));
				retExpr  = createIdentifier(context, useExistingVarProb);
				break;
				
			// Should not get to this
			default: retExpr = new This();		
		}
		
		return retExpr;
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
		Context newCont = new Context(context, true);
		StatementsBlock stmtsBlock = createStatementsBlock(newCont);
		
		return (new ForEach(id, arr, stmtsBlock));
	}

	private Switch createSwitch(Context context)
	{
		//TODO: we may want expr to be an identifier with high prob.
		AbsExpression expr = generateExpression(context);
		Switch switchStmt = new Switch(expr);
		
		int exp = Integer.parseInt(_configs.getProperty("cases_blocks_num_normal_exp"));
		int stddev = Integer.parseInt(_configs.getProperty("cases_blocks_num_normal_stddev"));
		int caseBlocksNum = (int) StdRandom.gaussian(exp, stddev);

		double defaultProb = Double.parseDouble(_configs.getProperty("case_block_include_default_bernoully_p"));
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
		int exp = Integer.parseInt(_configs.getProperty("cases_num_normal_exp"));
		int stddev = Integer.parseInt(_configs.getProperty("cases_num_normal_stddev"));
		int casesNum = (int) StdRandom.gaussian(exp, stddev);
		
		//TODO : maybe 0 cases
		//in case we got zero or less cases
		casesNum = (casesNum > 0) ? casesNum : 1; 
				
		List<Caseable> cases = new LinkedList<Caseable>();
		for(int i = 0; i < casesNum; i++)
			cases.add(createCase(context));
		
		//TODO how do we check that "default" was not generated randomlly?
		if(includeDefault)
			cases.add((Default)_factoryJST.getConstantNode("default"));
		
		/***** generate statements *****/
		exp = Integer.parseInt(_configs.getProperty("case_block_stmts_num_normal_exp"));
		stddev = Integer.parseInt(_configs.getProperty("case_block_stmts_num_normal_stddev"));
		//TODO: int stmtsNum = getRandNormalNumber(caseBlockStmtsNumNormalExp)
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
		int decleratorsNum = Math.ceil(StdRandom.exp());
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
			Identifier id = createIdentifier(context, _configs.valInt(ConfigProperties.VAR_DECL_NUM_LAMBDA_EXP));	
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
		double returnValueProb = Double.parseDouble(_configs.getProperty("return_value_bernoully_p"));
		
		//decide whether to return a value
		if(StdRandom.bernoulli(returnValueProb))
			return new Return(generateExpression(context));
		
		return new Return();
	}

	private StatementsBlock createStatementsBlock(Context context)
	{
		//choose the block size
		double exp = Double.parseDouble(_configs.getProperty("stmts_block_size_normal_exp"));
		double stddev = Double.parseDouble(_configs.getProperty("stmts_block_size_normal_stddev"));
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
		double useExistingVarProb = Double.parseDouble(_configs.getProperty("assignment_use_existing_var_bernoully_p"));
		
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
		int p = Integer.parseInt(_configs.getProperty("array_length_parameter"));
		
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
			String newName = IdNameGenerator.getNextFreeName();
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
		double lambda = Double.parseDouble(_configs.getProperty("literal_string_lambda"));
		int maxLength = Integer.parseInt(_configs.getProperty("literal_string_max_length"));
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
		double infinity = Double.parseDouble(_configs.getProperty("literal_number_max_probability"));
		if (StdRandom.bernoulli(infinity))
		{
			return (new LiteralNumber("9007199254740992"));
		}
		else
		{
			double lambda = Double.parseDouble(_configs.getProperty("literal_number_lambda"));
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
