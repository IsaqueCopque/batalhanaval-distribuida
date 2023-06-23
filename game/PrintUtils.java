package game;

public abstract class PrintUtils {
	public static final String BLUE = "\u001B[34m";
	public static final String BLUEBG = "\u001B[44m";
	public static final String WHITE = "\u001B[37m";
	public static final String WHITEBG = "\u001B[47m";
	public static final String RED = "\u001B[31m";
	public static final String REDBG = "\u001B[41m";
	public static final String ANSI_RESET = "\u001B[0m";
	
	public static String toCollor(String txt, String collor, String bg) {
		String collored = txt;
		if(collor != null)
			collored = collor+collored;
		if(bg != null)
			collored = bg+collored;
		if(bg != null || collor != null)
			collored = collored+ANSI_RESET;
		return collored;
	}
}