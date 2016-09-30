package server;

import java.util.*;

import dinosaur.Dinosaur;
import dinosaur.RaceManager;
import dinosaur.Specie;
import exception.*;
import map.*;

/**
 * @author gas12n
 */
public class Game {

	/**
	 * @uml.property name="server"
	 * @uml.associationEnd
	 */
	private Server server;
	/**
	 * @uml.property name="userMan"
	 * @uml.associationEnd
	 */
	private UserManager userMan;
	/**
	 * @uml.property name="mapMan"
	 * @uml.associationEnd
	 */
	private MapManager mapMan;
	/**
	 * @uml.property name="raceMan"
	 * @uml.associationEnd
	 */
	private RaceManager raceMan;
	/**
	 * @uml.property name="roundMan"
	 * @uml.associationEnd
	 */
	private RoundManager roundMan;

	/**
	 * Costruttore della classe game
	 * 
	 * @param server
	 *            Il server che fa partire la connessione (rmi o server)
	 */
	public Game(Server server) {
		this.server = server;
		/* Costruttore che ini8zializza i vari manager */
		this.mapMan = new MapManager();
		this.raceMan = new RaceManager();
		this.userMan = new UserManager();
		this.roundMan = new RoundManager(this);

		/*
		 * Questi metodi inizializzano i link fra i manager. TODO eliminarli
		 * quando c'e' tempo
		 */
		mapMan.MapManagerGo(userMan, raceMan);
		raceMan.raceManagerGo(userMan);
		userMan.UserManagerGo(raceMan);
	}

	/**
	 * Invoca il metodo checkUsers di userManager
	 * 
	 * @param username
	 *            Nome utente
	 * @param password
	 *            Password dell'utente
	 * @return Restituisce l'esito dell'operazione
	 * @see server.UserManager#checkUsers(String, String)
	 */
	synchronized public final String checkUsers(String username, String password) {
		return userMan.checkUsers(username, password);
	}

	/**
	 * Invoca il metodo logUsers di userManager
	 * 
	 * @param username
	 *            Nome utente
	 * @param password
	 *            Password dell'utente
	 * @return Restituisce l'esito dell'operazione
	 * @see server.UserManager#logUsers(String, String)(String, String)
	 */
	synchronized public final String logUsers(String username, String password) {
		return userMan.logUsers(username, password);
	}

	/**
	 * Il metodo verifica che sia possibile creare la razza e risponde di
	 * conseguenza
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @param name
	 *            Nome della razza che si vuole creare
	 * @param type
	 *            Tipo della razza: erbivoro o carnivoro
	 * @return Restituisce l'esito dell'operazione
	 * @see dinosaur.RaceManager#setRace(String, String, String, int, int)
	 * @see InvalidTokenException
	 * @see RaceNameTakenException
	 * @see RaceAlreadyExistsException
	 */
	synchronized public final String createNewRace(String token, String name,
			String type) {
		/*
		 * Metodo che riceve in ingresso l'array degli utenti loggati, un token
		 * un nome dell'eventuale razza e un tipo di dinosauro. Lancia una
		 * eccezionenel caso il token sia invalido, nel caso il nome della razza
		 * sia gia'occupato, nel caso esiste gia' una razza per l'utente
		 * selezionato
		 */

		int coord[];
		coord = mapMan.randomPositioner();
		String message;
		Dinosaur denver;
		boolean[][] localPlayerMap;

		try {
			denver = raceMan.setRace(token, name, type, coord[0], coord[1]);
			localPlayerMap = userMan.returnUserfromUserList(
					userMan.tokenToUsername(token)).getUserMap();
			localPlayerMap = mapMan.updateVisibility(denver.visualDino(denver),
					localPlayerMap, denver.getRowPosition(),
					denver.getColPosition());

			userMan.saveUsers();
			raceMan.saveRaceDB();
			message = "@ok";
		} catch (InvalidTokenException e) {
			message = e.getError();
		} catch (RaceNameTakenException e) {
			message = e.getError();
		} catch (RaceAlreadyExistsException e) {
			message = e.getError();
		}

		return message;
	}

	/**
	 * Il metodo tenta l'accesso in gioco per l'utente identificato dal token
	 * passato
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return Restituisce l'esito dell'operazione
	 * @see InvalidTokenException
	 * @see RaceNullException
	 * @see server.UserManager#gameAccess(String)
	 */
	synchronized public final String gameAccess(String token) {
		String result = userMan.gameAccess(token);
		List<Dinosaur> dinosList = new ArrayList<Dinosaur>();

		try {
			dinosList = raceMan.fromUsertoRace(
					userMan.returnUserfromLoggedList(token).getUsername())
					.getDinos();
			for (Dinosaur currentDino : dinosList) {
				mapMan.putInMap(currentDino);
				/*
				 * Aggiorno la visibilita' per gestire il caso
				 * "dinosauro spostato"
				 */
				User u = userMan.returnUserfromUserList(userMan
						.tokenToUsername(token));
				u.setUserMap(mapMan.updateVisibility(
						currentDino.visualDino(currentDino), u.getUserMap(),
						currentDino.getRowPosition(),
						currentDino.getColPosition()));
				userMan.saveUsers();
				if (userMan.isFirstUser()) {
					FirstStart fs = new FirstStart(roundMan);
					fs.start();
					// roundMan.start();
				}
			}

		} catch (InvalidTokenException e) {
			result = e.getError();
		} catch (RaceNullException e) {
			result = e.getError();
		}

		return result;
	}

