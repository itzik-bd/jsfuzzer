import java.io.IOException;

import Generator.Generator;
import Generator.Config.Configs;
import JST.Vistors.JstToJs;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Configs c = Configs.loadConfigFile(null);
		
		JST.Program p = Generator.generate(c, false);
		
		System.out.println(JstToJs.executeCostum(p,"  ","\n"));
	}
}