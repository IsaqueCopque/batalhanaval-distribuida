package game;

import game.enums.PositionStatus;

/*
 * Métodos auxiliares para impressão
 */
public abstract class PrintUtils {
	public static final String BLUE = "\u001B[34m";
	public static final String BLUEBG = "\u001B[44m";
	public static final String WHITE = "\u001B[37m";
	public static final String WHITEBG = "\u001B[47m";
	public static final String WHITE_BOLD = "\033[1;37m";
	public static final String RED_BOLD = "\033[1;31m";
	public static final String REDBG = "\u001B[41m";
	public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";
	public static final String BLACK_BRIGHT = "\033[0;90m";
	public static final String ANSI_RESET = "\u001B[0m";
	
	/*
	 * Retorna um texto colorido
	 */
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
	
	/*
	 * Retorna o conteúdo de uma posição do tabueleiro colorido para imprimir
	 */
	public static String positionColored(int value) {
		if(value == PositionStatus.WATER.getStatus())
			return toCollor(" ",BLUE, BLUEBG);
		if(value == PositionStatus.FOG.getStatus())
			return toCollor(" ",WHITE,WHITEBG);
		if(value == PositionStatus.CARRIER.getStatus())
			return toCollor("P",WHITE_BOLD,BLUEBG);
		if(value == PositionStatus.BATTLESHIP.getStatus())
			return toCollor("N",WHITE_BOLD,BLUEBG);
		if(value == PositionStatus.CRUISER.getStatus())
			return toCollor("C",WHITE_BOLD,BLUEBG);
		if(value == PositionStatus.SUBMARINE.getStatus())
			return toCollor("S",WHITE_BOLD,BLUEBG);
		if(value == PositionStatus.DESTROYER.getStatus())
			return toCollor("D",WHITE_BOLD,BLUEBG);
		if(value == PositionStatus.HIT.getStatus())
			return toCollor("X",RED_BOLD,REDBG);
		if(value == PositionStatus.HITWATER.getStatus())
			return toCollor("X",RED_BOLD,BLUEBG);
		return null;
	}
	
	/*
	 * Limpa o console de acordo com SO
	 */
	public static void clearConsole() {
		try  
		{  
			final String os = System.getProperty("os.name");  
			if (os.contains("Windows"))   
				Runtime.getRuntime().exec("cls");  
			else  
				Runtime.getRuntime().exec("clear");  
		}  catch (final Exception e)  {System.out.println("\n\n"); }  
	}

	public static void main(String[] args) {
		System.out.println(positionColored(0));
		System.out.println(positionColored(1));
		System.out.println(positionColored(2));
		System.out.println(positionColored(3));
		System.out.println(positionColored(4));
		System.out.println(positionColored(5));
		System.out.println(positionColored(6));
		System.out.println(positionColored(7));
		System.out.println(positionColored(8));
		clearConsole();
	}
}