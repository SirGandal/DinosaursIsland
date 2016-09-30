package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class RaceAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4918727212530724567L;
	private String error;

	public RaceAlreadyExistsException() {
		error = "@no,@razzaGiaCreata";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void raceAlreadyExistsExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow, "Hai gia' creato una razza",
				"Razzia gia' creata", JOptionPane.WARNING_MESSAGE);
	}
}
