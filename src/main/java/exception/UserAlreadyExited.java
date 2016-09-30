package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class UserAlreadyExited extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3065161144178136730L;
	private String error;

	public UserAlreadyExited() {
		error = "@no";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void userAlreadyExitedPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Hai gia' effettuato l'uscita dal gioco.", "Errore",
				JOptionPane.WARNING_MESSAGE);
	}

}
