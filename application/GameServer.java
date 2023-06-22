package application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.Match;
import game.Position;
import game.enums.Ship;
import game.exceptions.GameException;

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
	private void receiveShips() throws IOException, ClassNotFoundException {
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
	 * Começa e executa partida até o final 
	 * Retorna para o cliente:
	 * Primeiro a jogar (boolean), Partida chegou ao fim (boolean),
	 * Ataque realizado (boolean), Tabuleiro(Board), TabuleiroAtaque(Board)
	 * Motivo(String) se ataque não realizado
	 */
	private void startMatch() throws ClassNotFoundException, IOException {
		boolean matchEnded = false;
		Position attackPosition;
		ObjectOutputStream outPlayer1 = new ObjectOutputStream(player1.getOutputStream());
		ObjectOutputStream outPlayer2 = new ObjectOutputStream(player2.getOutputStream());
		
		//Informa para o player 1 que ele é o primeiro a jogar e para o 2 aguardar
		outPlayer1.writeObject(Boolean.TRUE);
		outPlayer2.writeObject(Boolean.FALSE);
		outPlayer1.flush();
		outPlayer2.flush();
		
		while(! matchEnded) {
			attackPosition = (Position) in.readObject(); //Recebe posição de ataque
			try {
				matchEnded = match.shoot(attackPosition); //Realiza tiro
				//Se tiro não falhou
				//Informa se partida chegou ao fim
				outPlayer1.writeObject(Boolean.valueOf(matchEnded));
				outPlayer2.writeObject(Boolean.valueOf(matchEnded));
				outPlayer1.flush();
				outPlayer2.flush();
				//Informa que o ataque foi realizado para o jogador do turno
				if(match.isPlayer1Turn()) {
					outPlayer1.writeObject(Boolean.TRUE);
					outPlayer1.flush();
				}else {
					outPlayer2.writeObject(Boolean.TRUE);
					outPlayer2.flush();
				}
				//Devolve tabuleiro
				outPlayer1.writeObject(match.getBoard1());
				outPlayer2.writeObject(match.getBoard2());
				outPlayer1.flush();
				outPlayer2.flush();
				//Devolve tabuleiro de ataque
				outPlayer1.writeObject(match.getAttackBoard1());
				outPlayer2.writeObject(match.getAttackBoard2());
				outPlayer1.flush();
				outPlayer2.flush();
			}catch(GameException e) {// Se atirou em posição inválida
				matchEnded = false;
				if(match.isPlayer1Turn()) {
					outPlayer1.writeObject(Boolean.FALSE); //Informa que partida não chegou ao fim
					outPlayer1.flush();
					outPlayer1.writeObject(Boolean.FALSE); //Informa que o ataque não foi realizado
					outPlayer1.flush();
					outPlayer1.writeObject(e.getMessage());//Devolve o motivo do ataque não ter sido realizado
					outPlayer1.flush();
				}else {
					outPlayer2.writeObject(Boolean.FALSE);
					outPlayer2.flush();
					outPlayer2.writeObject(Boolean.FALSE);
					outPlayer2.flush();
					outPlayer2.writeObject(e.getMessage());
					outPlayer2.flush();
				}
			}
		}
		
		String winnerMsg = "Fim da partida. Você destruiu os navios inimigos!";
		String looserMsg = "Fim da partida. Seus navios foram destruídos!";
		if(match.getPlayer1Points() == 0) {
			outPlayer1.writeObject(looserMsg);
			outPlayer1.flush();
			outPlayer2.writeObject(winnerMsg);
			outPlayer2.flush();
		}else {
			outPlayer1.writeObject(winnerMsg);
			outPlayer1.flush();
			outPlayer2.writeObject(looserMsg);
			outPlayer2.flush();
		}
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
		System.out.println("Iniciando partida");
		startMatch();
		System.out.println("Encerrando servidor ...");
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