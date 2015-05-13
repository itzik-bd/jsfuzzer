package testing;

import java.util.ArrayList;
import java.util.List;

import JST.*;
import JST.Enums.BinaryOps;
import JST.Enums.UnaryOps;

public class sampleAST 
{
	public static Program getSampleAST()
	{
		Program prog = new Program();
		
		// function - faranhight 22 celsious 	
		// Identifiers list containing one number
		List<Identifier> input = new ArrayList<Identifier>();
		input.add(new Identifier("faranhight"));
		
		// create the function
		FunctionDefinition far2cels = new FunctionDefinition(new Identifier("far2Cels"), input);
		
		// create and add the function def
		AbsExpression op1 = new BinaryOp(new LiteralNumber("5"),BinaryOps.DIVIDE,new LiteralNumber("9"));
		AbsExpression op2 = new BinaryOp(new Identifier("faranhight"),BinaryOps.MINUS,new LiteralNumber("32"));
		far2cels.getStatementsBlock().addStatement(new Return(new BinaryOp(op1,BinaryOps.MULTIPLY, op2)));
		prog.addStatement(far2cels);
		
		// call the function
		List<AbsExpression> parameterList = new ArrayList<AbsExpression>();
		parameterList.add(new LiteralNumber("90"));
		prog.addStatement(new Call(new Identifier("far2Cels"),parameterList));
		
		// For loop - print numbers from 1-4,6-9, print odds id alert and even id log
		VarDecleration initStatement = new VarDecleration();
		Identifier i = new Identifier("i");
		initStatement.addDeclerator(i);
		
		BinaryOp conditionExpression = new BinaryOp(i, BinaryOps.LT, new LiteralNumber("10"));
		
		UnaryOp stepExpression = new UnaryOp(i, UnaryOps.PLUSPLUSLEFT);
		
		// the operation
		StatementsBlock operationIf = new StatementsBlock();
		operationIf.addStatement(new Continue());
		If if1 = new If(new BinaryOp(i,BinaryOps.EQUAL,new LiteralNumber("5")), operationIf);
		
		
		For forr = new For(initStatement, conditionExpression, stepExpression);
		forr.addStatement(if1);
		prog.addStatement(forr);
	
		// while that does nothing and stops after five iterations
		VarDecleration x = new VarDecleration();
		x.addDeclerator(new Identifier("x"), new LiteralNumber("0"));
		x.addDeclerator(new Identifier("y"), new LiteralString("Hello \"ldldl\" \tworld!\nnew line :)"));
		
		StatementsBlock operations = new StatementsBlock();
		operations.addStatement(x);
		
		While while1 = new While(new BinaryOp(new Identifier("x"), BinaryOps.LTE, new LiteralNumber("4")), 
				operations);
		prog.addStatement(while1);
		
		// return the Program
		return prog;
	}
}
