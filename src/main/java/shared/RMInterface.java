package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

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

public interface RMInterface extends Remote {

	public abstract void submitClient(ChangeRoundRMI cfcr)
			throws RemoteException;

	public abstract void createNewUser(String username, String password)
			throws RemoteException, BusyUsernameException;

	public abstract String logUsers(String username, String password)
			throws RemoteException, AutenticationFailedException, UserAlreadyLoggedException;

	public abstract void createNewRace(String token, String name, String type)
			throws RemoteException, InvalidTokenException,
			RaceNameTakenException, RaceAlreadyExistsException;

	public abstract void gameAccess(String token) throws RemoteException,
			TooPlayingUserException, InvalidTokenException, RaceNullException, UserAlreadyEnteredInGame;

	public abstract void gameOut(String token) throws RemoteException,
			InvalidTokenException, UserAlreadyExited;

	public abstract List<String> playingList(String token)
			throws RemoteException, InvalidTokenException;

	public abstract void logOut(String token) throws RemoteException,
			InvalidTokenException;

	public abstract List<Specie> giveMeRanking(String token)
			throws RemoteException, InvalidTokenException;

	public abstract String[][] showMap(String token) throws RemoteException,
			InvalidTokenException, UserNotPlayingException;

	public abstract Cell[][] showLocalView(String token, String idDino)
			throws RemoteException, InvalidTokenException, InvalidIdException,
			UserNotPlayingException;

	public abstract List<String> denverList(String token)
			throws RemoteException, InvalidTokenException,
			UserNotPlayingException, InvalidIdException;

	public abstract String[] denverState(String token, String idDino)
			throws RemoteException, InvalidIdException, InvalidTokenException,
			UserNotPlayingException;

	public abstract void denverMove(String token, String idDino, int rowDest,
			int colDest) throws RemoteException, MovesLimitExceededException,
			DestinationUnreachableException, NotEnoughEnergy, YouWonTheBattle,
			YouLostTheBattle, NotYourTurnException, InvalidTokenException,
			UserNotPlayingException, InvalidIdException;

	public abstract void growingUp(String idDino, String token)
			throws RemoteException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException,
			MaxDimensionReachedException, InvalidTokenException,
			InvalidIdException, UserNotPlayingException;

	public abstract void layAnEgg(String idDino, String token)
			throws RemoteException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException,
			NumberOfDinosaursExceededException, UserNotPlayingException,
			InvalidIdException, InvalidTokenException;

	public abstract void acceptRound(String token) throws RemoteException,
			InvalidTokenException, UserNotPlayingException,
			NotYourTurnException;

	public abstract void passRound(String token) throws RemoteException,
			InvalidTokenException, UserNotPlayingException,
			NotYourTurnException;

	public abstract void changeRound(String currentUser) throws RemoteException;

}