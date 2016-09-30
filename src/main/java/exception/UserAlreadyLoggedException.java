package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class UserAlreadyLoggedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3400084459777715090L;
	private String error;

	public UserAlreadyLoggedException() {
		error = "@no";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void userAlreadyLoggedExceptionPopUp(JFrame frame, String username) {
		JOptionPane.showMessageDialog(frame, "Utente loggato", "Utente "
				+ username + " gia' loggato", JOptionPane.WARNING_MESSAGE);
	}

}
