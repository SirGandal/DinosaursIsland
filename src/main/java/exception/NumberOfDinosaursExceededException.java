package exception;

import javax.swing.*;

/**
 * @author  gas12n
 */
public class NumberOfDinosaursExceededException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1323579705840748492L;
	String error;

	public NumberOfDinosaursExceededException() {
		error = "@no,@raggiuntoNumeroMaxDinosauri";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void numberOfDinosaursExceededExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Hai raggiunto il numero massimo di dinosauri per una specie.\n"
						+ "Puoi avere solo 5 dinosauri per specie.",
				"Limite dinosauri per specie", JOptionPane.WARNING_MESSAGE);
	}

}
