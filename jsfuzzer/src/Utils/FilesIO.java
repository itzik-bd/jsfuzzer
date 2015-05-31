package Utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FilesIO
{
	public static void WriteToFile(String path, String contents) throws UnsupportedEncodingException, FileNotFoundException, IOException
	{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
		writer.write(contents);
		writer.close();
	}
}