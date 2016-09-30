package client;


import static org.junit.Assert.*;
import static org.junit.Assume.*;
import java.io.IOException;
import java.net.UnknownHostException;

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

public class ClientSocketComunicatorTest {

	private static ServerForTest sft = new ServerForTest();
	static ClientSocketComunicator csc;
	static String beforeToken;
	static Game game;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {


		InitDB.main(null);
		sft.setPort(1288);
		sft.setRMIport(8813);
		sft.start();
		// mainServer.main(null);
		try {
			csc = new ClientSocketComunicator("127.0.0.1", "1288");
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		csc.createNewUser("pippott", "pippott");
		beforeToken = csc.logYourUser("pippott", "pippott");
		csc.createNewRace(beforeToken, "pippoRace", "e");

	}

	@AfterClass
	public static void after() {
		csc.closeSocket();
		//sft.setLoop(false);
	}

	// TEST CHE NON DEVONO DARE ECCEZIONI
	@Test
	public void testCreateNewUser2() {

		try {
			csc.createNewUser("ok4", "ok4");
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		}

	}

	@Test
	public void testLogYourUser2() {

		try {
			csc.createNewUser("ok3", "ok3");
			csc.logYourUser("ok3", "ok3");
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateNewRace4() {

		try {
			csc.createNewUser("ok2", "ok2");
			String tokenok = csc.logYourUser("ok2", "ok2");
			csc.createNewRace(tokenok, "o2k", "c");
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
		}

	}

	@Test
	public void testGameAccess5() {

		String tokenok = null;

		try {
			csc.createNewUser("ok1", "ok1");
			tokenok = csc.logYourUser("ok1", "ok1");
			csc.createNewRace(tokenok, "o1k", "c");
			csc.gameAccess(tokenok);
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
		} finally {
			try {
				csc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testGameOut3() {
		try {
			csc.createNewUser("ok", "ok");
			String tokenok = csc.logYourUser("ok", "ok");
			csc.createNewRace(tokenok, "ok", "c");
			csc.gameAccess(tokenok);
			csc.gameOut(tokenok);
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
		}

	}

	@Test
	public void testPlayingList2() {
		String tokenok = null;
		try {
			csc.createNewUser("ok5", "ok5");
			tokenok = csc.logYourUser("ok5", "ok5");
			csc.createNewRace(tokenok, "o5k", "c");
			csc.gameAccess(tokenok);
			csc.askForList(tokenok);
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
		} finally {
			try {
				csc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testLogOut2() {
		try {
			csc.createNewUser("ok6", "ok6");
			String tokenok = csc.logYourUser("ok6", "ok6");
			csc.logOut(tokenok);
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			
			e.printStackTrace();
		} catch (InvalidTokenException e) {
			
			e.printStackTrace();
		}

	}

	@Test
	public void testGiveMeRanking3() {
		String tokenok = null;
		try {
			csc.createNewUser("ok11", "ok11");
			tokenok = csc.logYourUser("ok11", "ok11");
			csc.createNewRace(tokenok, "o11k", "c");
			csc.gameAccess(tokenok);
			csc.giveMeRanking(tokenok);
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
		} finally {
			try {
				csc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testForMap4() {
		String tokenok = null;
		try {
			csc.createNewUser("ok7", "ok7");
			tokenok = csc.logYourUser("ok7", "ok7");
			csc.createNewRace(tokenok, "o7k", "c");
			csc.gameAccess(tokenok);
			csc.askForMap(tokenok);
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
		} finally {
			try {
				csc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}
	}

	// FINE TEST OK

	@Test(expected = BusyUsernameException.class)
	public void testCreateNewUser() throws BusyUsernameException {
		csc.createNewUser("pluto", "pluto");
		csc.createNewUser("pluto", "pluto");
	}

	@Test(expected = AutenticationFailedException.class)
	public void testLogYourUser() throws AutenticationFailedException,
			UserAlreadyLoggedException {
		csc.logYourUser("ajeje", "brazorf");
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testLogYourUser3() throws AutenticationFailedException,
			UserAlreadyLoggedException {
		try {
			csc.createNewUser("jkl", "jkl");
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		}
		csc.logYourUser("jkl", "jkl");
		csc.logYourUser("jkl", "jkl");
	}

	@Test(expected = RaceNameTakenException.class)
	public void testCreateNewRace() throws RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {

		String provaToken = null;
		try {
			csc.createNewUser("prova", "prova");
			provaToken = csc.logYourUser("prova", "prova");
			csc.createNewRace(provaToken, "prova", "e");
			csc.gameAccess(provaToken);
			csc.createNewUser("prova5", "prova5");
			String prova5Token = csc.logYourUser("prova5", "prova5");
			csc.createNewRace(prova5Token, "prova", "e");
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
		} finally {
			try {
				csc.gameOut(provaToken);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}
	}

	@Test(expected = InvalidTokenException.class)
	public void testCreateNewRace2() throws RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {

		csc.createNewRace("x", "pippoRace", "e");
	}

	@Test(expected = RaceAlreadyExistsException.class)
	public void testCreateNewRace3() throws RaceNameTakenException,
			InvalidTokenException, RaceAlreadyExistsException {

		try {
			csc.createNewUser("prova2", "prova2");
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		}

		try {
			String provaToken = csc.logYourUser("pluto", "pluto");
			csc.createNewRace(provaToken, "plutoRace", "e");
			csc.createNewRace(provaToken, "plutoRace2", "e");
		} catch (AutenticationFailedException e) {
			
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			
			e.printStackTrace();
		}
	}

	@Test(expected = TooPlayingUserException.class)
	public void testGameAccess() throws UserAlreadyEnteredInGame,
			TooPlayingUserException, RaceNullException {
		try {
			csc.createNewUser("1", "1");
			csc.createNewUser("2", "2");
			csc.createNewUser("3", "3");
			csc.createNewUser("4", "4");
			csc.createNewUser("5", "5");
			csc.createNewUser("6", "6");
			csc.createNewUser("7", "7");
			csc.createNewUser("8", "8");
			csc.createNewUser("9", "9");
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		}

		try {
			String token1 = csc.logYourUser("1", "1");
			String token2 = csc.logYourUser("2", "2");
			String token3 = csc.logYourUser("3", "3");
			String token4 = csc.logYourUser("4", "4");
			String token5 = csc.logYourUser("5", "5");
			String token6 = csc.logYourUser("6", "6");
			String token7 = csc.logYourUser("7", "7");
			String token8 = csc.logYourUser("8", "8");
			String token9 = csc.logYourUser("9", "9");

			csc.createNewRace(token1, "p", "e");
			csc.createNewRace(token2, "o", "e");
			csc.createNewRace(token3, "u", "e");
			csc.createNewRace(token4, "y", "e");
			csc.createNewRace(token5, "hhh", "e");
			csc.createNewRace(token6, "j", "e");
			csc.createNewRace(token7, "k", "e");
			csc.createNewRace(token8, "l", "e");
			csc.createNewRace(token9, "m", "e");

			csc.gameAccess(token1);
			csc.gameAccess(token2);
			csc.gameAccess(token3);
			csc.gameAccess(token4);
			csc.gameAccess(token5);
			csc.gameAccess(token6);
			csc.gameAccess(token7);
			csc.gameAccess(token8);
			csc.gameAccess(token9);

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
		} finally {
			try {
				csc.gameOut("11");
				csc.gameOut("21");
				csc.gameOut("31");
				csc.gameOut("41");
				csc.gameOut("51");
				csc.gameOut("61");
				csc.gameOut("71");
				csc.gameOut("81");

				csc.logOut("11");
				csc.logOut("21");
				csc.logOut("31");
				csc.logOut("41");
				csc.logOut("51");
				csc.logOut("61");
				csc.logOut("71");
				csc.logOut("81");
				csc.logOut("91");

			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}

		}

	}

	@Test(expected = RaceNullException.class)
	public void testGameAccess2() throws UserAlreadyEnteredInGame,
			InvalidTokenException, TooPlayingUserException, RaceNullException {
		try {
			csc.createNewUser("x", "x");
			String tokenx = csc.logYourUser("x", "x");
			csc.gameAccess(tokenx);
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			
			e.printStackTrace();
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testGameAccess3() throws UserAlreadyEnteredInGame,
			InvalidTokenException, TooPlayingUserException, RaceNullException {
		csc.gameAccess("abc");
	}

	@Test(expected = UserAlreadyEnteredInGame.class)
	public void testGameAccess4() throws UserAlreadyEnteredInGame,
			InvalidTokenException, TooPlayingUserException, RaceNullException {
		try {
			csc.createNewUser("r", "r");
			String tokenr = csc.logYourUser("r", "r");
			csc.createNewRace(tokenr, "rRace", "e");
			csc.gameAccess(tokenr);
			csc.gameAccess(tokenr);
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
		} finally {
			try {
				csc.gameOut("r1");
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
			csc.logOut("r1");
		}

	}

	@Test(expected = UserAlreadyExited.class)
	public void testGameOut() throws UserAlreadyExited {
		try {
			csc.createNewUser("h", "h");
			String tokenh = csc.logYourUser("h", "h");
			csc.createNewRace(tokenh, "h", "e");
			csc.gameAccess(tokenh);
			csc.playingList(tokenh);
			csc.gameOut(tokenh);
			csc.gameOut(tokenh);
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
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testGameOut2() throws UserAlreadyExited, InvalidTokenException {
		csc.gameOut("awe");
	}

	@Test(expected = InvalidTokenException.class)
	public void testPlayingList() throws InvalidTokenException {
		csc.playingList("abc");
	}

	@Test(expected = InvalidTokenException.class)
	public void testLogOut() throws InvalidTokenException {
		csc.logOut("abc");
	}

	@Test(expected = InvalidTokenException.class)
	public void testGiveMeRanking() throws InvalidTokenException {
		csc.giveMeRanking("abc");
	}

	@Test
	public void testGiveMeRanking2() throws InvalidTokenException {

		try {
			csc.createNewUser("qwerty", "qwerty");
			String tokenqwerty = csc.logYourUser("qwerty", "qwerty");
			csc.createNewRace(tokenqwerty, "qwerty", "e");
			assertNotNull(csc.giveMeRanking(tokenqwerty));
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
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForMap() throws InvalidTokenException,
			UserNotPlayingException {
		csc.askForMap("xyz");
	}

	@Test(expected = UserNotPlayingException.class)
	public void testAskForMap2() throws InvalidTokenException,
			UserNotPlayingException {
		try {
			csc.createNewUser("try", "try");
			String tokentry = csc.logYourUser("try", "try");
			csc.askForMap(tokentry);
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			
			e.printStackTrace();
		}

	}

	@Test
	public void testAskForMap3() {
		try {
			csc.gameAccess(beforeToken);
			assertNotNull(csc.askForMap(beforeToken));
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
		} finally {
			try {
				csc.gameOut(beforeToken);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}
	}

	// ///
	@Test(expected = UserNotPlayingException.class)
	public void testAskForlocalView() throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {

		try {
			csc.createNewUser("aaaa", "aaaa");
			String tokenaaaa = csc.logYourUser("aaaa", "aaaa");
			csc.createNewRace(tokenaaaa, "aaaa", "e");
			csc.askForlocalView(tokenaaaa, "a");
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
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForlocalView2() throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {
		csc.askForlocalView("abc", "a");
	}

	@Test(expected = InvalidIdException.class)
	public void testAskForlocalView3() throws InvalidTokenException,
			UserNotPlayingException, InvalidIdException {
		try {
			csc.gameAccess(beforeToken);
			csc.askForlocalView(beforeToken, "abc");
		} catch (UserAlreadyEnteredInGame e) {
			
			e.printStackTrace();
		} catch (TooPlayingUserException e) {
			
			e.printStackTrace();
		} catch (RaceNullException e) {
			
			e.printStackTrace();
		} finally {
			try {
				csc.gameOut(beforeToken);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testAskForlocalView4() {
		try {
			csc.createNewUser("ciao", "ciao");
			String tokenCiao = csc.logYourUser("ciao", "ciao");
			csc.createNewRace(tokenCiao, "ciaoRace", "c");
			csc.gameAccess(tokenCiao);
			String[] listCiao = csc.askForList(tokenCiao);
			assertNotNull(csc.askForlocalView(tokenCiao, listCiao[0]));
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
		} finally {
			try {
				csc.gameOut("ciao1");
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testAskForLocalView5() {
		String tokenok = null;
		try {
			csc.createNewUser("ok8", "ok8");
			tokenok = csc.logYourUser("ok8", "ok8");
			csc.createNewRace(tokenok, "o8k", "c");
			csc.gameAccess(tokenok);
			String[] list = csc.askForList(tokenok);
			csc.askForlocalView(tokenok, list[0]);
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
		} finally {
			try {
				csc.gameOut(tokenok);
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test(expected = UserNotPlayingException.class)
	public void testAskForList() throws InvalidTokenException,
			UserNotPlayingException {

		try {
			csc.createNewUser("abcde", "abcde");
			csc.logYourUser("abcde", "abcde");
		} catch (BusyUsernameException e) {
			
			e.printStackTrace();
		} catch (AutenticationFailedException e) {
			
			e.printStackTrace();
		} catch (UserAlreadyLoggedException e) {
			
			e.printStackTrace();
		}

		csc.askForList("abcde1");
	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForList2() throws InvalidTokenException,
			UserNotPlayingException {
		csc.askForList("abc");
	}

	@Test(expected = InvalidTokenException.class)
	public void testAskForStatus() throws UserNotPlayingException,
			InvalidTokenException, InvalidIdException {
		csc.askForStatus("abc", "abc");
	}

	@Test(expected = InvalidIdException.class)
	public void testAskForStatus2() throws UserNotPlayingException,
			InvalidTokenException, InvalidIdException {
		String tokenbarba = null;
		try {
			csc.createNewUser("barba", "barba");
			tokenbarba = csc.logYourUser("barba", "barba");
			csc.createNewRace(tokenbarba, "barba", "c");
			csc.gameAccess(tokenbarba);
			csc.askForStatus(tokenbarba, "qwertyu");
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
		} finally {
			try {
				csc.gameOut(tokenbarba);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test(expected = UserNotPlayingException.class)
	public void testAskForStatus3() throws UserNotPlayingException,
			InvalidTokenException, InvalidIdException {
		csc.askForStatus(beforeToken, "abc");
	}

	@Test
	public void testAskForStatus4() {
		String tokenok = null;
		try {
			csc.createNewUser("ok9", "ok9");
			tokenok = csc.logYourUser("ok9", "ok9");
			csc.createNewRace(tokenok, "o9k", "c");
			csc.gameAccess(tokenok);
			String[] list = csc.askForList(tokenok);
			csc.askForStatus(tokenok, list[0]);
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
		} finally {
			try {
				csc.gameOut(tokenok);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testMoveDinosaur() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		csc.moveDinosaur("abc", "abc", 0, 0);
	}

	@Test(expected = InvalidIdException.class)
	public void testMoveDinosaur2() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		try {
			csc.gameAccess(beforeToken);
			csc.moveDinosaur(beforeToken, "abc", 0, 0);
		} catch (UserAlreadyEnteredInGame e) {
			
			e.printStackTrace();
		} catch (TooPlayingUserException e) {
			
			e.printStackTrace();
		} catch (RaceNullException e) {
			
			e.printStackTrace();
		} finally {
			try {
				csc.gameOut(beforeToken);
			} catch (UserAlreadyExited e) {
				
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
			csc.createNewUser("ao", "ao");
			tokenao = csc.logYourUser("ao", "ao");
			csc.createNewRace(tokenao, "ao", "e");
			csc.gameAccess(tokenao);
			Thread.sleep(4000);
			csc.confirm(tokenao);
			String[] list = csc.askForList(tokenao);
			String[] status = csc.askForStatus(tokenao, list[0]);
			csc.moveDinosaur(tokenao, list[0],
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
		} finally {
			try {
				csc.gameOut(tokenao);
			} catch (UserAlreadyExited e) {
				
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
			csc.createNewUser("xxx", "xxx");
			tokenauser = csc.logYourUser("xxx", "xxx");
			csc.createNewRace(tokenauser, "xxx", "e");
			Specie spec = game.fromUsertoRace("xxx");
			
			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setDimension(1);
			denver.setCurrentEnergy(5);
			denver.setxPosition(5);
			denver.setyPosition(5);
			csc.gameAccess(tokenauser);
			Thread.sleep(4000);
			csc.confirm(tokenauser);

			String[] list = csc.askForList(tokenauser);
			csc.askForStatus(tokenauser, list[0]);

			csc.moveDinosaur(tokenauser, denver.getIdDino(),
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
		}
	}

	@Test(expected = MovesLimitExceededException.class)
	public void testMoveDinosaur5() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		String tokenauser = null;
		Dinosaur denver = null;
		try {
			game = sft.getGame();
			csc.createNewUser("zxxc", "zxxc");
			tokenauser = csc.logYourUser("zxxc", "zxxc");
			csc.createNewRace(tokenauser, "zxxc", "e");
			Specie spec = game.fromUsertoRace("zxxc");
			
			denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setxPosition(5);
			denver.setyPosition(5);
			csc.gameAccess(tokenauser);
			Thread.sleep(4000);
			csc.confirm(tokenauser);
			
			String[] list = csc.askForList(tokenauser);
			csc.askForStatus(tokenauser, list[0]);

			csc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 1, 49 - (denver.getColPosition()+1));
			
			csc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 1, 49 - denver.getColPosition()+1);
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
		} catch(NotYourTurnException e){
			
			try {
				Thread.sleep(4000);
				csc.confirm(tokenauser);
				
				String[] list = csc.askForList(tokenauser);
				csc.askForStatus(tokenauser, list[0]);

				csc.moveDinosaur(tokenauser, denver.getIdDino(),
						denver.getRowPosition() + 1, 49 - (denver.getColPosition()+1));
				
				csc.moveDinosaur(tokenauser, denver.getIdDino(),
						denver.getRowPosition() + 1, 49 - denver.getColPosition()+1);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			
		} finally {
			try {
				csc.gameOut(tokenauser);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}
	}

	//Movimento di tre per il carnivoro
	@Test
	public void testMoveDinosaur6() throws YouWonTheBattle, YouLostTheBattle,
			InvalidIdException, InvalidTokenException,
			DestinationUnreachableException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {
		String tokenauser = null;
		try {
			game = sft.getGame();
			csc.createNewUser("zxc", "zxc");
			tokenauser = csc.logYourUser("zxc", "zxc");
			csc.createNewRace(tokenauser, "zxc", "c");
			Specie spec = game.fromUsertoRace("zxc");
			
			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setxPosition(5);
			denver.setyPosition(5);
			csc.gameAccess(tokenauser);
			Thread.sleep(2000);
			csc.confirm(tokenauser);

			String[] list = csc.askForList(tokenauser);
			csc.askForStatus(tokenauser, list[0]);

			csc.moveDinosaur(tokenauser, denver.getIdDino(),
					denver.getRowPosition() + 3, 49 - (denver.getColPosition()+3));
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
		} finally {
			try {
				csc.gameOut(tokenauser);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testLetYourDinosaurGrow() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		csc.letYourDinosaurGrow("abc", "abc");
	}

	@Test(expected = InvalidIdException.class)
	public void testLetYourDinosaurGrow2() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenW = null;
		try {
			csc.createNewUser("w", "w");
			tokenW = csc.logYourUser("w", "w");
			csc.createNewRace(tokenW, "wRace", "e");
			csc.gameAccess(tokenW);
			csc.letYourDinosaurGrow(tokenW, "abc");
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
		} finally {
			try {
				csc.gameOut(tokenW);
			} catch (UserAlreadyExited e) {
				
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
			csc.createNewUser("q", "q");
			tokenQ = csc.logYourUser("q", "q");
			csc.createNewRace(tokenQ, "qRace", "e");
			
			Specie spec = game.fromUsertoRace("q");
			
			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setDimension(5);
			denver.setCurrentEnergy(5000);			
			
			csc.gameAccess(tokenQ);
			Thread.sleep(4000);
			csc.confirm(tokenQ);
			
			String[] list = csc.askForList(tokenQ);
			csc.letYourDinosaurGrow(tokenQ, list[0]);
			
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
		} finally {
			try {
				csc.gameOut(tokenQ);
			} catch (UserAlreadyExited e) {
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
			csc.createNewUser("ajejeje", "ajejeje");
			tokenaje = csc.logYourUser("ajejeje", "ajejeje");
			csc.createNewRace(tokenaje, "aje", "e");
			csc.gameAccess(tokenaje);
			String[] list = csc.askForList(tokenaje);
			RaceManager raceMan = new RaceManager();
			Specie spec = raceMan.fromUsertoRace("ajejeje");
			Dinosaur denver = spec.checkIdAndReturnDino(list[0]);
			denver.setDimension(3);
			denver.setCurrentEnergy(3000);
			csc.confirm(tokenaje);
			csc.letYourDinosaurGrow(tokenaje, list[0]);
			csc.letYourDinosaurGrow(tokenaje, list[0]);
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
		} finally {
			try {
				csc.gameOut(tokenaje);
			} catch (UserAlreadyExited e) {
				
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
			csc.createNewUser("barbie", "raperonzolo");
			tokenBarbie = csc.logYourUser("barbie", "raperonzolo");
			csc.createNewRace(tokenBarbie, "bar", "e");
			
			
			Specie spec = game.fromUsertoRace("barbie");
			
			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			denver.setCurrentEnergy(100);
			
			csc.gameAccess(tokenBarbie);
			Thread.sleep(4000);
			csc.confirm(tokenBarbie);
			
			String[] list = csc.askForList(tokenBarbie);
			csc.letYourDinosaurGrow(tokenBarbie, list[0]);
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
		} 	 
	  }
	@Test(expected = UserNotPlayingException.class)
	public void testLetYourDinosaurGrow6() throws InvalidIdException,
			InvalidTokenException, MaxDimensionReachedException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {

		try {
			csc.createNewUser("simone", "meme");
			String tokenSimone = csc.logYourUser("simone", "meme");
			csc.createNewRace(tokenSimone, "simoRace", "e");
			csc.letYourDinosaurGrow(tokenSimone, "x");
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
			csc.createNewUser("simone1", "meme1");
			tokenSimone1 = csc.logYourUser("simone1", "meme1");
			csc.createNewRace(tokenSimone1, "simo1Race", "e");
			csc.gameAccess(tokenSimone1);

			csc.createNewUser("simone2", "meme2");
			tokenSimone2 = csc.logYourUser("simone2", "meme2");
			csc.createNewRace(tokenSimone2, "simo2Race", "e");
			csc.gameAccess(tokenSimone2);

			String[] list = csc.askForList(tokenSimone2);
			csc.letYourDinosaurGrow(tokenSimone2, list[0]);

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
		} finally {
			try {
				csc.gameOut(tokenSimone1);
				csc.gameOut(tokenSimone2);
			} catch (UserAlreadyExited e) {
				
				e.printStackTrace();
			}
		}

	}

	@Test(expected = InvalidTokenException.class)
	public void testLeaveYourEgg() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		csc.leaveYourEgg("aaaaa", "WW");
	}

	@Test(expected = InvalidIdException.class)
	public void testLeaveYourEgg2() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenpape = null;
		try {
			csc.createNewUser("paperino", "paperino");
			tokenpape = csc.logYourUser("paperino", "paperino");
			csc.createNewRace(tokenpape, "pap", "e");
			csc.gameAccess(tokenpape);
			csc.leaveYourEgg(tokenpape, "xxxx");
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
		} finally {
			try {
				csc.gameOut(tokenpape);
			} catch (UserAlreadyExited e) {
				
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
			csc.createNewUser("batman", "batman");
			tokenbat = csc.logYourUser("batman", "batman");
			csc.createNewRace(tokenbat, "bat", "e");
			csc.gameAccess(tokenbat);
			String[] list = csc.askForList(tokenbat);
			csc.confirm(tokenbat);
			csc.leaveYourEgg(tokenbat, list[0]);
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
		}
	}

	
	@Test(expected = MovesLimitExceededException.class)
	public void testLeaveYourEgg4() throws InvalidIdException,
			InvalidTokenException, NumberOfDinosaursExceededException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {
		String tokenrob = null;
		try {
			csc.createNewUser("robin", "robin");
			tokenrob = csc.logYourUser("robin", "robin");
			csc.createNewRace(tokenrob, "rob", "e");
			csc.gameAccess(tokenrob);
			Thread.sleep(4000);
			csc.confirm(tokenrob);
			String[] list = csc.askForList(tokenrob);
			csc.letYourDinosaurGrow(tokenrob, list[0]);
			csc.leaveYourEgg(tokenrob, list[0]);
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
		} catch (NotYourTurnException e){
			
			try {
				Thread.sleep(4000);
				csc.confirm(tokenrob);
				String[] list = csc.askForList(tokenrob);
				csc.letYourDinosaurGrow(tokenrob, list[0]);
				csc.leaveYourEgg(tokenrob, list[0]);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			} catch (MaxDimensionReachedException e1) {
				
				e1.printStackTrace();
			}
			
			
		}finally {
			try {
				csc.gameOut(tokenrob);
			} catch (UserAlreadyExited e) { 
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
			csc.createNewUser("aj", "aj");
			tokenaje = csc.logYourUser("aj", "aj");
			csc.createNewRace(tokenaje, "aj", "e");
			Specie spec = game.fromUsertoRace("aj");
			
			Dinosaur denver = spec.checkIdAndReturnDino(spec.getDinos().get(0)
					.getIdDino());
			
			denver.setDimension(3);
			denver.setCurrentEnergy(3000);
			
			spec.addDenver(new Dinosaur(5, 5, spec));
			spec.addDenver(new Dinosaur(5, 6, spec));
			spec.addDenver(new Dinosaur(5, 7, spec));
			spec.addDenver(new Dinosaur(5, 8, spec));
			
			csc.gameAccess(tokenaje);
			Thread.sleep(4000);
			csc.confirm(tokenaje);
			
			String[] list = csc.askForList(tokenaje);
			csc.leaveYourEgg(tokenaje, list[0]);

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
		} catch(NotYourTurnException e){
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			csc.confirm(tokenaje);
			
			String[] list = csc.askForList(tokenaje);
			csc.leaveYourEgg(tokenaje, list[0]);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				csc.gameOut(tokenaje);
			} catch (UserAlreadyExited e) { 
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
			csc.createNewUser("c", "c");
			String tokenc = csc.logYourUser("c", "c");
			csc.createNewRace(tokenc, "c", "e");
			csc.leaveYourEgg(tokenc, "ww");
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
			csc.createNewUser("e", "e");
			csc.createNewUser("f", "f");
			csc.createNewUser("g", "g");

			tokene = csc.logYourUser("e", "e");
			tokenf = csc.logYourUser("f", "f");
			tokeng = csc.logYourUser("g", "g");

			csc.createNewRace(tokene, "bnm", "e");
			csc.createNewRace(tokenf, "cvb", "e");
			csc.createNewRace(tokeng, "zxccx", "e");

			csc.gameAccess(tokene);
			Thread.sleep(4000);
			csc.confirm(tokene);
			csc.gameAccess(tokenf);
			csc.gameAccess(tokeng);

			String[] liste = csc.askForList(tokene);
			String[] listf = csc.askForList(tokenf);
			String[] listg = csc.askForList(tokeng);

			csc.leaveYourEgg(tokeng, listg[0]);
			csc.leaveYourEgg(tokenf, listf[0]);
			csc.leaveYourEgg(tokene, liste[0]);

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
		} finally {
			csc.logOut(tokene);
			csc.logOut(tokenf);
			csc.logOut(tokeng);
		}

	}

}
