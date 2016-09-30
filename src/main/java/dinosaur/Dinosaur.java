package dinosaur;

import java.io.Serializable;

import exception.NotEnoughEnergy;

/**
 * @author  gas12n
 */
public class Dinosaur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7012316901898243777L;
	private int initDimension = 1;
	private int initEnergy = 750;
	private int dimension;
	private int age;
	private int currentEnergy;
	private int maxEnergy = 1000;
	private String idDino;
	private static int numberCurrent = 0;
	private int maxAge;
	private int colPosition;
	private int rowPosition;
	private boolean alreadyMoved;
	private boolean eggOrGrow;

	/**
	 * Costruttore predefinito dei dinosauri 
	 * @param row Riga in cui verra' posizionato
	 * @param col Colonna in cui verra' posizionato
	 * @param spec Specie del dinosauro
	 */
	public Dinosaur(int row, int col, Specie spec) {
		super();
		this.dimension = initDimension;
		this.maxEnergy = this.dimension * 1000;
		this.currentEnergy = initEnergy;
		this.maxAge = GenerateLifeDinosaurRandom.randLife();
		setIdDino(spec.getUsername() + "0" + spec.getName() + ++numberCurrent);
		this.rowPosition = row;
		this.colPosition = col;
		setMaxEnergy(initDimension);
		System.out.println("Il dinosauro e' stato messo nella riga "
				+ rowPosition + " e colonna " + colPosition);
		alreadyMoved = false;
		eggOrGrow = false;

	}

	/**
	 * Costruttore delle uova del dinosauro
	 * @param username Nome del proprietario della razza a cui il dinosauro apparterra'
	 * @param race Nome della razza 
	 * @param row Riga in cui e' stato deposto
	 * @param col Colonna in cui e' stato deposto
	 */
	public Dinosaur(String username, String race, int row, int col) {
		/* Costruttore delle uova */
		super();
		this.dimension = 0;
		this.maxEnergy = this.dimension * 1000;
		this.currentEnergy = initEnergy;
		this.maxAge = GenerateLifeDinosaurRandom.randLife();
		setIdDino(username + "0" + race + ++numberCurrent);
		this.rowPosition = row;
		this.colPosition = col;
		alreadyMoved = true;
		eggOrGrow = false;
	}

	/**
	 * @return
	 * @uml.property  name="alreadyMoved"
	 */
	public final boolean isAlreadyMoved() {
		return alreadyMoved;
	}

	/**
	 * @param alreadyMoved
	 * @uml.property  name="alreadyMoved"
	 */
	public final void setAlreadyMoved(boolean alreadyMoved) {
		this.alreadyMoved = alreadyMoved;
	}

	/**
	 * @return
	 * @uml.property  name="eggOrGrow"
	 */
	public boolean isEggOrGrow() {
		return eggOrGrow;
	}

	/**
	 * @param eggOrGrow
	 * @uml.property  name="eggOrGrow"
	 */
	public void setEggOrGrow(boolean eggOrGrow) {
		this.eggOrGrow = eggOrGrow;
	}

	/**
	 * @return
	 * @uml.property  name="idDino"
	 */
	public final String getIdDino() {
		return idDino;
	}

	/**
	 * @param idDino
	 * @uml.property  name="idDino"
	 */
	public final void setIdDino(String idDino) {
		this.idDino = idDino;
	}

	/**
	 * @return
	 * @uml.property  name="colPosition"
	 */
	public final int getColPosition() {
		return colPosition;
	}

	public final void setxPosition(int colPosition) {
		this.colPosition = colPosition;
	}

	/**
	 * @return
	 * @uml.property  name="rowPosition"
	 */
	public final int getRowPosition() {
		return rowPosition;
	}

	public final void setyPosition(int rowPosition) {
		this.rowPosition = rowPosition;
	}

	/*
	 * inner class poiche' uso il generatore random di vita per il dinosauro
	 * solo nella classe dinosauro
	 */
	private static class GenerateLifeDinosaurRandom {

		public static int randLife() {
			/* genero la vita per il dinosauro in valore compreso tra 24 e 36 */
			int min = 24;
			int max = 36;

			return (min + (int) (Math.random() * ((max - min) + 1)));
		}
	}

	/**
	 * @return
	 * @uml.property  name="dimension"
	 */
	public final int getDimension() {
		return dimension;
	}

	/**
	 * @param dimension
	 * @uml.property  name="dimension"
	 */
	public final void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public final void incrementDimension() {
		this.dimension++;
	}

	/**
	 * @return
	 * @uml.property  name="maxAge"
	 */
	public int getMaxAge() {
		return maxAge;
	}

	/**
	 * @return
	 * @uml.property  name="age"
	 */
	public final int getAge() {
		return age;
	}

	/**
	 * @param age
	 * @uml.property  name="age"
	 */
	public final void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return
	 * @uml.property  name="maxEnergy"
	 */
	public final int getMaxEnergy() {
		return maxEnergy;
	}

	/**
	 * @param dimension
	 * @uml.property  name="maxEnergy"
	 */
	public final void setMaxEnergy(int dimension) {
		this.maxEnergy = 1000 * dimension;
	}

	/**
	 * @return
	 * @uml.property  name="currentEnergy"
	 */
	public final int getCurrentEnergy() {
		return currentEnergy;
	}

	/**
	 * @param currentEnergy
	 * @uml.property  name="currentEnergy"
	 */
	public final void setCurrentEnergy(int currentEnergy) {
		this.currentEnergy = currentEnergy;
	}

	/**
	 * Il metodo permette la crescita del dinosauro
	 * @param dino Il dinosauro che e' soggetto della crescita
	 * @throws NotEnoughEnergy
	 * @see NotEnoughEnergy;
	 */
	public final void growDinosaur(Dinosaur dino) throws NotEnoughEnergy {
		if (dino.getCurrentEnergy() < ((1000 * dino.getDimension()) / 2)) {
			throw new NotEnoughEnergy();
		} else {
			dino.incrementDimension();
		}
	}
/**
 * Il metodo ritorna la visibilita' massima del dinosauro in base alla sua dimensione
 * @param denver Il dinosauro di cui si vuol sapere la sua visibilita'
 * @return L'intero rappresentante la visibilita' del dinosauro
 */
	public final int visualDino(Dinosaur denver) {

		int view = 0;

		switch (denver.getDimension()) {

		/*
		 * visibilita' fino a 2 caselle di distanza l'array di visibilita' avra'
		 * dimensione 5x5
		 */
		case 1: {
			view = 2;
			break;
		}

			/*
			 * visibilita' fino a 3 caselle di distanza l'array di visibilita'
			 * avra' dimensione 7x7
			 */
		case 2:
		case 3: {
			view = 3;
			break;
		}

			/*
			 * visibilita' fino a 4 caselle di distanza l'array di visibilita'
			 * avra' dimensione 9x9
			 */

		case 4:
		case 5: {
			view = 4;
			break;
		}
		default:
			break;
		}
		return view;
	}

}
