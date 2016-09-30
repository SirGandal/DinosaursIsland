package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import dinosaur.Specie;

import map.Cell;
import shared.ChangeRoundRMI;
import shared.RMInterface;
import GUI.WindowGame;
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
public class ClientRmiComunicator implements ClientComunicator {

	private final int MapLength = 50;
	/**
	 * @uml.property  name="rMIServer"
	 * @uml.associationEnd  
	 */
	private RMInterface RMIServer;
	private Registry register;
	/**
	 * @uml.property  name="crRmi"
	 * @uml.associationEnd  
	 */
	private ChangeRoundRMI crRmi;

	public ClientRmiComunicator(String serverAddress, String serverPort)
			throws RemoteException, RemoteException {

		try {
			register = LocateRegistry.getRegistry(serverAddress,
					Integer.parseInt(serverPort));
			RMIServer = (RMInterface) (register.lookup("rmiServer"));
			crRmi = new ClientForChangeRound();
			RMIServer.submitClient(crRmi);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createNewUser(String username, String password)
			throws RemoteException, BusyUsernameException {
		RMIServer.createNewUser(username, password);
	}

	@Override
	public void setWindowGame(WindowGame winGame) {
		if (crRmi != null) {
			((ClientForChangeRound) crRmi).setWindowGame(winGame);
		}
	}

	@Override
	public String logYourUser(String username, String password)
			throws RemoteException, AutenticationFailedException,
			UserAlreadyLoggedException {

		String token = RMIServer.logUsers(username, password);

		if (token.equals("no")) {
			throw new UserAlreadyLoggedException();
		}

		return token;

	}

	@Override
	public void createNewRace(String token, String raceName, String type)
			throws RemoteException, RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {
		RMIServer.createNewRace(token, raceName, type);
	}

	@Override
	public void gameAccess(String token) throws RemoteException,
			UserAlreadyEnteredInGame, InvalidTokenException,
			TooPlayingUserException, RaceNullException {
		RMIServer.gameAccess(token);
	}

	@Override
	public void gameOut(String token) throws RemoteException,
			UserAlreadyExited, InvalidTokenException {
		RMIServer.gameOut(token);
	}

	@Override
	public ArrayList<String> playingList(String token) throws RemoteException,
			InvalidTokenException {
		
		ArrayList<String> playingList = new ArrayList<String>();
		List<String> list = RMIServer.playingList(token);
		
		playingList.add("@listaGiocatori");
		for(int i=0;i<list.size();i++){
			playingList.add(list.get(i));
		}
		
		return playingList;
	}

	@Override
	public void logOut(String T) throws RemoteException, InvalidTokenException {
		RMIServer.logOut(T);
	}

	@Override
	public Object[][] giveMeRanking(String T) throws RemoteException,
			InvalidTokenException {

		List<Specie> rank = RMIServer.giveMeRanking(T);

		Object[][] ranking = new Object[rank.size()][4];

		for (int i = 0; i < rank.size(); i++) {
			ranking[i][0] = rank.get(i).getUsername();
			ranking[i][1] = rank.get(i).getName();
			ranking[i][2] = rank.get(i).getScore();
			if (rank.get(i).getCondition().equals("n")) {
				ranking[i][3] = "si";
			} else {
				ranking[i][3] = "no";
			}
		}

		return ranking;
	}

	@Override
	public Cell[][] askForMap(String T) throws RemoteException,
			InvalidTokenException, UserNotPlayingException {

		Cell[][] map = new Cell[MapLength][MapLength];

		String[][] mapString = RMIServer.showMap(T);

		for (int i = 0; i < MapLength; i++) {
			for (int j = 0; j < MapLength; j++) {
				if (mapString[i][j].equals("t")) {
					map[i][j] = new Cell();
					map[i][j].setEarth(true);
					map[i][j].setVisible(true);
				} else {
					if (mapString[i][j].equals("v")) {
						map[i][j] = new Cell();
						map[i][j].setVegetation(true);
						map[i][j].setVisible(true);
					} else {
						if (mapString[i][j].equals("a")) {
							map[i][j] = new Cell();
							map[i][j].setWater(true);
							map[i][j].setVisible(true);
						} else {
							if (mapString[i][j].equals("b")) {
								map[i][j] = new Cell();
								map[i][j].setVisible(false);
								map[i][j].setVisible(true);
							}
						}
					}
				}
			}
		}

		return map;
	}

	@Override
	public Cell[][] askForlocalView(String T, String id)
			throws RemoteException, InvalidTokenException,
			UserNotPlayingException, InvalidIdException {

		Cell[][] localMap = RMIServer.showLocalView(T, id);

		for (Cell[] element : localMap) {
			for (int j = 0; j < localMap[0].length; j++) {
				if (element[j].getThereIsDinosaur()) {
					element[j].setIdDinosaur(element[j].getLocalDinosaur()
							.getIdDino());
				}
			}
		}
		for (int i = 0; i < localMap.length; i++) {
			for (int j = 0; j < localMap[0].length; j++) {
				System.out.print(localMap[i][j].getType() + " " + localMap[i][j].getCurrentEnergyVegetation());
			}
			System.out.print("\n");
		}
		return localMap;
	}

	@Override
	public String[] askForList(String T) throws RemoteException,
			InvalidTokenException, UserNotPlayingException, InvalidIdException {

		List<String> dinosaurList = RMIServer.denverList(T);
		;
		String[] list = new String[dinosaurList.size()];

		for (int i = 0; i < dinosaurList.size(); i++) {
			list[i] = dinosaurList.get(i);
		}

		return list;
	}

	@Override
	public String[] askForStatus(String T, String id) throws RemoteException,
			UserNotPlayingException, InvalidTokenException, InvalidIdException {

		String[] status = RMIServer.denverState(T, id);

		//System.out.println("COLONNA: " + status[3]);
		//System.out.println("RIGA: " + status[4]);

		int col = (MapLength - 1) - Integer.parseInt(status[4]);
		status[4] = Integer.toString(col);

		return status;
	}

	@Override
	public void moveDinosaur(String T, String id, int x, int y)
			throws RemoteException, YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		RMIServer.denverMove(T, id, MapLength - 1 - y, x);
	}

	@Override
	public void letYourDinosaurGrow(String T, String id)
			throws RemoteException, InvalidIdException, InvalidTokenException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException, MaxDimensionReachedException {
		RMIServer.growingUp(id, T);

	}

	@Override
	public void leaveYourEgg(String T, String id) throws RemoteException,
			InvalidIdException, InvalidTokenException,
			NumberOfDinosaursExceededException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		RMIServer.layAnEgg(id, T);
	}

	@Override
	public void pass(String T) throws RemoteException, InvalidTokenException,
			NotYourTurnException, UserNotPlayingException {
		RMIServer.passRound(T);

	}

	@Override
	public void confirm(String token) throws RemoteException,
			InvalidTokenException, NotYourTurnException,
			UserNotPlayingException {
		RMIServer.acceptRound(token);

	}
}
