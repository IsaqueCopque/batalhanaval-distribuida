package game;

import java.io.Serializable;

import game.enums.PositionStatus;

/*
 * Classe que representa uma posição de tabuleiro
 */
public class Position implements Serializable {
	private static final long serialVersionUID = 1L;
	private int row; //0-9
	private int column; //0-9
	private int status;
	
	
	public Position(int row, int column){
		this.row =row;
		this.column = column;
		this.status = PositionStatus.WATER.getStatus();
	}
	
	public Position(int row, char column){
		this.row = row;
		this.column = parseColumn(column);
		this.status = PositionStatus.WATER.getStatus();
	}
	
	public static int parseColumn(char column) {
		int charCode = (int) Character.toUpperCase(column);
		if(charCode < 65 || charCode >74) //A-J
			throw new IllegalArgumentException();
		return charCode - 65;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public int getStatus() {
		return status;
	}
	
	public boolean isValid() {
		return row < 10 && row > -1 && column < 10 & column > -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		Position posi = (Position) obj;
		if(this.column == posi.column && this.row == posi.row)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "("+(row+1)+"-"+(char)(column+65)+")";
	}
	
	public String printPosition() {
		return toString() + status;
	}
}
