package game.enums;

/*
 * Status de uma posição 
 */
public enum PositionStatus {
	FOG(0),
	WATER(1),
	CARRIER(2),//Porta-avião
	BATTLESHIP(3), //Navio-tanque
	CRUISER(4), //Crusador 
	SUBMARINE(5),//Submarino
	DESTROYER(6), //Destroyer
	HIT(7),		//Atirou
	HITWATER(8); //Atirou na água (para o tabuleiro do jogador)
	
	
	private int status;
	
	PositionStatus(int status){
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	//Retorna o status da posição dado um navio
	public static int getStatusShipStatus(Ship ship) {
		for(PositionStatus p : PositionStatus.values()) {
			if(ship.name() == p.name())
				return p.status;
		}
		return -1;
	}
}

