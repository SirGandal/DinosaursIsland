package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class InvalidIdException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7398716889483248775L;
	String error;

	public InvalidIdException() {
		error = "@no,@idNonValido";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void invalidIdExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Il tuo dinosauro e' morto o per limite di eta o perche'\n"
						+ "ti sei imbattuto in un combattimento ed hai perso.",
				"Morte dinosauro", JOptionPane.WARNING_MESSAGE);
	}

}
