package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import shared.ChangeRoundRMI;

/**
 * Classe che si occupa di gestire le connessioni con i client sia socket che rmi.
 */
public class Server {

	private ArrayList<ClientSocketHandler> clientsConnected = new ArrayList<ClientSocketHandler>();
	private boolean loop = true;
	private ServerSocket serverSocket;
	int port = 1288;
	int RMIport = 8812;
	/**
	 * @return
	 * @uml.property  name="game"
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Metodo che serve a porre il booleano che fa girare il while, teoricamente infinito, a false e permettere cosi' di chiudere il server.
	 * @param loop  Booleano che permette di fare girare il while fintantoche' non viene settato a false.
	 * @uml.property  name="loop"
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
		try {  
			if(serverSocket!=null){
				serverSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private ArrayList<ChangeRoundRMI> clientsRmiConnected = new ArrayList<ChangeRoundRMI>();
	/**
	 * @uml.property  name="game"
	 * @uml.associationEnd  
	 */
	private Game game;
	
	/**
	 * Permette di gestire il cambio del turno.
	 * @param currentUser L'utente corrente per il quale notifiare il cambio turno.
	 */
	public void changeRound(String currentUser) {
		synchronized (clientsConnected) {
			ClientSocketHandler ch;

			@SuppressWarnings("rawtypes")
			Iterator i = clientsConnected.iterator();

			while (i.hasNext()) {
				ch = (ClientSocketHandler) i.next();

				// ch.changeRound(currentUser);

				if (ch.isAlive()) {
					ch.changeRound(currentUser);
				} else {
					i.remove();
				}

			}
		}

		synchronized (clientsRmiConnected) {
			for (ChangeRoundRMI c : clientsRmiConnected) {
				try {
					c.changeRound(currentUser);
				} catch (RemoteException e) {
				}
			}
		}
	}
	
	/**
	 * @param port
	 * @uml.property  name="port"
	 */
	public void setPort(int port) {
		this.port = port;
	}

	
	/**
	 * @param rMIport
	 * @uml.property  name="rMIport"
	 */
	public void setRMIport(int rMIport) {
		RMIport = rMIport;
	}

	public void run() {
		Socket socket;
		// ServerSocketComunicator serverSockCom;

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			game = new Game(this);

			new ServerRMIComunicator(game,
					clientsRmiConnected, RMIport);

			while (loop) {
				System.out.println("Waiting for connection...");
				socket = serverSocket.accept();
				//System.out.println("Connection started");
				ClientSocketHandler clientHandler = new ClientSocketHandler(
						socket, game);

				synchronized (clientsConnected) {
					clientsConnected.add(clientHandler);
				}

				clientHandler.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
