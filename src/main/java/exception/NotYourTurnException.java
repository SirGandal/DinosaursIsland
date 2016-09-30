package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class NotYourTurnException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 548178021630201603L;
	private String error;

	public NotYourTurnException() {
		error = "@no,@nonIlTuoTurno";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void notYourTurnExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow, "Aspetta il tuo turno.",
				"Non e' il tuo turno", JOptionPane.WARNING_MESSAGE);
	}

}
