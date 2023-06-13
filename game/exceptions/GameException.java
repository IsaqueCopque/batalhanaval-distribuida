package game.exceptions;

public class GameException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public GameException(String errorMsg) {
		super(errorMsg);
	}
}