	/**
	 * Il metodo permette l'uscita dal gioco dell'utente identificato dal token
	 * passato al metodo
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return Restituisce l'esito dell'operazione
	 * @see server.UserManager#gameAccess(String)
	 * @see InvalidTokenException
	 * @see UserNotPlayingException
	 * @see RaceNullException
	 */
	synchronized public final String gameOut(String token) {
		List<Dinosaur> dinosList = new ArrayList<Dinosaur>();
		String result;

		try {
			userMan.returnUserfromUserList(userMan.tokenToUsername(token));
			dinosList = raceMan.fromUsertoRace(
					userMan.returnUserfromPlayingList(token).getUsername())
					.getDinos();
			for (Dinosaur currentDino : dinosList) {
				mapMan.removeDinosaurFromMap(currentDino);
			}
			result = userMan.gameOut(token);

			if (userMan.isLastUser()) {
				roundMan.stop();
			}

		} catch (InvalidTokenException e) {
			result = e.getError();
		} catch (UserNotPlayingException e) {
			result = "@no";
		} catch (RaceNullException e) {
			result = e.getError();
		}

		return result;
	}

	/**
	 * Il metodo ritorna la lista dei giocatori in partita
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return Ritorna la lista dei giocatori attualmente in partita
	 * @throws InvalidTokenException
	 */
	public final List<String> playingList(String token)
			throws InvalidTokenException {
		return userMan.playingList(token);
	}

	/**
	 * Il metodo invoca il logout per il giocatore identificato dal token
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return
	 */
	public final String logOut(String token) {
		return userMan.logOut(token);
	}

	/**
	 * Il metodo costruira' la classifica ritornando un'arraylist con tutte le
	 * razze create sul gioco
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return Il metodo ritorna la lista delle razze
	 * @throws InvalidTokenException
	 */
	synchronized public final List<Specie> ranking(String token)
			throws InvalidTokenException {
		List<Specie> raceList = new ArrayList<Specie>();
		userMan.returnUserfromLoggedList(token);
		Map<String, Specie> supportDB = raceMan.getRaceDB();

		for (Specie current : supportDB.values()) {
			raceList.add(current);
			raceMan.saveRaceDB();
		}
		return raceList;
	}

	public final LoggedUser returnUserfromLoggedList(String token)
			throws InvalidTokenException {
		return userMan.returnUserfromLoggedList(token);
	}

	public final LoggedUser returnUserfromPlayingList(String token)
			throws UserNotPlayingException {
		return userMan.returnUserfromPlayingList(token);
	}

	public final Specie fromUsertoRace(String username)
			throws InvalidTokenException, RaceNullException {

		return raceMan.fromUsertoRace(username);

	}

	/**
	 * Il metodo aggiorna la visibilita' dell'utente comparandola la mappa
	 * globale con la sua locale
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return
	 * @throws InvalidTokenException
	 * @throws UserNotPlayingException
	 */
	public final String[][] mapCompare(String token)
			throws InvalidTokenException, UserNotPlayingException {
		userMan.returnUserfromUserList(userMan.tokenToUsername(token));
		userMan.returnUserfromPlayingList(token);
		return mapMan.mapCompare(token,
				userMan.returnUserfromUserList(userMan.tokenToUsername(token))
						.getUserMap());
	}

	/**
	 * Il metodo chiama il map manager e ritorna la vista locale del dinosauro
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @param idDino
	 *            Id del dinosauro
	 * @return Ritorna la vista locale del dinosauro
	 * @throws InvalidTokenException
	 * @throws InvalidIdException
	 * @throws UserNotPlayingException
	 */
	public final Cell[][] showLocalView(String token, String idDino)
			throws InvalidTokenException, InvalidIdException,
			UserNotPlayingException {
		try {
			return mapMan.showLocalView(token, idDino, userMan
					.returnUserfromLoggedList(token).getUsername());
		} catch (RaceNullException e) {
			throw new InvalidIdException();
		}
	}

	/**
	 * Il metodo fa dapprima il controllo sulla validita' del token. Quindi
	 * preleva l'username dal token e con questo seleziona la razza. Infine
	 * ritorna la lista di dinosauri.
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @return Il metodo ritorna la lista dei dinosauri della specie che non
	 *         siano uova
	 * @throws InvalidTokenException
	 * @throws UserNotPlayingException
	 * @throws InvalidIdException
	 */
	public final List<String> giveMeDenverList(String token)
			throws InvalidTokenException, UserNotPlayingException,
			InvalidIdException {

		String username;
		Specie spec;
		userMan.returnUserfromLoggedList(token);
		username = userMan.returnUserfromPlayingList(token).getUsername();
		try {
			spec = raceMan.fromUsertoRace(username);
		} catch (RaceNullException e) {
			throw new InvalidIdException();
		}

		return spec.getDenverList(token);
	}

