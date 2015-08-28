package Engines;

public class RunEngineResult
{	
	private String _stdout;
	private String _stderr;
	private boolean _timeoutExceeded;
	private double _actualRuntime;
	
	public RunEngineResult(String stdout, String stderr, boolean timeoutExceeded, double actualRuntime) {
		_stdout = normalizeLineFeed(stdout);
		_stderr = normalizeLineFeed(stderr);
		_timeoutExceeded = timeoutExceeded;
		_actualRuntime = actualRuntime;
	}
	
	public String getStdout() {
		return _stdout;
	}
	
	public String getStderr() {
		return _stderr;
	}
	
	public boolean isTimeExceeded() {
		return _timeoutExceeded;
	}
	
	public double getActualRuntime() {
		return _actualRuntime;
	}
	
	private String normalizeLineFeed(String s)
	{
		return s.replace("\r\n", "\n");
	}
}