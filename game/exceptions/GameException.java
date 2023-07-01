package game.exceptions;

/*
 * Exceção de jogo
 */
public class GameException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public GameException(String errorMsg) {
		super(errorMsg);
	}
}
