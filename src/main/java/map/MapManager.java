package map;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import dinosaur.Dinosaur;
import dinosaur.RaceManager;
import dinosaur.Specie;
import server.Server;
import server.UserManager;
import exception.InvalidIdException;
import exception.InvalidTokenException;
import exception.RaceNullException;
import exception.UserNotPlayingException;

/**
 * @author  gas12n
 */
public class MapManager {

	/**
	 * @uml.property  name="userMan"
	 * @uml.associationEnd  
	 */
	private UserManager userMan;
	/**
	 * @uml.property  name="global"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Cell[][] globalMap;
	private String[][] supportMap;
	/**
	 * @uml.property  name="localView"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Cell[][] localViewMap;
	/**
	 * @uml.property  name="raceMan"
	 * @uml.associationEnd  
	 */
	private RaceManager raceMan;

	/**
	 * Costrutto della classe Map Manager
	 */
	public MapManager() {
		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = new FileInputStream("src/main/resources/Database/mapDB.out");
			ois = new ObjectInputStream(fis);
			globalMap = (Cell[][]) ois.readObject();
			int i = 0;
			while (i < 30) {
				putNewCarrion();
				i++;
			}
			for (Cell[] element : globalMap) {
				for (int j = 0; j < globalMap.length; j++) {
					if(element[j].getThereIsDinosaur()){
					System.out.println("Found it and removed \n");
					element[j].setThereIsDinosaur(false);
					element[j].setLocalDinosaur(null);
					element[j].setIdDinosaur(null);}
				}
			}
			saveMapDB();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Metodo che permette il salvataggio in database della mappa
	 */
	public void saveMapDB() {
		FileOutputStream fos;
		ObjectOutputStream oos;

		try {
			fos = new FileOutputStream("src/main/resources/Database/mapDB.out");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(globalMap);
			oos.flush();
			oos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void MapManagerGo(UserManager userMan, RaceManager raceMan) {
		this.userMan = userMan;
		this.raceMan = raceMan;

	}

	/**
	 * @return
	 * @uml.property  name="localView"
	 */
	public Cell[][] getLocalViewMap() {
		return localViewMap;
	}

	/**
	 * @return
	 * @uml.property  name="global"
	 */
	public Cell[][] getGlobalMap() {
		return globalMap;
	}

	/**
	 * Il metodo costrituisce la mappa delle raggiungibilita'
	 * @param mappa La mappa globale su cui si sta giocando
	 * @param reachableMap Mappa di interi in cui un 1 rappresenta una casella raggiungibile, uno 0 una non raggiungibile
	 * @param colDestAss Colonna di destinazione
	 * @param rigDestAss Riga di destinatione
	 * @param colPartAss Colonna di partenza
	 * @param rigPartAss Riga di partenza
	 * @param maxDist Distanza massima percorribile dal dinosauro in questione
 	 * @param spec Specie del dinosauro che si muove
	 * @return Ritorna la mappa delle raggiungibilita'
	 */
	public int[][] buildReachableMap(Cell[][] mappa, int[][] reachableMap,
			int colDestAss, int rigDestAss, int colPartAss, int rigPartAss,
			int maxDist, Specie spec) {
		/* metodo costruttore la mappa delle raggiungibilita' */

		/*
		 * adattamento coordinate di destinazione assolute a relative per via
		 * della mappa di raggiungibilita'
		 */

		for (int i = rigPartAss - maxDist, newRow = 0; i <= (rigPartAss + maxDist); newRow++, i++) {
			for (int j = colPartAss - maxDist, newCol = 0; j <= (colPartAss + maxDist); newCol++, j++) {
				if (mappa[i][j].isWater()
						&& !((j == colPartAss) && (i == rigPartAss))) {
					reachableMap[newRow][newCol] = 0;
				} else if (!mappa[i][j].isWater()
						&& !((j == colPartAss) && (i == rigPartAss))) {
					reachableMap[newRow][newCol] = 1;
				} else if ((j == colPartAss) && (i == rigPartAss)) {
					/* e' la posizione di dino; */
					reachableMap[newRow][newCol] = 2;
				}

			}
		}/*
		 * for (int[] element : reachableMap) { for (int h = 0; h <
		 * reachableMap.length; h++) { System.out.println(element[h]); }
		 * 
		 * System.out.println("\n"); }
		 */

		return reachableMap;
	}

	/**
	 * Il metodo e' un costruttore di una coda di supporto all'algoritmo di movimento. I livelli di ampiezza sono separati dal carattere '$'
	 * @param colDest Colonna di destinazione
	 * @param rigDest Riga di destinazione
	 * @param rigPart Riga di partenza
	 * @param colPart Colonna di partenza
	 * @param reachableMap Mappe delle raggiungibilita' 
	 * @param queue Coda in cui vengono salvati le caselle inesplorate
	 * @param garbage ArrayList in cui vengono salvate le caselle gia' controllate
	 */
	public void quequeMaker(int colDest, int rigDest, int rigPart, int colPart,
			int[][] reachableMap, Queue<String> queue, List<String> garbage) {
		/*
		 * metodo che incoda elementi intorno alla coordinate di partenza:
		 * queque maker
		 */
		String support;
		boolean reachableDest = false;

		for (int i = rigPart - 1; (i <= rigPart + 1) && !reachableDest; i++) {
			for (int j = colPart - 1; (j <= colPart + 1) && !reachableDest; j++) {
				if (!((i < 0) || (j < 0))
						&& (!((i >= reachableMap.length) || (j >= reachableMap.length)))) {
					if (!((i == rigPart) && (j == colPart))) {
						/* se non sono nelle coordinate di partenza */
						support = i + "#" + j;
						if ((reachableMap[i][j] == 1)
								&& !garbage.contains(support)) {
							queue.add(support);
							garbage.add(support);
						} else if (!garbage.contains(support)) {
							garbage.add(support);
						}
					}
				}
			}
		}
	}
    /**
     * Il metodo permette di stabilire se il movimento e' valido o meno
     * @param colDestAss Colonna di destinazione
     * @param rigDestAss Riga di destinazione
     * @param colPartAss Colonna di partenza
     * @param rigPartAss Riga di partenza
     * @param spec Specie del dinosauro che si sta muovendo
     * @return Numero di passi necessari per raggiungere la destinazione
     * @see map.MapManager#buildReachableMap(Cell[][], int[][], int, int, int, int, int, Specie)
     * @see map.MapManager#quequeMaker(int, int, int, int, int[][], Queue, List)
     */
	synchronized public int isReachable(int colDestAss, int rigDestAss,
			int colPartAss, int rigPartAss, Specie spec) {
		Queue<String> queue = new LinkedList<String>();
		List<String> garbage = new ArrayList<String>();
		int maxDist;
		int colDestRel;
		int rigDestRel;
		int[][] reachableMap;
		boolean reachableDest = false;
		int[] coordinates = new int[2];
		int i = 0;
		int stepOfNumber = 0;
		int[][] reachMap;

		if (spec.getType().equals("e")) {
			maxDist = 2;
			reachableMap = new int[5][5];
		}

		else {
			maxDist = 3;
			reachableMap = new int[7][7];
		}

		colDestRel = colDestAss - colPartAss + maxDist;
		rigDestRel = rigDestAss - rigPartAss + maxDist;

		reachMap = buildReachableMap(globalMap, reachableMap, colDestAss,
				rigDestAss, colPartAss, rigPartAss, maxDist, spec);
		/* solo la prima iterazione! */
		quequeMaker(colDestRel, rigDestRel, maxDist, maxDist, reachMap, queue,
				garbage);
		if (queue.contains(rigDestRel + "#" + colDestRel)) {
			reachableDest = true;
			return ++stepOfNumber;
		}
		queue.add("$");
		stepOfNumber++;
		String support = queue.poll();
		/* restituisce null in presenza di coda vuota */
		while ((support != null) && !reachableDest && (stepOfNumber <= maxDist)) {

			StringTokenizer st = new StringTokenizer(support, "#");

			// mette in un array di 2 elem prima l'indice di colonna e poi
			// quello di riga
			while (st.hasMoreElements() && (i < 2)) {
				String token = st.nextToken();
				coordinates[i] = Integer.parseInt(token);
				i++;
			}
			i = 0;

			quequeMaker(colDestRel, rigDestRel, coordinates[0], coordinates[1],
					reachMap, queue, garbage);
			if (queue.contains(rigDestRel + "#" + colDestRel)) {
				reachableDest = true;
				return ++stepOfNumber;
			}
			support = queue.poll();
			if (support.equals("$")) {
				queue.add("$");
				support = queue.poll();
				stepOfNumber++;
			}

		}
		return stepOfNumber;
	}

	/**
	 * Il metodo compara la mappa di visibilita' locale all'utente con la
	 * mappa generale e restituisce una mappa al client come da protocollo.
	 * @param token Token identificativo del'utente
	 * @param localPlayerMap Mappa locale all'utente
	 * @return Ritorna le informazione della mappa in base alla visibilita' dell'utente
	 */
	public final String[][] mapCompare(String token, boolean[][] localPlayerMap) {
		/*
		 * localPlayerMap e' una mappa di booleani con campi true solo quelli
		 * che lo specifico utente ha visitato
		 */
		supportMap = new String[localPlayerMap.length][localPlayerMap.length];
		for (int i = 0; i < localPlayerMap.length; i++) {
			for (int j = 0; j < localPlayerMap.length; j++) {
				if (!localPlayerMap[i][j]) {
					supportMap[i][j] = "b";
				} else {
					if (globalMap[i][j].isCarrion()) {
						supportMap[i][j] = "t";
					} else if (globalMap[i][j].isEarth()) {
						supportMap[i][j] = "t";
					} else if (globalMap[i][j].isVegetation()) {
						supportMap[i][j] = "v";
					} else if (globalMap[i][j].isWater()) {
						supportMap[i][j] = "a";
					}
				}
			}
		}

		return supportMap;
	}

	/**
	 * 
		 * Il metodo prende in ingresso il token e un id di un dinosauro
		 * Verifica la validita' del token e dell'id (scorpora l'id del dino in
		 * modo opportuno, ndr.vedi sotto) e dopo aver preso la razza
		 * appartentente all'utente (con il token passato al metodo) accede al
		 * dinosauro avente l'Id passato al metodo. Infine cicla intorno ad
		 * esso.
		 
	 * @param token Token identificativo dell'utente
	 * @param idDino Id del dinosauro
	 * @param username Username dell'utente
	 * @return Ritorna una mappa desiderata della vista locale del dinosauro
	 * @throws InvalidTokenException
	 * @throws InvalidIdException
	 * @throws UserNotPlayingException
	 * @throws RaceNullException
	 */
	public final Cell[][] showLocalView(String token, String idDino,
			String username) throws InvalidTokenException, InvalidIdException,
			UserNotPlayingException, RaceNullException {

		int temp = 0;
		String[] agent = new String[3];
		Dinosaur denver;
		int offSet;
		Specie race;

		StringTokenizer st = new StringTokenizer(idDino, "0");
		agent[temp] = st.nextToken();
		temp++;
		while (st.hasMoreElements()) {
			agent[temp] = st.nextToken();
			temp++;
		}
		/*
		 * In 0 ho l'username proprietario del dinosauro, in 1 il nome della
		 * razza, in 2 un numero identificativo e univoco del dinosauro
		 */
		userMan.returnUserfromUserList(username);
		userMan.returnUserfromPlayingList(token);
		race = raceMan.fromUsertoRace(username);
		denver = race.checkIdAndReturnDino(idDino);
		if (denver.getDimension() != 0) {
			offSet = denver.visualDino(denver);
			localViewMap = new Cell[(offSet * 2) + 1][(offSet * 2) + 1];

			for (int i = denver.getRowPosition() - offSet, row = 0; i <= denver
					.getRowPosition() + offSet; i++, row++) {
				for (int j = denver.getColPosition() - offSet, col = 0; j <= denver
						.getColPosition() + offSet; j++, col++) {
					localViewMap[row][col] = globalMap[i][j];
					if (globalMap[i][j].getThereIsDinosaur()) {
						localViewMap[row][col].setIdDinosaur(globalMap[i][j]
								.getLocalDinosaur().getIdDino());
					}

				}
			}
		} else {
			throw new InvalidIdException();
		}

		return localViewMap;
	}
	
	/**
	 * Genera casualmente le coordinate dove posizionare il dinosauro
	 * @return Un array di int rappresentanti le coordinate
	 */
	public final int[] randomPositioner() {
		/* genero le coordinate dove posizionare un dinosauro */

		/*
		 * i valori da generare saranno compresi tra 0 e la massima lunghezza
		 * della mappa -1 poiche' le coordinate partono da 0
		 */
		int min = 0;
		int max = globalMap.length - 1;

		/*
		 * un'array conterra' di lunghezza 2 conterra' le coordinate x ed y dove
		 * verra' posizionato il dinosauro
		 */
		int[] coordXY = new int[2];

		/*
		 * utilizzo un flag poiche' devo controllare che la posizione random
		 * generata non mi dia un posto dove c'e' o un altro dinosauro o acqua
		 */
		boolean flag = false;

		while (flag == false) {
			coordXY[0] = (min + (int) (Math.random() * ((max - min) + 1))); // conterra'
			// l'indice
			// di
			// riga
			coordXY[1] = (min + (int) (Math.random() * ((max - min) + 1))); // conterra'
			// l'indice
			// di
			// colonna
			if ((globalMap[coordXY[0]][coordXY[1]].getThereIsDinosaur() == false)
					&& (globalMap[coordXY[0]][coordXY[1]].isWater() == false)) {
				flag = true;
			}
		}

		return coordXY;
	}

	/**
	 * Aggiorna la mappa di visibilita' dell'utente
	 * @param offSet Raggio di massima visibilita'
	 * @param localPlayerMap Mappa di visibilita' dell'utente
	 * @param row Riga in cui si trova il dinosauro
	 * @param col Colonna in cui si trova il dinosauro
	 * @return La mappa di visibilita' aggiornata
	 */
	public final boolean[][] updateVisibility(int offSet,
			boolean[][] localPlayerMap, int row, int col) {
		/*
		 * L'offset sarebbe il raggio della massima visibilita' vista dal
		 * dinosauro. Un dinosauro di dimensione 1 ha offset 2. Un dinosauro di
		 * dimensione 2 o 3 ha un offset di 3. Infine uno di dimensione 4-5 ha
		 * un offset di 4
		 */
		for (int i = row - offSet; i <= row + offSet; i++) {
			for (int j = col - offSet; j <= col + offSet; j++) {
				localPlayerMap[i][j] = true;
			}
		}
		return localPlayerMap;
	}

	/**
	 * Sistema una nuova carogna in una cella che sia di terra
	 * @return Le coordinate dove e' stata posizionata la carogna
	 */
	synchronized public final int[] putNewCarrion() {
		int coord[] = randomPositioner();
		while (globalMap[coord[0]][coord[1]].isVegetation()
				|| globalMap[coord[0]][coord[1]].isWater()
				|| globalMap[coord[0]][coord[1]].getThereIsDinosaur() || 
				globalMap[coord[0]][coord[1]].isCarrion()) {
			/*
			 * RandomPositioner evita che le coordinate siano di acqua o siano
			 * la posizione di un dinosauro. Devo inoltre controllare che non
			 * sia di vegetazione
			 */
			coord = randomPositioner();
		}
		/*
		 * Metto una carogna nelle coordinate corrette e setto in modo pseudo-
		 * random l'energia ad essa allegata
		 */
		globalMap[coord[0]][coord[1]].setCarrion(true);
		globalMap[coord[0]][coord[1]].setCurrentEnergyCarrion(Rand
				.generateEnergyCarrionRandom());
		globalMap[coord[0]][coord[1]]
				.setEnergyCarrion(globalMap[coord[0]][coord[1]]
						.getCurrentEnergyCarrion());

		saveMapDB();

		return coord;
	}
    /**
     * Inserisce in mappa il dinosauro
     * @param currentDino Il dinosauro da inserire in mappa
     */
	synchronized public final void putInMap(Dinosaur currentDino) {
		int offset = 1;
		boolean found = false;

		if (!globalMap[currentDino.getRowPosition()][currentDino
				.getColPosition()].getThereIsDinosaur()) {
			/* Se la posizione in cui c'era il dino e' attualmente vuota */
			globalMap[currentDino.getRowPosition()][currentDino
					.getColPosition()].setLocalDinosaur(currentDino);
			globalMap[currentDino.getRowPosition()][currentDino
					.getColPosition()].setThereIsDinosaur(true);
			globalMap[currentDino.getRowPosition()][currentDino.getColPosition()]
				                   				.setIdDinosaur(currentDino.getIdDino());
			currentDino.getRowPosition();
			currentDino.getColPosition();
		} else {
			/* Posizione originaria del dino gia' occupata */
			do {
				for (int i = currentDino.getRowPosition() - offset; !found
						&& (i <= currentDino.getRowPosition() + offset); i++) {
					for (int j = currentDino.getColPosition() - offset; !found
							&& (j <= currentDino.getColPosition() + offset); j++) {
						if (!((i < 0) || (j < 0))
								&& (!((i >= globalMap.length) || (j >= globalMap.length)))) {
							/* Evito bordi */
							if (!globalMap[i][j].getThereIsDinosaur()
									&& !globalMap[i][j].isWater()) {
								/* Trovata cella libera */
								currentDino.setyPosition(i);
								currentDino.setxPosition(j);
								found = true;
								globalMap[i][j].setLocalDinosaur(currentDino);
								globalMap[i][j].setThereIsDinosaur(true);
								globalMap[currentDino.getRowPosition()][currentDino.getColPosition()]
										                   				.setIdDinosaur(currentDino.getIdDino());
							}
						}
					}
				}
				offset++;
			} while (!found);
		}
		saveMapDB();
	}

	/**
	 * Elimina il dinosauro dalla mappa
	 * @param denver Il dinosauro da eliminare
	 */
	public final void removeDinosaurFromMap(Dinosaur denver) {
		globalMap[denver.getRowPosition()][denver.getColPosition()]
				.setLocalDinosaur(null);
		globalMap[denver.getRowPosition()][denver.getColPosition()]
				.setThereIsDinosaur(false);
		globalMap[denver.getRowPosition()][denver.getColPosition()]
		                   				.setIdDinosaur(null);
		saveMapDB();
	}
/**
 * Invecchia la mappa, la vegetazione e le carogne
 */
	synchronized public final void growUpMap() {
		for (int i = 0; i < globalMap.length; i++) {
			for (int j = 0; j < globalMap.length; j++) {
				if (globalMap[i][j].isVegetation()) {
					if (globalMap[i][j].getCurrentEnergyVegetation()
							+ (globalMap[i][j].getEnergyVegetation() / 20) > globalMap[i][j]
							.getEnergyVegetation()) {
						/*
						 * Se l'energia dovesse crescere oltre il limite
						 * consentito viene messa al suo massimo
						 */
						globalMap[i][j]
								.setCurrentEnergyVegetation(globalMap[i][j]
										.getEnergyVegetation());
					} else {
						/* L'energia crescera' ancora... */
						globalMap[i][j]
								.setCurrentEnergyVegetation(globalMap[i][j]
										.getCurrentEnergyVegetation()
										+ (globalMap[i][j]
												.getEnergyVegetation() / 20));
					}
				}

				else if (globalMap[i][j].isCarrion()) {
					/*Si tratta di carogna*/
					if (globalMap[i][j].getCurrentEnergyCarrion()
							- (globalMap[i][j].getEnergyCarrion() / 30) > 0) {
						globalMap[i][j].setCurrentEnergyCarrion(globalMap[i][j]
								.getCurrentEnergyCarrion()
								- (globalMap[i][j].getEnergyCarrion() / 30));
					} else {
						/* Se la carogna si e' esaurita rimettine una nuova */
						globalMap[i][j].setCarrion(false);
						globalMap[i][j].setCurrentEnergyCarrion(0);
						globalMap[i][j].setEnergyCarrion(0);
						globalMap[i][j].setEarth(true);
						putNewCarrion();
					}
				}
			}

		}
		saveMapDB();
	}
}