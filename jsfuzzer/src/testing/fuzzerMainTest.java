package testing;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import Main.JsFuzzer;

public class fuzzerMainTest
{

	@Test
	public void test() {
		int num = (new Random()).nextInt(Integer.MAX_VALUE);
		String outputFile = "tests/" + num + ".js";
		
		JsFuzzer.main("-o",outputFile);
	}

}