package Engines;

public class RunEngineResult
{	
	private String _stdout;
	private String _stderr;
	
	public RunEngineResult(String stdout, String stderr) {
		_stdout = normalizeLineFeed(stdout);
		_stderr = normalizeLineFeed(stderr);
	}
	
	public String getStdout() {
		return _stdout;
	}
	
	public String getStderr() {
		return _stderr;
	}
	
	private String normalizeLineFeed(String s)
	{
		return s.replace("\r\n", "\n");
	}
}