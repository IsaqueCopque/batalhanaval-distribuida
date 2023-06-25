package game;

import java.io.Serializable;
import java.util.ArrayList;

import game.enums.PositionStatus;
import game.enums.Ship;

public class Board implements Serializable{
	private static final long serialVersionUID = 1L;
	private int[][] board;
	
	public Board() {
		board = new int[10][10];
	}
	
	public Board(Board other) {
		board = new int[10][10];
		for(int i =0; i<10;i++) 
			for(int j=0;j<10;j++)
				board[i][j] = other.board[i][j];
	}
	
	//Construtor para tabuleiro do jogador, sem fog apenas água se true
	public Board(boolean water) {
		board = new int [10] [10];
		if(water) {
			for(int i = 0; i<10;i++)
				for(int j = 0; j<10;j++)
					board[i][j] = PositionStatus.WATER.getStatus();
		}
	}
	
	/*
	 * Posiciona navio no tabuleiro. True se o navio foi colocado.
	 */
	public boolean placeShip(Ship ship, Position posi1, Position posi2) {
		ArrayList<Position> positions = getAvailablePositions(ship, posi1);
		if(positions.contains(posi2)) {
			int dx = 0, dy = 0; //direção de posicionamento
			
			if(posi1.getRow()>posi2.getRow()) //cima
				dx = -1;
			else if(posi1.getRow()<posi2.getRow())//baixo
				dx = 1;
			else if(posi1.getColumn()<posi2.getColumn())//direita
				dy = 1;
			else //esquerda
				dy = -1;
				
			for(int i = 0; i<ship.getSize();i++) {
				board[ posi1.getRow()+(i*dx)][posi1.getColumn()+(i*dy)] = 
						PositionStatus.getStatusShipStatus(ship); //fazer match do ship com position status
			}
			return true;
		}
		return false;
	}
	
	/*
	 * Retorna as posições possíveis de se colocar um navio a partir de uma
	 * posição inicial
	 */
	public ArrayList<Position> getAvailablePositions(Ship ship, Position posi) {
		switch(ship) {
			case CARRIER: //Carrier
				return searchNeighborhood(Ship.CARRIER.getSize(),posi);
			case BATTLESHIP: //BATTLESHIP
				return searchNeighborhood(Ship.BATTLESHIP.getSize(), posi);
			case CRUISER: //CRUISER
				return searchNeighborhood(Ship.CRUISER.getSize(),posi);
			case SUBMARINE: //Submarine
				return searchNeighborhood(Ship.SUBMARINE.getSize(), posi);
			case DESTROYER: //Destroyer
				return searchNeighborhood(Ship.DESTROYER.getSize(), posi);
		}
		return null;
	}
	
	/*
	 * Retorna posição do tabuleiro
	 */
	public int getPosition(Position posi) {
		return board[posi.getRow()][posi.getColumn()];
	}
	
	/*
	 * Define status de uma posição
	 */
	public void setPosition(Position posi, PositionStatus status) {
		board[posi.getRow()][posi.getColumn()] = status.getStatus();
	}
	
	/*
	 * Coloca todas posições do tabuleiro como água
	 */
	public void clear() {
		for(int i = 0; i<10;i++)
			for(int j = 0; j<10;j++)
				board[i][j] = PositionStatus.WATER.getStatus();
	}
	
	/*
	 * Vasculha vizinhança da posição para verificar se é possível
	 * posicionar um navio
	 */
	private ArrayList<Position> searchNeighborhood(int size, Position posi) {
		ArrayList<Position> positions = new ArrayList<Position>();
		//Cima
		int i;
		for(i = 0; i<size; i++)
			if(!isAvailableCoordinate(posi.getRow()-i,posi.getColumn()))
				break;
		if(i == size)
			positions.add( new Position(posi.getRow()-(size-1), posi.getColumn()) );
		//Direita
		for(i = 0; i<size; i++)
			if(!isAvailableCoordinate(posi.getRow(),posi.getColumn()+i))
				break;
		if(i == size)
			positions.add( new Position(posi.getRow(), posi.getColumn()+(size-1)) );
		//Baixo
		for(i = 0; i<size; i++)
			if(!isAvailableCoordinate(posi.getRow()+i,posi.getColumn()))
				break;
		if(i == size)
			positions.add( new Position(posi.getRow()+(size-1), posi.getColumn()) );
		//Esquerda
		for(i = 0; i<size; i++)
			if(!isAvailableCoordinate(posi.getRow(),posi.getColumn()-i))
				break;
		if(i == size)
			positions.add( new Position(posi.getRow(), posi.getColumn()-(size-1)) );
		
		return positions;
	}
	
	/*
	 * Retorna se uma coordenada está disponível para se colocar um navio
	 */
	public boolean isAvailableCoordinate(int row, int column) {
		if(row<0 || row>9 || column<0 || column>9)
			return false;
		if(board[row][column] != PositionStatus.WATER.getStatus())
			if(board[row][column] != PositionStatus.FOG.getStatus())
				return false;
		return true;
	}
	
	/*
	 * Retorna uma linha do tabuleiro
	 */
	public String getStringBoardLine(int i) {
		if(i<-1 || i > 9) 
			return null;
		if(i == -1) 
			return "   |A|B|C|D|E|F|G|H|I|J|";
		String line = "";
		int j;
		line = line.concat((i+1)+"");
		if(i != 9) line = line.concat("  |");
		else line = line.concat(" |");
		for(j = 0; j<10; j++) {
			line = line.concat(board[i][j] + "|");
		}
		return line;
	}
	
	/*
	 * Imprime todo tabuleiro
	 */
	public void printBoard() {
		System.out.println("\n------------------------"); 
		System.out.println("   |A|B|C|D|E|F|G|H|I|J|");
		for(int i = 0; i< 10;i++) {
			System.out.print((i+1)+ " ");
			if(i != 9)System.out.print(" ");
			for(int j =0;j<10;j++) {
				System.out.print("|"+board[i][j]);	
			}
			System.out.println("|");
		}
		System.out.println("------------------------\n");
	}
	
}