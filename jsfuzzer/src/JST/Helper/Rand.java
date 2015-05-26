package JST.Helper;

import java.util.HashMap;
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

	//Get probs and chose rendomly with respect to their relations
	public static String choseFromProbList(HashMap<String,Integer> hs)
	{
		// randomlly get a value in range 0-sumOfProps
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
		
		// Should never get here
		return null;
	}
}
