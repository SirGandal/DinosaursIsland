package exception;

import javax.swing.*;

public class YouLostTheBattle extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1088183142889587477L;

	public void youLostTheBattlePopUp(JFrame frameWindow) {
		JOptionPane
				.showMessageDialog(
						frameWindow,
						"Ti sei imbattuto in un altro dinosauro ed hai perso il combattimento.",
						"Combattimento", JOptionPane.WARNING_MESSAGE);
	}

}
