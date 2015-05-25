package JST.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import Generator.Context;

public abstract class Rand 
{
	private static Random _rnd = new Random();
	
	/**
	 * randomly returns a number between 1 and infinity 
	 * with probability factor p
	 */
	public static double getExponential(int p)
	{
		double u = _rnd.nextFloat();

		return (Math.abs(Math.log(1-u)/(-1 * p)));
	}

	/**
	 *  statically get a uniform random number
	 */
	public static int getUniform(int max)
	{
		return (_rnd.nextInt(max));
	}
	
	/**
	 *  get a random boolean, distributed equal (p=1/2)
	 */
	public static boolean getBoolean()
	{
		return (_rnd.nextFloat() > 0.5) ? true : false;
	}

	public static String getRndExpressionType(Properties conf, Context context)
	{
		// TODO consider what to do with This(the expression "This")
		
		HashMap<String, Integer> hs = new HashMap<String, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put("UnaryOp", Integer.parseInt(conf.getProperty("expr_UnaryOp")));
		hs.put("BinaryOp", Integer.parseInt(conf.getProperty("expr_BinaryOp")));
		hs.put("TrinaryOp", Integer.parseInt(conf.getProperty("expr_TrinaryOp")));
		hs.put("ArrayExpression", Integer.parseInt(conf.getProperty("expr_ArrayExpression")));
		hs.put("Call", Integer.parseInt(conf.getProperty("expr_Call")));
		hs.put("Identifier", Integer.parseInt(conf.getProperty("expr_Identifier")));
		hs.put("Literal", Integer.parseInt(conf.getProperty("expr_Literal")));
		hs.put("MemberExpression", Integer.parseInt(conf.getProperty("expr_MemberExpression")));
		hs.put("This", Integer.parseInt(conf.getProperty("expr_This")));
		
		// randomlly get a value
		int max=0;
		for (Integer val : hs.values())
			max +=  val;
		int r = getUniform(max);
		
		// Return the chosen value
		int sum = 0;
		for (String exprName : hs.keySet())
		{
			sum += hs.get(exprName);
			
			if (sum > r)
				return exprName;
		}
		
		// Sould never get here.
		return null;
	}

	public static String getRndStatementType(Properties conf, Context context)
	{
		// TODO finish....
		
		HashMap<String, Integer> hs = new HashMap<String, Integer>();
		
		// All properties are relative to the total of all properties
		hs.put("CompoundAssignment", Integer.parseInt(conf.getProperty("stmt_CompoundAssignment")));
		hs.put("DoWhile", Integer.parseInt(conf.getProperty("stmt_DoWhile")));
		
		
		// Only relevant to in loop
		if (context.isInWhileLoop())
		{
			hs.put("Break", Integer.parseInt(conf.getProperty("stmt_Break")));
			hs.put("Continue", Integer.parseInt(conf.getProperty("stmt_Continue")));
		}
				
		// randomlly get a value
		int max=0;
		for (Integer val : hs.values())
			max +=  val;
		int r = getUniform(max);
		
		// Return the chosen value
		int sum = 0;
		for (String exprName : hs.keySet())
		{
			sum += hs.get(exprName);
			
			if (sum > r)
				return exprName;
		}
		
		// Sould never get here.
		return null;
	}
}
