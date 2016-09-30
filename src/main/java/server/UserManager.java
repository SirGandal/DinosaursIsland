package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import dinosaur.RaceManager;
import dinosaur.Specie;

import exception.AutenticationFailedException;
import exception.BusyUsernameException;
import exception.InvalidTokenException;
import exception.RaceAlreadyExistsException;
import exception.RaceNameTakenException;
import exception.RaceNullException;
import exception.TooPlayingUserException;
import exception.UserNotPlayingException;

/**
 * @author  gas12n
 */
public class UserManager {
	private ArrayList<User> users = new ArrayList<User>();
	private List<LoggedUser> loggedUsers = new ArrayList<LoggedUser>();
	private List<LoggedUser> playingUsers = new ArrayList<LoggedUser>();
	/**
	 * @uml.property  name="raceMan"
	 * @uml.associationEnd  
	 */
	private RaceManager raceMan;
	private FileOutputStream fos;
	private ObjectOutputStream oos;

	@SuppressWarnings("unchecked")
	/**
	 * Costruttore della classe UserManager
	 */
	public UserManager() {
		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = new FileInputStream("src/main/resources/Database/usersDB.out");
			ois = new ObjectInputStream(fis);
			users = (ArrayList<User>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * Il metodo permette il salvataggio del database dei giocatori
	 */
	public void saveUsers() {

		try {
			fos = new FileOutputStream(
					"src/main/resources/Database/usersDB.out");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(users);
			oos.flush();
			oos.close();
			fos.close();

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public final void UserManagerGo(RaceManager raceMan) {
		/* Inizializza il manager delle razze */
		this.raceMan = raceMan;
	}

	/**
	 * @return
	 * @uml.property  name="loggedUsers"
	 */
	public List<LoggedUser> getLoggedUsers() {
		return loggedUsers;
	}

	/**
	 * @return
	 * @uml.property  name="playingUsers"
	 */
	public List<LoggedUser> getPlayingUsers() {
		return playingUsers;
	}

	/**
	 * Restituisce true se e' possibile creare in nuovo utente false se
	 * l'username gia' esiste
	 * 
	 * @param username
	 *            Username dell'utente
	 * @param users
	 *            Arraylist degli utenti
	 * @return Ritorna true se e' possibile creare tale utente
	 */
	public final boolean scanUser(String username, List<User> users) {
		/*
		 * 
		 */
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Setta e aggiunge l'utente all'array degli utenti e restituisce la stringa
	 * da protocollo, affermando se e' possibile o meno creare un nuovo utente
	 * con username username
	 * 
	 * @param username
	 *            Username dell'utente da controllare
	 * @param password
	 *            Password dell'utente da controllare
	 * @throws BusyUsernameException
	 */
	public final void isUserBusy(String username, String password)
			throws BusyUsernameException {
		/*
		 * Setta e aggiunge l'utente all'array degli utenti e restituisce la
		 * stringa da protocollo, affermando se e' possibile o meno creare un
		 * nuovo utente con username username
		 */
		boolean freeUser = false;
		if ((username != null) && scanUser(username, users)
				&& !username.contains("0")) {
			freeUser = true;
			User account = new User(username, password);
			users.add(account);
			saveUsers();
			// println di prova
			//System.out.println(account.getUsername());
			//System.out.println(account.getPassword());
		}

		if (!freeUser) {
			throw new BusyUsernameException();
		}
	}

	/**
	 * Il metodo controlla se al token passatogli corrisponde gia' una razza In
	 * caso abbia gia' una razza lancia RaceAlreadyExistsException o nel caso il
	 * token sia non valido lancia RaceAlreadyExistsException
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @throws RaceAlreadyExistsException
	 * @throws InvalidTokenException
	 */
	public final void checkRaceToken(String token)
			throws RaceAlreadyExistsException, InvalidTokenException {
		/*
		 * Il metodo controlla se al token passatogli corrisponde gia' una razza
		 * In caso abbia gia' una razza lancia RaceAlreadyExistsException o nel
		 * caso il token sia non valido lancia RaceAlreadyExistsException
		 */
		LoggedUser logUser;
		try {
			logUser = returnUserfromLoggedList(token);
			if ((logUser.getRace() != null)) {
				throw new RaceAlreadyExistsException();
			}
		} catch (InvalidTokenException e) {
			throw e;
		}
	}

	/**
	 * Il metodo scorre l'array degli utenti loggati e tenta di accedere alla
	 * specie di ognuno di essi. Nel caso esista una specie, ne chiede il nome e
	 * lo confronta con quello che si desidera creare. Eventualmente lancia
	 * l'eccezione RaceNameTakenException
	 * 
	 * @param name
	 *            Nome della razza da creare
	 * @throws RaceNameTakenException
	 * @throws InvalidTokenException
	 */
	public final void checkNameRace(String name) throws RaceNameTakenException,
			InvalidTokenException {

		Specie supportSpecie;
		for (LoggedUser logUser : loggedUsers) {
			try {
				supportSpecie = raceMan.fromUsertoRace(logUser.getUsername());
				if ((logUser.getRace() != null)
						&& supportSpecie.getName().equals(name)) {
					/* nome razza occupato */
					throw new RaceNameTakenException();
				}
			} catch (RaceNullException e) {
				/* Salta al prossimo utente */;
			}
		}
	}

	/**
	 * * Il metodo invoca isUserBusy e costruisce la stringa di risposta
	 * 
	 * @param username
	 *            Username dell'utente da controllare
	 * @param password
	 *            Password dell'utente da controllare
	 * @return Stringa di risposta con l'esito dell'operazione
	 * @see UserManager#isUserBusy(String, String)
	 */
	public final String checkUsers(String username, String password) {
		String message;
		/*
		 */
		try {
			isUserBusy(username, password);
			message = "@ok";
		} catch (BusyUsernameException e) {
			message = e.getError();
		}
		return message;
	}

	/**
	 * Il metodo prende in ingresso l'array degli utenti e l'array degli utenti
	 * loggati, oltre alla stringa contenente l'username e la password.
	 * Restituisce una stringa di log confermato o meno (nel caso l'utente sia
	 * gia' loggato o se l'identificazione fallisce
	 * 
	 * @param username
	 *            Username dell'utente da controllare
	 * @param password
	 *            Password dell'utente da controllare
	 * @return
	 * @throws AutenticationFailedException
	 */
	synchronized public final String checkLogUsers(String username,
			String password) throws AutenticationFailedException {
		boolean userFound = false;
		String token;
		String message = null;

		for (LoggedUser logUser : loggedUsers) {
			if (logUser.getUsername().equals(username)) {
				message = "@no";
				userFound = true;
			}
		}
		if (!userFound) {
			for (User u : users) {
				if (u.getUsername().equals(username)
						&& u.getPassword().equals(password)) {
					userFound = true;
					LoggedUser logUser;
					int access = u.getAccess();
					access++;
					u.setAccess(access);
					token = u.getUsername() + access;
					logUser = new LoggedUser(u.getUsername(), u.getPassword(),
							token, access, u.getRace());
					loggedUsers.add(logUser);

					saveUsers();
					message = token;
					return message;
				}
			}
		}
		if (!userFound) {
			throw new AutenticationFailedException();
		}
		return message;
	}

	/**
	 * Il metodo invoca checkLogUsers e gestisce le eccezioni
	 * 
	 * @param username
	 *            Username dell'utente
	 * 
	 * @param password
	 *            Password dell'utente
	 * @return Ritorna l'esito dell'operazione inserita in una stringa
	 * @see server.UserManager#checkLogUsers(String, String)
	 */
	public final String logUsers(String username, String password) {
		String message;

		try {
			message = checkLogUsers(username, password);
		} catch (AutenticationFailedException e) {
			message = e.getError();
		}
		return message;
	}

	/**
	 * Risale all'username dal token
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @return Ritorna l'username che corrisponde a tale token
	 * @throws InvalidTokenException
	 */
	public final String tokenToUsername(String token)
			throws InvalidTokenException {

		for (LoggedUser logUser : loggedUsers) {
			if (logUser.getToken().equals(token)) {
				return logUser.getUsername();
			}
		}

		throw new InvalidTokenException();
	}

	/**
	 * Risale al token dall'username
	 * 
	 * @param username
	 *            Username dell'utente
	 * @return Ritorna il token che corrisponde a tale username
	 * @throws InvalidTokenException
	 */
	public final String usernametoToken(String username)
			throws InvalidTokenException {

		for (LoggedUser logUser : loggedUsers) {
			if (logUser.getUsername().equals(username)) {
				return logUser.getToken();
			}
		}

		throw new InvalidTokenException();
	}

	/**
	 * * Metodo che riceve in ingresso un token, controlla nell'array degli
	 * utenti loggati se e' presente un utente con tale token (in caso contrario
	 * lancia una opportuna eccezione) e se e' presente una razza per l'utente
	 * col token valido viene aggiunto all'array degli utenti in gioco (in caso
	 * contrario viene lanciata l'opportuna eccezione
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @throws TooPlayingUserException
	 * @throws RaceNullException
	 * @throws InvalidTokenException
	 */
	public final void checkTokenToPlay(String token)
			throws TooPlayingUserException, RaceNullException,
			InvalidTokenException {
		/*
		 * Metodo che riceve in ingresso un token, controlla nell'array degli
		 * utenti loggati se e' presente un utente con tale token (in caso
		 * contrario lancia una opportuna eccezione) e se e' presente una razza
		 * per l'utente col token valido viene aggiunto all'array degli utenti
		 * in gioco (in caso contrario viene lanciata l'opportuna eccezione
		 */

		boolean foundToken = false;
		boolean foundRace = false;
		LoggedUser newLogUser;

		if (playingUsers.size() >= 8) {
			throw new TooPlayingUserException();
		}

		if (!foundToken) {
			for (LoggedUser logUser : loggedUsers) {
				if (logUser.getUsername().equals(tokenToUsername(token))) {
					foundToken = true;
					if (logUser.getRace() != null) {
						foundRace = true;
						newLogUser = new LoggedUser(logUser.getUsername(),
								logUser.getPassword(), logUser.getToken(),
								logUser.getAccess(), logUser.getRace(),
								logUser.getUserMap());
						playingUsers.add(newLogUser);
					}
				}
			}
		} else if (!foundToken) {
			throw new InvalidTokenException();
		}
		if (!foundRace) {
			throw new RaceNullException();
		}
	}

	/**
	 * Il metodo verifica se l'utente e' abilitato ad accedere in gioco e in
	 * caso positivo invoca il metodo checkTokenToPlay
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @return
	 * @see server.UserManager#checkTokenToPlay(String)
	 */
	public final String gameAccess(String token) {
		String message;
		boolean foundToken = false;

		for (LoggedUser playUser : playingUsers) {
			if (playUser.getToken().equals(token)) {
				foundToken = true;
			}
		}

		if (!foundToken) {
			try {
				checkTokenToPlay(token);
				message = "@ok";
			}

			catch (TooPlayingUserException e) {
				message = e.getError();
			} catch (RaceNullException e) {
				message = e.getError();
			} catch (InvalidTokenException e) {
				message = e.getError();
			}
		} else {
			message = "@no";
		}

		return message;
	}

	/**
	 * * Il metodo controlla se e' presente il token. In caso positivo elimina
	 * il giocatore dall'Arraylist dei giocatori giocanti; in caso negativo
	 * lancia l'eccezione "Token Invalido"
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @throws InvalidTokenException
	 */
	public final void checkTokenToQuit(String token)
			throws InvalidTokenException {

		boolean foundToken = false;

		for (LoggedUser playUser : playingUsers) {
			if (playUser.getToken().equals(token)) {
				foundToken = true;
				playingUsers.remove(playUser);
				break;
			}
		}
		if (!foundToken) {
			throw new InvalidTokenException();
		}
	}

	/**
	 * Il metodo invoca checkTokenToQuit e gestisce le eccezioni
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @return Ritorna l'esito dell'operazione inserita in una stringa
	 * @see server.UserManager#checkTokenToQuit(String)
	 */
	public final String gameOut(String token) {
		try {
			checkTokenToQuit(token);
			return "@ok";
		}

		catch (InvalidTokenException e) {
			return e.getError();
		}
	}

	/**
	 * * Il metodo prende in ingresso l'array degli utenti loggati e di quelli
	 * in partita, oltre al token. Controlla dapprima che esista un token valido
	 * fra i token forniti agli utenti loggati. In caso positivo viene
	 * restituita L'ARRAYlIST degli utenti in gioco. In caso negativo viene
	 * lanciata l'opportuna eccezione
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @param playUsers
	 *            Arraylist degli utenti in gioco
	 * @return Ritorna l'arraylist degli utenti in gioco
	 * 
	 * @throws InvalidTokenException
	 */
	public final List<String> scanPlayingList(String token,
			List<String> playUsers) throws InvalidTokenException {

		List<String> supportList = new ArrayList<String>();

		boolean foundToken = false;
		/*
	
		 */

		for (LoggedUser logUser : loggedUsers) {
			if (logUser.getToken().equals(token)) {
				foundToken = true;
			}
		}
		if (!foundToken) {
			throw new InvalidTokenException();
		}

		for (LoggedUser playUser : playingUsers) {
			supportList.add(playUser.getUsername());
		}

		return supportList;
	}

	/**
	 * Il metodo invoca scanPlaylingList
	 * 
	 * @param token
	 *            Token identificativo dell'utente
	 * @return Ritorna la lista degli utenti in gioco
	 * @throws InvalidTokenException
	 * @see {@link UserManager#scanPlayingList(String, List)}
	 */
	public final List<String> playingList(String token)
			throws InvalidTokenException {
		List<String> playUsers = new ArrayList<String>();
		playUsers = scanPlayingList(token, playUsers);
		return playUsers;
	}

	/**
	 * 
	 * @param playingUsernames L'arraylist degli utenti in gioco
	 */
	public final void allPlayingUsernames(List<String> playingUsernames) {
		for (LoggedUser playUser : playingUsers) {
			playingUsernames.add(playUser.getUsername());
		}
	}
/**
  	 * Il metodo prende un ingresso un token. Tenta di farlo uscire dalla
	 * partita rimuovendolo dall'arrayList degli utenti in partita. Qualsiasi
	 * sia l'esito di questa operazione procede eliminando l'utente anche dagli
	 * utenti loggati. Infine ritorna le stringhe di protocollo, segnalando
	 * opportunatamente il caso di "token non trovato"
 * @param token Token identificativo dell'utente
 * @return Ritorna l'esito dell'operazione salvandola in una stringa
 * 
 */
	public final String logOut(String token) {

		String message = null;
		boolean tokenFound = false;

		for (LoggedUser logUser : loggedUsers) {
			if (logUser.getToken().equals(token)) {
				tokenFound = true;
				try {
					checkTokenToQuit(token);
				} catch (InvalidTokenException e) {
					//System.out.println("L'utente e' gia' uscito dalla partita");
				}

				loggedUsers.remove(logUser);
				message = "@ok";
				return message;
			}
		}
		if (!tokenFound) {
			message = "@no,@autenticazioneFallita";
		}

		return message;
	}
/**
 * * Il metodo cerca fra gli utenti in gioco l'utente con token token e lo
		 * ritorna. Lancia eventualmente l'eccezione UserNotPlayingException
		 *
 * @param token Token identificativo dell'utente
	 * @return Ritorna la lista degli utenti in gioco
 * @throws UserNotPlayingException
 */
	public final LoggedUser returnUserfromPlayingList(String token)
			throws UserNotPlayingException {
		 
		for (LoggedUser playUser : playingUsers) {
			if (playUser.getToken().equals(token)) {
				return playUser;
			}
		}
		throw new UserNotPlayingException();
	}
/**
 *  Il metodo cerca fra gli utenti loggati l'utente con token token e lo
		 * ritorna. Lancia eventualmente l'eccezione InvalidTokenException
		 *
 * @param token Token identificativo dell'utente
	 * @return Ritorna la lista degli utenti loggati
 * @throws InvalidTokenException
 */
	public final LoggedUser returnUserfromLoggedList(String token)
			throws InvalidTokenException {
		
		

		for (LoggedUser logUser : loggedUsers) {
			if (logUser.getToken().equals(token)) {
				return logUser;
			}
		}
		throw new InvalidTokenException();
	}
/**
 * * Il metodo cerca fra gli utenti loggati l'utente con token token e lo
		 * ritorna. Lancia eventualmente l'eccezione InvalidTokenException
 * @param username L'username dell'utente in considerazione
 * @return L'arraylist degli utenti generali
 */
	public final User returnUserfromUserList(String username) {

		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}

	public final User getCurrentUser() {
		if (playingUsers.size() != 0) {
			return playingUsers.get(0);
		}
		return null;
	}

	public final void swapUsers() {
		LoggedUser currentUser = playingUsers.get(0);
		playingUsers.remove(0);
		playingUsers.add(currentUser);
	}

	public final boolean isFirstUser() {
		if (playingUsers.size() == 1) {
			return true;
		}
		return false;
	}

	public final boolean isLastUser() {
		if (playingUsers.size() == 0) {
			return true;
		}
		return false;
	}

	/*
	 * public final void growUpUsers(){ User user; LoggedUser logUser; for
	 * (LoggedUser playUser : playingUsers) { playUser.se }
	 * 
	 * }
	 */
}
