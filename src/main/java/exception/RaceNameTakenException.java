package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class RaceNameTakenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2981627501063831324L;
	private String error;

	public RaceNameTakenException() {
		error = "@no,@nomeRazzaOccupato";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void raceNameTakenExceptionPopUp(JFrame frameWindow) {
		JOptionPane
				.showMessageDialog(
						frameWindow,
						"Il nome della razza inserito e' gia' usato da un altro giocatore",
						"Nome razza in uso", JOptionPane.WARNING_MESSAGE);
	}
}
