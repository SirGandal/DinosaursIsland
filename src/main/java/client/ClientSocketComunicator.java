package client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import GUI.WindowGame;
import exception.*;
import map.Cell;

/**
 * Classe che gestisce la connessione al server tramite socket lato client.
 */
public class ClientSocketComunicator implements ClientComunicator {

	private OutputStreamWriter oSW;
	private BufferedWriter buffWriter;
	private BufferedReader buffReader;
	private InputStreamReader iSR;
	private Socket socket;
	private static Logger log = Logger.getLogger("ClientLog");

	private PipedInputStream pipeInput;
	private PipedOutputStream pipeOutput;
	/**
	 * @uml.property  name="manInMid"
	 * @uml.associationEnd  
	 */
	private ManInTheMiddle manInMid;

	/**
	 * Metodo per avere accesso al log del client.
	 * @return  Il log del client.
	 * @uml.property  name="log"
	 */
	public static Logger getLog() {
		return log;
	}

	/**
	 * Metodo per scrivere sul log del client.
	 * @uml.property  name="log"
	 */
	public static void setLog(Logger log) {
		ClientSocketComunicator.log = log;
	}

	/**
	 * Si occupa di scrivere sul buffer la stringa presa in ingresso.
	 * 
	 * @param requestToServer
	 *            Stringa che verra' scritta sul buffer in uscita.
	 */
	private void writer(String requestToServer) {

		try {

			//System.out.println("\nInviato: " + requestToServer);

			buffWriter.write(requestToServer);
			buffWriter.newLine();
			buffWriter.flush();
		}

		catch (IOException e1) {
			getLog().log(Level.WARNING, e1.getMessage());
		}

	}

	/**
	 * Costruttore che tenta di avviare una connessione verso un determianto
	 * server ad un determianto ip tramite una data porta.
	 * 
	 * @param ip
	 *            Ip del server al quale ci si vuole connettere.
	 * 
	 * @param port
	 *            Porta del server al quale ci si vuole connettere.
	 * 
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws UnknownHostException
	 * 
	 * @see IOException
	 * @see NumberFormatException
	 * @see UnknownHostException
	 * 
	 */
	public ClientSocketComunicator(String ip, String port) throws IOException,
			NumberFormatException, UnknownHostException {

		FileHandler fh = new FileHandler("src/main/resources/ClientLog.log",
				true);
		log.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);

		this.socket = new Socket(ip, Integer.parseInt(port));
		oSW = new OutputStreamWriter(socket.getOutputStream());
		buffWriter = new BufferedWriter(oSW);

		pipeOutput = new PipedOutputStream();
		pipeInput = new PipedInputStream(pipeOutput);
		manInMid = new ManInTheMiddle(socket, pipeOutput);

		manInMid.start();

		iSR = new InputStreamReader(pipeInput);
		buffReader = new BufferedReader(iSR);

