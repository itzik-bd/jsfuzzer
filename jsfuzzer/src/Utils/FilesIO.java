package Utils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

public class FilesIO
{
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
	
	public static Properties loadPropertiesFile(String propertiesFile) throws IOException
	{
		Properties p = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(propertiesFile);
			p.load(input);
		} finally {
			if (input != null) {
				try { input.close(); }
				catch (IOException e) { /* ignore */ }
			}
		}
		
		return p;
	}
}