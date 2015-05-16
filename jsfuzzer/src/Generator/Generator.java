package Generator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import JST.*;
import JST.Switch.CaseBlock;
import JST.VarDecleration.VarDeclerator;
import JST.Interfaces.Visitor;

public class Generator implements Visitor
{
	private JST.Helper.Factory _factoryJST = new JST.Helper.Factory();
	private static Properties _properties = new Properties();
	private static final String _propertiesPath = "resources/config/config.properties"; 
	
	public static Program generate()
	{
		Generator gen = new Generator();
		Program prog = new Program();
		Context context = new Context(); // global scope
		loadProperties();
		
		// generate program
		prog.accept(gen, context);
		
		return prog;
	}
	
	private static void loadProperties()
	{
		InputStream input = null;
		try {
			input = new FileInputStream(_propertiesPath);
		} catch (FileNotFoundException e) {
			System.err.println("Failed opening properties file for input stream");
			e.printStackTrace();
		}
		
		try {
			_properties.load(input);
		} catch (IOException e) {
			System.err.println("Failed loading properties file");
			e.printStackTrace();
		}
	}
	
	private AbsStatement generateStatement()
	{
		// TODO :-)
		// the fun part should be here
		return null;
	}
	
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
	public Object visit(This thisExpr, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UnaryOp unaryOp, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(BinaryOp binaryOp, Object context) {
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