		//System.out.println("Connesso a " + ip + " sulla porta " + port);
	}

	public void setWindowGame(WindowGame winGame) {
		if (manInMid != null) {
			manInMid.setWindowGame(winGame);
		}
	}

	/**
	 * Invia al server la richiesta di creazione di un nuovo user con associati
	 * i parametri username e password.
	 * 
	 * @param username
	 *            L'username scelto dall'utente.
	 * @param password
	 *            La password associata allo username.
	 * 
	 * @see server.ServerSocketComunicator#createNewUser(String, String)
	 */
	synchronized public void createNewUser(String username, String password)
			throws BusyUsernameException {

		writer("@creaUtente,user=" + username + ",pass=" + password);

		try {
			String buf = buffReader.readLine();

			if ((buf != null) && (buf.equals("@ok") == false)) {
				throw new BusyUsernameException();
			} //else {
				//System.out.println("Ricevuto: " + buf);
			//}

		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta di un determinato utente per effettuare il
	 * login.
	 * 
	 * @param username
	 *            L'username dell'utente per il quale effettuare il login.
	 * 
	 * @param password
	 *            La password associata allo username.
	 * 
	 * @return Il token dell'utente che si e' loggato.
	 * 
	 * @throws AutenticationFailedException
	 *             Eccezione lanciata quando il server non riesce a fare loggare
	 *             l'utente. I possibili motivi del lancio dell'eccezione
	 *             possono essere: l'utente non esiste poiche' non e' stato
	 *             creato oppure perche' allo username non coincide la password
	 *             e viceversa.
	 * 
	 * @throws UserAlreadyLoggedException
	 *             Eccezione lanciata quando un utente e' gia' connesso e da un
	 *             altro client si prova ad accedere con lo stesso username e
	 *             password.
	 * 
	 * @see server.ServerSocketComunicator#logUsers(String, String)
	 */
	synchronized public String logYourUser(String username, String password)
			throws AutenticationFailedException, UserAlreadyLoggedException {

		writer("@login,user=" + username + ",pass=" + password);

		try {
			String buf = buffReader.readLine();

			if (buf != null) {
				//System.out.println("Ricevuto: " + buf);

				StringTokenizer st;

				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (support.equals("@ok")) {
					return st.nextToken();
				} else if (st.hasMoreElements()) {
					throw new AutenticationFailedException();
				} else {
					throw new UserAlreadyLoggedException();
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	/**
	 * Invia al server la richiesta di creazione di una nuova razza per un
	 * determinato utente.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @param raceName
	 *            Il nome della razza.
	 * 
	 * @param type
	 *            Stringa contenente "e" o "c" in base alla tipologia di razza
	 *            scelta. "e" per erbivoro, "c" per carnivoro.
	 * 
	 * @throws RaceAlreadyExistsException
	 *             Eccezione lanciata quando l'utente tenta di creare una razza
	 *             ma in precedenza gia' ne avevea creata una.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws RaceNameTakenException
	 *             Eccezione lanciata se il nome della razza e' gia' stato
	 *             utilizzato da un altro utente.
	 * 
	 * @see server.ServerSocketComunicator#createNewRace(String, String, String)
	 */
	synchronized public void createNewRace(String token, String raceName,
			String type) throws RaceNameTakenException, InvalidTokenException,
			RaceAlreadyExistsException {

		writer("@creaRazza,token=" + token + ",nome=" + raceName + ",tipo="
				+ type);

		try {
			String buf = buffReader.readLine();

			if (buf != null) {
				//System.out.println("Ricevuto: " + buf);

				StringTokenizer st;

				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();

				if (support.equals("@ok") == false) {
					if (support.equals("@no")) {
						support = st.nextToken();
					}
					if (support.equals("@nomeRazzaOccupato")) {
						throw new RaceNameTakenException();
					} else if (support.equals("@tokenNonValido")) {
						throw new InvalidTokenException();
					} else if (support.equals("@razzaGiaCreata")) {
						throw new RaceAlreadyExistsException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta di accesso all'ambiente di gioco.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @throws UserAlreadyEnteredInGame
	 *             Eccezione lanciata quando si tenta di accedere alla partita e
	 *             gia' da un altro cliente e' stato effettuato l'acesso con lo
	 *             stesso utente.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws TooPlayingUserException
	 *             Eccezione lanciata stanno giocando gia' 8 persone.
	 * 
	 * @throws RaceNullException
	 *             Eccezione lanciata se si tenta di accedere al gioco senza
	 *             avere precedentemente creato una razza.
	 * 
	 * @see server.ServerSocketComunicator#gameAccess(String)
	 */
	synchronized public void gameAccess(String token)
			throws UserAlreadyEnteredInGame, InvalidTokenException,
			TooPlayingUserException, RaceNullException {

		writer("@accessoPartita,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();

				if (!st.hasMoreElements()) {
					if (!support.equals("@ok")) {
						throw new UserAlreadyEnteredInGame();
					}
				} else {
					if (!st.hasMoreTokens()) {
						throw new UserAlreadyEnteredInGame();
					} else {
						support = st.nextToken();

						if (support.equals("@tokenNonValido")) {
							throw new InvalidTokenException();
						} else if (support.equals("@troppiGiocatori")) {
							throw new TooPlayingUserException();
						} else if (support.equals("@razzaNonCreata")) {
							throw new RaceNullException();
						}
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta di uscita all'ambiente di gioco.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @throws UserAlreadyExited
	 *             Eccezione lanciata quando si tenta di uscire dalla partita ma
	 *             l'uscita e' gia' avvenuta
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @see server.ServerSocketComunicator#gameOut(String)
	 */
	synchronized public void gameOut(String token) throws UserAlreadyExited,
			InvalidTokenException {

		writer("@uscitaPartita,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();

				if (!st.hasMoreElements()) {
					if (!support.equals("@ok")) {
						throw new UserAlreadyExited();
					}
				} else {
					throw new InvalidTokenException();
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta per ottenere la lista degli utenti che
	 * attualmente stanno giocando.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @return Restituisce un array list contente lo username dei giocatori
	 *         attualmente in gioco.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @see server.ServerSocketComunicator#playingListSocketMaker(String)
	 */
	synchronized public ArrayList<String> playingList(String token)
			throws InvalidTokenException {

		ArrayList<String> playingList = new ArrayList<String>();

		writer("@listaGiocatori,token=" + token);

		/*
		 * questo ramo try riceve dal server la stringa contenente la lista dei
		 * giocatori in gioco secondo le stringhe di protocollo (o eventualmente
		 * il messaggio di token non valido)
		 */
		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();
				if (support.equals("@listaGiocatori")) {
					playingList.add("@listaGiocatori");
					//System.out.println(buf);
				} else {
					throw new InvalidTokenException();
				}
				while (st.hasMoreElements()) {
					support = st.nextToken();
					playingList.add(support);
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
		return playingList;
	}

	/**
	 * Invia al server la richiesta per effettuare il logout.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @see server.ServerSocketComunicator#logOut(String)
	 */
	synchronized public void logOut(String token) throws InvalidTokenException {

		writer("@logout,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();
				if (support.equals("@ok") == false) {
					throw new InvalidTokenException();
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta per ottenere la classifica globale.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @return Restituisce un array list contente una stringa recante le
	 *         informazioni sugli utenti che fino a quel momento hanno giocato:
	 *         username, razza con la quale e' stato effettuato il punteggio
	 *         incriminato, punteggio, ed una informazioni sul fatto che la
	 *         razza e' ancora in gioco oppure e' estinta.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @see server.ServerSocketComunicator#giveMeRanking(String)
	 */
	public Object[][] giveMeRanking(String token) throws InvalidTokenException {

		ArrayList<String> rankingList = new ArrayList<String>();

		writer("@classifica,token=" + token);

		try {
			String buf = buffReader.readLine();
			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();
				Object[][] ranking;
				/*
				 * costruisco un array list che contiene le stringhe che andro'
				 * a stampare in una text area
				 */
				if (support.equals("@classifica")) {
					int i = 1;
					while (st.hasMoreTokens()) {
						support = st.nextToken();
						if (i == 1) {
							rankingList.add(support.substring(1));
							// rankingString = "L'utente " + "'" +
							// support.substring(1) + "'";
						} else if (i == 2) {
							rankingList.add(support);
							// rankingString = rankingString + " con la razza "
							// + "'" + support + "'";
						} else if (i == 3) {
							rankingList.add(support);
							// rankingString = rankingString +
							// " ha totalizzato " + support + " punti";
						} else {
							if (support.equals("n}")) {
								rankingList.add("si");
							} else {
								rankingList.add("no");
							}
							// rankingString = rankingString +
							// ". La razza non e' ancora estinta.";
							// rankingList.add(rankingString);
							// rankingString = new String();
							i = 0;
						}
						i++;
					}

					ranking = new Object[rankingList.size()/4][4];

					for (i = 0; i < rankingList.size() / 4; i++) {
						for (int j = 0; j < 4; j++) {
							if (j != 2) {
								/*
								 * in posizione 2 c'e' il punteggio
								 */
								ranking[i][j] = rankingList.get(i * 4 + j);
							} else {
								ranking[i][j] = Integer.parseInt(rankingList
										.get(i * 4 + j));
							}
						}
					}
					return ranking;

				} else {
					throw new InvalidTokenException();
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	/**
	 * Costruisce una mappa con tutte le informazioni necessarie alla
	 * renderizzazione da parte dell'interfaccia grafica in base ad una
	 * richiesta al server.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @return Restituisce la mappa generale.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see server.ServerSocketComunicator#showMap(String)
	 */
	public Cell[][] askForMap(String token) throws InvalidTokenException,
			UserNotPlayingException {

		writer("@mappaGenerale,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();
				if (support.equals("@mappaGenerale")) {
					return stringMaptoArray(buf);
				} else if (support.equals("@no")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido")) {
						throw new InvalidTokenException();
					} else if (support.equals("@nonInPartita")) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	/**
	 * Metodo di supporto: costruisce una mappa a partire da una stringa
	 * contente le info necessarie in sequenza.
	 * 
	 * @param stringMap
	 *            Mappa sottoforma di stringa ricevuta dal server.
	 * 
	 * @return Restituisce la mappa generale.
	 * 
	 * @see client.ClientSocketComunicator#Map(String)
	 */
	public Cell[][] stringMaptoArray(String stringMap) {
		String[][] map;

		StringTokenizer st = new StringTokenizer(stringMap);
		st.nextToken(",");
		st.nextToken("{");
		String stringRow = st.nextToken(","); // contiene "{RIGHE"
		String stringCol = st.nextToken("}"); // contiene ",COLONNE"

		stringRow = stringRow.substring(1);
		int row = Integer.parseInt(stringRow);

		stringCol = stringCol.substring(1);
		int col = Integer.parseInt(stringCol);

		map = new String[row][col];

		st.nextToken("["); // butto ','

		String[] rowArray = new String[row];

		/*
		 * costruisco l'array di stringhe che conterra la sequenza di tipologia
		 * di cella per riga contenuta nella mappa
		 */
		for (int i = 0; i < rowArray.length; i++) {
			rowArray[i] = st.nextToken(";");
		}
		/*
		 * ogni riga di row array sara' del tipo [a][t][a] se una mappa e' 3 x 3
		 */
		int k = 0;
		for (int i = rowArray.length - 1; i >= 0; i--, k++) {
			st = new StringTokenizer(rowArray[i]);
			for (int j = 0; j < rowArray.length; j++) {
				map[k][j] = st.nextToken("[");
			}
		}

		for (int i = 0; i < rowArray.length; i++) {
			for (int j = 0; j < rowArray.length; j++) {
				st = new StringTokenizer(map[i][j]);
				map[i][j] = st.nextToken("]");
			}
		}

		return arraytoGeneralMap(map);
	}

	/**
	 * Metodo di supporto: costruisce una mappa da un array di stringhe
	 * costruito a partire dalla stringa ricevuta dal server.
	 * 
	 * @param stringMap
	 *            array di stringhe contenenti le info per la costruzione della
	 *            mappa.
	 * 
	 * @return Restituisce la mappa generale.
	 * 
	 * @see client.ClientSocketComunicator#stringMaptoArray(String)
	 */
	synchronized public Cell[][] arraytoGeneralMap(String[][] stringMap) {

		Cell[][] m = new Cell[stringMap.length][stringMap.length];

		for (int i = 0; i < stringMap.length; i++) {
			for (int j = 0; j < stringMap.length; j++) {
				m[i][j] = new Cell();
				if (stringMap[i][j].equals("a")) {
					m[i][j].setWater(true);
					m[i][j].setVisible(true);
				} else if (stringMap[i][j].equals("t")) {
					m[i][j].setEarth(true);
					m[i][j].setVisible(true);
				} else if (stringMap[i][j].equals("v")) {
					m[i][j].setVegetation(true);
					m[i][j].setVisible(true);
				} else if (stringMap[i][j].equals("b")) {
					m[i][j].setVisible(false);
					m[i][j].setWater(false);
					m[i][j].setEarth(false);
					m[i][j].setVegetation(false);
				}
			}
		}

		return m;

	}

	/**
	 * Costruisce una mappa locale ad uno specifico dinosauro con tutte le
	 * informazioni necessarie alla renderizzazione da parte dell'interfaccia
	 * grafica in base ad una richiesta al server.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @param id
	 *            Identificativo del dinosauro ottenibile tramite la richiesta
	 *            della lista dei dinosauri.
	 * 
	 * @return Restituisce la mappa locale di un dinosauro con tutte le
	 *         informazioni dettagliate su energia della vegetazione, energia
	 *         delle carogne e presenza di ulteriori dinosauri.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws InvalidIdException
	 *             Eccezione lanciata quando l'id del dinosauro non e'
	 *             riscontrato da parte del server.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see server.ServerSocketComunicator#showLocalView(String, String)
	 * @see client.ClientSocketComunicator#askForList(String)
	 */
	synchronized public Cell[][] askForlocalView(String token, String id)
			throws InvalidTokenException, UserNotPlayingException,
			InvalidIdException {

		writer("@vistaLocale,token=" + token + ",idDino=" + id);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");
				String support = st.nextToken();
				if (support.equals("@vistaLocale")) {
					return stringViewtoArray(buf);
				} else if (support.equals("@no")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido")) {
						throw new InvalidTokenException();
					} else if (support.equals("@nonInPartita")) {
						throw new UserNotPlayingException();
					} else if (support.equals("@idNonValido")) {
						throw new InvalidIdException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	/**
	 * Metodo di supporto: costruisce una mappa locale ad un dinosauro a partire
	 * da una stringa contente le info necessarie in sequenza.
	 * 
	 * @param stringView
	 *            Mappa sottoforma di stringa ricevuta dal server.
	 * 
	 * @return Restituisce la mappa locale.
	 * 
	 * @see client.ClientSocketComunicator#askForlocalView(String, String)
	 */
	synchronized public Cell[][] stringViewtoArray(String stringView) {

		String[][] view;

		StringTokenizer st = new StringTokenizer(stringView);
		st.nextToken(","); // butto '@vistaLocale'
		st.nextToken("{"); // butto ','
		/*String stringx =*/ st.nextToken(","); // contiene "{PosizioneX"
		/*String stringy =*/ st.nextToken("}"); // contiene ",PosizioneY"

		/*
		 * TODO vedere se servono x ed y qui
		 */
		//stringx = stringx.substring(1);
		// int x=Integer.parseInt(stringx); //contiene l'indice colonna in basso
		// a sinistra della vista locale

		//stringy = stringy.substring(1);
		// int y=Integer.parseInt(stringy); //contiene l'indice riga in basso a
		// sinistra della vista locale

		st.nextToken("{"); // butto ','

		String stringRow = st.nextToken(","); // contiene "{RIGHE"
		String stringCol = st.nextToken("}"); // contiene ",COLONNE"

		stringRow = stringRow.substring(1);
		int row = Integer.parseInt(stringRow);

		stringCol = stringCol.substring(1);
		int col = Integer.parseInt(stringCol);

		view = new String[row][col];

		st.nextToken("["); // butto ','

		String[] rowArray = new String[row];

		/*
		 * costruisco l'array di stringhe che conterra la sequenza di tipologia
		 * di cella per riga contenuta nella mappa
		 */
		for (int i = rowArray.length - 1; i >= 0; i--) {
			rowArray[i] = st.nextToken(";");
		}
		/*
		 * ogni riga di row array sara' del tipo [a][t][a] se una mappa e' 3 x 3
		 */

		for (int i = row - 1; i >= 0; i--) {
			st = new StringTokenizer(rowArray[i]);
			for (int j = 0; j < col; j++) {
				view[i][j] = st.nextToken("[");
			}
		}

		for (int i = row - 1; i >= 0; i--) {
			for (int j = 0; j < col; j++) {
				st = new StringTokenizer(view[i][j]);
				view[i][j] = st.nextToken("]");
			}
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.print(view[i][j] + " ");
			}
			System.out.print("\n");
		}

		return arraytoLocalMap(view);
	}

	/**
	 * Metodo di supporto: costruisce una mappa locale da un array di stringhe
	 * costruito a partire dalla stringa ricevuta dal server.
	 * 
	 * @param stringView
	 *            di stringhe contenenti le info per la costruzione della mappa
	 *            locale.
	 * 
	 * @return Restituisce la mappa locale.
	 * 
	 * @see client.ClientSocketComunicator#stringViewtoArray(String)
	 */
	synchronized public Cell[][] arraytoLocalMap(String[][] stringView) {

		StringTokenizer st;

		Cell[][] m = new Cell[stringView.length][stringView[0].length];

		for (int i = 0; i < stringView.length; i++) {
			for (int j = 0; j < stringView[0].length; j++) {
				st = new StringTokenizer(stringView[i][j], ",");

				m[i][j] = new Cell();

				String support = st.nextToken();

				if (st.hasMoreTokens()) {
					if (support.equals("v")) {
						m[i][j].setVegetation(true);
						m[i][j].setCurrentEnergyVegetation(Integer.parseInt(st
								.nextToken()));
					} else if (support.equals("c")) {
						m[i][j].setCarrion(true);
						m[i][j].setCurrentEnergyCarrion(Integer.parseInt(st
								.nextToken()));
					} else if (support.equals("d")) {
						m[i][j].setIdDinosaur(st.nextToken());
						m[i][j].setThereIsDinosaur(true);
					}
				} else {
					if (support.equals("a")) {
						m[i][j].setWater(true);
					} else if (support.equals("t")) {
						m[i][j].setEarth(true);
					}
				}

			}
		}
		return m;
	}

	/**
	 * Richiede al server la lista di tutti i dinosauro posseduti da uno
	 * specifico utente.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @return Restituisce un array list contenente la lista degli Id dei
	 *         dinosauri posseduti dall'utente.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see server.ServerSocketComunicator#denverList(String)
	 */
	synchronized public String[] askForList(String token)
			throws InvalidTokenException, UserNotPlayingException {

		writer("@listaDinosauri,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			int maxNumDin = 5;

			if (buf != null) {
				String[] returnString = new String[maxNumDin];

				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (support.equals("@listaDinosauri")) {

					for (int i = 0; st.hasMoreTokens() && (i < maxNumDin); i++) {
						returnString[i] = st.nextToken();

					}
					return returnString;
				} else if (support.equals("@no")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido")) {
						throw new InvalidTokenException();
					} else if (support.equals("@nonInPartita")) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}

		String[] a = new String[0];
		return a;
	}

	/**
	 * Richiede al server la lista di tutti i dinosauro posseduti da uno
	 * specifico utente.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @return Restituisce un array di stringhe contente in posizione '0' lo
	 *         username dell'utente, in posizione '1' il nome della razza, in
	 *         posizione '2' la tipologia della razza, in posizione '3' la
	 *         coordinata X del dinosauro, in posizione '4' la coordinata Y del
	 *         dinosauro, in posizione '5' la dimensione. Opzionale: in
	 *         posizione '6' l'energia del dinosauro e in posizione '7' i turni
	 *         fino a quel momenti vissuti dal dinosauro.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @throws InvalidIdException
	 *             Eccezione lanciata quando l'id del dinosauro non e'
	 *             riscontrato da parte del server.
	 * 
	 * @see server.ServerSocketComunicator#denverState(String, String)
	 */
	synchronized public String[] askForStatus(String token, String id)
			throws UserNotPlayingException, InvalidTokenException,
			InvalidIdException {

		writer("@statoDinosauro,token=" + token + ",idDino=" + id);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				int numOfInformation = 8;

				String[] returnString = new String[numOfInformation];

				if (support.equals("@statoDinosauro")) {
					for (int i = 0; st.hasMoreTokens()
							&& (i < numOfInformation); i++) {
						if (i == 3) {
							returnString[i] = st.nextToken().substring(1);
						} else if (i == 4) {
							returnString[i] = st.nextToken("}").substring(1);
							st.nextToken(","); // butto '}'
						} else {
							returnString[i] = st.nextToken();
						}
					}
					return returnString;
				} else if (support.equals("@no")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido")) {
						throw new InvalidTokenException();
					} else if (support.equals("@nonInPartita")) {
						throw new UserNotPlayingException();
					} else if (support.equals("@idNonValido")) {
						throw new InvalidIdException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}

		String[] a = new String[0];
		return a;

	}

	/**
	 * Richiede al server lo spostamente di un dinosauro verso una determinata
	 * posizione all'interno della mappa generale.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @param id
	 *            Identificativo del dinosauro ottenibile tramite la richiesta
	 *            della lista dei dinosauri.
	 * 
	 * @param x
	 *            Ascissa della casella di dove si vuole spostare il dinosauro.
	 * @param y
	 *            Ordinata della casella di dove si vuole spostare il dinosauro.
	 * 
	 * @throws YouWonTheBattle
	 *             Eccezione lanciata quando il dinosauro si sposta in una
	 *             casella contenente un dinosauro nemico e, scontrandosi, vince
	 *             la battaglia.
	 * 
	 * @throws YouLostTheBattle
	 *             Eccezione lanciata quando il dinosauro si sposta in una
	 *             casella contenente un dinosauro nemico e, scontrandosi, perde
	 *             la battaglia.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws InvalidIdException
	 *             Eccezione lanciata quando l'id del dinosauro non e'
	 *             riscontrato da parte del server.
	 * 
	 * @throws DestinationUnreachableException
	 *             Eccezione lanciata quando la destinazione scelta dall'utente
	 *             per il suo dinosauro non e' raggiungibile a causa delle
	 *             caratteristiche del dinosauro stesso.
	 * 
	 * @throws MovesLimitExceededException
	 *             Eccezione lanciata quando ho gia' effettuato il movimento in
	 *             un turno e di conseguenza non posso effettuarlo piu' per il
	 *             resto del turno stesso.
	 * 
	 * @throws NotEnoughEnergy
	 *             Eccezione lanciata quando il dinosauro non ha abbastanza
	 *             energia per compiere una determinata mossa.
	 * 
	 * @throws NotYourTurnException
	 *             Eccezione lanciata quando si tenta di effetuare qualcosa ma
	 *             non e' il turno dell'utente in questione.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see exception.YouWonTheBattle
	 * @see exception.YouLostTheBattle
	 * @see exception.InvalidIdException
	 * @see exception.InvalidTokenException
	 * @see exception.DestinationUnreachableException
	 * @see exception.NotEnoughEnergy
	 * @see exception.NotYourTurnException
	 * @see exception.UserNotPlayingException
	 * 
	 *      //TODO aggiungere il collegamente col movimento del server
	 * @see server.ServerSocketComunicator#
	 */
	synchronized public void moveDinosaur(String token, String id, int x, int y)
			throws YouWonTheBattle, YouLostTheBattle, InvalidIdException,
			InvalidTokenException, DestinationUnreachableException,
			MovesLimitExceededException, NotEnoughEnergy, NotYourTurnException,
			UserNotPlayingException {

		writer("@muoviDinosauro,token=" + token + ",idDino=" + id + ",dest={"
				+ x + "," + y + "}");

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (support.equals("@ok")) {
					if (st.hasMoreTokens()) {
						st.nextToken();// conterra' '@combattimento'
						if (st.nextToken().equals("v")) {
							throw new YouWonTheBattle();
						} else {
							throw new YouLostTheBattle();
						}
					}
				} else if (support.equals("@no") ) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido") ) {
						throw new InvalidTokenException();
					} else if (support.equals("@idNonValido") ) {
						throw new InvalidIdException();
					} else if (support.equals("@destinazioneNonValida") ) {
						throw new DestinationUnreachableException();
					} else if (support.equals("@raggiuntoLimiteMosseDinosauro") ) {
						throw new MovesLimitExceededException();
					} else if (support.equals("@mortePerInedia") ) {
						throw new NotEnoughEnergy();
					} else if (support.equals("@nonIlTuoTurno") ) {
						throw new NotYourTurnException();
					} else if (support.equals("@nonInPartita") ) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta per la crescita di un dinosauro.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @param id
	 *            Identificativo del dinosauro ottenibile tramite la richiesta
	 *            della lista dei dinosauri.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws InvalidIdException
	 *             Eccezione lanciata quando l'id del dinosauro non e'
	 *             riscontrato da parte del server.
	 * 
	 * @throws MaxDimensionReachedException
	 *             Eccezione lanciata quando il dinosauro ha raggiunto gia' il
	 *             massimo livello possibile e non puo' ulteriormente evolvere.
	 * 
	 * @throws MovesLimitExceededException
	 *             Eccezione lanciata quando ho gia' effettuato il movimento in
	 *             un turno e di conseguenza non posso effettuarlo piu' per il
	 *             resto del turno stesso.
	 * 
	 * @throws NotEnoughEnergy
	 *             Eccezione lanciata quando il dinosauro non ha abbastanza
	 *             energia per compiere una determinata mossa.
	 * 
	 * @throws NotYourTurnException
	 *             Eccezione lanciata quando si tenta di effetuare qualcosa ma
	 *             non e' il turno dell'utente in questione.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see exception.InvalidIdException
	 * @see exception.InvalidTokenException
	 * @see exception.MaxDimensionReachedException
	 * @see exception.MovesLimitExceededException
	 * @see exception.NotEnoughEnergy
	 * @see exception.NotYourTurnException
	 * @see exception.UserNotPlayingException
	 * 
	 *      //TODO aggiungere il collegamente con la crescita del server
	 * @see server.ServerSocketComunicator#
	 * 
	 */
	synchronized public void letYourDinosaurGrow(String token, String id)
			throws InvalidIdException, InvalidTokenException,
			MaxDimensionReachedException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {

		writer("@cresciDinosauro,token=" + token + ",idDino=" + id);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if ((buf != null) && (!buf.equals(""))) {
				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (!support.equals("@ok")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido") ) {
						throw new InvalidTokenException();
					} else if (support.equals("@idNonValido") ) {
						throw new InvalidIdException();
					} else if (support.equals("@raggiuntaDimensioneMax") ) {
						throw new MaxDimensionReachedException();
					} else if (support.equals("@raggiuntoLimiteMosseDinosauro") ) {
						throw new MovesLimitExceededException();
					} else if (support.equals("@mortePerInedia") ) {
						throw new NotEnoughEnergy();
					} else if (support.equals("@nonIlTuoTurno") ) {
						throw new NotYourTurnException();
					} else if (support.equals("@nonInPartita") ) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta per la deposizione di un uovo da parte di un
	 * dinosauro e la conseguente nascita di un dinosauro al turno successivo.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @param id
	 *            Identificativo del dinosauro ottenibile tramite la richiesta
	 *            della lista dei dinosauri.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws InvalidIdException
	 *             Eccezione lanciata quando l'id del dinosauro non e'
	 *             riscontrato da parte del server.
	 * 
	 * @throws NumberOfDinosaursExceededException
	 *             Eccezione lanciata quando si e' raggiunto il numero massimo
	 *             di dinosauri per specie e di conseguenza non e' possibile
	 *             farne nascere un altro.
	 * 
	 * @throws MovesLimitExceededException
	 *             Eccezione lanciata quando ho gia' effettuato il movimento in
	 *             un turno e di conseguenza non posso effettuarlo piu' per il
	 *             resto del turno stesso.
	 * 
	 * @throws NotEnoughEnergy
	 *             Eccezione lanciata quando il dinosauro non ha abbastanza
	 *             energia per compiere una determinata mossa.
	 * 
	 * @throws NotYourTurnException
	 *             Eccezione lanciata quando si tenta di effetuare qualcosa ma
	 *             non e' il turno dell'utente in questione.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see exception.InvalidIdException
	 * @see exception.InvalidTokenException
	 * @see exception.NumberOfDinosaursExceededException
	 * @see exception.MovesLimitExceededException
	 * @see exception.NotEnoughEnergy
	 * @see exception.NotYourTurnException
	 * @see exception.UserNotPlayingException
	 * 
	 *      //TODO aggiungere il collegamente con il deposita uovo del server
	 * @see server.ServerSocketComunicator#
	 * 
	 */
	synchronized public void leaveYourEgg(String token, String id)
			throws InvalidIdException, InvalidTokenException,
			NumberOfDinosaursExceededException, MovesLimitExceededException,
			NotEnoughEnergy, NotYourTurnException, UserNotPlayingException {

		writer("@deponiUovo,token=" + token + ",idDino=" + id);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (!support.equals("@ok")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido") ) {
						throw new InvalidTokenException();
					} else if (support.equals("@idNonValido") ) {
						throw new InvalidIdException();
					} else if (support.equals("@raggiuntoNumeroMaxDinosauri") ) {
						throw new NumberOfDinosaursExceededException();
					} else if (support.equals("@raggiuntoLimiteMosseDinosauro") ) {
						throw new MovesLimitExceededException();
					} else if (support.equals("@mortePerInedia") ) {
						throw new NotEnoughEnergy();
					} else if (support.equals("@nonIlTuoTurno") ) {
						throw new NotYourTurnException();
					} else if (support.equals("@nonInPartita") ) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Invia al server la richiesta per la deposizione di un uovo da parte di un
	 * dinosauro e la conseguente nascita di un dinosauro al turno successivo.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws NotYourTurnException
	 *             Eccezione lanciata quando si tenta di effetuare qualcosa ma
	 *             non e' il turno dell'utente in questione.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see exception.InvalidTokenException
	 * @see exception.NotYourTurnException
	 * @see exception.UserNotPlayingException
	 * 
	 *      //TODO aggiungere il collegamente con il passa turno del server
	 * @see server.ServerSocketComunicator#
	 * 
	 */
	synchronized public void pass(String token) throws InvalidTokenException,
			NotYourTurnException, UserNotPlayingException {

		writer("@passaTurno,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (!support.equals("@ok")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido") ) {
						throw new InvalidTokenException();
					} else if (support.equals("@nonIlTuoTurno") ) {
						throw new NotYourTurnException();
					} else if (support.equals("@nonInPartita") ) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}

	}

	/**
	 * Invia al server la richiesta di confermare il turno del determinato
	 * giocatore che lo richiede.
	 * 
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @throws InvalidTokenException
	 *             Eccezione lanciata quando il token passato per parametro non
	 *             e' riconosciuto dal server.
	 * 
	 * @throws NotYourTurnException
	 *             Eccezione lanciata quando si tenta di effetuare qualcosa ma
	 *             non e' il turno dell'utente in questione.
	 * 
	 * @throws UserNotPlayingException
	 *             Eccezione lanciata quando l'utente non risulta essere in
	 *             gioco.
	 * 
	 * @see exception.InvalidTokenException
	 * @see exception.NotYourTurnException
	 * @see exception.UserNotPlayingException
	 * 
	 *      //TODO aggiungere il collegamente con il conferma turno del server
	 * @see server.ServerSocketComunicator#
	 * 
	 */
	synchronized public void confirm(String token)
			throws InvalidTokenException, NotYourTurnException,
			UserNotPlayingException {

		writer("@confermaTurno,token=" + token);

		try {
			String buf = buffReader.readLine();

			//System.out.println("Ricevuto: " + buf);

			StringTokenizer st;

			if (buf != null) {
				st = new StringTokenizer(buf, ",");

				String support = st.nextToken();

				if (!support.equals("@ok")) {
					support = st.nextToken();
					if (support.equals("@tokenNonValido") ) {
						throw new InvalidTokenException();
					} else if (support.equals("@nonIlTuoTurno") ) {
						throw new NotYourTurnException();
					} else if (support.equals("@nonInPartita") ) {
						throw new UserNotPlayingException();
					}
				}
			}
		} catch (IOException e) {
			getLog().log(Level.WARNING, e.getMessage());
		}
	}

	synchronized public void closeSocket() {
		writer("@chiudiSocket");
	}
}
