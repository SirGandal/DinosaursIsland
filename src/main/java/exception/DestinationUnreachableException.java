package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class DestinationUnreachableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3692312153495948295L;
	String error;

	public DestinationUnreachableException() {
		error = "@no,@destinazioneNonValida";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void destinationUnreachableExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"La destinazione selezionata non e' raggiungibile",
				"Destinazione irragiungibile", JOptionPane.WARNING_MESSAGE);
	}

}
