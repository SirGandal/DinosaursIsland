package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class MovesLimitExceededException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3434212708949834371L;
	private String error;

	public MovesLimitExceededException() {
		error = "@no,@raggiuntoLimiteMosseDinosauro";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void movesLimitExceededExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Hai raggiunto il limite massimo di mosse effettuabili",
				"Limite mosse raggiunto", JOptionPane.WARNING_MESSAGE);
	}

}
