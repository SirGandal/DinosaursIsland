package server;

import java.io.*;
import java.net.Socket;
import java.util.*;
import dinosaur.Dinosaur;
import dinosaur.Specie;
import exception.*;

/**
 * @author  gas12n
 */
public class ServerSocketComunicator {

	private String username;
	private String password;
	private String token;
	private String name;
	private String type;
	private String command;
	private String idDino;
	final static int D = 50;
	private int lenghtMap = 49;
	private String tokenToDestroy;
	private OutputStreamWriter oSW;
	private BufferedReader buffReader;
	private InputStreamReader iSR;
	private Socket socket;
	/**
	 * @uml.property  name="game"
	 * @uml.associationEnd  
	 */
	private Game game;
	private boolean connected = false;

	/**
	 * Costruttore che tenta di avviare una connessione verso un determianto
	 * server ad un determianto ip tramite una data porta.
	 * 
	 * @param socket
	 *            Socket col quale ci si vuole connettere
	 * 
	 * @param Game
	 *            Istanzia del gioco, unico durante tutta la vita del server.
	 * 
	 * @throws IOException
	 * 
	 * 
	 * @see IOException
	 * 
	 */
	public ServerSocketComunicator(Socket socket, Game game) throws IOException {
		super();
		this.socket = socket;
		connected = true;
		this.game = game;
		iSR = new InputStreamReader(socket.getInputStream());
		setBuffReader(new BufferedReader(iSR));
		oSW = new OutputStreamWriter(socket.getOutputStream());
		new BufferedWriter(oSW);

	}

	/**
	 * Il metodo setta il buffer di lettura
	 * @param buffReader  Buffer di lettura
	 * @uml.property  name="buffReader"
	 */
	public final void setBuffReader(BufferedReader buffReader) {
		this.buffReader = buffReader;
	}

	/**
	 * Il metodo ritorna il buffer di lettura
	 * @return  Restituisce il buffer di lettura
	 * @uml.property  name="buffReader"
	 */
	public final BufferedReader getBuffReader() {
		return buffReader;
	}

	/**
	 * Il metodo scrive nel socket la stringa passatogli
	 * 
	 * @param magicValue
	 *            Stringa da scrivere nel buffer
	 */
	synchronized public final void putReplyInSocket(String magicValue) {

		try {
			OutputStreamWriter oSW = new OutputStreamWriter(
					socket.getOutputStream());
			BufferedWriter buffWriter = new BufferedWriter(oSW);
			buffWriter.write(magicValue);
			buffWriter.newLine();
			buffWriter.flush();
		} catch (IOException e1) {
			connected = false;
			try {
				socket.close();
				//System.out.println("Socket chiuso per errore in scrittura");
				if (tokenToDestroy != null) {
					tokenToDestroy = game.destroyUser(tokenToDestroy);
				}
			} catch (IOException e) {
			}
		}

	}

	/**
	 * Il metodo riceve in ingresso dal socket una stringa da protocollo.
	 * Ricostruisce le singole stringhe e le memorizza in un array di stringhe.
	 * 
	 * @param strings
	 *            Array di stringhe su cui verranno memorizzate le stringhe
	 *            tokenizzate.
	 * @param socket
	 *            Il socket necessario per ricevere la stringa dal socket
	 * @return Un array di stringhe contenenti le stringhe parsate
	 * @see server.ServerSocketComunicator#returnDestfromStrings(String[],
	 *      String)
	 */

	public final String[] returnStrings(String[] strings, Socket socket) {
		String buf;
		int i = 0;

		try {
			iSR = new InputStreamReader(socket.getInputStream());
			buffReader = new BufferedReader(iSR);
			buf = buffReader.readLine();
			if (buf != null) {
				//System.out.println(buf);
				if (buf.startsWith("@muoviDinosauro")) {
					returnDestfromStrings(strings, buf);
				} else {
					StringTokenizer stBegin = new StringTokenizer(buf, ",");
					strings[i] = stBegin.nextToken();
					i++;
					while (stBegin.hasMoreElements()) {
						StringTokenizer stFinal = new StringTokenizer(
								stBegin.nextToken(), "=");
						stFinal.nextToken();
						String support = stFinal.nextToken();
						strings[i] = support;

						i++;
					}
				}

			}

			return strings;
		} catch (IOException e1) {
			//System.out.println("chiudere client socket");
			return null;

		}
	}

