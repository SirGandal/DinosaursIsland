package GUI;

import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;

import client.*;
import exception.*;

/**
 * Action listener che viene dichiarato in corrispondenza del bottone della finestra di gioco che permette ad un singolo dinosauro di depositare un uovo, ovvero di generare un altro dinosauro per il turno succcessivo a quello dopove e' stato deposto l'uovo. Si occupa di chiamare in causa il corrispettivo metodo del comunicatore client che invia al server la richiesta di deponi uovo per un dinosauro con determinato id.
 */
public class makeEggActionListener implements ActionListener {

	private int absoluteRow;
	private int absoluteCol;
	private String type;
	private String localIdDinosaur;
	/**
	 * @uml.property  name="localcc"
	 * @uml.associationEnd  
	 */
	private ClientComunicator localcc;
	private String token;
	private JFrame frame;
	/**
	 * @uml.property  name="w"
	 * @uml.associationEnd  
	 */
	private WindowGame w;
	private ImageIcon imageBackHerbivorousEgg = new ImageIcon(
			"src/main/resources/herbivorousEgg.png");
	private ImageIcon imageBackCarnivorousEgg = new ImageIcon(
			"src/main/resources/carnivorousEgg.png");

	/**
	 * Costruttore che prende in ingresso i parametri necessari per la
	 * deposizione di un uovo.
	 * 
	 * @param idDinosaur
	 *            Id del dinosauro che si vuole fare crescere.
	 * @param type
	 *            Tipo del dinosauro.
	 * @param absoluteRow
	 *            Indice di riga del dinosauro all'interno della mappa
	 *            principale.
	 * @param absoluteCol
	 *            Indice di colonna del dinosauro all'interno della mappa
	 *            principale.
	 * @param cc
	 *            Comunicatore client per chiamare i metodi relativi alla vista
	 *            locale.
	 * @param token
	 *            Token ricevuto al momento del login.
	 * @param w
	 *            L'oggetto stesso della finestra di gioco per poter aggiornare
	 *            direttamente le viste locali gia' create in precedenza.
	 * 
	 * @see server.ServerSocketComunicator#layAnEgg(String, String)
	 * @see client.ClientSocketComunicator#leaveYourEgg(String, String)
	 * @see GUI.WindowGame#giveMelocalView(String, String, ClientComunicator,
	 *      String, int, int)
	 * @see GUI.WindowGame#buildMainMap(ClientComunicator, String)
	 */
	public makeEggActionListener(String idDinosaur, String type,
			int absoluteRow, int absoluteCol, ClientComunicator cc,
			String token, WindowGame w) {

		this.absoluteRow = absoluteRow;
		this.absoluteCol = absoluteCol;
		this.type = type;
		this.localIdDinosaur = idDinosaur;
		this.localcc = cc;
		this.token = token;
		this.w = w;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			localcc.leaveYourEgg(token, localIdDinosaur);
			w.giveMelocalView(localIdDinosaur, type, localcc, token,
					absoluteRow, absoluteCol);
			if (type.equals("e")) {
				JOptionPane
						.showMessageDialog(
								frame,
								"Hai deposto correttamente il tuo uovo.\n"
										+ "Il nuovo dinosauro sara' disponibile a partire dal turno successivo.",
								"Uovo deposto",
								JOptionPane.INFORMATION_MESSAGE,
								imageBackHerbivorousEgg);
			} else {
				JOptionPane
						.showMessageDialog(
								frame,
								"Hai deposto correttamente il tuo uovo.\n"
										+ "Il nuovo dinosauro sara' disponibile a partire dal turno successivo.",
								"Uovo deposto",
								JOptionPane.INFORMATION_MESSAGE,
								imageBackCarnivorousEgg);
			}

		} catch (InvalidIdException e3) {
		} catch (InvalidTokenException e3) {
			e3.invalidTokenExceptionPopUp(frame);
		} catch (NumberOfDinosaursExceededException e3) {
			e3.numberOfDinosaursExceededExceptionPopUp(frame);
		} catch (MovesLimitExceededException e3) {
			e3.movesLimitExceededExceptionPopUp(frame);
		} catch (NotEnoughEnergy e1) {
			e1.NotEnoughEnergyPopUp(frame);
			try {
				/*
				 * se la razza estinta viene lanciata l'eccezione user not
				 * playing
				 */
				localcc.askForList(token);
				w.buildMainMap(localcc, token);
				try {
					w.giveMelocalView(localIdDinosaur, type, localcc, token,
							absoluteRow, absoluteCol);
				} catch (InvalidIdException e2) {
					w.getImageDin().setVisible(false);
					w.getActionsPanel().setVisible(false);
					w.getDinosaurMapPanel().setVisible(false);
					w.getLocalDinosaurEnergyLabel().setVisible(false);
					w.getLocalDinosaurLevelLabel().setVisible(false);
					w.getLocalDinosaurLivedRoundsLabel().setVisible(false);
				}

			} catch (InvalidTokenException e3) {
			} catch (UserNotPlayingException e3) {
				JOptionPane.showMessageDialog(frame,
						"GAME OVER! La tua razza si e' estinta.",
						"Razza estinta", JOptionPane.ERROR_MESSAGE);
				try {
					localcc.pass(token);
				} catch (InvalidTokenException e2) {
				} catch (NotYourTurnException e2) {
				} catch (UserNotPlayingException e2) {
				} catch (RemoteException e2) {
					JOptionPane
							.showMessageDialog(
									frame,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}
				w.getFrameWindow().dispose();
				w.getMenuWindow().setVisible(true);
			} catch (RemoteException e2) {
				JOptionPane.showMessageDialog(frame,
						"RMI. Problema, contattare l'amministratore di rete.",
						"Errore", JOptionPane.WARNING_MESSAGE);
			} catch (InvalidIdException e2) {
			}
		} catch (NotYourTurnException e3) {
			e3.notYourTurnExceptionPopUp(frame);
		} catch (UserNotPlayingException e3) {
			e3.userNotPlayingExceptionPopUp(frame);
		} catch (RemoteException e3) {
			JOptionPane.showMessageDialog(frame,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}
	}

}
