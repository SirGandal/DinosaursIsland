package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class BusyUsernameException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5921413416151135865L;
	private String error;

	public BusyUsernameException() {
		error = "@no,@usernameOccupato";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void busyUsernameExceptionPopUp(JFrame frame) {
		JOptionPane.showMessageDialog(frame,
				"Username gia' in uso, si prega di inserirne uno valido",
				"Username occupato", JOptionPane.ERROR_MESSAGE);
	}
}
