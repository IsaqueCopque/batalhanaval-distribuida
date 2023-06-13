package game.enums;

public enum Ship {
	CARRIER(5),//Porta-avião 5
	BATTLESHIP(4), //Navio-tanque 4
	CRUISER(3), //Crusador 3
	SUBMARINE(3),//Submarino 3
	DESTROYER(2); //Destroyer 2
	
	private int size;
	
	Ship(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}