	/**
	 * Il metodo controlla se il dinosauro e' di proprieta' del giocatore
	 * identificato dal token. Ritorna le informazioni sul dinosauro
	 * identificato dall'id in base a tale informazione.
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @param idDino
	 *            Id del dinosauro
	 * @return Ritorna un'array di stringhe contenente tutte le informazioni sul
	 *         dinosauro
	 * @throws InvalidIdException
	 * @throws InvalidTokenException
	 * @throws UserNotPlayingException
	 */
	public final String[] denverState(String token, String idDino)
			throws InvalidIdException, InvalidTokenException,
			UserNotPlayingException {
		Dinosaur denver;
		String username;
		Specie spec;
		boolean isDenverMine;
		String[] denverInfo = new String[8];
		int i = 0;

		userMan.returnUserfromLoggedList(token); /* controllo sul token */
		username = userMan.returnUserfromPlayingList(token).getUsername();
		/*
		 * controllo sull'id se user e' in partita
		 */
		try {
			spec = raceMan.fromUsertoRace(username);
		} catch (RaceNullException e1) {
			throw new InvalidIdException();
		}
		/*
		 * prelievo specie e controllo sull'id
		 */
		isDenverMine = spec.checkIdAndReturnBoolean(idDino);

		if (isDenverMine) {
			denver = spec.checkIdAndReturnDino(idDino);
			denverInfo[i] = spec.getUsername(); /* 0 */
			i++;
			denverInfo[i] = spec.getName();/* 1 */
			i++;
			denverInfo[i] = spec.getType();/* 2 */
			i++;
			denverInfo[i] = Integer.toString(denver.getColPosition());/* 3 */
			// System.out.println(denverInfo[i]);
			i++;
			denverInfo[i] = Integer.toString(denver.getRowPosition());/* 4 */
			// System.out.println(denverInfo[i]);
			i++;
			denverInfo[i] = Integer.toString(denver.getDimension());/* 5 */
			++i;
			denverInfo[i] = Integer.toString(denver.getCurrentEnergy());/* 6 */
			++i;
			denverInfo[i] = Integer.toString(denver.getAge());/* 7 */
		} else {
			/* Dinosauro non e' mio, chiamamolo */
			Specie enemyspec;
			try {
				enemyspec = raceMan.fromIdtoSpec(idDino);
			} catch (RaceNullException e) {
				/*
				 * Se l'id non e' della razza padrone e neanche delle altre
				 * razze allora invia id non valido.
				 */
				throw new InvalidIdException();
			}

			Dinosaur enemyDenver;
			// boolean enemyDenverFound = false;
			try {
				enemyDenver = enemyspec.checkIdAndReturnDino(idDino);/*
																	 * for
																	 * (Dinosaur
																	 * currentDino
																	 * :
																	 * spec.getDinos
																	 * ()) { if
																	 * (!
																	 * enemyDenverFound
																	 * ) { for
																	 * (int j =
																	 * currentDino
																	 * .
																	 * getRowPosition
																	 * ()-
																	 * currentDino
																	 * .
																	 * visualDino
																	 * (
																	 * currentDino
																	 * ); !
																	 * enemyDenverFound
																	 * && (j <=
																	 * currentDino
																	 * .
																	 * getRowPosition
																	 * () +
																	 * currentDino
																	 * .
																	 * visualDino
																	 * (
																	 * currentDino
																	 * )); j++)
																	 * {
																	 * 
																	 * for (int
																	 * k =
																	 * currentDino
																	 * .
																	 * getColPosition
																	 * ()-
																	 * currentDino
																	 * .
																	 * visualDino
																	 * (
																	 * currentDino
																	 * ); !
																	 * enemyDenverFound
																	 * && (k <=
																	 * currentDino
																	 * .
																	 * getColPosition
																	 * () +
																	 * currentDino
																	 * .
																	 * visualDino
																	 * (
																	 * currentDino
																	 * )); k++)
																	 * { if
																	 * ((enemyDenver
																	 * .
																	 * getRowPosition
																	 * () == j)
																	 * &&
																	 * (enemyDenver
																	 * .
																	 * getColPosition
																	 * () == k))
																	 * {
																	 * enemyDenverFound
																	 * = true; }
																	 * } } } if
																	 * (!
																	 * enemyDenverFound
																	 * ) { throw
																	 * new
																	 * InvalidIdException
																	 * (); }
																	 */
				i = 0;
				denverInfo[i] = enemyspec.getUsername(); /* 0 */
				i++;
				denverInfo[i] = enemyspec.getName();/* 1 */
				i++;
				denverInfo[i] = enemyspec.getType();/* 2 */
				i++;
				denverInfo[i] = Integer.toString(enemyDenver.getColPosition());/* 3 */
				// System.out.println(denverInfo[i] + "nemico");
				i++;
				denverInfo[i] = Integer.toString(enemyDenver.getRowPosition());/* 4 */
				// System.out.println(denverInfo[i] + "nemico");
				i++;
				denverInfo[i] = Integer.toString(enemyDenver.getDimension());/* 5 */
				// System.out.println(denverInfo[i] + "nemico");
				++i;
				denverInfo[i] = "no";/* 6 */
				++i;
				denverInfo[i] = "no";/* 7 */
			} catch (InvalidIdException e) {
			}
		}
		return denverInfo;
	}

