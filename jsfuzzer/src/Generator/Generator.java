package Generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import Generator.SymTable.SymEntryType;
import JST.*;
import JST.VarDecleration.VarDeclerator;
import JST.Enums.BinaryOps;
import JST.Enums.CompoundOps;
import JST.Enums.LiteralTypes;
import JST.Enums.TrinaryOps;
import JST.Enums.UnaryOps;
import JST.Helper.Rand;
import JST.Helper.StdRandom;

public class Generator
{	
	private JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private Properties _configs;
	
	public static Program generate(Properties configs)
	{
		Generator gen = new Generator(configs);
		Context context = new Context(); // global scope
		
		// generate program
		Program prog = gen.createProgram(context);
		
		return prog;
	}
	
	public Generator(Properties configs)
	{
		_configs = configs;
	}
	
	private AbsStatement generateStatement(Context context)
	{
		String stmtType = Rand.getRndStatementType(_configs, context);
		AbsStatement retStmt;
		
		switch(stmtType)
		{
			// TODO: finish...
		}
		
		return null;
	}
	
	private AbsExpression generateExpression(Context context)
	{
		String exprType = Rand.getRndExpressionType(_configs, context);
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
	
	private Program createProgram(Context context)
	{
		Program prog = new Program();
		AbsStatement stmt;
		
		while ((stmt = generateStatement(context)) != null)
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
		if (Rand.getBoolean())
		{
			StatementsBlock falseOp = createStatementsBlock(context);
			return new If(conditionExp, trueOp, falseOp);
		}
		
		return new If(conditionExp, trueOp);
	}

	private While createWhile(Context context)
	{
		AbsExpression conditionExp = generateExpression(context);
		StatementsBlock op = createStatementsBlock(context);
		
		return new While(conditionExp, op);
	}

	private DoWhile createDoWhile(Context context)
	{
		AbsExpression conditionExp = generateExpression(context);
		StatementsBlock op = createStatementsBlock(context);
		
		return new DoWhile(conditionExp, op);
	}

	private For createFor(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private ForEach createForEach(Context context) {
		// TODO Auto-generated method stub
		return null;
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
		
		//in case we got zero or less cases
		casesNum = (casesNum > 0) ? casesNum : 1; 
				
		List<AbsExpression> cases = new LinkedList<AbsExpression>();
		for(int i = 0; i < casesNum; i++)
			cases.add(generateExpression(context));
		
		//TODO how do we check that "default" was not generated randomlly?
		if(includeDefault)
			cases.add(new LiteralString("default"));
		
		/***** generate statements *****/
		exp = Integer.parseInt(_configs.getProperty("case_block_stmts_num_normal_exp"));
		stddev = Integer.parseInt(_configs.getProperty("case_block_stmts_num_normal_stddev"));
		int stmtsNum = (int) StdRandom.gaussian(exp, stddev);

		//in case we got zero or less cases
		stmtsNum = (stmtsNum > 0) ? stmtsNum : 1; 
		
		List<AbsStatement> stmts = new LinkedList<AbsStatement>();
		for(int i = 0; i < stmtsNum; i++)
			stmts.add(generateStatement(context));
		
		return new CaseBlock(cases, stmts);
	}

	private FunctionDefinition createFunctionDefinition(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private VarDecleration createVarDecleration(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private VarDeclerator createVarDeclerator(Context context) {
		// TODO Auto-generated method stub
		return null;
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

	private Assignment createAssignment(Context context) 
	{
		double useExistingVarProb = Double.parseDouble(_configs.getProperty("assignment_use_existing_var_bernoully_p"));
		
		Identifier var = createIdentifier(context, useExistingVarProb);
		AbsExpression expr = generateExpression(context);
		
		return new Assignment(var, expr);
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
		//TODO: test length parameter
		int p = Integer.parseInt(_configs.getProperty("array_length_parameter"));
		
		long length = Math.round(Rand.getExponential(p));
		
		// TODO: finish construction
		//for (long i=0;)
		
		return null;
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
			//TODO: get next new usable var name and create new Identifier with it
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
		// TODO Auto-generated method stub
		return null;
	}

	private LiteralNumber createLiteralNumber(Context  context) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
