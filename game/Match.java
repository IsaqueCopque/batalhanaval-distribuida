package game;

import java.util.ArrayList;

import game.enums.Ships;

public class Match {

	private Board board1;
	private Board board2;
	boolean player1Move;  //indica se o turno é do player 1
	boolean player1Ready; //indica se o player1 posicionou os navios
	boolean player2Ready; //indica se o player2 posicionou os navios
	int player1Points;    //pontos restantes do player1
	int player2Points;	  //pontos restantes do player2
	
	public Match(){
		board1 = new Board();
		board2 = new Board();
		player1Move = true;
		player1Ready = false;
		player2Ready = false;
		player1Points = 17;
		player2Points = 17;
	}
	
	/*
	 * Retorna as posições possíveis para se colocar um navio
	 * a partir de uma posição inicial
	 */
	public ArrayList<Position> prePlaceShip(boolean player1, int ship, Position posi) {
		if(player1)
			return board1.getAvailablePositions(ship, posi);
		else return board2.getAvailablePositions(ship, posi);
	}
	
	/*
	 * Posiciona um navio
	 */
	public boolean placeShip(boolean player1, Ships ship, Position posi1, Position posi2) {
		if(player1)
			return board1.placeShip(ship, posi1, posi2);
		return board2.placeShip(ship, posi1, posi2);
	}
	
	public void printBoards() {
		System.out.println("Board Player 1");
		board1.printBoard();
		System.out.println("Board Player 2");
		board2.printBoard();
	}
	
}