	/**
	 * Il metodo permette il movimento del dinosauro. Gestisce il mangiare da
	 * parte del dinosauro che si muove, gestisce eventualmente il combattimento
	 * e se la destinazione non e' valida. Elimina il dinosauro dalla mappa se
	 * questo ha esaurito la sua energia durante una delle dinamiche del
	 * movimento.
	 * 
	 * 
	 * @param token
	 *            Token univoco associato all'utente attualmente loggato
	 * @param idDino
	 *            Id del dinosauro
	 * @param rowDest
	 *            Riga di destinazione
	 * @param colDest
	 *            Colonna di destinazione
	 * 
	 * @see server.Game#catchHerbCollision(Specie, Specie)
	 * @see server.Game#whoWinTheBattle(Dinosaur, Dinosaur, Specie, Specie)
	 * @see server.Game#updateDenverPosition(Cell[][], Dinosaur, User, int[],
	 *      int, int, int)
	 * @see server.Game#eatDenverEat(Dinosaur, String, Cell[][], int, int) * @see
	 *      server.Game#removeDinosaur(Dinosaur, Specie, int, int, String)
	 * @return Restituisce l'esito dell'operazione
	 */
	synchronized public final String denverMove(String token, String idDino,
			int rowDest, int colDest) {

		int step;
		LoggedUser playUser;
		Specie spec;
		Dinosaur denver;
		try {
			User u = userMan.returnUserfromUserList(userMan
					.tokenToUsername(token));
			/* Gestione token non valido */
			userMan.returnUserfromLoggedList(token);
			/* Gestione utente non in partita */
			playUser = userMan.returnUserfromPlayingList(token);
			spec = raceMan.fromUsertoRace(playUser.getUsername());

			/* Gestione id non valido */
			denver = spec.checkIdAndReturnDino(idDino);

			int[] part = new int[2];
			part[0] = denver.getRowPosition();
			part[1] = denver.getColPosition();

			if ((userMan.getCurrentUser().getUsername()).equals(playUser
					.getUsername()) && roundMan.isRoundConfirmed()) {
				/* Se il tuo utente e' quello abilitato a muoversi */
				if (denver.isAlreadyMoved()) {
					return "@no,@raggiuntoLimiteMosseDinosauro";
				}
				/*
				 * Lancia l'eccezione "destinazione irraggiungibile" se il
				 * numero di passi necessario e' maggiore di quanto il dinosauro
				 * potrebbe fare (in dipendenza della sua specie)
				 */
				step = mapMan.isReachable(colDest, rowDest, part[1], part[0],
						spec);
				// System.out.println(step);
				if (step > spec.maxStep(spec.getType())) {
					return "@no,@destinazioneNonValida";
				} else {
					try {
						if (!raceMan.istooWeakDenver(denver, step, spec)) {
							/* C'e' abbastanza energia */
							denver.setAlreadyMoved(true);
							Cell[][] globalMap = mapMan.getGlobalMap();
							if (globalMap[rowDest][colDest]
									.getThereIsDinosaur()) {
								/* Se mi sono incappato in un altro dino */
								Dinosaur enemyDenver = globalMap[rowDest][colDest]
										.getLocalDinosaur();
								Specie enemySpec = raceMan
										.fromIdtoSpec(enemyDenver.getIdDino());
								if (catchHerbCollision(spec, enemySpec)) {
									denver.setAlreadyMoved(false);
									return "@no,@destinazioneNonValida";
								}
								whoWinTheBattle(denver, enemyDenver, spec,
										enemySpec);
								return null;
							}

							else { /* semplice spostamento, nessun problema */
								/* Aggiornamento posizione dinosauro */
								updateDenverPosition(globalMap, denver, u,
										part, rowDest, colDest, step);
								try {
									eatDenverEat(denver, spec.getType(),
											globalMap, rowDest, colDest);
								} catch (CarrionExhaustException e) {
									mapMan.putNewCarrion();
								}
								return "@ok";
							}
						} else { /* Ha meno energia di quanta gliene serva */
							removeDinosaur(denver, spec, part[0], part[1],
									token);
							return "@no,@mortePerInedia";

						}

					} catch (YouWonTheBattle e) {
						updateDenverPosition(mapMan.getGlobalMap(), denver, u,
								part, rowDest, colDest, step);
						return "@ok,@combattimento,v";
					} catch (YouLostTheBattle e) {
						removeDinosaur(denver, spec, part[0], part[1], token);
						return "@ok,@combattimento,p";

					} catch (RaceNullException e) {
						;
					}
				}

			} else {
				return "@no,@nonIlTuoTurno";
			}
		} catch (InvalidTokenException e1) {
			return e1.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		} catch (InvalidIdException e) {
			return e.getError();
		} catch (RaceNullException e) {
			return "@no,@nonInPartita";
		}
		return null;
	}

