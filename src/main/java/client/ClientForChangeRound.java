package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import GUI.WindowGame;
import shared.ChangeRoundRMI;

/**
 * @author  gas12n
 */
public class ClientForChangeRound extends UnicastRemoteObject implements
		ChangeRoundRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6702405784677390980L;

	protected ClientForChangeRound() throws RemoteException {
		super();
	}

	/**
	 * @uml.property  name="winGame"
	 * @uml.associationEnd  
	 */
	private WindowGame winGame;

	@Override
	public void changeRound(String currentUser) {
		if (winGame != null) {
			(new TaskChangeRound(currentUser, winGame)).start();
		}

	}

	public void setWindowGame(WindowGame windowGame) {
		this.winGame = windowGame;
	}

}
