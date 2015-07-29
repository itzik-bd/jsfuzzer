package Engines;

public class RunEngineResult
{	
	private String _stdout;
	private String _stderr;
	private boolean _timeoutExcided;
	private double _actualRuntime;
	
	public RunEngineResult(String stdout, String stderr, boolean timeoutExcided, double actualRuntime) {
		_stdout = normalizeLineFeed(stdout);
		_stderr = normalizeLineFeed(stderr);
		_timeoutExcided = timeoutExcided;
		_actualRuntime = actualRuntime;
	}
	
	public String getStdout() {
		return _stdout;
	}
	
	public String getStderr() {
		return _stderr;
	}
	
	public boolean isTimeExcided() {
		return _timeoutExcided;
	}
	
	public double getActualRuntime() {
		return _actualRuntime;
	}
	
	private String normalizeLineFeed(String s)
	{
		return s.replace("\r\n", "\n");
	}
}