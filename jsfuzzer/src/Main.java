import java.io.IOException;
import java.io.PrintWriter;

import Engines.NodejsEngine;
import Engines.SpiderMonkeyEngine;
import Generator.Generator;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("Hello jsfuzzer\n");
		
		Generator.generate(null);
		
		/*
		JST.Program p = testing.sampleASToutput.getSampleAST();
		
		String progStr = JST.Vistors.JstToJs.execute(p);
		System.out.println(progStr);
		
		String jsFile = "tests/test1.js";

		PrintWriter writer = new PrintWriter(jsFile, "UTF-8");
		writer.println(progStr);
		writer.close();
		
		
		SpiderMonkeyEngine spiderMonkey = new SpiderMonkeyEngine();
		spiderMonkey.runFile(jsFile);
		
		NodejsEngine nodejs = new NodejsEngine();
		nodejs.runFile(jsFile);
		*/
	}
	
}