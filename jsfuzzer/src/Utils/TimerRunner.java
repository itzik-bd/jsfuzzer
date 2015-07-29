package Utils;

import java.util.Calendar;

public abstract class TimerRunner<T>
{
	private T _lastResult;
	private double _lastRuntime = 0; // still did not ran
	
	public TimerRunner() {
		executeScript();
	}
	
	public final T executeScript() {
		long startTime = Calendar.getInstance().getTimeInMillis();
		_lastResult = run();
		long total = Calendar.getInstance().getTimeInMillis() - startTime;
		_lastRuntime = total/1000.0;
		return _lastResult;
	}
	
	public final double lastRuntime()	{
		return _lastRuntime;
	}
	
	public final T lastResult() {
		return _lastResult;
	}
	
	public abstract T run();
}