package JST.Helper;

import java.util.Random;

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
}
