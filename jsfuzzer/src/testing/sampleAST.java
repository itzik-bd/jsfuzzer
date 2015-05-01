package testing;

import java.util.ArrayList;
import java.util.List;

import JST.*;
import JST.Enums.BinaryOps;
import JST.Enums.OutputType;
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
		AbsExpression op1 = new BinaryOp(new LitNumber("5"),BinaryOps.DIVIDE,new LitNumber("9"));
		AbsExpression op2 = new BinaryOp(new Identifier("faranhight"),BinaryOps.MINUS,new LitNumber("32"));
		far2cels.addStatement(new Return(new BinaryOp(op1,BinaryOps.MULTIPLY, op2)));
		prog.addStatement(far2cels);
		
		// call the function
		List<AbsExpression> parameterList = new ArrayList<AbsExpression>();
		parameterList.add(new LitNumber("90"));
		prog.addStatement(new FunctionCall(new Identifier("far2Cels"),parameterList));
		
	// For loop - print numbers from 1-4,6-9, print odds id alert and even id log
		VarDefinition initStatement = new VarDefinition();
		Identifier i = new Identifier("i");
		initStatement.addIdInit(i);
		
		BinaryOp conditionExpression = new BinaryOp(i, BinaryOps.LT, new LitNumber("10"));
		
		UnaryOp stepExpression = new UnaryOp(i, UnaryOps.PLUSPLUSlEFT);
		
		// the operation
		StatementsBlock operation = new StatementsBlock();
		If if1 = new If(new BinaryOp(i,BinaryOps.EQUAL,new LitNumber("5")),
					new Continue());
		
		SingleLineComment slc = new SingleLineComment("Whatssss uuuUUUUuuUpppPp?!?!");
		
		If if2 = new If(new BinaryOp(new BinaryOp(i,BinaryOps.MOD,new LitNumber("2")), BinaryOps.EQUAL, new LitNumber("0")), 
				new OutputStatement(i, OutputType.OUT_LOG),
				new OutputStatement(i, OutputType.OUT_ALERT));
		operation.addStatement(if1);
		operation.addStatement(slc);
		operation.addStatement(if2);
		
		For forr = new For(initStatement, conditionExpression, stepExpression, operation);
		prog.addStatement(forr);
	
	// while that does nothing and stops after five iterations
		VarDefinition x = new VarDefinition();
		x.addIdInit(new Identifier("x"), new LitNumber("0"));
		x.addIdInit(new Identifier("y"), new LitString("Hello world!"));
		
		StatementsBlock operations = new StatementsBlock();
		operations.addStatement(x);
		operations.addStatement(new OutputStatement(new Identifier("y"), OutputType.OUT_LOG));
		
		While while1 = new While(new BinaryOp(new Identifier("x"), BinaryOps.LTE, new LitNumber("4")), 
				operations);
		prog.addStatement(while1);
		
	// return the Program
		return prog;
	}
}