	/**
	 * Aggiorna la posizione del dinosauro, riposizionandolo nella cella di
	 * destinazione prescelta
	 * 
	 * @param globalMap
	 *            La mappa globale del gioco
	 * @param denver
	 *            Il dinosauro selezionato
	 * @param u
	 *            L'utente proprietario del dinosauro
	 * @param part
	 * @param rowDest
	 *            Riga di destinazione
	 * @param colDest
	 *            Colonna di destinazione
	 * @param step
	 *            Numero di passi
	 */
	synchronized private final void updateDenverPosition(Cell[][] globalMap,
			Dinosaur denver, User u, int[] part, int rowDest, int colDest,
			int step) {

		/* Elimina i riferimenti alla posizione del dino prima di partire */
		mapMan.removeDinosaurFromMap(denver);
		/* Aggiornamento nuova posizione nelle coordinate del dino */
		denver.setyPosition(rowDest);
		denver.setxPosition(colDest);
		/* Aggiornamento energia corrente */
		denver.setCurrentEnergy(denver.getCurrentEnergy() - 10
				* (int) Math.pow(2, step));
		boolean[][] localUserMap = mapMan.updateVisibility(
				denver.visualDino(denver), u.getUserMap(), rowDest, colDest);
		u.setUserMap(localUserMap);
		userMan.saveUsers();
		/* Aggiornamento posizione nella mappa globale e salvataggio user */
		globalMap[rowDest][colDest].setLocalDinosaur(denver);
		globalMap[rowDest][colDest].setThereIsDinosaur(true);
		globalMap[rowDest][colDest].setIdDinosaur(denver.getIdDino());

		mapMan.saveMapDB();
		raceMan.saveRaceDB();
	}

	/**
	 * Il metodo calcola la forza dei due dinosauri e fornisce un vincitore. Se
	 * il dinosauro e' carnivoro lo fa mangiare, evitando che si superi
	 * l'energia massima del dinosauro
	 * 
	 * @param denver
	 *            Dinosauro che attacca
	 * @param enemyDenver
	 *            Dinosauro che difesa
	 * @param spec
	 *            Specie del dinosauro attaccante
	 * @param enemySpec
	 *            Specie del dinosauro attaccato
	 * @throws YouWonTheBattle
	 * @throws YouLostTheBattle
	 */
	synchronized private void whoWinTheBattle(Dinosaur denver,
			Dinosaur enemyDenver, Specie spec, Specie enemySpec)
			throws YouWonTheBattle, YouLostTheBattle {

		int eatable;
		double perc = 0.75;

		int powerOfMine = denver.getDimension() * denver.getCurrentEnergy();
		if (spec.getType().equals("c")) {
			powerOfMine = 2 * powerOfMine;
		}

		int enemyPower = enemyDenver.getDimension()
				* enemyDenver.getCurrentEnergy();
		if (enemySpec.getType().equals("c")) {
			enemyPower = 2 * enemyPower;
		}
		if (powerOfMine >= enemyPower) {
			if (spec.getType().equals("c")) {
				/*
				 * Il dinosauro, se carnivoro, dara' due mozzicate al dinosauro
				 * s<confitto
				 */
				eatable = (int) (enemyDenver.getCurrentEnergy() * perc);
				if (denver.getMaxEnergy() - denver.getCurrentEnergy() > eatable) {
					denver.setCurrentEnergy(denver.getCurrentEnergy() + eatable);
				} else {
					denver.setCurrentEnergy(denver.getMaxEnergy());
				}
				String enemyToken;
				try {
					enemyToken = userMan.usernametoToken(enemySpec
							.getUsername());
					removeDinosaur(enemyDenver, enemySpec,
							enemyDenver.getRowPosition(),
							enemyDenver.getColPosition(), enemyToken);

					throw new YouWonTheBattle();
				} catch (InvalidTokenException e) {
					/* caso gia' controllato */;
				}
			}

		} else {
			String token;
			try {
				if (enemySpec.getType().equals("c")) {
					eatable = (int) (denver.getCurrentEnergy() * perc);
					if (enemyDenver.getMaxEnergy()
							- enemyDenver.getCurrentEnergy() > eatable) {
						enemyDenver.setCurrentEnergy(enemyDenver
								.getCurrentEnergy() + eatable);
					} else {
						enemyDenver
								.setCurrentEnergy(enemyDenver.getMaxEnergy());
					}
				}
				token = userMan.usernametoToken(spec.getUsername());
				removeDinosaur(denver, spec, denver.getRowPosition(),
						denver.getColPosition(), token);
				throw new YouLostTheBattle();
			} catch (InvalidTokenException e) {
				/* caso gia' controllato */;
			}

		}
	}

