package game;

import java.util.ArrayList;

import game.enums.PositionStatus;
import game.enums.Ships;

public class Board {
	private int[][] board;
	
	public Board() {
		board = new int[10][10];
	}
	
	/*
	 * Posiciona navio no tabuleiro
	 */
	public boolean placeShip(Ships ship, Position posi1, Position posi2) {
		ArrayList<Position> positions = getAvailablePositions(ship.ordinal(), posi1);
		if(positions.contains(posi2)) {
			int dx = 0, dy = 0; //direção de posicionamento
			
			if(posi1.getRow()>posi2.getRow()) //cima
				dx = -1;
			else if(posi1.getRow()<posi2.getRow())//baixo
				dx = 1;
			else if(posi1.getColumn()>posi2.getColumn())//direita
				dy = 1;
			else //esquerda
				dy = -1;
				
			/* Reminder: fazer match do ship com position status
			for(int i = 0; i<ship.getSize();i++) {
				board[ posi1.getRow()+(i*dx)][posi1.getColumn()+(i*dy)] = 
						PositionStatus.
			}*/
		}
		return false;
	}
	
	/*
	 * Retorna as posições possíveis de se colocar um navio a partir de uma
	 * posição inicial
	 */
	public ArrayList<Position> getAvailablePositions(int ship, Position posi) {
		switch(ship) {
		//Mudar para o enum
			case 0: //Carrier
				return searchNeighborhood(Ships.CARRIER.getSize(),posi);
			case 1: //BATTLESHIP
				return searchNeighborhood(Ships.BATTLESHIP.getSize(), posi);
			case 2: //CRUISER
				return searchNeighborhood(Ships.CRUISER.getSize(),posi);
			case 3: //Submarine
				return searchNeighborhood(Ships.SUBMARINE.getSize(), posi);
			case 4: //Destroyer
				return searchNeighborhood(Ships.DESTROYER.getSize(), posi);
		}
		return null;
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
		if(board[row][column] != 0)
			return false;
		return true;
	}
	
	public void printBoard() {
		System.out.println("\n---------------------");
		for(int i = 0; i< 10;i++) {
			for(int j =0;j<10;j++) {
				System.out.print("|"+board[i][j]);	
			}
			System.out.println("|");
		}
		System.out.println("---------------------\n");
	}
	
}