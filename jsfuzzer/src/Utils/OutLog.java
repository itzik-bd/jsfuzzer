package Utils;

public class OutLog
{
	private static final String pattern = "[ %-5s ] %s";
	
	enum LogLevel {
		INFO("info"), WARN("warn"), ERROR("error"), DEBUG("debug");
		private String _text;
		private LogLevel(String text) {
			_text = text;
		}
		@Override
		public String toString() {
			return _text;
		}
	}
	
	public static void printInfo(String msg) {
		print(msg, LogLevel.INFO);
	}
	
	public static void printWarn(String msg) {
		print(msg, LogLevel.WARN);
	}
	
	public static void printError(String msg) {
		print(msg, LogLevel.ERROR);
	}
	
	public static void printDebug(String title, String content) {
		print(title, LogLevel.DEBUG);
		System.out.println(content);
		System.out.print("------------------------------------");
	}
	
	private static void print(String msg, LogLevel level) {
		System.out.println();
		System.out.print(String.format(pattern, level, msg));
	}

	public static void appendLastLine(String msg) {
		System.out.print(msg);		
	}
}