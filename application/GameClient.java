package application;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import game.Position;
import game.enums.Ship;

class GameClient {
	private Socket clientSocket;	
	private DataInputStream in;
	private ObjectOutputStream out;
	
	static String defaultServerIP = "127.0.0.1";
	static int defaultServerPort = 5000;
	
	
	public GameClient(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		in = new DataInputStream(clientSocket.getInputStream());
		out = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	
	/*
	 * Obtem as posições dos navios para enviar para o servidor
	 */
	public void getPositions() throws IOException {
		ArrayList<Ship> ships = new ArrayList<Ship>(
				Arrays.asList(Ship.BATTLESHIP,Ship.CARRIER, Ship.CRUISER, Ship.DESTROYER, Ship.SUBMARINE));
		ArrayList<Position> initials = new ArrayList<Position>(
				Arrays.asList(new Position(0,0),new Position(1,1),new Position(8,9),new Position(9,0),new Position(4,7)));
		ArrayList<Position> finals = new ArrayList<Position>(
				Arrays.asList(new Position(0,3),new Position(5,1),new Position(8,7),new Position(9,1),new Position(4,9)));
		
		
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
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {
		GameClient client;
		if(args.length == 2) 
			client = new GameClient(args[0],Integer.parseInt(args[1]));
		else
			client = new GameClient(defaultServerIP,defaultServerPort);
		System.out.println("Cliente --> Conectado ao servidor");
		
		client.getPositions();
		
		System.out.println("Cliente --> Desligando ....");
		client.clientSocket.close();
	}
}
