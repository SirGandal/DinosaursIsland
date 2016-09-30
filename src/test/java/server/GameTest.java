package server;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.List;

import javax.xml.transform.Templates;

import map.Cell;
import map.MapManager;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import dinosaur.Dinosaur;
import dinosaur.Specie;
import exception.BusyUsernameException;
import exception.DestinationUnreachableException;
import exception.InvalidIdException;
import exception.InvalidTokenException;
import exception.NewDinosaurException;
import exception.NumberOfDinosaursExceededException;
import exception.RaceNullException;
import exception.UserNotPlayingException;

/**
 * @author gas12n
 */

public class GameTest {

	private static String token;
	private static String enemyToken;
	private static Cell[][] globalMap; /**
	 * @uml.property name="game"
	 * @uml.associationEnd
	 */
	private static Game game;
	/**
	 * @uml.property name="s"
	 * @uml.associationEnd
	 */
	private static Server s;

	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		InitDB.main(null);
		MapManager mm = new MapManager();
		globalMap = mm.getGlobalMap();
		s = new Server();
		game = new Game(s);
		game.checkUsers("prova", "prova");
		token = game.logUsers("prova", "prova");
		System.out.println(token);
		game.createNewRace("prova1", "razzadiprova", "e");
		game.gameAccess(token);
		game.checkUsers("nemico", "nemico");
		enemyToken = game.logUsers("nemico", "nemico");
		System.out.println(enemyToken);
		game.createNewRace(enemyToken, "razzanemica", "c");
		game.gameAccess(enemyToken);
	}

	@Test
	public void testCheckUsers() {
		Game game = new Game(s);
		assertEquals("@ok", game.checkUsers("pippo", "pippo"));
		assertEquals("@ok", game.checkUsers("pippo f", "pippo"));
		assertEquals("@no,@usernameOccupato", game.checkUsers("pippo", "awad"));
		assertEquals("@no,@usernameOccupato", game.checkUsers(null, null));
		assertEquals("@no,@usernameOccupato", game.checkUsers("pippo0", "awad"));
	}

	/**
	 * Test method for
	 * {@link server.Game#logUsers(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLogUsers() {
		InitDB.main(null);
		Game game = new Game(s);
		int support = 0;
		assertEquals("@no,@autenticazioneFallita",
				game.logUsers("pippo", "pippo"));
		game.checkUsers("a", "a");
		assertEquals("a" + ++support, game.logUsers("a", "a"));
		assertEquals("@no", game.logUsers("a", "a"));
		assertEquals("@no,@autenticazioneFallita", game.logUsers(null, null));
	}

	/**
	 * Test method for
	 * {@link server.Game#createNewRace(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */

	@Test
	public void testCreateNewRace() {

		assertEquals("@no,@razzaGiaCreata",
				game.createNewRace(token, "cattivi", "c"));
		Game game = new Game(s);
		game.checkUsers("asd", "asd");
		String token = game.logUsers("asd", "asd");
		assertEquals("@no,@tokenNonValido",
				game.createNewRace("blabla", "buonissimi", "c"));
		assertEquals("@ok", game.createNewRace(token, "cattivi", "c"));
		game.checkUsers("asd2", "asd2");
		String token2 = game.logUsers("asd2", "asd2");
		assertEquals("@no,@nomeRazzaOccupato",
				game.createNewRace(token2, "cattivi", "c"));

	}

	@Test
	public void testGameAccess() {
		assertNotSame("@ok", game.gameAccess(token));
		assertEquals("@no,@tokenNonValido", game.gameAccess("tokenfuffa"));
		game.gameOut(enemyToken);
		assertEquals("@ok", game.gameAccess(enemyToken));
	}

	@Test
	public void denverStateTest() {
		InitDB.main(null);
		LoggedUser currentUser;
		try {
			currentUser = game.returnUserfromPlayingList(token);
			Specie spec = game.fromUsertoRace(currentUser.getUsername());
			Dinosaur dino = spec.getDinos().get(0);
			Thread.sleep(3000);
			game.acceptRound(token);
			int row = dino.getRowPosition();
			int col = dino.getColPosition();

			LoggedUser enemyUser = game.returnUserfromPlayingList(enemyToken);
			Specie enemYSpec = game.fromUsertoRace(enemyUser.getUsername());
			Dinosaur enemyDino = enemYSpec.getDinos().get(0);
			enemyDino.setxPosition(col - 1);
			enemyDino.setyPosition(row);
			enemyDino.setCurrentEnergy(1000);
			Thread.sleep(2500);
			game.acceptRound(enemyToken);
			game.denverState(token, enemyDino.getIdDino());

		} catch (UserNotPlayingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RaceNullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void denverMoveTest() {
		Specie spec;
		String temp;

		try {
			InitDB.cleanMap();
			LoggedUser currentUser = game.returnUserfromPlayingList(token);
			spec = game.fromUsertoRace(currentUser.getUsername());
			Dinosaur dino = spec.getDinos().get(0);
			dino.setyPosition(5);
			dino.setxPosition(5);
			int oldRow = dino.getRowPosition();			
			
			do{
			temp = game.acceptRound(token);
			System.out.println("Tentativo accetta turno\n");}
			while(!temp.equals("@ok"));
			
			int oldCurrentEnergy = dino.getCurrentEnergy();
			game.denverMove(token, dino.getIdDino(), 5,
				7);
			System.out.println("nuovo " + dino.getColPosition() + " Vecchio "
					+ oldRow);
			
			/*Si muove, si stanca e mangia*/
			assertTrue(dino.getColPosition() != oldRow);
			assertTrue(dino.getCurrentEnergy() > oldCurrentEnergy - 40);
			globalMap[5][7].setCurrentEnergyVegetation(globalMap[5][7].getEnergyVegetation());
			dino.setCurrentEnergy(dino.getMaxEnergy());
			oldCurrentEnergy = dino.getCurrentEnergy();
		
			game.denverMove(temp, dino.getIdDino(), 6, 7);
			/*Non mangia*/
			assertTrue(oldCurrentEnergy == dino.getCurrentEnergy());
			
			game.passRound(token);
			Thread.sleep(1500);
			game.changeRound();
			Thread.sleep(1500);
			int row = dino.getRowPosition();
			int col = dino.getColPosition();
			LoggedUser enemyUser = game.returnUserfromPlayingList(enemyToken);
			Specie enemYSpec = game.fromUsertoRace(enemyUser.getUsername());
			Dinosaur enemyDino = enemYSpec.getDinos().get(0);
			enemyDino.setxPosition(col - 1);
			enemyDino.setyPosition(row);
			enemyDino.setCurrentEnergy(1000);
			spec.addDenver(dino);
			assertTrue(spec.getDinos().size() == 2);

			System.out.println(spec.getCondition());
			Thread.sleep(2500);
			game.acceptRound(enemyToken);
			int oldSize = spec.getDinos().size();
			enemyDino.setCurrentEnergy(800);
			enemYSpec.setType("e");
			
			/*Non sale sugli erbivori*/
			assertEquals("@no,@destinazioneNonValida", game.denverMove(enemyToken, enemyDino.getIdDino(), row, col));
			
			enemYSpec.setType("c");
			int oldEnemyPower = enemyDino.getCurrentEnergy();
			game.denverMove(enemyToken, enemyDino.getIdDino(), row, col);

			/*Si muore*/
			assertTrue(spec.getDinos().size() != 0);
			assertTrue(spec.getDinos().size() != oldSize);
			game.ranking(token);
			
			/*Si mangia i dino ammazzati*/
			assertTrue(spec.getCondition().equals("s"));
			assertTrue(enemyDino.getCurrentEnergy() > oldEnemyPower);
			enemyDino.setyPosition(8);
			enemyDino.setxPosition(11);
			oldEnemyPower = enemyDino.getCurrentEnergy();
			game.denverMove(enemyToken, enemyDino.getIdDino(), 9, 11);
			assertTrue(enemyDino.getCurrentEnergy() > oldEnemyPower - 20);
			

			/*
			 * row = enemyDino.getRowPosition(); col =
			 * enemyDino.getColPosition(); game.passRound(enemyToken);
			 * Thread.sleep(1000); game.changeRound(); Thread.sleep(2000);
			 * game.acceptRound(token); int dinoOldEnergy =
			 * dino.getCurrentEnergy(); game.denverMove(token, dino.getIdDino(),
			 * row, col); assertTrue(enemYSpec.getCondition().equals("n"));
			 * assertTrue(dino.getCurrentEnergy() > dinoOldEnergy);
			 */
			game.gameOut(enemyToken);
			game.logOut(enemyToken);
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (RaceNullException e) {
			e.printStackTrace();
		} catch (UserNotPlayingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NumberOfDinosaursExceededException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void layAnEggTest() {
		int loop = 0;
		Specie spec;

		while (loop < 10) {
			InitDB.cleanMap();
			try {
				LoggedUser currentUser = game.returnUserfromPlayingList(token);
				System.out.println(currentUser.getUsername());
				spec = game.fromUsertoRace(currentUser.getUsername());
				Dinosaur dino = spec.getDinos().get(0);
				game.acceptRound(token);
				dino.setMaxEnergy(1000000);
				dino.setCurrentEnergy(100000);
				dino.setAge(1);
				spec.setLivedRound(1);
				int i = 0;
				while (i < 4) {
					int oldDenverListSize = spec.getDenverList(token).size();
					System.out.println("Senza uovo " + oldDenverListSize);
					game.layAnEgg(dino.getIdDino(), currentUser.getToken());
					game.pleaseGrowUpAll();
					System.out.println("Dopo deposizione "
							+ spec.getDenverList(currentUser.getToken()).size()
							+ " con energia " + dino.getCurrentEnergy()
							+ "\n \n");
					if (spec.getDenverList(currentUser.getToken()).size() > oldDenverListSize) {
						assertTrue(spec.getDenverList(currentUser.getToken())
								.size() > oldDenverListSize);
					} else {
						for (String tempDino : spec.getDenverList(token)) {
							System.out.println(tempDino);
						}
					}
					i++;

				}
				System.out.println("FINE ci sono "
						+ spec.getDenverList(token).size() + " dinos");
				while (spec.getDinos().size() != 1) {
					spec.getDinos().remove(1);
				}
			} catch (InvalidTokenException e) {
				e.printStackTrace();
			} catch (RaceNullException e) {
				e.printStackTrace();
			} catch (UserNotPlayingException e) {
				e.printStackTrace();
			}
			loop++;

		}
	}

	@Test
	public void dieDenverDie() {
		LoggedUser currentUser;
		int i = 0;
		try {game.gameOut(enemyToken);
			currentUser = game.returnUserfromPlayingList(token);
			System.out.println(currentUser.getUsername());
			while (i < 5) {
				Specie spec = game.fromUsertoRace(currentUser.getUsername());
				Dinosaur dino = spec.getDinos().get(0);
				dino.setAge(dino.getMaxAge());
				System.out.println(" TURNO # " + i + " Prima di crescere ho " + dino.getAge()
						+ " anni, ma posso vivere max " + dino.getMaxAge() + "\n");
				game.pleaseGrowUpAll();
				System.out.println("\nAdesso ho " + dino.getAge() + " anni");
				assertTrue(spec.getCondition().equals("n"));

				game.createNewRace(token, "nuovaSpecie" + i, "c");
				game.gameAccess(token);
				Thread.sleep(2000);
				game.acceptRound(token);
				i++;
			}
		
		} catch (UserNotPlayingException e) {
			e.printStackTrace();
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (RaceNullException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void removeRaceTest(){
		Specie spec;

			LoggedUser currentUser;
			try {
				currentUser = game.returnUserfromPlayingList(token);
				spec = game.fromUsertoRace(currentUser.getUsername());
				spec.setLivedRound(120);
				game.pleaseGrowUpAll();
				assertTrue(spec.getCondition().equals("n"));
				assertTrue(game.returnUserfromLoggedList(token).getRace() == null);
								
			} catch (UserNotPlayingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTokenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RaceNullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