	/**
	 * Il metodo riceve in ingresso dal socket una stringa da protocollo.
	 * Ricostruisce le singole stringhe e le memorizza in un array di stringhe.
	 * Il metodo viene invocato per restituire le stringhe opportunatamente
	 * separate al metodo che implementa il movimento
	 * 
	 * @param strings
	 *            Array di stringhe su cui verranno memorizzate le stringhe
	 *            tokenizzate.
	 * @param buf
	 *            Stringa da parsare
	 * @see server.ServerSocketComunicator#returnStrings(String[], Socket)
	 */

	private void returnDestfromStrings(String[] strings, String buf) {
		int i = 0;
		String support;
		StringTokenizer stBegin = new StringTokenizer(buf, ",");
		StringTokenizer stFinal = new StringTokenizer(stBegin.nextToken(), "=");
		strings[i] = "@muoviDinosauro";
		i++;
		while (stBegin.hasMoreElements() && (i < 3)) {
			stFinal = new StringTokenizer(stBegin.nextToken(), "=");
			stFinal.nextToken();
			support = stFinal.nextToken();
			strings[i] = support;

			i++;
		}

		support = stBegin.nextToken("}");
		String coord = support.substring(7);
		StringTokenizer stMove = new StringTokenizer(coord, ",");
		strings[i] = stMove.nextToken();
		i++;
		strings[i] = stMove.nextToken();
		;

	}

