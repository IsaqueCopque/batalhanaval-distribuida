package game;

import java.util.ArrayList;

import game.enums.PositionStatus;
import game.enums.Ship;
import game.exceptions.GameException;

/*
 * Classe que representa uma partida do jogo
 */
public class Match {

	private Board board1;		//tabuleiro do player 1
	private Board attackBoard1; //tabuleiro de ataque do player 1
	private Board board2;		//tabulerio do player 2
	private Board attackBoard2; //tabuleiro de ataque do player 2
	private boolean player1Move;  //indica se o turno é do player 1
	private int player1Points;    //pontos restantes do player1
	private int player2Points;	  //pontos restantes do player2
	
	public Match(){
		board1 = new Board(true);
		attackBoard1 = new Board();
		board2 = new Board(true);
		attackBoard2 = new Board();
		player1Move = true;
		player1Points = 17;
		player2Points = 17;
	}
	
	/*
	 * Retorna as posições possíveis para se colocar um navio
	 * a partir de uma posição inicial
	 */
	public ArrayList<Position> prePlaceShip(boolean player1, Ship ship, Position posi) {
		if(player1)
			return board1.getAvailablePositions(ship, posi);
		else return board2.getAvailablePositions(ship, posi);
	}
	
	/*
	 * Recebe conjunto de navios e de posições e tenta coloca-los no tabuleiro;
	 * Se houve êxito, retorna que o jogador está pronto para iniciar a partida
	 */
	public boolean placeShips(boolean player1, ArrayList<Ship> ships, 
		ArrayList<Position> initialPositions, ArrayList<Position> finalPositions) {
		
		if(ships.size() != 5 || initialPositions.size() != 5 || finalPositions.size() != 5 )
			return false; //faltam parâmetros
		
		boolean placed;
		for(int i = 0; i<5; i++) {
			if(player1) {
				placed = board1.placeShip(ships.get(i), initialPositions.get(i), finalPositions.get(i));
				if(!placed) { //se houver erro em posicionar um navio,limpa todo tabuleiro
					board1.clear();
					return false;
				}
			}else {
				placed = board2.placeShip(ships.get(i), initialPositions.get(i), finalPositions.get(i));
				if(!placed) { //se houver erro em posicionar um navio,limpa todo tabuleiro
					board2.clear();
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Atira no inimigo e troca o turno. Retorna se os pontos de vida do inimigo acabaram.
	 */
	public boolean shoot(Position attackPosition) {
		if(attackPosition.isValid()) {
			if(player1Move) { //Tiro do player 1
				int positionAttackValue = attackBoard1.getPosition(attackPosition); //no tab de ataque
				int positionEnemyValue = board2.getPosition(attackPosition); //no tab do oponente
				if(positionAttackValue != PositionStatus.FOG.getStatus()) 
					throw new GameException("Posição já atacada: " + attackPosition);
				if(positionEnemyValue != PositionStatus.WATER.getStatus()) { //Acertou
					player2Points -= 1;
					attackBoard1.setPosition(attackPosition, PositionStatus.HIT);
					board2.setPosition(attackPosition, PositionStatus.HIT);
				}else { //Errou
					attackBoard1.setPosition(attackPosition, PositionStatus.WATER);
					board2.setPosition(attackPosition, PositionStatus.HITWATER);
				}
				player1Move = !player1Move;
				if(player2Points == 0) return true;
			}
			
			else {//Tiro do player 2
				int positionAttackValue = attackBoard2.getPosition(attackPosition); //no tab de ataque
				int positionEnemyValue = board1.getPosition(attackPosition); //no tab do oponente
				if(positionAttackValue != PositionStatus.FOG.getStatus()) 
					throw new GameException("Posição já atacada: " + attackPosition);
				if(positionEnemyValue != PositionStatus.WATER.getStatus()) { //Acertou
					player1Points -= 1;
					attackBoard2.setPosition(attackPosition, PositionStatus.HIT);
					board1.setPosition(attackPosition, PositionStatus.HIT);
				}else { //Errou
					attackBoard2.setPosition(attackPosition, PositionStatus.WATER);
					board1.setPosition(attackPosition, PositionStatus.HITWATER);
				}
				player1Move = !player1Move;
				if(player1Points == 0) return true;
			}
			return false;
		}
		else throw new GameException("Posição inválida para ataque:" + attackPosition);
	}
	
	/*
	 * Retorna se o turno é do jogador 1
	 */
	public boolean isPlayer1Turn() {
		return player1Move;
	}
	
	/*
	 * Printa tabuleiros para debug
	 */
	public void debugBoards() {
		System.out.println("Board Player 1");
		board1.printBoard();
		System.out.println("Attack Board Player 1");
		attackBoard1.printBoard();
		System.out.println("Board Player 2");
		board2.printBoard();
		System.out.println("Attack Board Player 2");
		attackBoard2.printBoard();
	}

	public Board getBoard1() {
		return board1;
	}
	public Board getAttackBoard1() {
		return attackBoard1;
	}
	public Board getBoard2() {
		return board2;
	}
	public Board getAttackBoard2() {
		return attackBoard2;
	}
	public int getPlayer1Points() {
		return player1Points;
	}
	public int getPlayer2Points() {
		return player2Points;
	}
}