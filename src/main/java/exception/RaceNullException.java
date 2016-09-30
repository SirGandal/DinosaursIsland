package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class RaceNullException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3699313680085292627L;
	private String error;

	public RaceNullException() {
		error = "@no,@razzaNonCreata";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void raceNullExceptionPopUp(JFrame frameWindow) {
		JOptionPane
				.showMessageDialog(
						frameWindow,
						"Non hai ancora creato una razza.\n"
								+ "Assicurati di averne creata una prima di avere accesso alla partita.",
						"Razza non creata", JOptionPane.WARNING_MESSAGE);
	}
}
