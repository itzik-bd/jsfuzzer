package il.ac.tau.jsfuzzer.Generator;

public enum ApiOptions
{
	PRINT("print"),
	TRACE_DEBUG_VARS("traceDebugVars"),
	PROXY_CALL("call"),
	REGISTER_FUNCTION_IN_RUNTIME("regFunc"),
	MEMBER_EXPR("mem");
	
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