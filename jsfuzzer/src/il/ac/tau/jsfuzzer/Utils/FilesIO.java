package il.ac.tau.jsfuzzer.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class FilesIO
{
	public static final String PROJECT_PATH = FilesIO.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6) + "../";
	
	public static void WriteToFile(String path, String contents) throws IOException
	{
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
			writer.write(contents);
		} finally {
			if (writer != null) {
				try { writer.close(); }
				catch (IOException e) { /* ignore */ }
			}
		}
	}
	
	public static String ReadFile(String path) throws IOException
	{
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	public static Properties loadPropertiesFile(String propertiesFile)
	{
		try
		{
			return loadPropertiesFile(new FileInputStream(propertiesFile));
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
		
		return null;
	}
	
	public static Properties loadPropertiesFile(InputStream propertiesFileIS)
	{
		try
		{
			Properties p = new Properties();
			p.load(propertiesFileIS);
			return p;
		}
		catch (IOException e) { e.printStackTrace(); }
		
		return null;
	}
	
	public static String getSnippet(String snippetName)
	{
		try
		{
			String resource = "resources/snippets/" + snippetName + ".txt";
	        BufferedReader reader = new BufferedReader(new InputStreamReader(getResource(resource)));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line+"\n");
	        }
	        reader.close();
	        return out.toString();
		}
		catch(IOException e) { e.printStackTrace(); }
		
		return null;
	}
	
	public static InputStream getResource(String resource)
	{
		InputStream res = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
		if (res == null) {
			try { res = new FileInputStream(PROJECT_PATH + resource); }
			catch (FileNotFoundException e) { OutLog.printError(String.format("Could not found resource %s", resource)); }
		}
		return res;
	}
}