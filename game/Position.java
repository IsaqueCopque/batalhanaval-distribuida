package game;

import game.enums.PositionStatus;

public class Position {
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
