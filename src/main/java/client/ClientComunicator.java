package client;

import java.rmi.RemoteException;
import java.util.*;

import GUI.WindowGame;

import map.Cell;

import exception.*;

/**
 * Interfaccia con tutti i metodi necessari al client per comunicare con il
 * server.
 */
public interface ClientComunicator {
	/**
	 * @see client.ClientSocketComunicator#createNewUser(String, String)
	 */
	void createNewUser(String username, String password)
			throws RemoteException, BusyUsernameException;

	void setWindowGame(WindowGame winGame);

	/**
	 * @see client.ClientSocketComunicator#logYourUser(String, String)
	 */
	String logYourUser(String username, String password)
			throws RemoteException, AutenticationFailedException,
			UserAlreadyLoggedException;

	/**
	 * @see client.ClientSocketComunicator#createNewRace(String, String, String)
	 */
	void createNewRace(String token, String raceName, String type)
			throws RemoteException, RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException;

	/**
	 * @see client.ClientSocketComunicator#gameAccess(String)
	 */
	void gameAccess(String token) throws RemoteException,
			UserAlreadyEnteredInGame, InvalidTokenException,
			TooPlayingUserException, RaceNullException;

	/**
	 * @see client.ClientSocketComunicator#gameOut(String)
	 */
	void gameOut(String token) throws RemoteException, UserAlreadyExited,
			InvalidTokenException;

	/**
	 * @see client.ClientSocketComunicator#playingList(String)
	 */
	ArrayList<String> playingList(String token) throws RemoteException,
			InvalidTokenException;

	/**
	 * @see client.ClientSocketComunicator#logOut(String)
	 */
	void logOut(String token) throws RemoteException, InvalidTokenException;

	/**
	 * @see client.ClientSocketComunicator#giveMeRanking(String)
	 */
	Object[][] giveMeRanking(String token) throws RemoteException,
			InvalidTokenException;

	/**
	 * @see client.ClientSocketComunicator#askForMap(String)
	 */
	Cell[][] askForMap(String token) throws RemoteException, InvalidTokenException,
			UserNotPlayingException;

	/**
	 * @see client.ClientSocketComunicator#askForlocalView(String, String)
	 */
	Cell[][] askForlocalView(String token, String id) throws RemoteException,
			InvalidTokenException, UserNotPlayingException, InvalidIdException;

	/**
	 * @throws InvalidIdException
	 * @see client.ClientSocketComunicator#askForList(String)
	 */
	String[] askForList(String token) throws RemoteException,
			InvalidTokenException, UserNotPlayingException, InvalidIdException;

	/**
	 * @see client.ClientSocketComunicator#askForStatus(String, String)
	 */
	String[] askForStatus(String token, String id) throws RemoteException,
			UserNotPlayingException, InvalidTokenException, InvalidIdException;

	/**
	 * @see client.ClientSocketComunicator#moveDinosaur(String, String, int,
	 *      int)
	 */
	void moveDinosaur(String token, String id, int x, int y)
			throws RemoteException, YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException;

	/**
	 * @see client.ClientSocketComunicator#letYourDinosaurGrow(String, String)
	 */
	void letYourDinosaurGrow(String token, String id) throws RemoteException,
			InvalidIdException, InvalidTokenException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException, MaxDimensionReachedException;

	/**
	 * @see client.ClientSocketComunicator#leaveYourEgg(String, String)
	 */
	void leaveYourEgg(String token, String id) throws RemoteException,
			InvalidIdException, InvalidTokenException,
			NumberOfDinosaursExceededException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException;

	/**
	 * @see client.ClientSocketComunicator#pass(String)
	 */
	void pass(String token) throws RemoteException, InvalidTokenException,
			NotYourTurnException, UserNotPlayingException;

	/**
	 * @see client.ClientSocketComunicator#confirm(String)
	 */
	void confirm(String token) throws RemoteException,
			InvalidTokenException, NotYourTurnException,
			UserNotPlayingException;
}