	/**
	 * Il metodo dopo aver tokenizzato la stringa tramite il metodo opportuno
	 * lancia il metodo desiderato
	 * 
	 * @see server.ServerSocketComunicator#createNewRace(String, String, String)
	 * @see server.ServerSocketComunicator#logUsers(String, String)
	 * @see server.ServerSocketComunicator#createNewRace(String, String, String)
	 * @see server.ServerSocketComunicator#gameAccess(String)
	 * @see server.ServerSocketComunicator#gameOut(String)
	 * @see server.ServerSocketComunicator#playingList(String)
	 * @see server.ServerSocketComunicator#logOut(String)
	 * @see server.ServerSocketComunicator#giveMeRanking(String)
	 * @see server.ServerSocketComunicator#showMap(String)
	 * @see server.ServerSocketComunicator#showLocalView(String, String)
	 * @see server.ServerSocketComunicator#denverList(String)
	 * @see server.ServerSocketComunicator#denverState(String, String)
	 * @see server.ServerSocketComunicator#denverMove(String, String, int, int)
	 * @see server.ServerSocketComunicator#growingUp(String, String)
	 * @see server.ServerSocketComunicator#layAnEgg(String, String)
	 * @see server.ServerSocketComunicator#acceptRound(String)
	 * @see server.ServerSocketComunicator#passRound(String)
	 * 
	 */
	public final void start() {
		boolean loop = true;
		while (loop && connected) {
			/*
			 * Il metodo strings ritorna nella prima posizione (0) dell'array il
			 * comando Nella seconda e nella terza rispettivamente user e
			 * password
			 */
			String[] strings = new String[5];
			if (returnStrings(strings, socket) == null) {
				tokenToDestroy = game.destroyUser(tokenToDestroy);
				return;
			}
			command = strings[0];

			if (command != null) {
				if (command.equals("@creaUtente")) {
					username = strings[1];
					password = strings[2];
					createNewUser(username, password);
				} else if (command.equals("@login")) {
					username = strings[1];
					password = strings[2];
					logUsers(username, password);
				} else if (command.equals("@creaRazza")) {
					token = strings[1];
					name = strings[2];
					type = strings[3];
					createNewRace(token, name, type);
				} else if (command.equals("@accessoPartita")) {
					token = strings[1];
					gameAccess(token);
				} else if (command.equals("@uscitaPartita")) {
					token = strings[1];
					gameOut(token);
				} else if (command.equals("@listaGiocatori")) {
					token = strings[1];
					playingList(token);
				} else if (command.equals("@logout")) {
					token = strings[1];
					logOut(token);
				} else if (command.equals("@classifica")) {
					token = strings[1];
					giveMeRanking(token);
				} else if (command.equals("@mappaGenerale")) {
					token = strings[1];
					showMap(token);
				} else if (command.equals("@vistaLocale")) {
					token = strings[1];
					idDino = strings[2];
					showLocalView(token, idDino);
				} else if (command.equals("@listaDinosauri")) {
					token = strings[1];
					denverList(token);
				} else if (command.equals("@statoDinosauro")) {
					token = strings[1];
					idDino = strings[2];
					denverState(token, idDino);
				} else if (command.equals("@muoviDinosauro")) {
					token = strings[1];
					idDino = strings[2];
					int y = Integer.parseInt(strings[4]);
					int col = Integer.parseInt(strings[3]);
					int row = D - y - 1;
					denverMove(token, idDino, row, col);
				} else if (command.equals("@cresciDinosauro")) {
					token = strings[1];
					idDino = strings[2];
					growingUp(idDino, token);
				} else if (command.equals("@deponiUovo")) {
					token = strings[1];
					idDino = strings[2];
					layAnEgg(idDino, token);
				} else if (command.equals("@confermaTurno")) {
					token = strings[1];
					acceptRound(token);
				} else if (command.equals("@passaTurno")) {
					token = strings[1];
					passRound(token);
				} else if (command.equals("@chiudiSocket")) {
					try {
						socket.close();
						loop = false;
						//System.out.println("Socket Chiuso");
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * Crea un nuovo utente e crea l'apposita stringa di risposta, in base
	 * all'esito dell'operazione richiesta, da scrivere sul buffer.
	 * 
	 * @param username
	 *            Lo username scelto dall'utente.
	 * @param password
	 *            La password scelta dall'utente.
	 * 
	 * @see server.Game#checkUsers(String, String)
	 */
	public final void createNewUser(String username, String password) {
		String magicValue = game.checkUsers(username, password);
		putReplyInSocket(magicValue);
	}

	/**
	 * Permette ad un utente di effettuare il login e crea l'apposita stringa di
	 * risposta, in base all'esito dell'operazione richiesta, da scrivere sul
	 * buffer.
	 * 
	 * @param username
	 *            Lo username corrispondente, possibilmente, ad un utente
	 *            creato.
	 * @param password
	 *            La password corrispondente, possibilmente, ad un utente
	 *            creato.
	 * 
	 * @see server.Game#logUsers(String, String)
	 */
	public final void logUsers(String username, String password) {

		String magicValue = game.logUsers(username, password);
		if (!(magicValue.equals("@no") || magicValue
				.equals("@no,@autenticazioneFallita"))) {
			tokenToDestroy = magicValue;
			magicValue = "@ok," + magicValue;
		}
		putReplyInSocket(magicValue);
	}

	/**
	 * Crea una nuova razza per un determinato utente e crea l'apposita stringa
	 * di risposta, in base all'esito dell'operazione richiesta, da scrivere sul
	 * buffer.
	 * 
	 * @param token
	 *            Il token dell'utente che vuole creare la razza.
	 * @param name
	 *            Il nome della razza definita dall'utente.
	 * @param type
	 *            Il tipo della razza che l'utente vuole creare. (e|c)
	 * 
	 * @see server.Game#createNewRace(String, String, String)
	 */
	public final void createNewRace(String token, String name, String type) {
		/*
		 * Metodo che riceve in ingresso l'array degli utenti loggati, un token
		 * un nome dell'eventuale razza e un tipo di dinosauro. Lancia una
		 * eccezionenel caso il token sia invalido, nel caso il nome della razza
		 * sia giaoccupato, nel caso esiste gia una razza per l'utente
		 * selezionato
		 */

		String magicValue = game.createNewRace(token, name, type);
		putReplyInSocket(magicValue);

	}

	/**
	 * Il metodo invoca gameAccess (che in caso positivo settera l'utente e lo
	 * aggiungera all'arrayList dei giocatori in partita) e risponde con una
	 * stringa da protocollo sia nel caso l'accesso sia andato a buon fine sia
	 * nel caso l'accesso sia fallito per svariati motivi
	 * 
	 * @param token
	 *            Il token dell'utente che vuole entrare in gioco.
	 * 
	 * @see server.Game#gameAccess(String)
	 */
	public final void gameAccess(String token) {

		String magicValue = game.gameAccess(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * 
	 * Il metodo invoca gameOut (che in caso positivo eliminera l'utente
	 * dall'arrayList dei giocatori in partita) e risponde con una stringa da
	 * protocollo sia nel caso l'uscita sia andata a buon fine sia nel caso
	 * l'uscita sia fallita per token invalido
	 * 
	 * @param token
	 *            Il token dell'utente che vuole uscire dal gioco.
	 * 
	 * @see server.Game#gameOut(String)
	 */
	public final void gameOut(String token) {

		String magicValue = game.gameOut(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Il metodo invoca playingList (che in caso positivo restituira - come
	 * stringa - la lista dei giocatori in partita) e risponde con una stringa
	 * da protocollo sia nel caso la crazione della lista sia andata a buon fine
	 * sia nel caso l'operazione sia fallita per token invalido
	 * 
	 * @param token
	 */
	public final void playingList(String token) {
		String magicValue = playingListSocketMaker(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Restituisce la lista dei giocatori in gioco preparando la stringa da
	 * scrivere sul buffer.
	 * 
	 * @param token
	 *            Token dell'utente che ha richiesto la lista dei giocatori.
	 * @return Stringa da protocollo.
	 * @see server.Game#playingList(String)
	 */
	public final String playingListSocketMaker(String token) {
		StringBuffer support = new StringBuffer();
		support.append("@listaGiocatori");
		List<String> playUsers;
		try {
			playUsers = game.playingList(token);
			for (String v : playUsers) {
				support.append("," + v);
			}
			return support.toString();
		} catch (InvalidTokenException e) {
			return e.getError();
		}

	}

	/**
	 * Permette ad un utente di effettuare il logout.
	 * 
	 * @param token
	 *            Token dell'utente che vuole effettuare il logout.
	 * 
	 * @see server.Game#logOut(String)
	 */
	public final void logOut(String token) {

		String magicValue = game.logOut(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Il metodo tramite l'invocazione di game.ranking(token) aggiorna il
	 * database delle razze. Quindi produce una stringa (da protocollo)
	 * scandendo per intero l'hashmap. Nel caso di token non valido restituisce
	 * un messaggio d'errore
	 * 
	 * @param token
	 *            Token dell'utente che richiede la classifica.
	 * @return Stringa da protocollo.
	 * @see server.Game#ranking(String)
	 */
	public final String rankingSocketMaker(String token) {
		List<Specie> ranking = new ArrayList<Specie>();
		StringBuffer message = new StringBuffer();
		message.append("@classifica");
		try {
			ranking = game.ranking(token);

			if (ranking != null) {
				for (Specie current : ranking) {
					String temp = ",{" + current.getUsername() + ","
							+ current.getName() + "," + current.getScore()
							+ "," + current.getCondition() + "}";
					message.append(temp);
				}
			}
		} catch (InvalidTokenException e) {
			return e.getError();
		}

		return message.toString();
	}

	/**
	 * Il metodo invoca rankingSocketMaker e invia la stringa di risposta sul
	 * buffer di scrittura
	 * 
	 * @param token
	 */
	public final void giveMeRanking(String token) {
		String magicValue = rankingSocketMaker(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Mappa di supporto che verra' costruita opportunatamente e passata al
	 * client.
	 * 
	 * @param token
	 *            Token dell'utente che ha richiesto la mappa.
	 * @return Stringa da protocollo.
	 * @see server.Game#mapCompare(String)
	 */
	public final String showMapSocketMaker(String token) {
		/*
		 
		 */
		String[][] supportMap;
		StringBuffer message = new StringBuffer();
		message.append("@mappaGenerale,");

		try {
			supportMap = game.mapCompare(token);
			message.append("{" + supportMap.length + "," + +supportMap.length
					+ "},");

			for (int i = supportMap.length - 1; i >= 0; i--) {
				for (int j = 0; j < supportMap.length; j++) {
					String temp = "[" + supportMap[i][j] + "]";
					message.append(temp);
				}
				message.append(";");
			}
		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		}

		return message.toString();
	}

	/**
	 * Il metodo invoca showMapSocketMaker e invia la stringa di risposta sul
	 * buffer di scrittura
	 * 
	 * @param token
	 *            Token dell'utente.
	 */
	public final void showMap(String token) {
		String magicValue = showMapSocketMaker(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Il metodo prende in ingresso un token e un idDinosauro. Dopo aver
	 * verificato che il token sia valido e che l'utente sia in
	 * partita(lanciando le eventuali eccezioni in caso contrario) preleva le
	 * posizioni del dinosauro e costruisce la stringa da protocollo da passare
	 * al client
	 * 
	 * @param token
	 *            Token dell'utente
	 * @param idDino
	 *            Id del dino per il quale e' stata richiesta la vista locale.
	 * @return Stringa da protocollo.
	 * @see server.Game#showLocalView(String, String)
	 */
	private final String showLocalViewSocketMaker(String token, String idDino) {
		/*
		
		 */

		map.Cell[][] localViewMap;
		String username;
		Dinosaur denver;
		int x, y;
		String temp;
		StringTokenizer st = new StringTokenizer(idDino, "0");
		username = st.nextToken();

		try {
			localViewMap = game.showLocalView(token, idDino);
			Specie spec = game.fromUsertoRace(username);
			denver = spec.checkIdAndReturnDino(idDino);
			x = denver.getColPosition() - denver.visualDino(denver);
			y = lenghtMap - denver.getRowPosition() - denver.visualDino(denver);
			StringBuffer bufString = new StringBuffer();

			int dimensionMap = denver.visualDino(denver) * 2 + 1;
			bufString.append("@vistaLocale,");
			/*
			 * Appendo le coordinate. Fare attenzione al fatto che la prima
			 * coordinata e' la colonna (quindi y) e la seconda la riga (x)
			 */
			temp = "{" + x + "," + y + "}," + "{" + dimensionMap + ","
					+ dimensionMap + "},";
			bufString.append(temp);

			/*
			 * Costruisco la stringa da protocollo. I dinosauri saranno a
			 * conoscenza dell'energia delle vegetazione e delle carogne
			 * indipendentemente dal fatto che essi siano erbivori o carnivori.
			 */{
				for (int i = localViewMap.length - 1; i >= 0; i--) {
					for (int j = 0; j < localViewMap.length; j++) {

						if (localViewMap[i][j].getThereIsDinosaur()) {
							temp = "["
									+ "d"
									+ ","
									+ localViewMap[i][j].getLocalDinosaur()
											.getIdDino() + "]";
							bufString.append(temp);
						} else if (localViewMap[i][j].isCarrion()) {
							temp = "["
									+ "c"
									+ ","
									+ localViewMap[i][j]
											.getCurrentEnergyCarrion() + "]";
							bufString.append(temp);
						} else if (localViewMap[i][j].isVegetation()) {
							temp = "["
									+ "v"
									+ ","
									+ localViewMap[i][j]
											.getCurrentEnergyVegetation() + "]";
							bufString.append(temp);
						} else if (localViewMap[i][j].isEarth()) {
							temp = "[" + "t" + "]";
							bufString.append(temp);
						} else if (localViewMap[i][j].isWater()) {
							temp = "[" + "a" + "]";
							bufString.append(temp);
						}

					}
					bufString.append(";");

				}
				return bufString.toString();
			}
		}

		catch (InvalidTokenException e) {
			return e.getError();
		} catch (InvalidIdException e) {
			return e.getError();
		} catch (UserNotPlayingException e1) {
			return e1.getError();
		} catch (RaceNullException e) {
			/*
			 * Controllo mai eseguito, prima viene gestito l'utente non in
			 * partita
			 */
			return null;
		}

	}

	/**
	 * Il metodo invoca showLocalViewSocketMaker e invia la stringa di risposta
	 * sul buffer di scrittura
	 * 
	 * @param token
	 *            Token dell'utente.
	 * @idDino Id del dino del quale e' stata richiesta la vista locale.
	 */
	public final void showLocalView(String token, String idDino) {

		String magicValue = showLocalViewSocketMaker(token, idDino);
		putReplyInSocket(magicValue);
	}

	/**
	 * Il metodo invoca giveMeDenverList e costruisce la stringa da protocollo
	 * in base alla lista tornata dal metodo invocato.
	 * 
	 * @param token
	 *            Stringa dell'utente che ha richiesto la lista.
	 * @return Stringa da protocollo.
	 * @see server.Game#giveMeDenverList(String)
	 */
	private final String giveMeSocketDenverList(String token) {
		String temp;
		StringBuffer message = new StringBuffer();
		List<String> dinosList = new ArrayList<String>();
		try {
			message.append("@listaDinosauri");
			dinosList = game.giveMeDenverList(token);
			/*
			 * Controlla se la lista dei dinosauri non sia eventualmente vuota.
			 * TODO problema UOVA. gestire.
			 */
			if (dinosList != null) {
				for (String currentIdDino : dinosList) {
					temp = "," + currentIdDino;
					message.append(temp);
				}
			}

		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		} catch (InvalidIdException e) {
			return e.getError();
		}
		return message.toString();
	}

	/**
	 * Il metodo invoca giveMeSocketDenverList e invia la stringa di risposta
	 * sul buffer di scrittura
	 * 
	 * @param token
	 *            Token dell'utente che ha richiesto la lista.
	 */
	public final void denverList(String token) {

		String magicValue = giveMeSocketDenverList(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Restituisce lo stato di uno specifico dinosauro passato come id in ingresso.
	 * @param token Token dell'utente che ha richiesto lo stato del dino.
	 * @param idDino Id dino del quale e' stato richiesto lo stato.
	 * @return Stringa da protocollo.
	 * @see server.Game#denverState(String, String)
	 */
	private final String giveMeSocketDenverState(String token, String idDino) {
		StringBuffer message = new StringBuffer();
		String[] denverInfo;
		String temp;
		int i = 6;
		String x, y;

		message.append("@statoDinosauro");
		try {
			denverInfo = game.denverState(token, idDino);

			x = denverInfo[3];
			y = Integer.toString((lenghtMap - Integer.parseInt(denverInfo[4])));

			temp = "," + denverInfo[0] + "," + denverInfo[1] + ","
					+ denverInfo[2] + ",{" + x + "," + y + "}," + denverInfo[5]
					+ ",";
			message.append(temp);
			if (!denverInfo[i].equals("no")) {
				temp = denverInfo[6] + "," + denverInfo[7];
				message.append(temp);
			}
		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		} catch (InvalidIdException e) {
			return e.getError();
		}
		return message.toString();
	}

	/**
	 * Il metodo invoca giveMeSocketDenverState e invia la stringa di risposta
	 * sul buffer di scrittura
	 * @param token Token dell'utente che ha richiesto lo stato.
	 * @param idDino Id del dino del quale e' stato richiesto lo stato.
	 */
	public final void denverState(String token, String idDino) {

		String magicValue = giveMeSocketDenverState(token, idDino);
		putReplyInSocket(magicValue);
	}

	/**
	 * Metodo che richiede di spostare un dino in una determinata posizione.
	 * @param token Token utente.
	 * @param idDino Id del dino che compie il movimento.
	 * @param row Riga di Arrivo del dino.
	 * @param col Colonna di arrivo del dino.
	 * @see server.Game#denverMove(String, String, int, int)
	 */
	public final void denverMove(String token, String idDino, int row, int col) {

		String magicValue = game.denverMove(token, idDino, row, col);
		putReplyInSocket(magicValue);
	}

	/**
	 * Richiede la crescita per uno specifico dinosauro.
	 * @param idDino Id del dino da fare crescere.
	 * @param token Token dell'utente proprietario del dino.
	 * @see server.Game#growingUp(String, String)
	 */
	public final void growingUp(String idDino, String token) {
		String magicValue = game.growingUp(idDino, token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Richiede la deposizione di un uovo da parte di un determinato dinosauro.
	 * @param idDino Id del dino che deve fare l'uovo.
	 * @param token Token dell'utente proprietario del dino.
	 * @see server.Game#layAnEgg(String, String)
	 */
	public final void layAnEgg(String idDino, String token) {
		String magicValue = game.layAnEgg(idDino, token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Permetta ad un utente di accettare il turno.
	 * @param token Token utente.
	 * @see server.Game#acceptRound(String)
	 */
	public final void acceptRound(String token) {

		String magicValue = game.acceptRound(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Permette ad un utente di passare il turno.
	 * @param token Token utente.
	 * @see server.Game#passRound(String)
	 */
	public final void passRound(String token) {

		String magicValue = game.passRound(token);
		putReplyInSocket(magicValue);
	}

	/**
	 * Notifica il cambio turno agli utenti in gioco.
	 * @param currentUser L'utente corrente da fare giocare.
	 */
	public final void changeRound(String currentUser) {

		putReplyInSocket("@cambioTurno," + currentUser);
		//System.out.println("@cambioTurno," + currentUser);

	}
}
