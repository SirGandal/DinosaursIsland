package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class UserAlreadyEnteredInGame extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6142187425211764870L;
	private String error;

	public UserAlreadyEnteredInGame() {
		error = "@no";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void userAlreadyEnteredInGamePopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Hai gia' avuto accesso alla partita.", "Accesso avvenuto",
				JOptionPane.WARNING_MESSAGE);
	}
}
