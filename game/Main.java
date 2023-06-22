package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import game.enums.Ship;

public class Main {

	public static void main(String[] args) {
		Board board = new Board();
		board.printBoard();
		Scanner scan = new Scanner(System.in);
		char c = (scan.next().toUpperCase().charAt(0));
		System.out.println(c);
//		int s =-1;
//		System.out.println("(1)Porta-avião | (2)Navio-tanque | (3)Crusador | (4)Submarino | (5)Destroyer");
//		System.out.println("Qual navio deseja posicionar?");
//		while(s<1 || s>5) 
//			s = scan.nextInt();
//		s--;
//		System.out.println(Ship.values()[s]);
	}
//		Match match = new Match();
//		boolean player1Ready = false;
//		boolean player2Ready = false;
//		
//		ArrayList<Ship> navios = new ArrayList<Ship>(
//				Arrays.asList(Ship.BATTLESHIP,Ship.CARRIER, Ship.CRUISER, Ship.DESTROYER, Ship.SUBMARINE));
//		
//		//Navios Player 1 ------
//		ArrayList<Position> initials1 = new ArrayList<Position>(
//				Arrays.asList(new Position(0,0),new Position(1,1),new Position(8,9),new Position(9,0),new Position(4,7)));
//		ArrayList<Position> finals1 = new ArrayList<Position>(
//				Arrays.asList(new Position(0,3),new Position(5,1),new Position(8,7),new Position(9,1),new Position(4,9)));
//		//Navios Player 2 ------
//		ArrayList<Position> initials2 = new ArrayList<Position>(
//				Arrays.asList(new Position(0,0),new Position(1,1),new Position(8,9),new Position(9,0),new Position(4,7)));
//		ArrayList<Position> finals2 = new ArrayList<Position>(
//				Arrays.asList(new Position(0,3),new Position(5,1),new Position(8,7),new Position(9,1),new Position(4,9)));
//		
//		//Coloca navios ----------
//		if(match.placeShips(true, navios, initials1, finals1))
//			player1Ready = true;
//		if(match.placeShips(false, navios, initials2, finals2))
//			player2Ready = true;
//		
//		//Tiros
//		match.shoot(new Position(0,0)); //tiro do 1
//		match.shoot(new Position(0,0));
//		
//		match.shoot(new Position(0,1)); //tiro do 1
//		match.shoot(new Position(0,1)); 
//		
//		match.shoot(new Position(0,2)); //tiro do 1
//		match.shoot(new Position(0,2)); //tiro do 1
//		
//		match.shoot(new Position(0,3)); //tiro do 1
//		match.shoot(new Position(0,3)); //tiro do 1
//		
//		match.shoot(new Position(1,1)); //tiro do 1
//		match.shoot(new Position(1,1)); //tiro do 1
//		
//		match.shoot(new Position(2,1)); //tiro do 1
//		match.shoot(new Position(2,1)); //tiro do 1
//		
//		match.shoot(new Position(3,1)); //tiro do 1
//		match.shoot(new Position(3,1)); //tiro do 1
//		
//		match.shoot(new Position(4,1)); //tiro do 1
//		match.shoot(new Position(4,1)); //tiro do 1
//		
//		match.shoot(new Position(5,1)); //tiro do 1
//		match.shoot(new Position(5,1)); //tiro do 1
//		
//		match.shoot(new Position(9,0)); //tiro do 1
//		match.shoot(new Position(9,0)); //tiro do 1
//		
//		match.shoot(new Position(9,1)); //tiro do 1
//		match.shoot(new Position(9,1)); //tiro do 1
//		
//		match.shoot(new Position(8,7)); //tiro do 1
//		match.shoot(new Position(8,7)); //tiro do 1
//		
//		match.shoot(new Position(8,8)); //tiro do 1
//		match.shoot(new Position(8,8)); //tiro do 1
//		
//		match.shoot(new Position(8,9)); //tiro do 1
//		match.shoot(new Position(8,9)); //tiro do 1
//		
//		match.shoot(new Position(4,7)); //tiro do 1
//		match.shoot(new Position(4,7)); //tiro do 1
//		
//		match.shoot(new Position(4,8)); //tiro do 1
//		match.shoot(new Position(4,8)); //tiro do 1
//		
//		if(!match.shoot(new Position(4,9))) //tiro do 1
//			match.shoot(new Position(4,9)); 
//		else{ System.out.println("\n GAME END \n");
//		match.printBoards();
//		}
//	}
}