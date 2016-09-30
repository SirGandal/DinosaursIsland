package server;

import java.io.IOException;
import java.net.*;

/**
 * @author  gas12n
 */
public class ClientSocketHandler extends Thread {

	private Socket socket;

	// Server s = new Server();
	/**
	 * @uml.property  name="game"
	 * @uml.associationEnd  
	 */
	private Game game;
	/**
	 * @uml.property  name="ssc"
	 * @uml.associationEnd  
	 */
	private ServerSocketComunicator ssc;

	public void changeRound(String currentUser) {
		ssc.changeRound(currentUser);
	}

	/**
	 * @param socket
	 * @uml.property  name="socket"
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return
	 * @uml.property  name="socket"
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param game
	 * @uml.property  name="game"
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return
	 * @uml.property  name="game"
	 */
	public Game getGame() {
		return game;
	}
/**
 * Il metodo istanzia il ServerComunicator come di tipo socket e passa
		 * al costruttore di ssc socket e game
 * @param socket Il socket univoco per l'utente
 * @param game Il game univoco per l'inter partita
 */
	public ClientSocketHandler(Socket socket, Game game) {
		
		super();
		this.setSocket(socket);
		this.setGame(game);
		try {
			ssc = new ServerSocketComunicator(socket, game);
		} catch (IOException e) {

		}
	}

	@Override
	public void run() {
		ssc.start();
	}

}