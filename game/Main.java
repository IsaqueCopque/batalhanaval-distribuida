package game;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Match match = new Match();
		match.prePlaceShip(false, 0, new Position(0,0));
		match.prePlaceShip(false, 0, new Position(0,9));
		match.prePlaceShip(false, 0, new Position(9,0));
		match.prePlaceShip(false, 0, new Position(9,9));
	}
}
