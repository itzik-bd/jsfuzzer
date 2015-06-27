package testing;

import java.util.Random;

import org.junit.Test;

import Main.JsFuzzer;

public class fuzzerMainTest
{

	@Test
	public void test() {
		System.out.println("test0");
		
		int num = (new Random()).nextInt(Integer.MAX_VALUE);
		String outputFile = "tests/" + num + ".js";
		
		JsFuzzer.main("--out",outputFile);
	}
	
	@Test
	public void test1() {
		System.out.println("test1");
	}
	
	@Test
	public void test2() {
		System.out.println("test2");
	}


}