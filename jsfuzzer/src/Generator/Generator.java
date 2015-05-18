package Generator;

import java.util.Properties;

import JST.*;
import JST.Switch.CaseBlock;
import JST.VarDecleration.VarDeclerator;
import JST.Enums.BinaryOps;
import JST.Enums.LiteralTypes;
import JST.Enums.TrinaryOps;
import JST.Enums.UnaryOps;
import JST.Helper.Rand;

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
	
	private AbsStatement generateStatement()
	{
		// TODO :-)
		// the fun part should be here
		return null;
	}
	
	private AbsExpression generateExpression(Context context)
	{
		// TODO :-}
		// the secondary fun part should be here
		return null;
	}
	
	// ===============================================================================	
	
	private Program createProgram(Context context)
	{
		Program prog = new Program();
		AbsStatement stmt;
		
		while ((stmt = generateStatement()) != null)
		{
			prog.addStatement(stmt);
		}
		
		return prog;
	}

	private If createIf(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private While createWhile(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private DoWhile createDoWhile(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private For createFor(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private ForEach createForEach(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private Switch createSwitch(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private CaseBlock createCaseBlock(Context context) {
		// TODO Auto-generated method stub
		return null;
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

	private Return createReturn(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private StatementsBlock createStatementsBlock(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private Assignment createAssignment(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private CompoundAssignment createCompoundAssignment(Context context) {
		// TODO Auto-generated method stub
		return null;
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
	
	private Identifier createIdentifier(Context context) 
	{
		// TODO: do
		return null;
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
			// This should never happend (no literal is nither of the above)
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
