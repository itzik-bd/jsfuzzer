import java.io.IOException;
import java.io.PrintWriter;
import Engines.SpiderMonkeyEngine;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("Hello jsfuzzer\n");
		
		JST.Program p = testing.sampleAST.getSampleAST();
		
		String progStr = JST.Vistors.JstToJs.execute(p);
		System.out.println(progStr);
		
		PrintWriter writer = new PrintWriter("file1.js", "UTF-8");
		writer.println(progStr);
		writer.close();
		
		String fileToRun = "file2.js";
		
		SpiderMonkeyEngine spiderMonkey = new SpiderMonkeyEngine();
		spiderMonkey.runFile(fileToRun);
	}
	
}