	/**
	 * Il metodo rimuove il dinosauro dalla mappa e dal gioco. Gestisce il caso
	 * 'Specie estinta'
	 * 
	 * @param denver
	 *            Dinosauro da rimuovere
	 * @param spec
	 *            Specie del dinosauro da rimuovere
	 * @param row
	 *            Riga del dinosauro
	 * @param col
	 *            Colonna del dinosauro
	 * @param token
	 */
	synchronized private void removeDinosaur(Dinosaur denver, Specie spec,
			int row, int col, String token) {

		spec.removeDenver(denver);
		mapMan.removeDinosaurFromMap(denver);
		denver = null;
		mapMan.saveMapDB();

		if (spec.getDinos().size() == 0) {
			/*
			 * Elimina la razza per l'utente in gioco, quello loggato e quello
			 * generale e rende totalmente buio la sua mappa delle visibilita'
			 */
			try {
				spec.setCondition("n");
				userMan.getPlayingUsers().remove(
						userMan.returnUserfromPlayingList(token));

				LoggedUser supportUser = userMan
						.returnUserfromLoggedList(token);
				supportUser.setRace(null);
				User u = userMan.returnUserfromUserList(userMan
						.tokenToUsername(token));
				u.setUserMapToFalse();
				u.setRace(null);
				userMan.saveUsers();
				raceMan.saveRaceDB();
			} catch (InvalidTokenException e) {
			} catch (UserNotPlayingException e) {
			}
		}
		raceMan.saveRaceDB();
		userMan.saveUsers();
	}

	/**
	 * Il metodo riceve le specie dei due dinosauri. Restituisce true se sono
	 * entrambi di tipo erbivoro, false altrimenti * @param spec Specie da
	 * esaminare
	 * 
	 * @param enemySpec
	 *            Altra specie da esaminare
	 * @return Ritorna true se c'e' stata una collisione
	 */
	private boolean catchHerbCollision(Specie spec, Specie enemySpec) {

		if (spec.getType().equals(enemySpec.getType())
				&& spec.getType().equals("e")) {
			return true;
		}
		return false;

	}

	/**
	 * Il metodo sceglie in base alla cella di destinazione e al tipo del
	 * dinosauro se il dinosauro puo' mangiare o meno. Gestisce il caso 'energia
	 * ricavabile troppo elevata'
	 * 
	 * @param denver
	 *            Dinosauro da far mangiare
	 * @param type
	 *            Tipo del dinosauro (erbivoro o carnivoro)
	 * @param eatMap
	 *            Mappa da cui prendere le informazioni sul cibo
	 * @param rowDest
	 *            Riga di destinazione
	 * @param coldest
	 *            Colonna di destinazione
	 * @throws CarrionExhaustException
	 */
	synchronized private void eatDenverEat(Dinosaur denver, String type,
			Cell[][] eatMap, int rowDest, int coldest)
			throws CarrionExhaustException {
		int row = denver.getRowPosition();
		int col = denver.getColPosition();
		int eatable = denver.getMaxEnergy() - denver.getCurrentEnergy();

		if (type.equals("e") && eatMap[row][col].isVegetation()) {
			/* Denver e' un erbivoro e si trova sulla vegetazione. SLURP! */
			int vegPower = eatMap[row][col].getCurrentEnergyVegetation();

			if (vegPower > eatable) {
				/*
				 * Se la vegetazone ha piu' energia di quanta denver ne possa
				 * mangiare
				 */
				denver.setCurrentEnergy(denver.getMaxEnergy());
				eatMap[row][col].setCurrentEnergyVegetation(vegPower - eatable);
				raceMan.saveRaceDB();
			} else {
				denver.setCurrentEnergy(denver.getCurrentEnergy() + vegPower);
				eatMap[row][col].setCurrentEnergyVegetation(0);
				raceMan.saveRaceDB();

			}
		} else if (type.equals("c") && eatMap[row][col].isCarrion()) {
			/* Denver e' un carnivoro e si trova su una carogna. GNAM! */
			int carrPower = eatMap[row][col].getCurrentEnergyCarrion();

			if (carrPower > eatable) {
				/*
				 * Se la carogna ha piu' energia di quanta denver ne possa
				 * mangiare
				 */
				denver.setCurrentEnergy(denver.getMaxEnergy());
				eatMap[row][col].setCurrentEnergyCarrion(carrPower - eatable);
				raceMan.saveRaceDB();
			} else {
				/*
				 * Il dinosauro raggiunge l'energia massima e la carogna si
				 * esaurisce. Verra' ricreata in un punto casuale della mappa
				 */
				denver.setCurrentEnergy(denver.getCurrentEnergy() + carrPower);
				eatMap[row][col].setCarrion(false);
				eatMap[row][col].setEarth(true);
				raceMan.saveRaceDB();
				mapMan.saveMapDB();
				throw new CarrionExhaustException();
			}
		}
	}

