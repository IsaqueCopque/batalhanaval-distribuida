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
	private ObjectInputStream in1;
	private ObjectInputStream in2;
	private ObjectOutputStream out1;
	private ObjectOutputStream out2;
	
	public GameServer(int port) throws IOException {
		server = new ServerSocket(port);
		player1 = null;
		player2 = null;
		in1 = null;
		in2 = null;
		out1 = null;
		out2 = null;
	}
	
	
	/*
	 * Escreve objeto no socket do jogador indicado.
	 */
	public void writeObject(Object obj,boolean player1) throws IOException {
		if(player1) {
			out1.writeObject(obj);
			out1.flush();
		}else {
			out2.writeObject(obj);
			out2.flush();
		}
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

		
		System.out.println("Recebendo navios do player 1 ...");
		
		while(player1.getInputStream().available() < 1) {}//espera receber dados
		in1 = new ObjectInputStream(player1.getInputStream());
		
		while(true) {
			ships = (ArrayList<Ship>) in1.readObject(); //Recebe navios
			initialPositions = (ArrayList<Position>) in1.readObject(); //Recebe Posições iniciais
			finalPositions = (ArrayList<Position>) in1.readObject(); //Recebe Posições finais
			
			boolean placed = match.placeShips(true, ships, initialPositions, finalPositions);
			writeObject(Boolean.valueOf(placed), true);
			
			if(placed) {
				System.out.println("Recebido navios do player 1.");
				break;
			}else //Se não foi possível posicionar os navios, recebe novamente
				System.out.println("Problema ao receber navios do player 1 .");
		}
		
		System.out.println("Recebendo navios do player 2 ...");
		
		while(player2.getInputStream().available() < 1){}
		in2 = new ObjectInputStream(player2.getInputStream());//espera receber dados
		
		while(true) {
			ships = (ArrayList<Ship>) in2.readObject();//Recebe navios
			initialPositions = (ArrayList<Position>) in2.readObject(); //Recebe Posições iniciais
			finalPositions = (ArrayList<Position>) in2.readObject(); //Recebe Posições finais
			
			boolean placed = match.placeShips(false, ships, initialPositions, finalPositions);
			writeObject(Boolean.valueOf(placed), false);
			
			if(placed) {
				System.out.println("Recebido navios do player 2.");
				break;
			}else //Se não foi possível posicionar os navios, recebe novamente
				System.out.println("Problema ao receber navios do player 2 .");
		}
		
		System.out.println("Recebido navios de ambos jogadores");
		match.printBoards();
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
		
		//Informa para o player 1 que ele é o primeiro a jogar e para o 2 aguardar
		writeObject(Boolean.TRUE,true);
		writeObject(Boolean.FALSE,false);
		
		while(! matchEnded) {
			if(match.isPlayer1Turn())
				attackPosition = (Position) in1.readObject(); //Recebe posição de ataque
			else
				attackPosition = (Position) in2.readObject(); //Recebe posição de ataque
			try {
				matchEnded = match.shoot(attackPosition); //Realiza tiro
				//Se tiro não falhou
				//Informa se partida chegou ao fim
				writeObject(Boolean.valueOf(matchEnded),true);
				writeObject(Boolean.valueOf(matchEnded),false);
				//Informa que o ataque foi realizado para o jogador do turno
				if(match.isPlayer1Turn()) writeObject(Boolean.TRUE,true);
				else writeObject(Boolean.TRUE,false);
				//Devolve tabuleiro
				writeObject(match.getBoard1(),true);
				writeObject(match.getBoard2(),false);
				//Devolve tabuleiro de ataque
				writeObject(match.getAttackBoard1(),true);
				writeObject(match.getAttackBoard2(),false);
			}catch(GameException e) {// Se atirou em posição inválida
				matchEnded = false;
				if(match.isPlayer1Turn()) {
					writeObject(Boolean.FALSE,true); //Informa que partida não chegou ao fim
					writeObject(Boolean.FALSE,true); //Informa que o ataque não foi realizado
					writeObject(e.getMessage(),true);//Devolve o motivo do ataque não ter sido realizado
				}else {
					writeObject(Boolean.FALSE,false);
					writeObject(Boolean.FALSE,false);
					writeObject(e.getMessage(),false);
				}
			}
		}
		
		String winnerMsg = "Fim da partida. Você destruiu os navios inimigos!";
		String looserMsg = "Fim da partida. Seus navios foram destruídos!";
		if(match.getPlayer1Points() == 0) {
			writeObject(looserMsg,true);
			writeObject(winnerMsg,false);
		}else {
			writeObject(winnerMsg,true);
			writeObject(looserMsg,false);
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
		
		out1 = new ObjectOutputStream(player1.getOutputStream());
		out2 = new ObjectOutputStream(player2.getOutputStream());
		
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