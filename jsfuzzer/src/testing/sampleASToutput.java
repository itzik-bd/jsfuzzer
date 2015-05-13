package testing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import JST.*;
import JST.Enums.BinaryOps;
import JST.Enums.UnaryOps;

public class sampleASToutput 
{
	public static Program getSampleAST()
	{
		Program prog = new Program();
		
		VarDecleration vd = new VarDecleration();
		vd.addDeclerator(new Identifier("x"), new LiteralNumber("2"));
		prog.addStatement(vd);
		
		// create the function
		List<Identifier> input = new ArrayList<Identifier>();
		input.add(new Identifier("x"));
		FunctionDefinition func = new FunctionDefinition(new Identifier("sqr"), input);
		func.addStatement(new Return(new BinaryOp(new Identifier("x"), BinaryOps.MULTIPLY, new Identifier("x"))));
		prog.addStatement(func);
		

		
		List<AbsExpression> argsPrint = new LinkedList<AbsExpression>();
		argsPrint.add(new Identifier("x"));
		Call print = new Call(new MemberExpression(new Identifier("console"), new Identifier("log")), argsPrint);
		
		VarDecleration initvd = new VarDecleration();
		initvd.addDeclerator(new Identifier("i"), new LiteralNumber("0"));
		AbsExpression cond = new BinaryOp(new Identifier("i"), BinaryOps.LT, new LiteralNumber("10"));
		AbsExpression step = new UnaryOp(new Identifier("i"), UnaryOps.PLUSPLUSLEFT);
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