	/**
	 * Il metodo permette la crescita del dinosauro. Vengono gestiti casi quali
	 * dinosauro gia' mosso, specie gia' completa, dinosauro troppo debole. Fa
	 * morire il dinosauro in caso di energia non sufficiente.
	 * 
	 * @param idDino
	 *            Id del dinosauro
	 * @param token
	 *            Token identificativo dell'utente
	 * @see server.Game#removeDinosaur(Dinosaur, Specie, int, int, String)
	 * 
	 * @return Ritorna l'esito dell'operazione
	 */
	synchronized public final String growingUp(String idDino, String token) {
		Specie spec;
		Dinosaur denver;
		int currentDimension;
		try {
			User supportUser = userMan.returnUserfromUserList(userMan
					.tokenToUsername(token));
			userMan.returnUserfromLoggedList(token);
			LoggedUser supportPlayUser = userMan
					.returnUserfromPlayingList(token);
			spec = raceMan.fromUsertoRace(supportPlayUser.getUsername());
			denver = spec.checkIdAndReturnDino(idDino);
			currentDimension = denver.getDimension();

			if ((userMan.getCurrentUser().getUsername()).equals(supportUser
					.getUsername()) && roundMan.isRoundConfirmed()) {
				/* Se il tuo utente e' quello abilitato a muoversi */
				if (!denver.isEggOrGrow()) {
					if (denver.getDimension() < 5) {
						if (denver.getCurrentEnergy() > (denver.getMaxEnergy() / 2)) {
							/*
							 * Il dinosauro ha sufficiente energia per crescere
							 */

							/*
							 * Cresci, modifica l'energia massima e aggiorna la
							 * visibilita' dell'utente.
							 */
							denver.setEggOrGrow(true);
							denver.setCurrentEnergy((denver.getCurrentEnergy() - (denver
									.getMaxEnergy() / 2)));
							denver.setDimension(++currentDimension);
							denver.setMaxEnergy(currentDimension);
							supportUser.setUserMap(mapMan.updateVisibility(
									denver.visualDino(denver),
									supportUser.getUserMap(),
									denver.getRowPosition(),
									denver.getColPosition()));
							mapMan.saveMapDB();
							userMan.saveUsers();
							raceMan.saveRaceDB();
							return "@ok";
						} else {
							/* Non c'e' abbastanza energia, muori */
							removeDinosaur(denver, spec,
									denver.getRowPosition(),
									denver.getColPosition(), token);
							return "@no,@mortePerInedia";
						}
					} else {
						return "@no,@raggiuntaDimensioneMax";
					}
				} else {
					return "@no,@raggiuntoLimiteMosseDinosauro";
				}
			} else {
				return "@no,@nonIlTuoTurno";
			}

		} catch (InvalidIdException e) {
			return e.getError();
		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		} catch (RaceNullException e) {
			return "@no,@nonInPartita";
		}
	}

	/**
	 * Il metodo permette al dinosauro di depositare un uovo. Vengono gestiti casi
	 * quali dinosauro gia' mosso, specie gia' completa, dinosauro troppo
	 * debole. Fa morire il dinosauro in caso di energia non sufficiente.
	 * 
	 * @param idDino
	 *            Id del dinosauro
	 * @param token
	 *            Token identificativo dell'utente
	 * @return Ritorna l'esito dell'operazione
	 */
	synchronized public String layAnEgg(String idDino, String token) {
		Specie spec;
		Dinosaur denver;
		final int powerToLay = 1500;
		try {
			userMan.returnUserfromUserList(userMan.tokenToUsername(token));
			LoggedUser user = userMan.returnUserfromPlayingList(token);
			spec = raceMan.fromUsertoRace(user.getUsername());
			denver = spec.checkIdAndReturnDino(idDino);
			if ((userMan.getCurrentUser().getUsername()).equals(user
					.getUsername()) && roundMan.isRoundConfirmed()) {
				if (!denver.isEggOrGrow()) {
					if (spec.getDinos().size() < 5) {
						if (denver.getCurrentEnergy() > powerToLay) {
							/* Se ho abbastanza energia */
							denver.setEggOrGrow(true);
							Dinosaur egg = new Dinosaur(spec.getUsername(),
									spec.getName(), denver.getRowPosition(),
									denver.getColPosition());
							spec.addDenver(egg);
							denver.setCurrentEnergy(denver.getCurrentEnergy()
									- powerToLay);
							raceMan.saveRaceDB();
							return "@ok";
						} else {
							denver.setCurrentEnergy(0);
							removeDinosaur(denver, spec,
									denver.getRowPosition(),
									denver.getColPosition(), token);
							return "@no,@mortePerInedia";
						}
					} else {
						return "@no,@raggiuntoNumeroMaxDinosauri";
					}
				} else {
					return "@no,@raggiuntoLimiteMosseDinosauro";
				}
			} else {
				return "@no,@nonIlTuoTurno";
			}
		} catch (InvalidIdException e) {
			return e.getError();
		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		} catch (NumberOfDinosaursExceededException e) {
			return e.getError();
		} catch (RaceNullException e) {
			return "@no,@nonInPartita";
		}
	}
	/**
	 * Il metodo permette l'accettazione del turno. Lo impedisce nel caso non sia il turno dell'utente identificato dal token
	 * @param token Token identificativo dell'utente
	 * @return Ritorna l'esito dell'operazione
	 */
	public final String acceptRound(String token) {

		User u;
		try {
			userMan.returnUserfromUserList(userMan.tokenToUsername(token));
			u = userMan.returnUserfromPlayingList(token);
			if (!userMan.getCurrentUser().getUsername().equals(u.getUsername())) {
				return "@no,@nonIlTuoTurno";
			} else {
				roundMan.acceptRound();
				return "@ok";
			}
		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		}

	}
	/**
	 * Il metodo permette il passaggio del turno. Lo impedisce nel caso non sia il turno dell'utente identificato dal token
	 * @param token Token identificativo dell'utente
	 * @return Ritorna l'esito dell'operazione
	 */
	public final String passRound(String token) {

		User u;
		try {
			userMan.returnUserfromUserList(userMan.tokenToUsername(token));
			u = userMan.returnUserfromPlayingList(token);
			if (!userMan.getCurrentUser().getUsername().equals(u.getUsername())) {
				return "@no,@nonIlTuoTurno";
			} else {
				userMan.swapUsers();
				roundMan.passRound();
				return "@ok";
			}
		} catch (InvalidTokenException e) {
			return e.getError();
		} catch (UserNotPlayingException e) {
			return e.getError();
		}

	}
	/**
	 * Il metodo permette il cambio del turno. Lo impedisce nel caso non sia il turno dell'utente identificato dal token
	 * @param token Token identificativo dell'utente
	 * @return Ritorna l'esito dell'operazione
	 * @see server.Game#pleaseGrowUpAll()
	 */
	synchronized public final void changeRound() {
		if (userMan.getCurrentUser() != null) {
			server.changeRound(userMan.getCurrentUser().getUsername());
			try {
				pleaseGrowUpAll();
			} catch (RaceNullException e) {
			}
		} else {
			roundMan.stop();
		}
	}
	
