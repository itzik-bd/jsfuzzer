package testing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import JST.*;
import JST.Enums.Operator;

public class sampleASToutput 
{
	public static void saveToFile(String filename) throws FileNotFoundException, UnsupportedEncodingException
	{
		JST.Program p = getSampleAST();
		
		String progStr = JST.Vistors.JstToJs.execute(p);
		System.out.println(progStr);

		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.println(progStr);
		writer.close();
	}
	
	private static Program getSampleAST()
	{
		Program prog = new Program();
		
		VarDecleration vd = new VarDecleration();
		vd.addDeclerator(new VarDeclerator(new Identifier("x"), new LiteralNumber("2")));
		prog.addStatement(vd);
		
		// create the function
		List<Identifier> input = new ArrayList<Identifier>();
		input.add(new Identifier("x"));
//		FunctionDefinition func = new FunctionDefinition(new Identifier("sqr"), input);
//		func.addStatement(new Return(new BinaryOp(BinaryOps.MULTIPLY, new Identifier("x"), new Identifier("x"))));
//		prog.addStatement(func);
		

		
		List<AbsExpression> argsPrint = new LinkedList<AbsExpression>();
		argsPrint.add(new Identifier("x"));
		Call print = new Call(new MemberExp(new Identifier("console"), new Identifier("log")), argsPrint);
		
		VarDecleration initvd = new VarDecleration();
		initvd.addDeclerator(new VarDeclerator(new Identifier("i"), new LiteralNumber("0")));
		AbsExpression cond = new OperationExp(Operator.LT, new Identifier("i"), new LiteralNumber("10"));
		AbsExpression step = new OperationExp(Operator.PLUSPLUSLEFT, new Identifier("i"));
		For forStmt = new For(initvd, cond, step);
		List<AbsExpression> args = new LinkedList<AbsExpression>();
		args.add(new Identifier("x"));
		forStmt.addStatement(new Assignment(new Identifier("x"), new Call(new Identifier("sqr"), args)));
		forStmt.addStatement(print);
		prog.addStatement(forStmt);		
		

		prog.addStatement(print);
		
		// return the Program
		return prog;
	}
}

