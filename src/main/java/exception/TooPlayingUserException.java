package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class TooPlayingUserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7080105321041517261L;
	private String error;

	public TooPlayingUserException() {
		error = "@no,@troppiGiocatori";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void tooPlayingUserExceptionPopUp(JFrame frameWindow) {
		JOptionPane
				.showMessageDialog(
						frameWindow,
						"Numero massimo di giocatori raggiunto, si prega di attendere.",
						"Numero massimo di giocatori",
						JOptionPane.WARNING_MESSAGE);
	}
}
