package exception;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ZeroDinosaurException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4076894407210135556L;

	public void ZeroDinosaurExceptionPopUp(JFrame frameWindow) {
		JOptionPane.showMessageDialog(frameWindow,
				"Non hai piu' dinosauri, la tua specie si e' estinta",
				"GAME OVER", JOptionPane.WARNING_MESSAGE);
	}
}
