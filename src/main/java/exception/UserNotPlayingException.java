package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class UserNotPlayingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6532325458849052551L;
	String error;

	public UserNotPlayingException() {
		error = "@no,@nonInPartita";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void userNotPlayingExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"L'utente non sta giocando.", "Errore",
				JOptionPane.WARNING_MESSAGE);
	}
}
