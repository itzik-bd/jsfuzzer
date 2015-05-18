import java.io.IOException;
import java.util.Properties;

import Generator.Generator;
import Generator.GeneratorUtils;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Properties p = GeneratorUtils.loadConfigFile(null);
		Generator.generate(p);
	}
}