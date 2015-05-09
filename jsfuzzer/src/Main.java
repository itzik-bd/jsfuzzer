public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello jsfuzzer\n");
		
		JST.Program p = testing.sampleAST.getSampleAST();
		
		String progStr = JST.Vistors.JstToJs.execute(p);
		System.out.println(progStr);
	}
}