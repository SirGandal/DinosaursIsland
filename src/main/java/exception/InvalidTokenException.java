package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class InvalidTokenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8772286275365394804L;
	private String error;

	public InvalidTokenException() {
		error = "@no,@tokenNonValido";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void invalidTokenExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Problema, contattare l'amministratore di rete.", "Errore",
				JOptionPane.WARNING_MESSAGE);
	}

}
