package Generator;

public enum ApiOptions
{
	PRINT("print"),
	DEBUG_VARS("debugVars"),
	PROXY_CALL("call"),
	TRACE_DEBUG("traceDebug");
	
	private String _apiName;
	
	private ApiOptions(String apiName)
	{
		_apiName = apiName;
	}
	
	public String getApiName()
	{
		return _apiName;
	}
}