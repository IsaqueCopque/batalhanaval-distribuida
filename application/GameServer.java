package application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.Match;
import game.Position;
import game.enums.Ship;

class GameServer {
	
	private ServerSocket server;
	private Socket player1;
	private Socket player2;
	private Match match;
	private ObjectInputStream in;
	
	public GameServer(int port) throws IOException {
		server = new ServerSocket(port);
		player1 = null;
		player2 = null;
		in = null;
	}
	
	/*
	 * Recebe as posições dos navios passados pelos players
	 * Recebe: Navios, posição inicial dos navios, posição final dos navios 
	*/
	@SuppressWarnings("unchecked")
	public void receiveShips() throws IOException, ClassNotFoundException {
		//To-do: Colocar threads para receber navios dos players
		ArrayList<Position> initialPositions;
		ArrayList<Position> finalPositions;
		ArrayList<Ship> ships;
		DataOutputStream outPlayer1 = new DataOutputStream(player1.getOutputStream());
		DataOutputStream outPlayer2 = new DataOutputStream(player2.getOutputStream());
		
		System.out.println("Recebendo navios do player 1 ...");
		
		in = new ObjectInputStream(player1.getInputStream());
		while(true) {
			ships = (ArrayList<Ship>) in.readObject(); //Recebe navios
			initialPositions = (ArrayList<Position>) in.readObject(); //Recebe Posições iniciais
			finalPositions = (ArrayList<Position>) in.readObject(); //Recebe Posições finais
			
			boolean placed = match.placeShips(true, ships, initialPositions, finalPositions);
			outPlayer1.writeBoolean(placed);
			outPlayer1.flush();
			
			if(placed) {
				System.out.println("Recebido navios do player 1.");
				break;
			}else {//Se não foi possível posicionar os navios, recebe novamente
				System.out.println("Problema ao receber navios do player 1 .");
				initialPositions.clear();
				finalPositions.clear();
			}
		}
		
		System.out.println("Recebendo navios do player 2 ...");
		
		initialPositions.clear();
		finalPositions.clear();
		in.close();
		in = new ObjectInputStream(player2.getInputStream());
		while(true) {
			ships = (ArrayList<Ship>) in.readObject();//Recebe navios
			initialPositions = (ArrayList<Position>) in.readObject(); //Recebe Posições iniciais
			finalPositions = (ArrayList<Position>) in.readObject(); //Recebe Posições finais
			
			boolean placed = match.placeShips(false, ships, initialPositions, finalPositions);
			outPlayer2.writeBoolean(placed);
			outPlayer2.flush();
			
			if(placed) {
				System.out.println("Recebido navios do player 2.");
				break;
			}else {//Se não foi possível posicionar os navios, recebe novamente
				System.out.println("Problema ao receber navios do player 2 .");
				initialPositions.clear();
				finalPositions.clear();
			}
		}
		
		System.out.println("Recebido de ambos");
		match.printBoards();
		
		outPlayer1.close();
		outPlayer2.close();
		in.close();
	}
	
	/*
	 * Recebe conexões com os clientes e começa a partida
	*/
	public void initialize() throws IOException, ClassNotFoundException {
		System.out.println("Servidor iniciado na porta: "+server.getLocalPort());
		player1 = server.accept();
		System.out.println("Player 1 conectado: "+player1.getInetAddress().getHostAddress());
		player2 = server.accept();
		System.out.println("Player 2 conectado: "+player2.getInetAddress().getHostAddress());
		
		match = new Match();
		receiveShips();
		server.close();
	}
	
	public static void main(String[] args) {
		GameServer server;
		try {
			if(args.length > 0) 
				server = new GameServer(Integer.parseInt(args[0]));
			else 
				server = new GameServer(5000);
			server.initialize();
		}
		catch(IOException e) {System.out.println("Servidor nao inciado: "+e.getMessage());}
		catch(ClassNotFoundException e) {System.out.println("Servidor nao inciado: "+e.getMessage());}
	}
	
}