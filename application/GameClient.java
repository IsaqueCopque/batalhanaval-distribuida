package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import game.Board;
import game.Position;
import game.enums.Ship;

class GameClient {
	private Socket clientSocket;	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	Scanner scan;
	Board board, attackBoard;
	
	static String defaultServerIP = "127.0.0.1";
	static int defaultServerPort = 5000;
	
	
	public GameClient(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		in = new ObjectInputStream(clientSocket.getInputStream());
		out = new ObjectOutputStream(clientSocket.getOutputStream());
		scan = new Scanner(System.in);
		board = new Board();
		attackBoard = null;
	}
	
	/*
	 * Executa a partida até o final
	 */
	public void startMatch() throws IOException, ClassNotFoundException {
		System.out.println("Cliente --> Iniciando partida.");
		boolean myTurn = in.readBoolean(); //recebe se é o primeiro a jogar
		boolean matchEnded = false, attackRealized;
		Position attackPosition = null;
		
		while(!matchEnded) {
			if(!myTurn) { // aguarda jogada do adversário
				System.out.println("Aguardando jogada do adversário ...");
				matchEnded = in.readBoolean();
				board = (Board) in.readObject();
				attackBoard = (Board) in.readObject();
				myTurn = true;
				System.out.println("___      ___     ___     ___");
				board.printBoard();
				System.out.println("\n___ Tabuleiro de ataque ___");
				attackBoard.printBoard();
			}else { //realiza ataque
				System.out.println("Seu turno. Escolha uma posição para atirar ...");
				attackPosition = getAttackPosition();
				out.writeObject(attackPosition); //envia posição
				out.flush();
				matchEnded = in.readBoolean();
				attackRealized = in.readBoolean();
				if(!attackRealized) { //tiro inválido
					String reason = (String) in.readObject();
					System.out.println("Ataque não realizado: "+reason);
				}else {
					board = (Board) in.readObject();
					attackBoard = (Board) in.readObject();
					System.out.println("___      ___     ___     ___");
					board.printBoard();
					System.out.println("\n___ Tabuleiro de ataque ___");
					attackBoard.printBoard();
				}
			}
		}
		
		//Fim de partida
		String msg = (String) in.readObject();
		System.out.println(msg);
		
		in.close();
		out.close();
	}
	
	
	/*
	 * Envia para o servidor as posições dos navios e inicia partida.
	 */
	public void initialize() throws IOException, ClassNotFoundException {
		ArrayList<Ship> ships = new ArrayList<Ship>();
		ArrayList<Position> initialPositions = new ArrayList<Position>();
		ArrayList<Position> finalPositions =  new ArrayList<Position>();
		Position initialP = null, finalP = null;
		int done = 0;
		
		System.out.println("Recebendo navios");
		while(done <5) { //Coleta os 5 navios e suas posições
			board.printBoard();
		
			Ship ship = getShip(ships);
			Position[] positions = getPositions(ship);
			initialP = positions[0];
			finalP = positions[1];
			
			boolean placed = board.placeShip(ship, initialP, finalP);
			if(placed) {
				ships.add(ship);
				initialPositions.add(initialP);
				finalPositions.add(finalP);
				done ++;
			}
		}
				
		boolean placedInServer = false; //resposta do servidor se foi possível posicionar
		
		while(!placedInServer) { //Envia os dados para o servidor
			out.writeObject(ships);
			out.flush();
			out.writeObject(initialPositions);
			out.flush();
			out.writeObject(finalPositions);
			out.flush();
			placedInServer = in.readBoolean(); 
		}
		
		System.out.println("Cliente --> Enviado dados. Servidor Posicionou navios.");
		startMatch();
	}
	
	/*
	 * Obtém do jogador o navio a ser colocado
	 * alreadyPlaced: navios já posicionados
	 */
	private Ship getShip(ArrayList<Ship> alreadyPlaced) {
		int s =-1, selected = -1;
		System.out.println("Qual navio deseja posicionar?");
		for(Ship ship : Ship.values()) {
			if(!alreadyPlaced.contains(ship)) { //Mostrar apenas ainda não colocado
				if(ship == Ship.CARRIER)
					System.out.print("(1)Porta-avião | ");
				else if(ship == Ship.BATTLESHIP)
					System.out.print("(2)Navio-tanque | ");
				else if(ship == Ship.CRUISER)
					System.out.print("(3)Crusador | ");
				else if(ship == Ship.SUBMARINE)
					System.out.print("(4)Submarino | ");
				else if(ship == Ship.DESTROYER)
					System.out.print("(5)Destroyer | ");
			}
		}
		while(s<1 || s>5) {
			s = scan.nextInt();
			selected = s-1;
			if(alreadyPlaced.contains(Ship.values()[selected]))//se passou valor do que já foi colocado
				s = -1;
		}
		return Ship.values()[selected];
	}
	
	/*
	 * Obtém do jogador a posição do navio a ser colocado
	 */
	private Position getAttackPosition() {
		int x = -1;
		char y = 'Z';
		Position attackPosition = null;
		while(true) {
			System.out.print("Qual a linha da posição a atacar? ");
			while(x<0 || x>9)
				x = scan.nextInt() -1;
			System.out.print("Qual a coluna da posição a atacar? ");
			while(((int) y)< 65 || ((int) y)>74) 
				y =  (scan.next().toUpperCase().charAt(0));
			attackPosition = new Position(x,y);
			if(!attackPosition.isValid()) {
				System.out.println("Posição "+attackPosition+" inválida...");
				x = -1; y = 'Z';
			}else return attackPosition;
		}
	}
	
	/*
	 * Obtém do jogador a posição do navio a ser colocado
	 */
	private Position[] getPositions(Ship ship) {
		int x = -1;
		char y = 'Z';
		boolean validPosition1 = false, validPosition2;
		Position initialP = null, finalP = null;
		ArrayList<Position> posibles;
		
		while(!validPosition1) {
			validPosition2 = false;
			System.out.print("Qual a linha da posição inicial? ");
			while(x<0 || x>9)
				x = scan.nextInt() -1;
			System.out.print("Qual a coluna da posição inicial? ");
			while(((int) y)< 65 || ((int) y)>74) 
				y =  (scan.next().toUpperCase().charAt(0));
			
			initialP = new Position(x,y);
			posibles = board.getAvailablePositions(ship,initialP);
			
			if(posibles.size() > 0) { //Posição inicial válida
				int i;
				while(!validPosition2) {
					x = -1;
					System.out.println("Qual a posição final? ");

					for(i = 0; i< posibles.size(); i++) 
						System.out.print("("+(i+1)+"): "+posibles.get(i)+" |");						
					System.out.println("("+(i+1)+"): Mudar posição inicial");

					while(x<0 || x>posibles.size()+1)
						x = scan.nextInt() -1;
					
					if(x == posibles.size()) {//Volta para mudar posição incial
						x = -1; y = 'Z';
						validPosition2 = true;
					}else {
						Position choosed = posibles.get(x);
						finalP = new Position(choosed.getRow(),choosed.getColumn());
						validPosition2 = true;
						validPosition1 = true;
					}
				}
			} else{
				System.out.println("Posição inválida");
				x = -1; y = 'Z';
			}
		}
		return new Position[]{initialP, finalP};
	}
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, ClassNotFoundException {
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
