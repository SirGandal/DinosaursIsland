package exception;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author  gas12n
 */
public class MaxDimensionReachedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6960610644247619122L;
	private String error;

	public MaxDimensionReachedException() {
		error = "@no,@raggiuntaDimensioneMax";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}

	public void maxDimensionReachedExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Hai raggiunto la dimensione massima per il tuo dinosauro.\n"
						+ "Un dinosauro non puo' crescere oltre il livello 5.",
				"Limite massima dimensione dinosauro",
				JOptionPane.WARNING_MESSAGE);
	}

}
