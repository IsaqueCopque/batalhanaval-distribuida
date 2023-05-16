package game.enums;

public enum PositionStatus {
	FOG(0),
	WATER(1),
	CARRIER(2),//Porta-avião
	BATTLESHIP(3), //Navio-tanque
	CRUISER(4), //Crusador 
	SUBMARINE(5),//Submarino
	DESTROYER(6), //Destroyer
	HIT(7);
	
	private int status;
	
	PositionStatus(int status){
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
}

