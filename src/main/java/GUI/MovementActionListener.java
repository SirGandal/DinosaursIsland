package GUI;

import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;

import client.*;
import exception.*;

/**
 * Action listener che viene dichiarato su ogni bottone della mappa locale. Quando verra' chiamato in causa chiamera' il metodo per richiedere lo spostamento in quella determinata casella.
 */
public class MovementActionListener implements ActionListener {

	private int offsetRow;
	private int offsetCol;
	private int absoluteRowStart;
	private int absoluteColStart;
	private int relativeRowDestination;
	private int relativeColDestination;
	private int mapLength;
	private int maxLocalView;
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
	 * 
	 * @param idDinosaur
	 *            Id del dinosauro che richiede il movimento.
	 * @param type
	 *            Tipo del dinosauro: erbivoro o carnivoro.
	 * @param mapLength
	 *            Lunghezza della mappa generale.
	 * @param maxLocalView
	 *            Lunghezza della vista locale.
	 * @param absoluteRowStart
	 *            Indice di riga del dinosauro all'interno della mappa
	 *            principale dal qual si vuole partire.
	 * @param absoluteColStart
	 *            Indice di colonna del dinosauro all'interno della mappa
	 *            principale dal quale si vuole partire.
	 * @param relativeRowDestination
	 * @param relativeColDestination
	 * @param cc
	 *            Comunicatore client per chiamare i metodi relativi alla vista
	 *            locale.
	 * @param token
	 *            Token ricevuto al momento del login.
	 * @param w
	 *            L'oggetto stesso della finestra di gioco per poter aggiornare
	 *            direttamente le viste locali gia' create in precedenza.
	 * 
	 * @see server.ServerSocketComunicator#denverMove(String, String, int, int)
	 * @see client.ClientSocketComunicator#moveDinosaur(String, String, int,
	 *      int)
	 * @see GUI.WindowGame#giveMelocalView(String, String, ClientComunicator,
	 *      String, int, int)
	 * @see GUI.WindowGame#buildMainMap(ClientComunicator, String)
	 */
	public MovementActionListener(String idDinosaur, String type,
			int mapLength, int maxLocalView, int absoluteRowStart,
			int absoluteColStart, int relativeRowDestination,
			int relativeColDestination, ClientComunicator cc, String token,
			WindowGame w) {

		this.absoluteRowStart = absoluteRowStart;
		this.absoluteColStart = absoluteColStart;
		this.relativeRowDestination = relativeRowDestination;
		this.relativeColDestination = relativeColDestination;
		this.mapLength = mapLength;
		this.maxLocalView = maxLocalView;
		this.type = type;
		this.localIdDinosaur = idDinosaur;
		this.localcc = cc;
		this.token = token;
		this.w = w;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		System.out.println("Riga assoluta di partenze: " + absoluteRowStart
				+ " colonna assoluta di partenza: " + absoluteColStart);
		System.out.println("Riga relativa di partenze: " + maxLocalView / 2
				+ " colonna relativa di partenza: " + maxLocalView / 2);
		System.out.println("Riga relativa di arrivo: " + relativeRowDestination
				+ " colonna relativa di arrivo: " + relativeColDestination);
		*/
		this.offsetRow = relativeRowDestination - maxLocalView / 2;
		this.offsetCol = relativeColDestination - maxLocalView / 2;
		
		/*
		System.out.println("Il dinosauro parte da "
				+ absoluteRowStart
				+ " "
				+ absoluteColStart
				+ " per andare in "
				+ (absoluteRowStart + offsetRow)
				+ " "
				+ (absoluteColStart + offsetCol)
				+ "    X:"
				+ ((absoluteColStart + offsetCol) - w.getOffset())
				+ "    Y:"
				+ ((mapLength - 1 - (absoluteRowStart + offsetRow)) + w
						.getOffset()));
		*/
		
		try {
			localcc.moveDinosaur(token, localIdDinosaur,
					((absoluteColStart + offsetCol) - w.getOffset()),
					((mapLength - 1 - (absoluteRowStart + offsetRow)) + w
							.getOffset()));
			w.buildMainMap(localcc, token);
			w.giveMelocalView(localIdDinosaur, type, localcc, token,
					(absoluteRowStart + offsetRow),
					(absoluteColStart + offsetCol));
		} catch (YouWonTheBattle e1) {
			e1.youWonTheBattlePopUp(frame);
			try {
				w.giveMelocalView(localIdDinosaur, type, localcc, token,
						(absoluteRowStart + offsetRow),
						(absoluteColStart + offsetCol));
			} catch (InvalidIdException e2) {
			}
		} catch (YouLostTheBattle e1) {
			e1.youLostTheBattlePopUp(frame);
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
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		} catch (InvalidIdException e1) {
			e1.invalidIdExceptionPopUp(frame);
			try {
				w.buildMainMap(localcc, token);
				w.giveMelocalView(localIdDinosaur, type, localcc, token,
						(absoluteRowStart + offsetRow),
						(absoluteColStart + offsetCol));
			} catch (InvalidIdException e2) {
				w.getImageDin().setVisible(false);
				w.getActionsPanel().setVisible(false);
				w.getDinosaurMapPanel().setVisible(false);
				w.getLocalDinosaurEnergyLabel().setVisible(false);
				w.getLocalDinosaurLevelLabel().setVisible(false);
				w.getLocalDinosaurLivedRoundsLabel().setVisible(false);
			}
		} catch (InvalidTokenException e1) {
			e1.invalidTokenExceptionPopUp(frame);
		} catch (DestinationUnreachableException e1) {
			e1.destinationUnreachableExceptionPopUp(frame);
		} catch (MovesLimitExceededException e1) {
			e1.movesLimitExceededExceptionPopUp(frame);
		} catch (NotEnoughEnergy e1) {
			e1.NotEnoughEnergyPopUp(frame);
			try {
				w.buildMainMap(localcc, token);
				w.giveMelocalView(localIdDinosaur, type, localcc, token,
						(absoluteRowStart + offsetRow),
						(absoluteColStart + offsetCol));
			} catch (InvalidIdException e2) {
				w.getImageDin().setVisible(false);
				w.getActionsPanel().setVisible(false);
				w.getDinosaurMapPanel().setVisible(false);
				w.getLocalDinosaurEnergyLabel().setVisible(false);
				w.getLocalDinosaurLevelLabel().setVisible(false);
				w.getLocalDinosaurLivedRoundsLabel().setVisible(false);
			}
		} catch (NotYourTurnException e1) {
			e1.notYourTurnExceptionPopUp(frame);
		} catch (UserNotPlayingException e1) {
			e1.userNotPlayingExceptionPopUp(frame);
		} catch (RemoteException e1) {
			JOptionPane.showMessageDialog(frame,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}
	}

}
