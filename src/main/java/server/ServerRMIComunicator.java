package server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import shared.ChangeRoundRMI;
import shared.RMInterface;

import map.Cell;

import dinosaur.Specie;

import exception.AutenticationFailedException;
import exception.BusyUsernameException;
import exception.DestinationUnreachableException;
import exception.InvalidIdException;
import exception.InvalidTokenException;
import exception.MaxDimensionReachedException;
import exception.MovesLimitExceededException;
import exception.NotEnoughEnergy;
import exception.NotYourTurnException;
import exception.NumberOfDinosaursExceededException;
import exception.RaceAlreadyExistsException;
import exception.RaceNameTakenException;
import exception.RaceNullException;
import exception.TooPlayingUserException;
import exception.UserAlreadyEnteredInGame;
import exception.UserAlreadyExited;
import exception.UserAlreadyLoggedException;
import exception.UserNotPlayingException;
import exception.YouLostTheBattle;
import exception.YouWonTheBattle;

/**
 * @author  gas12n
 */
public class ServerRMIComunicator extends UnicastRemoteObject implements
		RMInterface {
	/**
	 * @uml.property  name="game"
	 * @uml.associationEnd  
	 */
	Game game;
	int thisPort;
	String thisAddress;
	Registry registry;
	private ArrayList<ChangeRoundRMI> clientsRmiConnected;

	/**
	 * @param thisPort
	 * @uml.property  name="thisPort"
	 */
	public void setThisPort(int thisPort) {
		this.thisPort = thisPort;
	}

	protected ServerRMIComunicator(Game game,
			ArrayList<ChangeRoundRMI> clientsRmiConnected, int RMIport)
			throws RemoteException {
		{
			try {
				thisAddress = (InetAddress.getLocalHost()).toString();
				this.thisPort = RMIport;
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Waiting for RMI connection...");
			try {
				registry = LocateRegistry.createRegistry(thisPort);
				registry.rebind("rmiServer", this);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		this.game = game;
		this.clientsRmiConnected = clientsRmiConnected;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7808412140915829991L;

	String returnString;

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#createNewUser(java.lang.String, java.lang.String)
	 */
	@Override
	public void createNewUser(String username, String password)
			throws BusyUsernameException {
		returnString = game.checkUsers(username, password);
		if (returnString.equals("@no,@usernameOccupato")) {
			throw new BusyUsernameException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#logUsers(java.lang.String, java.lang.String)
	 */
	@Override
	public String logUsers(String username, String password)
			throws AutenticationFailedException, UserAlreadyLoggedException {
		returnString = game.logUsers(username, password);
		if (returnString.equals("@no,@autenticazioneFallita")) {
			throw new AutenticationFailedException();
		}
		if (returnString.equals("@no")) {
			throw new UserAlreadyLoggedException();
		}
		return returnString;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#createNewRace(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void createNewRace(String token, String name, String type)
			throws InvalidTokenException, RaceNameTakenException,
			RaceAlreadyExistsException {
		returnString = game.createNewRace(token, name, type);
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@nomeRazzaOccupato")) {
			throw new RaceNameTakenException();
		}
		if (returnString.equals("@no,@razzaGiaCreata")) {
			throw new RaceAlreadyExistsException();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#gameAccess(java.lang.String)
	 */
	@Override
	public void gameAccess(String token) throws TooPlayingUserException,
			InvalidTokenException, RaceNullException, UserAlreadyEnteredInGame {
		returnString = game.gameAccess(token);
		if (returnString.equals("@no,@razzaNonCreata")) {
			throw new RaceNullException();
		}
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@troppiGiocatori")) {
			throw new TooPlayingUserException();
		}
		if (returnString.equals("@no")) {
			throw new UserAlreadyEnteredInGame();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#gameOut(java.lang.String)
	 */
	@Override
	public void gameOut(String token) throws InvalidTokenException, UserAlreadyExited {
		returnString = game.gameOut(token);
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no")) {
			throw new UserAlreadyExited();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#playingList(java.lang.String)
	 */
	@Override
	public List<String> playingList(String token) throws InvalidTokenException {
		return game.playingList(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#logOut(java.lang.String)
	 */
	@Override
	public void logOut(String token) throws InvalidTokenException {
		returnString = game.logOut(token);
		if (returnString.equals("@no,@autenticazioneFallita")) {
			throw new InvalidTokenException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#giveMeRanking(java.lang.String)
	 */
	@Override
	public List<Specie> giveMeRanking(String token)
			throws InvalidTokenException {
		return game.ranking(token);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#showMap(java.lang.String)
	 */
	@Override
	public String[][] showMap(String token) throws InvalidTokenException,
			UserNotPlayingException {
		return game.mapCompare(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#showLocalView(java.lang.String, java.lang.String)
	 */
	@Override
	public Cell[][] showLocalView(String token, String idDino)
			throws InvalidTokenException, InvalidIdException,
			UserNotPlayingException {
		return game.showLocalView(token, idDino);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#denverList(java.lang.String)
	 */
	@Override
	public List<String> denverList(String token) throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {
		return game.giveMeDenverList(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#denverState(java.lang.String, java.lang.String)
	 */
	@Override
	public String[] denverState(String token, String idDino)
			throws InvalidIdException, InvalidTokenException,
			UserNotPlayingException {
		return game.denverState(token, idDino);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#denverMove(java.lang.String, java.lang.String,
	 * int, int)
	 */
	@Override
	public void denverMove(String token, String idDino, int rowDest, int colDest)
			throws MovesLimitExceededException,
			DestinationUnreachableException, NotEnoughEnergy, YouWonTheBattle,
			YouLostTheBattle, NotYourTurnException, InvalidTokenException,
			UserNotPlayingException, InvalidIdException {
		returnString = game.denverMove(token, idDino, rowDest, colDest);
		if (returnString.equals("@no,@raggiuntoLimiteMosseDinosauro")) {
			throw new MovesLimitExceededException();
		}
		if (returnString.equals("@no,@destinazioneNonValida")) {
			throw new DestinationUnreachableException();
		}
		if (returnString.equals("@no,@mortePerInedia")) {
			throw new NotEnoughEnergy();
		}
		if (returnString.equals("@ok,@combattimento,v")) {
			throw new YouWonTheBattle();
		}
		if (returnString.equals("@ok,@combattimento,p")) {
			throw new YouLostTheBattle();
		}
		if (returnString.equals("@no,@nonIlTuoTurno")) {
			throw new NotYourTurnException();
		}
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@nonInPartita")) {
			throw new UserNotPlayingException();
		}
		if (returnString.equals("@no,@idNonValido")) {
			throw new InvalidIdException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#growingUp(java.lang.String, java.lang.String)
	 */
	@Override
	public void growingUp(String idDino, String token)
			throws MovesLimitExceededException, NotEnoughEnergy,
			NotYourTurnException, MaxDimensionReachedException,
			InvalidTokenException, InvalidIdException, UserNotPlayingException {
		returnString = game.growingUp(idDino, token);
		if (returnString.equals("@no,@raggiuntoLimiteMosseDinosauro")) {
			throw new MovesLimitExceededException();
		}
		if (returnString.equals("@no,@mortePerInedia")) {
			throw new NotEnoughEnergy();
		}
		if (returnString.equals("@no,@nonIlTuoTurno")) {
			throw new NotYourTurnException();
		}
		if (returnString.equals("@no,@raggiuntaDimensioneMax")) {
			throw new MaxDimensionReachedException();
		}
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@idNonValido")) {
			throw new InvalidIdException();
		}
		if (returnString.equals("@no,@nonInPartita")) {
			throw new UserNotPlayingException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#layAnEgg(java.lang.String, java.lang.String)
	 */
	@Override
	public void layAnEgg(String idDino, String token)
			throws MovesLimitExceededException, NotEnoughEnergy,
			NotYourTurnException, NumberOfDinosaursExceededException,
			UserNotPlayingException, InvalidIdException, InvalidTokenException {
		returnString = game.layAnEgg(idDino, token);
		if (returnString.equals("@no,@raggiuntoLimiteMosseDinosauro")) {
			throw new MovesLimitExceededException();
		}
		if (returnString.equals("@no,@mortePerInedia")) {
			throw new NotEnoughEnergy();
		}
		if (returnString.equals("@no,@nonIlTuoTurno")) {
			throw new NotYourTurnException();
		}
		if (returnString.equals("@no,@raggiuntoNumeroMaxDinosauri")) {
			throw new NumberOfDinosaursExceededException();
		}
		if (returnString.equals("@no,@nonInPartita")) {
			throw new UserNotPlayingException();
		}
		if (returnString.equals("@no,@idNonValido")) {
			throw new InvalidIdException();
		}
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@idNonValido")) {
			throw new InvalidIdException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#acceptRound(java.lang.String)
	 */
	@Override
	public void acceptRound(String token) throws InvalidTokenException,
			UserNotPlayingException, NotYourTurnException {
		returnString = game.acceptRound(token);
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@nonInPartita")) {
			throw new UserNotPlayingException();
		}
		if (returnString.equals("@no,@nonIlTuoTurno")) {
			throw new NotYourTurnException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#passRound(java.lang.String)
	 */
	@Override
	public void passRound(String token) throws InvalidTokenException,
			UserNotPlayingException, NotYourTurnException {
		returnString = game.passRound(token);
		if (returnString.equals("@no,@tokenNonValido")) {
			throw new InvalidTokenException();
		}
		if (returnString.equals("@no,@nonInPartita")) {
			throw new UserNotPlayingException();
		}
		if (returnString.equals("@no,@nonIlTuoTurno")) {
			throw new NotYourTurnException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.RMInterface#changeRound(java.lang.String)
	 */
	@Override
	public void changeRound(String currentUser) throws RemoteException {
		game.changeRound();
	}

	@Override
	public void submitClient(ChangeRoundRMI crRmi) {
		synchronized (clientsRmiConnected) {
			clientsRmiConnected.add(crRmi);
		}
	}

}