	/**
	 * Il metodo fa crescere il gioco. Viene lanciato ad ogni cambio turno. Fa crescere la mappa e invecchia le razze.
	 *Gestisce eventualmente la deposizione di un uovo, l'eliminazione di una razza o di un solo dinosauro
	 * @throws RaceNullException
	 * @see {@link MapManager#growUpMap()}
	 * @see dinosaur.RaceManager#growUpRaces(String)
	 * @see server.Game#removeDinosaur(Dinosaur, Specie, int, int, String)
	 */
	synchronized public final void pleaseGrowUpAll() throws RaceNullException {
		List<String> commands = new ArrayList<String>();
		List<Dinosaur> dinosList = new ArrayList<Dinosaur>();
		String[] support = new String[3];
		Specie spec;

		mapMan.growUpMap();
		commands = raceMan.growUpRaces(userMan.getCurrentUser().getUsername());
		do {
			if (commands.size() != 0) {
				support = raceMan.parseCommandsGrowMethod(commands.remove(0));
				if (support[0].equals("egg")) {
					/* Risali all'uovo e mettilo in mappa */
					spec = raceMan.fromUsertoRace(support[1]);
					if (spec != null) {
						try {
							spec.checkIdAndReturnDino(support[2]).setDimension(
									1);
							spec.checkIdAndReturnDino(support[2]).setAge(1);
							spec.checkIdAndReturnDino(support[2]).setMaxEnergy(
									spec.checkIdAndReturnDino(support[2])
											.getDimension());
							mapMan.putInMap(spec
									.checkIdAndReturnDino(support[2]));
							raceMan.saveRaceDB();
						} catch (InvalidIdException e) {
							/* Eccezione mai catchata */;
						}
					}
				} else if (support[0].equals("removeDino")) {
					/* Risali al dinosauro ed eliminalo */
					spec = raceMan.fromUsertoRace(support[1]);
					if (spec != null) {
						try {
							removeDinosaur(
									spec.checkIdAndReturnDino(support[2]),
									spec, spec.checkIdAndReturnDino(support[2])
											.getRowPosition(), spec
											.checkIdAndReturnDino(support[2])
											.getRowPosition(),
									userMan.usernametoToken(spec.getUsername()));

						} catch (InvalidIdException e) {
							/* Eccezione mai catchata */;
						} catch (InvalidTokenException e) {
							/* Eccezione mai catchata */;
						}
					}
				} else if (support[0].equals("removeRace")) {
					/* Risali alla razza ed estinguila */
					spec = raceMan.fromUsertoRace(support[1]);
					spec.setCondition("n");
					dinosList = spec.getDinos();
					for (Dinosaur currentDino : dinosList) {
						mapMan.removeDinosaurFromMap(currentDino);
					}
					try {
						userMan.getPlayingUsers().remove(
								userMan.returnUserfromPlayingList(userMan
										.usernametoToken(spec.getUsername())));

						LoggedUser supportUser = userMan
								.returnUserfromLoggedList(userMan
										.usernametoToken(spec.getUsername()));
						supportUser.setUserMapToFalse();
						supportUser.setRace(null);
						userMan.returnUserfromUserList(
								supportUser.getUsername()).setRace(null);
					} catch (InvalidTokenException e) {
					} catch (UserNotPlayingException e) {
					}
				}
			}

		} while (commands.size() != 0);
	}
	
	/**
	 * Il metodo slogga il giocatore e lo fa uscire dal match
	 * @param token Token da distruggere 
	 * @return
	 */
	synchronized public String destroyUser(String token) {
		gameOut(token);
		logOut(token);

		return null;
	}

}
