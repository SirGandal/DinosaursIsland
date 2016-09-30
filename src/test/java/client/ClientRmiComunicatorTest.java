package client;


import static org.junit.Assert.*;
import static org.junit.Assume.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.naming.LimitExceededException;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dinosaur.Dinosaur;
import dinosaur.RaceManager;
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

import server.Game;
import server.InitDB;
import server.Server;
import server.mainServer;

public class ClientRmiComunicatorTest {

	private static ServerForTest sft = new ServerForTest();
	static ClientRmiComunicator crc;
	static String beforeToken;
	static Game game;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		InitDB.main(null);
		sft.setPort(1289);
		sft.start();

		// mainServer.main(null);

		try {
			crc = new ClientRmiComunicator("127.0.0.1", "8812");
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}

		crc.createNewUser("pippo", "pippo");
		beforeToken = crc.logYourUser("pippo", "pippo");
		crc.createNewRace(beforeToken, "pippoRace", "e");

	}

	@AfterClass
	public static void setUpAfterClass() throws Exception {
		sft.setLoop(false);
	}

	// TEST CHE NON DEVONO DARE ECCEZIONI
	@Test
	public void testCreateNewUser2() {

		try {
			crc.createNewUser("ok4", "ok4");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test
	public void testLogYourUser2() {

		try {
			crc.createNewUser("ok3", "ok3");
			crc.logYourUser("ok3", "ok3");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test
	public void testCreateNewRace4() {

		try {
			crc.createNewUser("ok2", "ok2");
			String tokenok = crc.logYourUser("ok2", "ok2");
			crc.createNewRace(tokenok, "o2k", "c");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test
	public void testGameAccess5() {

		String tokenok = null;

		try {
			crc.createNewUser("ok1", "ok1");
			tokenok = crc.logYourUser("ok1", "ok1");
			crc.createNewRace(tokenok, "o1k", "c");
			crc.gameAccess(tokenok);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test
	public void testGameOut3() {
		try {
			crc.createNewUser("ok", "ok");
			String tokenok = crc.logYourUser("ok", "ok");
			crc.createNewRace(tokenok, "ok", "c");
			crc.gameAccess(tokenok);
			crc.gameOut(tokenok);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyExited e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test
	public void testPlayingList2() {
		String tokenok = null;
		try {
			crc.createNewUser("ok5", "ok5");
			tokenok = crc.logYourUser("ok5", "ok5");
			crc.createNewRace(tokenok, "o5k", "c");
			crc.gameAccess(tokenok);
			crc.askForList(tokenok);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test
	public void testLogOut2() {
		try {
			crc.createNewUser("ok6", "ok6");
			String tokenok = crc.logYourUser("ok6", "ok6");
			crc.logOut(tokenok);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test
	public void testGiveMeRanking3() {
		String tokenok = null;
		try {
			crc.createNewUser("ok11", "ok11");
			tokenok = crc.logYourUser("ok11", "ok11");
			crc.createNewRace(tokenok, "o11k", "c");
			crc.gameAccess(tokenok);
			crc.giveMeRanking(tokenok);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test
	public void testForMap4() {
		String tokenok = null;
		try {
			crc.createNewUser("ok7", "ok7");
			tokenok = crc.logYourUser("ok7", "ok7");
			crc.createNewRace(tokenok, "o7k", "c");
			crc.gameAccess(tokenok);
			crc.askForMap(tokenok);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	// FINE TEST OK

	@Test(expected = BusyUsernameException.class)
	public void testCreateNewUser() throws BusyUsernameException {

		try {
			crc.createNewUser("pluto", "pluto");
			crc.createNewUser("pluto", "pluto");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = AutenticationFailedException.class)
	public void testLogYourUser() throws AutenticationFailedException,
			UserAlreadyLoggedException {
		try {
			crc.logYourUser("ajeje", "brazorf");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testLogYourUser3() throws AutenticationFailedException,
			UserAlreadyLoggedException {
		try {
			crc.createNewUser("jkl", "jkl");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
		try {
			crc.logYourUser("jkl", "jkl");
			crc.logYourUser("jkl", "jkl");
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = RaceNameTakenException.class)
	public void testCreateNewRace() throws RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {

		String provaToken = null;
		try {
			crc.createNewUser("prova", "prova");
			provaToken = crc.logYourUser("prova", "prova");
			crc.createNewRace(provaToken, "prova", "e");
			crc.gameAccess(provaToken);
			crc.createNewUser("prova5", "prova5");
			String prova5Token = crc.logYourUser("prova5", "prova5");
			crc.createNewRace(prova5Token, "prova", "e");
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(provaToken);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testCreateNewRace2() throws RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {

		try {
			crc.createNewRace("x", "pippoRace", "e");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = RaceAlreadyExistsException.class)
	public void testCreateNewRace3() throws RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {

		try {
			crc.createNewUser("prova2", "prova2");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		try {
			String provaToken = crc.logYourUser("pluto", "pluto");
			crc.createNewRace(provaToken, "plutoRace", "e");
			crc.createNewRace(provaToken, "plutoRace2", "e");
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = TooPlayingUserException.class)
	public void testGameAccess() throws UserAlreadyEnteredInGame,
			TooPlayingUserException, RaceNullException {
		try {
			crc.createNewUser("1", "1");
			crc.createNewUser("2", "2");
			crc.createNewUser("3", "3");
			crc.createNewUser("4", "4");
			crc.createNewUser("5", "5");
			crc.createNewUser("6", "6");
			crc.createNewUser("7", "7");
			crc.createNewUser("8", "8");
			crc.createNewUser("9", "9");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		try {
			String token1 = crc.logYourUser("1", "1");
			String token2 = crc.logYourUser("2", "2");
			String token3 = crc.logYourUser("3", "3");
			String token4 = crc.logYourUser("4", "4");
			String token5 = crc.logYourUser("5", "5");
			String token6 = crc.logYourUser("6", "6");
			String token7 = crc.logYourUser("7", "7");
			String token8 = crc.logYourUser("8", "8");
			String token9 = crc.logYourUser("9", "9");

			crc.createNewRace(token1, "p", "e");
			crc.createNewRace(token2, "o", "e");
			crc.createNewRace(token3, "u", "e");
			crc.createNewRace(token4, "y", "e");
			crc.createNewRace(token5, "hhh", "e");
			crc.createNewRace(token6, "j", "e");
			crc.createNewRace(token7, "k", "e");
			crc.createNewRace(token8, "l", "e");
			crc.createNewRace(token9, "m", "e");

			crc.gameAccess(token1);
			crc.gameAccess(token2);
			crc.gameAccess(token3);
			crc.gameAccess(token4);
			crc.gameAccess(token5);
			crc.gameAccess(token6);
			crc.gameAccess(token7);
			crc.gameAccess(token8);
			crc.gameAccess(token9);

		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut("11");
				crc.gameOut("21");
				crc.gameOut("31");
				crc.gameOut("41");
				crc.gameOut("51");
				crc.gameOut("61");
				crc.gameOut("71");
				crc.gameOut("81");

				crc.logOut("11");
				crc.logOut("21");
				crc.logOut("31");
				crc.logOut("41");
				crc.logOut("51");
				crc.logOut("61");
				crc.logOut("71");
				crc.logOut("81");
				crc.logOut("91");

			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}

		}

	}

	@Test(expected = RaceNullException.class)
	public void testGameAccess2() throws UserAlreadyEnteredInGame,
			InvalidTokenException, TooPlayingUserException, RaceNullException {
		try {
			crc.createNewUser("x", "x");
			String tokenx = crc.logYourUser("x", "x");
			crc.gameAccess(tokenx);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testGameAccess3() throws UserAlreadyEnteredInGame,
			InvalidTokenException, TooPlayingUserException, RaceNullException {
		try {
			crc.gameAccess("abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = UserAlreadyEnteredInGame.class)
	public void testGameAccess4() throws UserAlreadyEnteredInGame,
			InvalidTokenException, TooPlayingUserException, RaceNullException {
		try {
			crc.createNewUser("r", "r");
			String tokenr = crc.logYourUser("r", "r");
			crc.createNewRace(tokenr, "rRace", "e");
			crc.gameAccess(tokenr);
			crc.gameAccess(tokenr);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut("r1");
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}

		}

	}

	@Test(expected = UserAlreadyExited.class)
	public void testGameOut() throws UserAlreadyExited {
		try {
			crc.createNewUser("h", "h");
			String tokenh = crc.logYourUser("h", "h");
			crc.createNewRace(tokenh, "h", "e");
			crc.gameAccess(tokenh);
			crc.playingList(tokenh);
			crc.gameOut(tokenh);
			crc.gameOut(tokenh);
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testGameOut2() throws UserAlreadyExited, InvalidTokenException {
		try {
			crc.gameOut("awe");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testPlayingList() throws InvalidTokenException {
		try {
			crc.playingList("abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testLogOut() throws InvalidTokenException {
		try {
			crc.logOut("abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testGiveMeRanking() throws InvalidTokenException {
		try {
			crc.giveMeRanking("abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test
	public void testGiveMeRanking2() throws InvalidTokenException {

		try {
			crc.createNewUser("qwerty", "qwerty");
			String tokenqwerty = crc.logYourUser("qwerty", "qwerty");
			crc.createNewRace(tokenqwerty, "qwerty", "e");
			assertNotNull(crc.giveMeRanking(tokenqwerty));
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForMap() throws InvalidTokenException,
			UserNotPlayingException {
		try {
			crc.askForMap("xyz");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = UserNotPlayingException.class)
	public void testAskForMap2() throws InvalidTokenException,
			UserNotPlayingException {
		try {
			crc.createNewUser("try", "try");
			String tokentry = crc.logYourUser("try", "try");
			crc.askForMap(tokentry);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test
	public void testAskForMap3() {
		try {
			crc.gameAccess(beforeToken);
			assertNotNull(crc.askForMap(beforeToken));
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(beforeToken);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	// ///
	@Test(expected = UserNotPlayingException.class)
	public void testAskForlocalView() throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {

		try {
			crc.createNewUser("aaaa", "aaaa");
			String tokenaaaa = crc.logYourUser("aaaa", "aaaa");
			crc.createNewRace(tokenaaaa, "aaaa", "e");
			crc.askForlocalView(tokenaaaa, "a");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForlocalView2() throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {
		try {
			crc.askForlocalView("abc", "a");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidIdException.class)
	public void testAskForlocalView3() throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {
		try {
			crc.gameAccess(beforeToken);
			crc.askForlocalView(beforeToken, "abc");
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(beforeToken);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test
	public void testAskForlocalView4() {
		try {
			crc.createNewUser("ciao", "ciao");
			String tokenCiao = crc.logYourUser("ciao", "ciao");
			crc.createNewRace(tokenCiao, "ciaoRace", "c");
			crc.gameAccess(tokenCiao);
			String[] listCiao = crc.askForList(tokenCiao);
			assertNotNull(crc.askForlocalView(tokenCiao, listCiao[0]));
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.logOut("ciao1");
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test
	public void testAskForLocalView5() {
		String tokenok = null;
		try {
			crc.createNewUser("ok8", "ok8");
			tokenok = crc.logYourUser("ok8", "ok8");
			crc.createNewRace(tokenok, "o8k", "c");
			crc.gameAccess(tokenok);
			String[] list = crc.askForList(tokenok);
			crc.askForlocalView(tokenok, list[0]);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenok);
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = UserNotPlayingException.class)
	public void testAskForList() throws InvalidTokenException,
			UserNotPlayingException {

		try {
			crc.createNewUser("abcde", "abcde");
			crc.logYourUser("abcde", "abcde");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		try {
			crc.askForList("abcde1");
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForList2() throws InvalidTokenException,
			UserNotPlayingException {
		try {
			crc.askForList("abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForStatus() throws UserNotPlayingException,
			InvalidTokenException, InvalidIdException {
		try {
			crc.askForStatus("abc", "abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidIdException.class)
	public void testAskForStatus2() throws UserNotPlayingException,
			InvalidTokenException, InvalidIdException {
		String tokenbarba = null;
		try {
			crc.createNewUser("barba", "barba");
			tokenbarba = crc.logYourUser("barba", "barba");
			crc.createNewRace(tokenbarba, "barba", "c");
			crc.gameAccess(tokenbarba);
			crc.askForStatus(tokenbarba, "qwertyu");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenbarba);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = UserNotPlayingException.class)
	public void testAskForStatus3() throws UserNotPlayingException,
			InvalidTokenException, InvalidIdException {
		try {
			crc.askForStatus(beforeToken, "abc");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test
	public void testAskForStatus4() {
		String tokenok = null;
		try {
			crc.createNewUser("ok9", "ok9");
			tokenok = crc.logYourUser("ok9", "ok9");
			crc.createNewRace(tokenok, "o9k", "c");
			crc.gameAccess(tokenok);
			String[] list = crc.askForList(tokenok);
			crc.askForStatus(tokenok, list[0]);
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserNotPlayingException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (InvalidTokenException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testMoveDinosaur() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		try {
			crc.moveDinosaur("abc", "abc", 0, 0);
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidIdException.class)
	public void testMoveDinosaur2() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		try {
			crc.gameAccess(beforeToken);
			crc.moveDinosaur(beforeToken, "abc", 0, 0);
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(beforeToken);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = DestinationUnreachableException.class)
	public void testMoveDinosaur3() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		String tokenao = null;
		try {
			crc.createNewUser("ao", "ao");
			tokenao = crc.logYourUser("ao", "ao");
			crc.createNewRace(tokenao, "ao", "e");
			crc.gameAccess(tokenao);
			Thread.sleep(4000);
			crc.confirm(tokenao);
			String[] list = crc.askForList(tokenao);
			String[] status = crc.askForStatus(tokenao, list[0]);
			crc.moveDinosaur(tokenao, list[0],
					Integer.parseInt(status[3]) + 10,
					Integer.parseInt(status[4]) + 10);
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenao);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test(expected = NotEnoughEnergy.class)
	public void testMoveDinosaur4() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		String tokenauser = null;
		try {
			game = sft.getGame();
			crc.createNewUser("xxx", "xxx");
			tokenauser = crc.logYourUser("xxx", "xxx");
			crc.createNewRace(tokenauser, "xxx", "e");
			Specie spec = game.fromUsertoRace("xxx");

			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setDimension(1);
			denver.setCurrentEnergy(5);
			denver.setxPosition(5);
			denver.setyPosition(5);
			crc.gameAccess(tokenauser);
			Thread.sleep(4000);
			crc.confirm(tokenauser);

			String[] list = crc.askForList(tokenauser);
			crc.askForStatus(tokenauser, list[0]);

			crc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 1, 49 - denver.getColPosition());
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = MovesLimitExceededException.class)
	public void testMoveDinosaur5() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		String tokenauser = null;
		try {
			game = sft.getGame();
			crc.createNewUser("zxxc", "zxxc");
			tokenauser = crc.logYourUser("zxxc", "zxxc");
			crc.createNewRace(tokenauser, "zxxc", "e");
			Specie spec = game.fromUsertoRace("zxxc");

			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setxPosition(5);
			denver.setyPosition(5);
			crc.gameAccess(tokenauser);
			Thread.sleep(4000);
			crc.confirm(tokenauser);

			String[] list = crc.askForList(tokenauser);
			crc.askForStatus(tokenauser, list[0]);

			crc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 1,
					49 - (denver.getColPosition() + 1));

			crc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 1,
					49 - denver.getColPosition() + 1);
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenauser);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	// Movimento di tre per il carnivoro
	@Test
	public void testMoveDinosaur6() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		String tokenauser = null;
		try {
			game = sft.getGame();
			crc.createNewUser("zxc", "zxc");
			tokenauser = crc.logYourUser("zxc", "zxc");
			crc.createNewRace(tokenauser, "zxc", "c");
			Specie spec = game.fromUsertoRace("zxc");

			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setxPosition(5);
			denver.setyPosition(5);
			crc.gameAccess(tokenauser);
			Thread.sleep(2000);
			crc.confirm(tokenauser);

			String[] list = crc.askForList(tokenauser);
			crc.askForStatus(tokenauser, list[0]);

			crc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 3,
					49 - (denver.getColPosition() + 3));
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenauser);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testLetYourDinosaurGrow() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		try {
			crc.letYourDinosaurGrow("zxcvbnmhjkjg", "fcvhjbkbbknlj");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidIdException.class)
	public void testLetYourDinosaurGrow2() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenW = null;
		try {
			crc.createNewUser("w", "w");
			tokenW = crc.logYourUser("w", "w");
			crc.createNewRace(tokenW, "wRace", "e");
			crc.gameAccess(tokenW);
			crc.letYourDinosaurGrow(tokenW, "abc");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenW);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = MaxDimensionReachedException.class)
	public void testLetYourDinosaurGrow3() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {

		String tokenQ = null;
		try {
			game = sft.getGame();
			crc.createNewUser("q", "q");
			tokenQ = crc.logYourUser("q", "q");
			crc.createNewRace(tokenQ, "qRace", "e");

			Specie spec = game.fromUsertoRace("q");

			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setDimension(5);
			denver.setCurrentEnergy(5000);

			crc.gameAccess(tokenQ);
			Thread.sleep(4000);
			crc.confirm(tokenQ);

			String[] list = crc.askForList(tokenQ);
			crc.letYourDinosaurGrow(tokenQ, list[0]);

		} catch (RaceNullException e) {
			e.printStackTrace();
		} catch (BusyUsernameException e) {
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			e.printStackTrace();
		} catch (RaceNameTakenException e) {
			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {
			e.printStackTrace();
		} catch (TooPlayingUserException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (NotYourTurnException e) {
			try {
				Thread.sleep(2000);
				crc.confirm(tokenQ);

				String[] list = crc.askForList(tokenQ);
				crc.letYourDinosaurGrow(tokenQ, list[0]);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			} catch (RemoteException e1) {

				e1.printStackTrace();
			}

		} finally {
			try {
				crc.gameOut(tokenQ);
			} catch (UserAlreadyExited e) {
				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test(expected = MovesLimitExceededException.class)
	public void testLetYourDinosaurGrow4() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenaje = null;
		try {
			crc.createNewUser("ajejeje", "ajejeje");
			tokenaje = crc.logYourUser("ajejeje", "ajejeje");
			crc.createNewRace(tokenaje, "aje", "e");
			crc.gameAccess(tokenaje);
			String[] list = crc.askForList(tokenaje);
			RaceManager raceMan = new RaceManager();
			Specie spec = raceMan.fromUsertoRace("ajejeje");
			Dinosaur denver = spec.checkIdAndReturnDino(list[0]);
			denver.setDimension(3);
			denver.setCurrentEnergy(3000);
			crc.confirm(tokenaje);
			crc.letYourDinosaurGrow(tokenaje, list[0]);
			crc.letYourDinosaurGrow(tokenaje, list[0]);
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenaje);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	@Test(expected = NotEnoughEnergy.class)
	public void testLetYourDinosaurGrow5() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenBarbie = null;
		try {
			game = sft.getGame();
			crc.createNewUser("barbie", "raperonzolo");
			tokenBarbie = crc.logYourUser("barbie", "raperonzolo");
			crc.createNewRace(tokenBarbie, "bar", "e");

			Specie spec = game.fromUsertoRace("barbie");

			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setCurrentEnergy(100);

			crc.gameAccess(tokenBarbie);
			Thread.sleep(4000);
			crc.confirm(tokenBarbie);

			String[] list = crc.askForList(tokenBarbie);
			crc.letYourDinosaurGrow(tokenBarbie, list[0]);
		} catch (RaceNullException e) {
			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {
			e.printStackTrace();
		} catch (TooPlayingUserException e) {
			e.printStackTrace();
		} catch (RaceNameTakenException e) {
			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (BusyUsernameException e) {
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = UserNotPlayingException.class)
	public void testLetYourDinosaurGrow6() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {

		try {
			crc.createNewUser("simone", "meme");
			String tokenSimone = crc.logYourUser("simone", "meme");
			crc.createNewRace(tokenSimone, "simoRace", "e");
			crc.letYourDinosaurGrow(tokenSimone, "x");
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (InvalidIdException e) {

			e.printStackTrace();
		} catch (MaxDimensionReachedException e) {

			e.printStackTrace();
		} catch (MovesLimitExceededException e) {

			e.printStackTrace();
		} catch (NotEnoughEnergy e) {

			e.printStackTrace();
		} catch (NotYourTurnException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = NotYourTurnException.class)
	public void testLetYourDinosaurGrow7() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenSimone1 = null;
		String tokenSimone2 = null;
		try {
			crc.createNewUser("simone1", "meme1");
			tokenSimone1 = crc.logYourUser("simone1", "meme1");
			crc.createNewRace(tokenSimone1, "simo1Race", "e");
			crc.gameAccess(tokenSimone1);

			crc.createNewUser("simone2", "meme2");
			tokenSimone2 = crc.logYourUser("simone2", "meme2");
			crc.createNewRace(tokenSimone2, "simo2Race", "e");
			crc.gameAccess(tokenSimone2);

			String[] list = crc.askForList(tokenSimone2);
			crc.letYourDinosaurGrow(tokenSimone2, list[0]);

		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (InvalidTokenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenSimone1);
				crc.gameOut(tokenSimone2);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testLeaveYourEgg() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		try {
			crc.leaveYourEgg("aaaaa", "WW");
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = InvalidIdException.class)
	public void testLeaveYourEgg2() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenpape = null;
		try {
			crc.createNewUser("paperino", "paperino");
			tokenpape = crc.logYourUser("paperino", "paperino");
			crc.createNewRace(tokenpape, "pap", "e");
			crc.gameAccess(tokenpape);
			crc.leaveYourEgg(tokenpape, "xxxx");
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenpape);
			} catch (UserAlreadyExited e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = NotEnoughEnergy.class)
	public void testLeaveYourEgg3() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenbat = null;
		try {
			crc.createNewUser("batman", "batman");
			tokenbat = crc.logYourUser("batman", "batman");
			crc.createNewRace(tokenbat, "bat", "e");
			crc.gameAccess(tokenbat);
			String[] list = crc.askForList(tokenbat);
			crc.confirm(tokenbat);
			crc.leaveYourEgg(tokenbat, list[0]);
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Test(expected = MovesLimitExceededException.class)
	public void testLeaveYourEgg4() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenrob = null;
		try {
			crc.createNewUser("robin", "robin");
			tokenrob = crc.logYourUser("robin", "robin");
			crc.createNewRace(tokenrob, "rob", "e");
			crc.gameAccess(tokenrob);
			Thread.sleep(6000);
			crc.confirm(tokenrob);
			String[] list = crc.askForList(tokenrob);
			crc.letYourDinosaurGrow(tokenrob, list[0]);
			crc.leaveYourEgg(tokenrob, list[0]);
		} catch (RaceNullException e) {
			e.printStackTrace();
		} catch (TooPlayingUserException e) {
			e.printStackTrace();
		} catch (RaceNameTakenException e) {
			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			e.printStackTrace();
		} catch (BusyUsernameException e) {
			e.printStackTrace();
		} catch (MaxDimensionReachedException e) {
			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (NotYourTurnException e) {

			try {

				Thread.sleep(2000);
				System.out.println(crc.playingList(tokenrob));
				crc.confirm(tokenrob);
				String[] list = crc.askForList(tokenrob);
				crc.letYourDinosaurGrow(tokenrob, list[0]);
				crc.leaveYourEgg(tokenrob, list[0]);
			} catch (RemoteException e1) {

				e1.printStackTrace();
			} catch (MaxDimensionReachedException e1) {

				e1.printStackTrace();
			} catch (InterruptedException e1) {

				e.printStackTrace();
			}

		} finally {
			try {
				crc.gameOut(tokenrob);
			} catch (UserAlreadyExited e) {
				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = NumberOfDinosaursExceededException.class)
	public void testLeaveYourEgg5() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenaje = null;
		try {
			game = sft.getGame();
			crc.createNewUser("aj", "aj");
			tokenaje = crc.logYourUser("aj", "aj");
			crc.createNewRace(tokenaje, "aj", "e");
			Specie spec = game.fromUsertoRace("aj");

			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());

			denver.setDimension(3);
			denver.setCurrentEnergy(3000);

			spec.addDenver(new Dinosaur(5, 5, spec));
			spec.addDenver(new Dinosaur(5, 6, spec));
			spec.addDenver(new Dinosaur(5, 7, spec));
			spec.addDenver(new Dinosaur(5, 8, spec));

			crc.gameAccess(tokenaje);
			Thread.sleep(3000);
			crc.confirm(tokenaje);

			String[] list = crc.askForList(tokenaje);
			crc.leaveYourEgg(tokenaje, list[0]);

		} catch (RaceNullException e) {
			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {
			e.printStackTrace();
		} catch (TooPlayingUserException e) {
			e.printStackTrace();
		} catch (RaceNameTakenException e) {
			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			e.printStackTrace();
		} catch (BusyUsernameException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.gameOut(tokenaje);
			} catch (UserAlreadyExited e) {
				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

	@Test(expected = UserNotPlayingException.class)
	public void testLeaveYourEgg6() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {

		try {
			crc.createNewUser("c", "c");
			String tokenc = crc.logYourUser("c", "c");
			crc.createNewRace(tokenc, "c", "e");
			crc.leaveYourEgg(tokenc, "ww");
		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}

	}

	@Test(expected = NotYourTurnException.class)
	public void testLeaveYourEgg7() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {

		String tokene = null;
		String tokenf = null;
		String tokeng = null;
		try {
			InitDB.main(null);
			crc.createNewUser("e", "e");
			crc.createNewUser("f", "f");
			crc.createNewUser("g", "g");

			tokene = crc.logYourUser("e", "e");
			tokenf = crc.logYourUser("f", "f");
			tokeng = crc.logYourUser("g", "g");

			crc.createNewRace(tokene, "bnm", "e");
			crc.createNewRace(tokenf, "cvb", "e");
			crc.createNewRace(tokeng, "zxccx", "e");

			crc.gameAccess(tokene);
			Thread.sleep(4000);
			crc.confirm(tokene);
			crc.gameAccess(tokenf);
			crc.gameAccess(tokeng);

			String[] liste = crc.askForList(tokene);
			String[] listf = crc.askForList(tokenf);
			String[] listg = crc.askForList(tokeng);

			crc.leaveYourEgg(tokeng, listg[0]);
			crc.leaveYourEgg(tokenf, listf[0]);
			crc.leaveYourEgg(tokene, liste[0]);

		} catch (RaceNameTakenException e) {

			e.printStackTrace();
		} catch (RaceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (AutenticationFailedException e) {

			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {

			e.printStackTrace();
		} catch (BusyUsernameException e) {

			e.printStackTrace();
		} catch (UserAlreadyEnteredInGame e) {

			e.printStackTrace();
		} catch (TooPlayingUserException e) {

			e.printStackTrace();
		} catch (RaceNullException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} finally {
			try {
				crc.logOut(tokene);
				crc.logOut(tokenf);
				crc.logOut(tokeng);
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}

	}

}
