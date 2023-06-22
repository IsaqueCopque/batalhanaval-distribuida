package application;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import game.Board;
import game.Position;
import game.enums.Ship;

class GameClient {
	private Socket clientSocket;	
	private DataInputStream in;
	private ObjectOutputStream out;
	Scanner scan;
	Board board;
	
	static String defaultServerIP = "127.0.0.1";
	static int defaultServerPort = 5000;
	
	
	public GameClient(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		in = new DataInputStream(clientSocket.getInputStream());
		out = new ObjectOutputStream(clientSocket.getOutputStream());
		scan = new Scanner(System.in);
		board = new Board();
	}
	
	/*
	 * Obtem do jogador as posições dos navios para enviar para o servidor
	 */
	public void initialize() throws IOException {
		ArrayList<Ship> ships = new ArrayList<Ship>();
		int done = 0;
		
		while(done <5) {
			board.printBoard();
			Ship ship = getShip();
			Position initialP, finalP;
			getPositions(ship, initialP,finalP);
			boolean placed = board.placeShip(ship, initialP, finalP);
			if(placed) done ++;
		}
		
//		ArrayList<Ship> ships = new ArrayList<Ship>(
//				Arrays.asList(Ship.BATTLESHIP,Ship.CARRIER, Ship.CRUISER, Ship.DESTROYER, Ship.SUBMARINE));
//		ArrayList<Position> initials = new ArrayList<Position>(
//				Arrays.asList(new Position(0,0),new Position(1,1),new Position(8,9),new Position(9,0),new Position(4,7)));
//		ArrayList<Position> finals = new ArrayList<Position>(
//				Arrays.asList(new Position(0,3),new Position(5,1),new Position(8,7),new Position(9,1),new Position(4,9)));
		
		boolean placed = false;
		while(!placed) {
			out.writeObject(ships);
			out.flush();
			out.writeObject(initials);
			out.flush();
			out.writeObject(finals);
			out.flush();
			placed = in.readBoolean();
		}
		
		System.out.println("Cliente --> Enviado para servidor");
	}
	
	/*
	 * Obtém do jogador o navio a ser colocado
	 */
	private Ship getShip() {
		int s =-1;
		System.out.println("(1)Porta-avião | (2)Navio-tanque | (3)Crusador | (4)Submarino | (5)Destroyer");
		System.out.println("Qual navio deseja posicionar?");
		while(s<1 || s>5) 
			s = scan.nextInt();
		s--;
		return Ship.values()[s];
	}
	
	/*
	 * Obtém do jogador a posição do navio a ser colocado
	 */
	private void getPositions(Ship ship, Position initialP, Position finalP) {
		int x = -1;
		char y = 'Z';
		int yCode = (int) y;
		System.out.print("Qual a linha da posição inicial? ");
		while(x<0 || x>9)
			x = scan.nextInt() -1;
		System.out.print("Qual a coluna da posição inicial? ");
		while(((int) y)< 65 || ((int) y)>74) 
			y =  (scan.next().toUpperCase().charAt(0));
		initialP = new Position(x,y);
		
		ArrayList<Position> posibles = board.getAvailablePositions(ship,initialP);
		
		x = -1; y = 'Z';
		System.out.println("Qual a linha da posição final? ");
		for(Position posi : posibles)System.out.print(posi.getRow()+" ");
		System.out.println();
		while(x<0 || x>9) {
			x = scan.nextInt() -1;
			//To-do: Terminar aqui de validar se a posição final consta no array de disponiveis
		}
		System.out.print("Qual a coluna da posição final? ");
		while(((int) y)< 65 || ((int) y)>74) 
			y =  (scan.next().toUpperCase().charAt(0));
		
		finalP = new Position(x,y);
		
		
		
	}
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {
		GameClient client;
		if(args.length == 2) 
			client = new GameClient(args[0],Integer.parseInt(args[1]));
		else
			client = new GameClient(defaultServerIP,defaultServerPort);
		System.out.println("Cliente --> Conectado ao servidor");
		
		client.initialize();
		
		System.out.println("Cliente --> Desligando ....");
		client.clientSocket.close();
	}
}
