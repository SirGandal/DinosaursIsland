package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class AutenticationFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5063861328004536922L;
	private String error;

	public AutenticationFailedException() {
		error = "@no,@autenticazioneFallita";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void autenticationFailedExceptionPopUp(JFrame frame) {
		JOptionPane
				.showMessageDialog(
						frame,
						"Autenticazione fallita",
						"L'autenticazione e' fallita, ricontrollare attentamente username e password.",
						JOptionPane.WARNING_MESSAGE);
	}

}
