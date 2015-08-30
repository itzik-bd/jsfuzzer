package il.ac.tau.jsfuzzer.Generator.Params;

public abstract class createParams
{
	protected static Object decide(createParams obj, Object defaultValue, getParamField getter) {
		if (obj == null)
			return defaultValue;
		
		Object res = getter.fetch(obj);
		return res == null ? defaultValue : res;
	}
	
	public interface getParamField
	{
		public Object fetch(createParams params);
	}
}

