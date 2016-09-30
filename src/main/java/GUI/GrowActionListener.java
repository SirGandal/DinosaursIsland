package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.ClientComunicator;
import exception.InvalidIdException;
import exception.InvalidTokenException;
import exception.MaxDimensionReachedException;
import exception.MovesLimitExceededException;
import exception.NotEnoughEnergy;
import exception.NotYourTurnException;
import exception.UserNotPlayingException;

/**
 * Action listener che viene dichiarato in corrispondenza del bottone della finestra di gioco che permette ad un singolo dinosauro di crescere. Si occupa di chiamare in causa il corrispettivo metodo del comunicatore client che invia al server la richiesta di crescita per un dinosauro con determinato id.
 */
public class GrowActionListener implements ActionListener {

	private int absoluteRowStart;
	private int absoluteColStart;
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

	/**
	 * Costruttore che prende in ingresso i parametri necessari per chiamare la
	 * crescuta di un determinato dinosauro.
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
	 * @see server.ServerSocketComunicator#growingUp(String, String)
	 * @see client.ClientSocketComunicator#letYourDinosaurGrow(String, String)
	 * @see GUI.WindowGame#giveMelocalView(String, String, ClientComunicator,
	 *      String, int, int)
	 * @see GUI.WindowGame#buildMainMap(ClientComunicator, String)
	 */
	public GrowActionListener(String idDinosaur, String type, int absoluteRow,
			int absoluteCol, ClientComunicator cc, String token, WindowGame w) {

		this.absoluteRowStart = absoluteRow;
		this.absoluteColStart = absoluteCol;
		this.type = type;
		this.localIdDinosaur = idDinosaur;
		this.localcc = cc;
		this.token = token;
		this.w = w;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			localcc.letYourDinosaurGrow(token, localIdDinosaur);
			w.giveMelocalView(localIdDinosaur, type, localcc, token,
					absoluteRowStart, absoluteColStart);
			w.buildMainMap(localcc, token);
		} catch (InvalidIdException e1) {
		} catch (InvalidTokenException e1) {
			e1.invalidTokenExceptionPopUp(frame);
		} catch (MovesLimitExceededException e1) {
			e1.movesLimitExceededExceptionPopUp(frame);
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
							absoluteRowStart, absoluteColStart);
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
		} catch (NotYourTurnException e1) {
			e1.notYourTurnExceptionPopUp(frame);
		} catch (UserNotPlayingException e1) {
			e1.userNotPlayingExceptionPopUp(frame);
		} catch (MaxDimensionReachedException e1) {
			e1.maxDimensionReachedExceptionPopUp(frame);
		} catch (RemoteException e1) {
			JOptionPane.showMessageDialog(frame,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}

	}

}
