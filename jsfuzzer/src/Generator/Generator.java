package Generator;

import java.util.Properties;

import JST.*;
import JST.Switch.CaseBlock;
import JST.VarDecleration.VarDeclerator;
import JST.Enums.BinaryOps;
import JST.Enums.LiteralTypes;
import JST.Enums.TrinaryOps;
import JST.Enums.UnaryOps;
import JST.Helper.Factory;
import JST.Helper.Rand;
import JST.Interfaces.Visitor;

public class Generator implements Visitor
{	
	private JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private Properties _configs;
	
	public static Program generate(Properties configs)
	{
		Generator gen = new Generator(configs);
		Program prog = new Program();
		Context context = new Context(); // global scope
		
		// generate program
		prog.accept(gen, context);
		
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
	
	public ArrayExpression createArrayExpression(Context context) 
	{
		//TODO: test length parameter
		int p = Integer.parseInt(_configs.getProperty("array_length_parameter"));
		
		long length = Math.round(Rand.getExponential(p));
		
		// TODO: finish construction
		//for (long i=0;)
		
		return null;
	}
	
	public Identifier createIdentifier(Context context) 
	{
		// TODO: do
		return null;
	}
	
	public This createThis(Context context)
	{
		return ((This) _factoryJST.getConstantNode("this"));
	}
	
	public UnaryOp CreateUnaryOp(Context context) 
	{
		AbsExpression absExp = generateExpression(context);
		
		return (new UnaryOp(UnaryOps.getRandomly(), absExp));
	}

	public BinaryOp CreateBinaryOp(Context context) 
	{		
		AbsExpression absExp1 = generateExpression(context);
		AbsExpression absExp2 = generateExpression(context);
	
		return(new BinaryOp(BinaryOps.getRandomly(), absExp1, absExp2));
	}

	public TrinaryOp CreateTrinaryOp(Context context) 
	{		
		AbsExpression absExp1 = generateExpression(context);
		AbsExpression absExp2 = generateExpression(context);
		AbsExpression absExp3 = generateExpression(context);
	
		return(new TrinaryOp(TrinaryOps.getRandomly(), absExp1, absExp2, absExp3));
	}

	public Literal createLiteral(Context context) 
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

// ===============================================================================	
	
	@Override
	public Object visit(Program program, Object context)
	{
		AbsStatement stmt;
		
		while ((stmt = generateStatement()) != null)
		{
			program.addStatement(stmt);
		}
		
		return null;
	}

	@Override
	public Object visit(If ifStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(DoWhile doWhile, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(For forStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ForEach forEach, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Switch switchStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CaseBlock caseBlock, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(FunctionDefinition functionDefinition, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VarDecleration varDecleration, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VarDeclerator varDeclerator, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock stmtBlock, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Assignment assignment, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CompoundAssignment assignment, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Call call, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(FunctionExpression functionExpression, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MemberExpression memberExpr, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ObjectExpression objExpr, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ArrayExpression arrayExpr, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Identifier id, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpr, Object context) 
	{
		// Nothing to be done
		return null;
	}
	
	@Override
	public Object visit(UnaryOp unaryOp, Object context) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object visit(BinaryOp binaryOp, Object context) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object visit(TrinaryOp trinaryOp, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Literal literal, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LiteralString literal, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LiteralNumber literal, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

}
