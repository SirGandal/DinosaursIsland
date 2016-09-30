package dinosaur;

import java.io.Serializable;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import exception.InvalidIdException;
import exception.NumberOfDinosaursExceededException;

/**
 * @author  gas12n
 */
public class Specie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4077130974790686372L;
	private int maxNumDinForSpecie = 5;
	private ArrayList<Dinosaur> dinos = new ArrayList<Dinosaur>();
	private int livedRound;
	private String name; /* nome della specie */
	private String username; /* nome del proprietario della specie */
	private int score;
	private String condition; /* estinta o ancora in vita */
	private String type;

	/**
	 * Costruttore minore della classe specie
	 * @param name Nome della razza
	 * @param username L'username dell'utente che possiedera' la specie
	 * @param score Punteggio della specie
	 * @param condition Esprime l'esistenza della razza o meno
	 */
	public Specie(String name, String username, int score, String condition) {
		this.name = name;
		this.username = username;
		this.score = score;
		this.condition = condition;
	}

	/**
	 * Costruttore completo della classe Specie 
	 * @param maxNumDinForSpecie Numero massimo di dinosauri per la specie
	 * @param livedRound Turni vissuti
	 * 	 * @param score Punteggio della specie
	 * @param username L'username dell'utente che possiedera' la specie
	 * @param name Nome della razza
	 * @param condition Esprime l'esistenza della razza o meno
	 * @param type Tipo della specie (erbivoro o carnivoro)
	 */
	public Specie(int maxNumDinForSpecie, int livedRound, int score, String username, String name,
			String condition, String type) {
		this.maxNumDinForSpecie = maxNumDinForSpecie;
		this.livedRound = livedRound;
		this.name = name;
		this.username = username;
		this.score = score;
		this.condition = condition;
		this.type = type;
	}

	/**
	 * @return
	 * @uml.property  name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return
	 * @uml.property  name="dinos"
	 */
	public List<Dinosaur> getDinos() {
		return dinos;
	}

	/**
	 * @return
	 * @uml.property  name="livedRound"
	 */
	public int getLivedRound() {
		return livedRound;
	}

	/**
	 * @param livedRound
	 * @uml.property  name="livedRound"
	 */
	public void setLivedRound(int livedRound) {
		this.livedRound = livedRound;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="score"
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 * @uml.property  name="score"
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return
	 * @uml.property  name="type"
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 * @uml.property  name="type"
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 * @uml.property  name="condition"
	 */
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * Controlla la validita' dell'id e ritorna il Dinosauro avente tale id
	 * @param idDino L'id da controllare
	 * @return Il dinosauro con l'id passato
	 * @throws InvalidIdException
	 */
	public Dinosaur checkIdAndReturnDino(String idDino)
			throws InvalidIdException {
		/*
		 * Il metodo scorre nell'array dei dinosauri e restituisce il dinosauro
		 * con l'id passato. Se non viene trovato lancia InvalidIdException
		 */
		for (Dinosaur dino : dinos) {
			if (dino.getIdDino().equals(idDino)) {
				return dino;
			}
		}

		throw new InvalidIdException();
	}
    /**
     * Controlla se esiste l'id fornito al metodo e ritorna un booleano di ovvia interpretazione
     * @param idDino Id da controllare
     * @return True se esiste un dinosauro con tale id
     */
	public boolean checkIdAndReturnBoolean(String idDino) {
		/*
		 * Il metodo scorre nell'array dei dinosauri e restituisce true se viene
		 * trovato un dinosauro con l'id passatogli in ingresso. Falso
		 * altrimenti
		 */
		for (Dinosaur dino : dinos) {
			if (dino.getIdDino().equals(idDino)) {
				return true;
			}
		}
		return false;
	}
/**
 * Il metodo ritorna la lista dei dinosauri per l'utente indicato
 * @param token Il token identificativo dell'utente
 * @return Ritorna la lista dei dinosauri
 */
	public List<String> getDenverList(String token) {
		/*
		 * Restituisce una lista (arraylist) contenente gli id dei dinosauri
		 * della Specie desiderata
		 */
		List<String> dinosList = new ArrayList<String>();
		if (dinos.size() != 0) {
			for (Dinosaur denver : dinos) {
				if (denver.getDimension() != 0) {
					dinosList.add(denver.getIdDino());
				}
			}
		}
		return dinosList;
	}

	/**
	 *  Il metodo controlla che non venga superato il numero massimo di
		 * dinosauri (in tal caso viene lanciato
		 * NumberOfDinosaursExceededException) e in caso positivo aggiunge un
		 * dinosauro alla lista dei dinosauri
	 * @param denver Il dinosauro da aggiungere
	 * @throws NumberOfDinosaursExceededException
	 */
	public void addDenver(Dinosaur denver)
			throws NumberOfDinosaursExceededException {
	

		if (dinos.size() >= 5) {
			throw new NumberOfDinosaursExceededException();
		} else {
			dinos.add(denver);
		}
	}

	/**
	 *  Il metodo elimina il dinosauro dall'arrayList dei dinosauri
		 * dell'utente
	 * @param denver Il dinosauro da rimuovere
	 * @return Ritorna la dimensione della lista dei dinosauri successiva all'eliminazione del dinosauro
	 */
	public int removeDenver(Dinosaur denver) {
		

		dinos.remove(denver);
		if (dinos.size() == 0) {
			condition = "n";
		}
		return dinos.size();
	}
/**
 * Il metodo restituisce un booleano in base al numero dei dinosauri
		 * della specie che si vuole fare aumentare di numero. Se si raggiunge
		 * il tetto massimo di dinosauri per specie il numero di dinosauri per
		 * specie non puo' aumentare e viene restituito false.
 * @return Ritorna false se non puo' essere deposto un uovo, altrimenti ritorna true
 */
	public boolean canEgg() {

		if (dinos.size() < maxNumDinForSpecie) {
			return true;
		}
		return false;

	}
/**
 * Ritorna il numero massimo di passi effettuabili da un dinosauro dalla specie
 * @param type Il tipo della specie (erbivoro o carnivoro)
 * @return
 */
	public final int maxStep(String type) {

		if (type.equals("c")) {
			return 3;
		} else {
			return 2;
		}
	}
}
