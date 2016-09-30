package dinosaur;

/*TODO fare i controlli sul token nel manager utenti*/

import java.io.*;
import java.util.*;
import server.*;
import exception.*;

/**
 * @author  gas12n
 */
public class RaceManager {
	/*
	 * L'hashMap raceDB prende come chiave l'username possessore della razza,
	 * che rappresenta quindi il valore dell'hashmap
	 */
	private HashMap<String, Specie> raceDB;
	/**
	 * @uml.property  name="userMan"
	 * @uml.associationEnd  
	 */
	private UserManager userMan;
	private final static int maxNumberOfDino = 5;
	private final static int maxNumberOfTurn = 120;

	
	@SuppressWarnings("unchecked")
	/**
	 * Costruttore del RaceManager
	 */
	public RaceManager() {
		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = new FileInputStream("src/main/resources/Database/raceDB.out");
			ois = new ObjectInputStream(fis);
			raceDB = (HashMap<String, Specie>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}

	}

	public void raceManagerGo(UserManager userMan) {
		/*
		 * Il metodo inizializza i manager degli utenti e della mappa
		 */
		this.userMan = userMan;
	}
/**
 * Il metodo permette il salvataggio del database delle razze
 */
	public void saveRaceDB() {
		FileOutputStream fos;
		ObjectOutputStream oos;

		try {
			fos = new FileOutputStream("src/main/resources/Database/raceDB.out");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(raceDB);
			oos.flush();
			oos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * @uml.property  name="raceDB"
	 */
	public final Map<String, Specie> getRaceDB() {
		return raceDB;
	}
	/**
	 * 
		 * Il metodo prende in ingresso un token, un nome della razza e il tipo
		 * di quest'ultima. Lancia una eccezione nel caso il nome sia gia'
		 * occupato, nel caso esista gia' una razza per l'utente individuato dal
		 * token o nel caso il token stesso sia invalido. Nel caso positivo
		 * aggiorna l'utente fornendogli il nome della razza corrente e aggiorna
		 * quindi il database delle razze ponendo a 5 il numero massimo di
		 * dinosauri, a zero il punteggio e a 120 i turni rimasti prima
		 * dell'estinzione
		 
	 * @param token Token identificativo dell'utente
	 * @param name Nome della razza
	 * @param type Tipo della razza (carnivoro o erbivoro)
	 * @param row Riga dove verra' posizionato il primo dinosauro
	 * @param col Colonna dove verra' posizionato il primo dinosauro
	 * @return Il dinosauro appena creato
	 * @throws InvalidTokenException
	 * @throws RaceAlreadyExistsException
	 * @throws RaceNameTakenException
	 */
	synchronized public final Dinosaur setRace (String token, String name, String type,
			int row, int col) throws InvalidTokenException,
			RaceAlreadyExistsException, RaceNameTakenException {
		
		String supportUsername;
		Dinosaur denver;
		User u;

		/*
		 * Il metodo che passatogli il token restituisce l'username. Cattura
		 * l'eventuale eccezione InvalidTokenException
		 */
		supportUsername = userMan.tokenToUsername(token);

		/*
		 * Il metodo controlla se esiste fra gli utenti loggati una razza di
		 * loro appartenenza che abbia lo stesso nome di quella che si sta per
		 * creare Lancia eventualmente RaceNameTakenException
		 */
		userMan.checkNameRace(name);

		/*
		 * Il metodo controlla che non esista gia' per l'username identificato
		 * dal token una razza Lancia l'eventuale eccezione
		 * RaceAlreadyExistsException
		 */
		userMan.checkRaceToken(token);

		/* Questo ramo try setta la razza */
		/* Controllo della validita' del token */
		LoggedUser logUser = userMan.returnUserfromLoggedList(token);
		/* Set razza sull'utente loggato */
		logUser.setRace(name);

		/*
		 * Qui viene selezionato l'utente negli array degli utenti generici e
		 * settate razza e tipo anche a lui
		 */
		u = userMan.returnUserfromUserList(logUser.getUsername());
		u.setRace(name);

		/*
		 * Viene effettivamente creata la nuova razza e inserita nel database
		 * delle razze
		 */
		Specie newRace = new Specie(maxNumberOfDino, 0, 0, supportUsername,
				name, "s", type);
		raceDB.put(logUser.getUsername(), newRace);
		saveRaceDB();

		/*
		 * Crea un dinosauro invocando il costruttore(adattato al primo
		 * inserimento) e lo mette nella posizione x e y passatogli da un metodo
		 * esterno
		 */
		denver = new Dinosaur(row, col, newRace);
		denver.setAge(1);
		/*
		 * Aggiunge il dinosauro all'arrayList dei dino nella Specie e setta il
		 * numero di dinosauri correnti
		 */
		try {
			newRace.addDenver(denver);
		} catch (NumberOfDinosaursExceededException e) {
			/*
			 * Sicuramente non viene lanciata questa eccezione dal momento che
			 * la razza e' stata appena creata
			 */;
		}
		saveRaceDB();
		return denver;
	}
/**
 * Il metodo prende in ingresso un token. Ritorna l'hashmap (che
		 * rappresenta il database delle razze).
 * @return Il database delle razze
 */
	public final Map<String, Specie> ranking() {
		/*
		 * Il metodo prende in ingresso un token. Ritorna l'hashmap (che
		 * rappresenta il database delle razze).
		 */

		return raceDB;
	}

	/**
	 * 
		 * Il metodo riceve in ingresso una stringa rappresentante l'username.
		 * Restituisce la razza appartenente a quell'utente. Lancia in caso di
		 * token errato InvalidTokenException
		 *
	 * @param username L'username di cui vogliamo sapere la razza
	 * @return La razza associata all'username
	 * @throws RaceNullException
	 */
	public final Specie fromUsertoRace(String username)
			throws RaceNullException {
		
		Specie supportRace = null;
		if (raceDB != null) {
			supportRace = raceDB.get(username);
			if (supportRace == null) {
				throw new RaceNullException();
			} else {
				return supportRace;
			}
		}

		else {
			throw new RaceNullException();
		}

	}

	/**
	 * 
		 * Il metodo controlla se c'e' abbastanza energia per muovere il passo.
		 * Se cosi' non fosse elimina il dinosauro dagli array della razza e
		 * ritorna true. Se l'array risulta essere di dimensione zero dopo
		 * l'eliminazione del dinosauro lancia l'eccezione
		 * ZeroDinosaurException. Nel caso il dinosauro abbia energia a
		 * sufficienza diminuisce la energia del dinosauro e ritorna false
		 *
	 * @param denver Il dinosauro da esaminare
	 * @param step Il numero di passi da effettuare
	 * @param spec La specie del dinosauro
	 * @return Ritorna un boolean, true se il dinosauro e' troppo debole per fare il movimento
	 */
	public final boolean istooWeakDenver(Dinosaur denver, int step, Specie spec) {
		

		int currentEnergy = denver.getCurrentEnergy();
		int necessaryEnergy = 10 * (int) Math.pow(2, step);

		return currentEnergy <= necessaryEnergy;
	}
	/**
	 * 
		 * Prende in ingresso il dinosauro incontrato nella mappa.Restituisce la
		 * sua specie.
		 
	 * @param idDino L'id del dinosauro selezionato
	 * @return La specie a cui il dinosauro con tale id appartiene
	 * @throws RaceNullException
	 */
	public final Specie fromIdtoSpec(String idDino) throws RaceNullException {

		

		String tempId = idDino;
		StringTokenizer st = new StringTokenizer(tempId, "0123456789");
		String username = st.nextToken();
		return fromUsertoRace(username);
	}
/**
 * Il metodo invecchia le razze, schiude le uova, invecchia i dinosauri, estingue una razza o rimuove un dinosauro
 * @param playUsername
 * @return Una serie di comandi che devono esser svolti nel game
 * @throws RaceNullException
 */
	synchronized public final List<String> growUpRaces(String playUsername)
			throws RaceNullException {
		Specie spec;
		List<Dinosaur> dinos;
		List<String> commands = new ArrayList<String>();
		int score;

		spec = fromUsertoRace(playUsername);
		score = spec.getScore();
		spec.setLivedRound(spec.getLivedRound() + 1);
		if (spec.getLivedRound() < maxNumberOfTurn) {
			/* Se la specie puo' crescere ancora */
			dinos = spec.getDinos();
			for (Dinosaur currentDino : dinos) {
				/* Per tutti i dinosauri della specie */
				if (currentDino.getDimension() == 0) {
					/* Trattasi di un uovo, bisogna sistemarlo nella mappa */
					currentDino.setMaxEnergy(currentDino.getDimension());
					commands.add("egg" + "$" + spec.getUsername() + "$"
							+ currentDino.getIdDino());
				} else if (currentDino.getAge() < currentDino.getMaxAge()) {
					/* Se il dinosauro puo' ancora crescere */
					currentDino.setAge(currentDino.getAge() + 1);
				}

				else {
					/* Il dinosauro DEVE morire per sopraggiunti limiti d'eta' */
					commands.add("removeDino" + "$" + spec.getUsername() + "$"
							+ currentDino.getIdDino());
				}

				score += currentDino.getDimension() + 1;
				currentDino.setAlreadyMoved(false);
				currentDino.setEggOrGrow(false);
			}
			spec.setScore(score);
		} else {
			/* Questa razza e' troppo vecchia per vivere ancora... */
			commands.add("removeRace" + "$" + spec.getUsername());
		}

		saveRaceDB();
		return commands;
	}

	/**
	 * Parsa i comandi di crescita
	 * @param command Stringa da parsare
	 * @return Ritorna un'array di stringhe gia' parsate
	 */
	public final String[] parseCommandsGrowMethod(String command) {
		String[] strings = new String[3];
		int i = 0;
		StringTokenizer st = new StringTokenizer(command, "$");
		strings[i] = st.nextToken();
		i++;
		while (st.hasMoreTokens()) {
			strings[i] = st.nextToken();
			i++;
		}
		return strings;
	}
}