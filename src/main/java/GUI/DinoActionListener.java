package GUI;

import java.awt.event.*;

import javax.swing.JFrame;

import client.ClientComunicator;
import exception.InvalidIdException;

/**
 * Action listener che viene dichiarato su ogni dinosauro sulla mappa generale. Quando verra' chiamato in causa chiamera' il metodo per richiedere la vista locale di quel determianto dinosauto che e' stato cliccato.
 */
public class DinoActionListener implements ActionListener {

	private int absoluteRow;
	private int absoluteCol;
	private String type;
	private String[] localDinosaurList;
	private int[][] localCoordOfDinosaur;
	/**
	 * @uml.property  name="localcc"
	 * @uml.associationEnd  
	 */
	private ClientComunicator localcc;
	private String localToken;
	/**
	 * @uml.property  name="localW"
	 * @uml.associationEnd  
	 */
	private WindowGame localW;
	private JFrame frame;

	/**
	 * Costruttore che prende in ingresso i parametri necessari per chiamare la
	 * vista locale di un determinato dinosauro.
	 * 
	 * @param absoluteRow
	 *            Indice di riga del dinosauro all'interno della mappa
	 *            principale.
	 * @param absoluteCol
	 *            Indice di colonna del dinosauro all'interno della mappa
	 *            principale.
	 * @param type
	 *            Tipologia del dinosauro: 'e' erbivoro, 'c' carnivoro.
	 * @param dinosaurList
	 *            Lista dei dinosauri in gioco per un determinato utente.
	 * @param coordOfDinosaur
	 *            Array di coordinate di tutti i dinosauri di un determinato
	 *            utente.
	 * @param cc
	 *            Comunicatore client per chiamare i metodi relativi alla vista
	 *            locale.
	 * @param token
	 *            Token ricevuto al momento del login.
	 * @param w
	 *            L'oggetto stesso della finestra di gioco per poter aggiornare
	 *            direttamente le viste locali gia' create in precedenza.
	 * 
	 * @see client.ClientSocketComunicator#askForlocalView(String, String)
	 * @see client.ClientSocketComunicator#askForMap(String)
	 */
	public DinoActionListener(int absoluteRow, int absoluteCol, String type,
			String[] dinosaurList, int[][] coordOfDinosaur,
			ClientComunicator cc, String token, WindowGame w) {
		this.absoluteRow = absoluteRow;
		this.absoluteCol = absoluteCol;
		this.type = type;
		this.localDinosaurList = dinosaurList.clone();
		this.localCoordOfDinosaur = coordOfDinosaur.clone();
		this.localcc = cc;
		this.localToken = token;
		this.localW = w;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		localW.getImageDin().setVisible(true);
		localW.getActionsPanel().setVisible(true);
		localW.getDinosaurMapPanel().setVisible(true);
		localW.getLocalDinosaurEnergyLabel().setVisible(true);
		localW.getLocalDinosaurLevelLabel().setVisible(true);
		localW.getLocalDinosaurLivedRoundsLabel().setVisible(true);
		for (int i = 0; i < localCoordOfDinosaur.length; i++) {
			if ((localCoordOfDinosaur[i][0] == absoluteRow)
					&& (localCoordOfDinosaur[i][1] == absoluteCol)) {
				try {
					localW.giveMelocalView(localDinosaurList[i], type, localcc,
							localToken, absoluteRow, absoluteCol);
					localW.buildMainMap(localcc, localToken);
				} catch (InvalidIdException e1) {
					e1.invalidIdExceptionPopUp(frame);
					localW.buildMainMap(localcc, localToken);
					try {
						localW.giveMelocalView(localDinosaurList[i], type,
								localcc, localToken, absoluteRow, absoluteCol);
					} catch (InvalidIdException e2) {
						localW.getImageDin().setVisible(false);
						localW.getActionsPanel().setVisible(false);
						localW.getDinosaurMapPanel().setVisible(false);
						localW.getLocalDinosaurEnergyLabel().setVisible(false);
						localW.getLocalDinosaurLevelLabel().setVisible(false);
						localW.getLocalDinosaurLivedRoundsLabel().setVisible(
								false);
					}
				}
			}
		}
	}

}
